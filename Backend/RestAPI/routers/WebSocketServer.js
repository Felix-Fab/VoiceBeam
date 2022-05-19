import fetch from "node-fetch";
import { Server } from "socket.io";
import '../Parameters.js';
import Parameters from "../Parameters.js";
import 'colors';
import Logger from "../classes/Logger.js";

export default class WebSocketServer{

    constructor(){

        this.Sockets = [];

        this.server = new Server({cors: {
            origin: "*"
        }});
    
        this.server.on('connection', (socket) => {

            const authorization = socket.handshake.headers['authorization'];

            fetch(`http://127.0.0.1:${Parameters.ApiPort}/manager/checkAccessToken`, {
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
                console.log("Client Data Received");
                var JsonData = JSON.parse(data);

                this.Sockets.forEach(element => {
                    if(element.username == JsonData.to){
                        element.emit('SendDataToClient',JsonData);
                    }
                });
            });

            socket.on("ClientDisconnect", (username) => {
                this.Sockets.filter(socket => socket.username != username);
            })
        });
    
        this.server.listen(Parameters.WebSocketPort);

        Logger.writeServerLog("",`WebSocket running on Port ${Parameters.WebSocketPort}...`)
    }
}