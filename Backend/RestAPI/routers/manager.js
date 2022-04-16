import {Router} from "express";
import { check, validationResult } from "express-validator";
import bcrypt from "bcrypt";
import { SALTROUNDS,ACCESS_TOKEN_SECRET } from "../server.js";
import User from "../models/user.js";
import jwt from "jsonwebtoken"

const router = Router();

router.patch("/register",notAuthenticated,
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

router.patch("/login",notAuthenticated,
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

    const accessToken = jwt.sign({ _id: foundUser._id }, ACCESS_TOKEN_SECRET, { expiresIn: "15m" });

    await User.updateOne({ email: req.body.email}, { accessToken: accessToken });

    return res.status(200).json({
        username: foundUser.username,
        email: foundUser.email,
        accessToken: accessToken,
    });
});

router.patch("/status",authenticateToken, 
    check("status")
        .isBoolean()
            .withMessage("Please provide a valid status"),
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

router.post("/status",notAuthenticated,
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

router.patch("/getUsers",authenticateToken, async(req,res) => {
    const Users = await User.find({status: true, username: { $ne: req.user.username } },"username").exec();

    return res.status(200).json({
        users: Users
    });
});

router.get("/checkAccessToken",authenticateToken, (req, res) => {
    return res.json({
        username: req.user.username,
        email: req.user.email
    }).status(200);
});

function authenticateToken(req, res, next) {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
    if (!token) return res.status(401).json({ info: "Not Authorized!" });

    jwt.verify(token, ACCESS_TOKEN_SECRET, async (err, tokenData) => {
        if (err) return res.status(403).json({ errors: [{ msg: "Access Token Invalid or Expired!" }] });
        req.user = await User.findOne({ _id: tokenData._id });

        next();
    });
}

function notAuthenticated(req, res, next) {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
    if (token) return res.status(401).json({ info: "Not Authorized!" });

    next();
}

async function generateHash(rawPassword) {
    const salt = await bcrypt.genSalt(SALTROUNDS);
    return await bcrypt.hash(rawPassword, salt);
}

async function compareHash(hash, text) {
    return await bcrypt.compare(text, hash);
}

export default router;