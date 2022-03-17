import mongoose from "mongoose";

const userSchema = mongoose.Schema({
    from: {
        type: String,
        required: true,
        min: 1,
        max: 64
    },
    to: {
        type: String,
        required: true,
        min: 5,
        max: 254
    },
    audioLength: {
        type: Number,
        required: true
    },
    createdAt: {
        type: Date,
        required: true,
        default: Date.now()
    }    
})

export default mongoose.model("Message", userSchema);