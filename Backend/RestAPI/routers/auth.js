import {Router} from "express";
import { check, validationResult } from "express-validator";
import bcrypt from "bcrypt";
import User from "../models/user.js";
import moment from "moment";
import jwt from "jsonwebtoken"
import sessionsRouter from "./sessions.js";

const router = Router();

router.post("/register", isNotAuthorized,
    check("username")
        .isLength({min:1})
            .withMessage("Username has to be at least 1 Character long!")
        .isLength({max:64})
            .withMessage("Username cannot be longer than 64 Characters!")
        .custom(async value => {
            if(await User.findOne({username: value})){
                return Promise.reject('Username already in use!');
            }
        }),
    check("email")
        .isEmail()
            .withMessage("Please provide a valid Email!")
        .isLength({max: 254})
            .withMessage("Email connot be longer than 254 Characters!")
        .custom(async value => {
            if(await User.findOne({email: value})){
                return Promise.reject("Email already in use!");
            }
        }),
    check("password")
        .isLength({min: 1})
            .withMessage("Please provide a Password with at least 1 Character!")
        .isLength({max: 128})
            .withMessage("Password cannot be longer than 128 Characters!"),
async (req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const newUser = new User({
        username: req.body.username,
        email: req.body.email,
        status: false,
        password: await generateHash(req.body.password)
    });

    await newUser.save();

    return res.status(201).json({info: "Account has been successfully created!"});
});

router.post("/login", isNotAuthorized,
    check("email")
        .isEmail()
            .withMessage("Please provide a valid Email!")
        .isLength({max: 254})
            .withMessage("Email cannot be longer than 254 Characters!"),
    check("password")
        .isLength({min: 1, max: 128})
            .withMessage(("Please provide a valid Password!")),
async (req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const foundUser = await User.findOne({email: req.body.email});

    if(!foundUser || !await compareHash(foundUser.password,req.body.password)) {
        return res.status(401).json({ errors: [{ msg: "Invalid Credentials!"}] });
    }

    // TODO: We need a longer token time maybe 30 days?
    //       This should solve the issue of reauthenticating everytime you open the app.
    const accessToken = jwt.sign({ _id: foundUser._id }, process.env.JWT_SECRET, { expiresIn: "15m" });

    foundUser.sessions.push({
        token: accessToken,
        expiresAt: moment().add(15, "m")
    });
    await foundUser.save();

    return res.status(200).json({
        username: foundUser.username,
        email: foundUser.email,
        accessToken: accessToken,
    });
});

router.get("/logout", isAuthorized, async (req, res) => {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];

    await User.updateOne(
        {
            _id: req.user._id, "sessions.token": token
        },
        {
            $set: {
                "sessions.$.invalidated": true
            }
        }
    );

    return res.status(200).json({
        message: "Successfully logged out!"
    });
});

router.post("/status", isAuthorized, 
    check("status")
        .isBoolean()
            .withMessage("Please provide a valid status!"),
async(req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const foundUser = req.user;
    
    foundUser.status = req.body.status;

    await foundUser.save();

     return res.status(200).json({
         username: foundUser.username,
         email: foundUser.email,
         status: foundUser.status
     });
});

router.get("/status", isNotAuthorized,
    check("email")
        .isEmail()
            .withMessage("Please provide a valid Email!")
        .isLength({ max: 254 })
            .withMessage("Email cannot be longer that 254 Characters!"),
    check("status")
        .isBoolean(true) 
            .withMessage("Status must be a Boolean!"),
async (req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const foundUser = await User.findOne({email: req.body.email});

    if(!foundUser){
        return res.status(401).json({errors:[{msg: "Invalid Credentials"}]});
    }
    
    foundUser.status = req.body.status;

     await foundUser.save();

     return res.status(200).json({
         username: foundUser.username,
         email: foundUser.email,
         status: foundUser.status
     });
});

router.post("/getUsers", isAuthorized, async(req,res) => {
    const Users = await User.find({status: true, username: { $ne: req.user.username } },"username").exec();

    return res.status(200).json({
        users: Users
    });
});

router.get("/checkAccessToken", isAuthorized, (req, res) => {
    return res.json({
        username: req.user.username,
        email: req.user.email
    }).status(200);
});

export async function isAuthorized(req, res, next) {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];

    if (!token) {
        return res.status(401).json({ info: "Not Authorized!" });
    }

    let tokenData;
    try {
        tokenData = jwt.verify(token, process.env.JWT_SECRET);
    } catch {
        return res.status(403).json({
            errors: [
                {
                    msg: "Access Token Invalid or Expired!"
                }
            ]
        });
    }

    let foundUser = await User.findOne({ _id: tokenData._id });

    req.user = foundUser;

    if (!req.user) {
        return res.status(403).json({
            errors: [
                {
                    msg: "Access Token Invalid or Expired!"
                }
            ]
        });
    }

    let sessionInvalidated = false;
    foundUser.sessions.every(session => {
        if (session.token === token) {
            if (session.invalidated) {
                sessionInvalidated = true;
                return false;
            }
        }

        return true;
    });

    if (sessionInvalidated) {
        return res.status(403).json({
            errors: [
                {
                    msg: "Access Token Invalid or Expired!"
                }
            ]
        });
    }

    req.user.currentToken = token;

    return next();
}

export function isNotAuthorized(req, res, next) {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
    if (token){
        return res.status(401).json({ info: "Not Authorized!" });
    }

    return next();
}

async function generateHash(rawPassword) {
    const salt = await bcrypt.genSalt(10);
    return await bcrypt.hash(rawPassword, salt);
}

async function compareHash(hash, text) {
    return await bcrypt.compare(text, hash);
}

router.use("/sessions", sessionsRouter);

export default router;