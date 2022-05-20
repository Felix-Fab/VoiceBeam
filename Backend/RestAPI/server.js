import "dotenv/config";
import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import startCronJobs from "./cronJobs.js";

import AuthRouter from "./routers/auth.js";
import MessagesRouter from "./routers/messages.js";

import WebSocketServer from "./routers/WebSocketServer.js";

import Logger from "./classes/Logger.js";

console.clear();
await mongoose.connect(process.env.DB_URL).then(() => Logger.writeServerLog("",`Connetect to DB "${process.env.DB_URL}"!`));

startCronJobs();

const api = express();

api.use(express.json());

api.use(cors());

api.use("/auth", AuthRouter);
api.use("/messages", MessagesRouter);

api.listen(process.env.API_PORT, () => {
    Logger.writeServerLog("", `API running on Port ${process.env.API_PORT}...`);
});

new WebSocketServer();