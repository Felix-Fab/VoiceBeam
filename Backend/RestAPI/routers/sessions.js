import { Router } from "express";
import { isAuthorized } from "./auth.js";
import { check } from "express-validator";
import User from "../models/user.js";

const router = Router();

router.use(isAuthorized);

router.get("/", (req, res) => {
    let sessions = req.user.sessions;

    sessions = sessions.filter(session => {
        return session.expiresAt > Date.now() && !session.invalidated;
    });

    sessions = sessions.map(session => {
        return {
            _id : session._id,
            createdAt: session.createdAt,
            expiresAt: session.expiresAt,
            isCurrent: session.token === req.user.currentToken
        };
    });

    sessions.reverse();

    return res.status(200).json({
        sessions: sessions
    });
});

router.delete("/", 
    check("id")
        .isMongoId()
            .withMessage("Invalid ID!"),
async (req, res) => {
    let id = req.body.id;

    let foundSession = req.user.sessions.find(session => session.id === id);

    if (!foundSession || foundSession.invalidated) {
        return res.status(404).json({
            msg: "Invalid ID!"
        });
    }

    if (foundSession.token === req.user.currentToken) {
        return res.status(404).json({
            msg: "You cannot invalidate your current session!\nPlease Logout!"
        });
    }

    await User.updateOne(
        {
            _id: req.user._id, "sessions._id": foundSession._id
        },
        {
            $set: {
                "sessions.$.invalidated": true
            }
        }
    );

    return res.status(200).json({
        msg: "Successfully invalidated Session!"
    })
});

export default router;