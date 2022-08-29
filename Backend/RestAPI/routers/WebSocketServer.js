import fetch from "node-fetch";
import { Server } from "socket.io";
import 'colors';
import Logger from "../classes/Logger.js";
import Parameters from "../Parameters.js";
import https from "https";
import fs from "fs";
import jwt from "jsonwebtoken"
import User from "../models/user.js";
import { response } from "express";

export default class WebSocketServer{
    constructor() {
        this.Sockets = [];

        if(Parameters.Status == "Remote"){
            this.httpsServer = https.createServer({
                key: fs.readFileSync(Parameters.SSL_privateKey),
                cert: fs.readFileSync(Parameters.SSL_certificate)
            });

            this.server = new Server(this.httpsServer,{cors: {
                origin: "*"
            }});
        }else{
            this.server = new Server({cors: {
                origin: "*"
            }});
        }
    
        this.server.on('connection',async (socket) => {
            const authorization = socket.handshake.headers['authorization'];

            let tokenData;
            try {
                tokenData = jwt.verify(authorization, Parameters.AccessTokenSecret);
            } catch(error) {
                Logger.writeWarning("WebSocket-",`Unknown Client connected`);
                socket.disconnect();
                console.log(error);
                return;
            }

            let foundUser = await User.findOne({ _id: tokenData._id });

            foundUser.status = true;

            await foundUser.save();

            socket.username = foundUser.username;
            socket.accessToken = authorization;
            this.Sockets.push(socket);

            Logger.writeSuccess("WebSocket-",`${socket.username} connected`);

            socket.on("sendDataToServer", (data) => {

                const SocketFind = this.Sockets.find(element => element.username == data.to);

                if(SocketFind != undefined){

                    var BodyData = {
                        from: data.from,
                        to: data.to,
                        audioLength: 0
                    };
    
                    fetch(`http://127.0.0.1:${Parameters.ApiPort}/messages/add`, {
                        method: 'POST',
                        body: JSON.stringify(BodyData),
                        headers: {
                            authorization: `Bearer ${data.accessToken}`,
                            'Content-Type': 'application/json'
                        },
                    })
                    .then((response) => {
                        if(!response.ok){
                            console.log(response.status);
                        }else{
                            console.log(response.status);
                        }
                    })
                    .catch(error => {
                        Logger.writeError("",`AddMesage Error: ${error}`);
                    });
                        
                    SocketFind.emit('SendDataToClient',data);
                    Logger.writeSuccess("WebSocket-",`Receive data from ${data.from} and forward it to ${data.to}`);

                }else{
                    var data = {
                        status: 301,
                        message: "Benutzer nicht verfügbar"
                    }

                    socket.emit('ServerStatusResponse',data);
                }

            });

            socket.on("ClientDisconnect",async (username) => {
                
                this.socket.forEach(async (element) => {
                    if(element.username == username){
                        let tokenData;

                        try {
                            tokenData = jwt.verify(element.accessToken, Parameters.AccessTokenSecret);
                        } catch {
                            Logger.writeError("WebSocket-",`Socket AccesToken Error`);
                            socket.disconnect();
                            return;
                        }

                        let foundUser = await User.findOne({ _id: tokenData._id });

                        foundUser.status = false;

                        await foundUser.save();
                    }
                });

                this.Sockets.filter(socket => socket.username != username);

                Logger.writeInfo("WebSocket-","Socket successful disconnected");
            })
        });

        if(Parameters.Status == "Remote"){
            this.httpsServer.listen(Parameters.WebSocketPort);
        }else{
            this.server.listen(Parameters.WebSocketPort);
        }

        Logger.writeServerLog("",`WebSocket running on Port ${Parameters.WebSocketPort}...`)
    }
}