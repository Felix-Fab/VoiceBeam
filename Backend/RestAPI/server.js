import "dotenv/config";
import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import startCronJobs from "./cronJobs.js";

import AuthRouter from "./routers/auth.js";
import MessagesRouter from "./routers/messages.js";

import WebSocketServer from "./routers/WebSocketServer.js";

import Logger from "./classes/Logger.js";
import Parameters from "./Parameters.js";

console.clear();
await mongoose.connect(Parameters.DBURL).then(() => Logger.writeServerLog("",`Connetect to DB "${Parameters.DBURL}"!`));

startCronJobs();

const api = express();

api.use(express.json());

api.use(cors());

api.use("/auth", AuthRouter);
api.use("/messages", MessagesRouter);

api.listen(Parameters.ApiPort, () => {
    Logger.writeServerLog("", `API running on Port ${Parameters.ApiPort}...`);
});

if(Parameters.StartWebSocket){
    new WebSocketServer();
}