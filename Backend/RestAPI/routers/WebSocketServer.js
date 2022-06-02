import fetch from "node-fetch";
import { Server } from "socket.io";
import 'colors';
import Logger from "../classes/Logger.js";
import Parameters from "../Parameters.js";
import https from "https";
import fs from "fs";

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
    
        this.server.on('connection', (socket) => {
            const authorization = socket.handshake.headers['authorization'];

            // TODO: Do not fetch here, use a function instead.
            fetch(`http://127.0.0.1:${Parameters.ApiPort}/auth/checkAccessToken`, {
                method: 'GET',
                headers: {
                    Authorization: authorization
                }
            })
            .then(res => res.json())
            .then(json => {
                socket.username = json.username;
                this.Sockets.push(socket);

                Logger.writeSuccess("WebSocket-",`${socket.username} connected`);
            }).catch(error => {
                Logger.writeWarning("WebSocket-",`Unknown Client connected`);
                socket.disconnect();
            });

            socket.on("sendDataToServer", (data) => {
                this.Sockets.forEach(element => {
                    debugger;

                    if(element.username == data.to){

                    debugger;
                    console.log("Hallo");

                    var BodyData = {
                        from: data.from,
                        to: data.to,
                        audioLength: 0
                    };

                    debugger;

                    fetch(`http://127.0.0.1:${Parameters.ApiPort}/messages/add`, {
                        method: 'post',
                        body: JSON.stringify(BodyData),
                        headers: {
                            Authorization: `Bearer ${data.accessToken}`
                        },
                    })
                    .then(res => res.json())
                    .then(json => {
                        debugger;
                    }).catch(error => {
                        Logger.writeError("",`AddMesage Error: ${error}`);
                    });

                        element.emit('SendDataToClient',data);
                        Logger.writeSuccess("WebSocket-",`Receive data from ${data.from} and forward it to ${data.to}`)
                    }
                });
            });

            socket.on("ClientDisconnect", (username) => {
                this.Sockets.filter(socket => socket.username != username);
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