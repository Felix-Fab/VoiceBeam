import {Router} from "express";
import { check, validationResult } from "express-validator";
import Message from "../models/message.js";
import User from "../models/user.js";

const router = Router();

router.patch("/add",notAuthenticated,
    check("from")
        .isLength({min: 1})
            .withMessage("Username has to be at least 1 Character long!")
        .isLength({max:64})
            .withMessage("Username cannot be longer than 64 Characters!"),
    check("to")
        .isLength({min: 1})
            .withMessage("Username has to be at least 1 Character long!")
        .isLength({max:64})
            .withMessage("Username cannot be longer than 64 Characters!"),
    check("audioLength")
        .isInt()
            .withMessage("AudioLength must be a Number"),
async (req,res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const User1 = await User.findOne({username: req.body.from});
    const User2 = await User.findOne({username: req.body.to})


    if(!User1 || !User2){
        return res.status(401).json({errors:[{msg: "Invalid Credentials"}]});
    }

    const newMessage = new Message({
        from: req.body.from,
        to: req.body.to,
        audioLength: req.body.audioLength
    });

    await newMessage.save();

    return res.status(201).json({info: 'Message has been successfully created!'});
});   

router.patch("/getMessages",notAuthenticated,
    check("username1")
        .isLength({min: 1})
            .withMessage("Username1 has to be at least 1 Character long!")
        .isLength({max: 64})
            .withMessage("Username1 cannot be longer than 64 Characters!"),
    check("username2")
        .isLength({min: 1})
            .withMessage("Username2 has to be at least 1 Character long!")
        .isLength({max: 64})
            .withMessage("Username2 cannot be longer than 64 Characters!"),        
async (req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
        return res.status(400).json({
            errors: errors.array()
        });
    }

    const User1 = await User.findOne({username: req.body.username1});
    const User2 = await User.findOne({username: req.body.username2})

    if(!User1 || !User2){
        return res.status(401).json({errors:[{msg: "Invalid Credentials"}]});
    }

    const Messages = await Message.find({
        $or: [
            {
                $and: [
                    { from: req.body.username1 },
                    { to: req.body.username2}
                ]
            },
            {
                $and: [
                    { from: req.body.username2 },
                    { to: req.body.username1 }
                ]
            }
        ]
    }).exec();

    return res.status(200).json({
        messages: Messages
    });
});


function notAuthenticated(req, res, next) {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
    if (token) return res.status(401).json({ info: "Not Authorized!" });

    next();
}

export default router;