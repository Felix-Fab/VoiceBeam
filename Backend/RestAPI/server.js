import "dotenv/config";
import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import startCronJobs from "./cronJobs.js";
import fs from "fs";
import https from "https";

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

if(Parameters.Status == "Remote"){

    var credentials  = {
        key: fs.readFileSync(Parameters.SSL_privateKey),
        cert: fs.readFileSync(Parameters.SSL_certificate)
    }

    https.createServer(credentials,api).listen(Parameters.ApiPort);

    Logger.writeServerLog("", `API running on Port ${Parameters.ApiPort} with Https...`);
}else{
    api.listen(Parameters.ApiPort, () => {
        Logger.writeServerLog("", `API running on Port ${Parameters.ApiPort}...`);
    });
}

if(Parameters.StartWebSocket){
    new WebSocketServer();
}