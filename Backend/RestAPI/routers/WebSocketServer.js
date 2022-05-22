import fetch from "node-fetch";
import { Server } from "socket.io";
import 'colors';
import Logger from "../classes/Logger.js";

export default class WebSocketServer{
    constructor() {
        this.Sockets = [];

        this.server = new Server({cors: {
            origin: "*"
        }});
    
        this.server.on('connection', (socket) => {
            const authorization = socket.handshake.headers['authorization'];

            // TODO: Do not fetch here, use a function instead.
            fetch(`http://127.0.0.1:${process.env.API_PORT}/auth/checkAccessToken`, {
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
    
        this.server.listen(process.env.WEBSOCKET_PORT);

        Logger.writeServerLog("",`WebSocket running on Port ${process.env.WEBSOCKET_PORT}...`)
    }
}