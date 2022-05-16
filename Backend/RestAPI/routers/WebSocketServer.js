import fetch from "node-fetch";
import { Server } from "socket.io";
import '../Parameters.js';
import Parameters from "../Parameters.js";

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
            }).catch(error => {
                console.log("Client disconnected");
                console.log(error);
                socket.disconnect();
            });

            socket.on("sendDataToServer", (data) => {
                
                var JsonData = JSON.parse(data);

                this.Sockets.forEach(element => {
                    if(element.username == JsonData.to){
                        element.emit('SendDataToClient',data);
                        debugger;
                    }
                    debugger;
                });
            });
        });
    
        this.server.listen(Parameters.WebSocketPort);

        console.log(`WebSocket running on Port ${Parameters.WebSocketPort}...`)
    }
}