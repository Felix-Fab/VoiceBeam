import "dotenv/config";
import express from "express";
import mongoose from "mongoose";
import cors from "cors";

import AuthRouter from "./routers/auth.js";
import MessagesRouter from "./routers/messages.js";

import WebSocketServer from "./routers/WebSocketServer.js";

await mongoose.connect(process.env.DB_URL)
    .then(() => {
        console.log(`Connected to DB "${process.env.DB_URL}"!`)
    });

const api = express();

api.use(express.json());

api.use(cors());

api.use("/auth", AuthRouter);
api.use("/messages", MessagesRouter);

api.listen(process.env.API_PORT, () => {
    console.log(`API running on Port ${process.env.API_PORT}!`);
});

new WebSocketServer();