import fetch from "node-fetch";
import { Server } from "socket.io";

export default class WebSocketServer{
    constructor() {
        this.Sockets = [];

        this.server = new Server({cors: {
            origin: "*"
        }});
    
        this.server.on('connection', (socket) => {
            const authorization = socket.handshake.headers['authorization'];

            // TODO: Do not use static IP here
            // TODO: Do not fetch here, use a function instead.
            //       For example, 
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
            }).catch(error => {
                console.log("Client disconnected");
                console.log(error);
                socket.disconnect();
            });

            socket.on("sendDataToServer", (data) => {
                var JsonData = JSON.parse(data);

                this.Sockets.forEach(element => {
                    if(element.username == JsonData.to){
                        element.emit('SendDataToClient',JsonData);
                    }
                });
                debugger;
            });

            socket.on("ClientDisconnect", (username) => {
                this.Sockets.filter(socket => socket.username != username);
            })
        });
    
        this.server.listen(process.env.WEBSOCKET_PORT);

        console.log(`WebSocket running on Port ${process.env.WEBSOCKET_PORT}!`)
    }
}