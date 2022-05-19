import express from "express";
import mongoose from "mongoose";
import cors from "cors";

import Users from "./routers/manager.js";
import Messages from "./routers/messages.js";
import WebSocketServer from "./routers/WebSocketServer.js";
import Parameters from './Parameters.js';
import Logger from "./classes/Logger.js";

console.clear();
await mongoose.connect(Parameters.DBURL).then(() => Logger.writeServerLog("",`Connetect to DB "${Parameters.DBURL}"!`));

const RestAPIApp = express();

RestAPIApp.use(express.json());

RestAPIApp.use(cors())

RestAPIApp.use("/manager",Users);
RestAPIApp.use("/messages",Messages);

RestAPIApp.listen(Parameters.ApiPort,() => {
    Logger.writeServerLog("",`API running on Port ${Parameters.ApiPort}...`);
});

if(Parameters.StartWebSocket){
    new WebSocketServer();
}