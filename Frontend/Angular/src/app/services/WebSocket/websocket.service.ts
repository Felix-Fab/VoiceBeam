import { Injectable } from '@angular/core';
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import { io, Socket } from 'socket.io-client';
import Debug from 'src/app/Debug';
import Http from 'src/app/classes/Http';
import { LoginRegisterComponent } from 'src/app/pages/login-menu/register/login-register.component';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  websocket!: Socket;

  constructor() {
  }

  init(){

    this.websocket = io(Http.getWebSocketUrl(), {
      extraHeaders: {
        Authorization: "Bearer " + localStorage.getItem("accessToken")
      }
    });
    console.log("Connecting to WebSocket...");

    this.websocket.on('SendDataToClient', (data:any) => {

      var blob = new Blob([data.data], { type: 'audio/webm;codecs=opus' });

      const audioUrl = URL.createObjectURL(blob);
      const audio = new Audio(audioUrl);
      audio.play();
    });

    this.websocket.on('disconnected', () => {
      console.log("WebSocket disconnect");
      /* TODO: Insecure! You could potentially kill other sessions! */
      this.websocket.emit('ClientDisconnect', localStorage.getItem("username"));
    });

    this.websocket.on("connect", () => {
       console.log("Connected to WebSocket");
    });
  }

   send(data:any){
     this.websocket.emit("sendDataToServer",data)
   }
}
