import mongoose from "mongoose";

const userSchema = mongoose.Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        min: 1,
        max: 64
    },
    email: {
        type: String,
        required: true,
        unique: true,
        min: 5,
        max: 254
    },
    password: {
        type: String,
        required: true
    },
    status: {
        type: Boolean,
        required: true,
        default: false
    },
    sessions: [
        {
            token: {
                type: String,
                required: true
            },
            createdAt: {
                type: Date,
                default: Date.now
            },
            expiresAt: {
                type: Date,
                required: true
            },
            invalidated: {
                type: Boolean,
                default: false
            }
        }
    ],
    createdAt: {
        type: Date,
        required: false,
        default: Date.now()
    },
})

export default mongoose.model("User", userSchema);