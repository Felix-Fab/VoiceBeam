import { Injectable } from '@angular/core';
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import { io, Socket } from 'socket.io-client';
import UserInfo from 'src/app/classes/UserInfo';
import Debug from 'src/app/Debug';
import Http from 'src/app/classes/Http';

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
        Authorization: "Bearer " + UserInfo.getAccessToken()
      }
    });
    console.log("Connecting to WebSocket...");

    this.websocket.on('SendDataToClient', (data:any) => {
      
      var arraybuffer = ArrayBufferConverter.str2ab(data.data);

      var blob = new Blob([arraybuffer], { type: 'audio/x-mpeg-3' });

      var file = new File([blob],"voicebeam.mp3");

      var BlobLink = window.URL.createObjectURL(file);

      let audio = new Audio();
      audio.src = BlobLink;
      audio.load();
      audio.play();

      debugger;
    });

    this.websocket.on('disconnected', () => {
      console.log("WebSocket disconnect");
      this.websocket.emit('ClientDisconnect',UserInfo.getUsername);
    });

    this.websocket.on("connect", () => {
       console.log("Connected to WebSocket");
    });
  }

   send(data:any){
     this.websocket.emit("sendDataToServer",data)
   }
}
