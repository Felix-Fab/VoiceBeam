import { Injectable } from '@angular/core';
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import { io } from 'socket.io-client';
import UserInfo from 'src/app/classes/UserInfo';
import Debug from 'src/app/Debug';
import Http from 'src/app/classes/Http';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  websocket!: any;

  constructor() {
  }

  init(){

    this.websocket = io(`ws://${Http.getServerUrl}:8000`, {
      extraHeaders: {
        Authorization: "Bearer " + UserInfo.getAccessToken()
      }
    });

    this.websocket.on('SendDataToClient', (data:any) => {
      console.log("Data received!");
      debugger;
    });

    this.websocket.on('disconnected', () => {
      this.websocket.emit('ClientDisconnect',UserInfo.getUsername);
    })
  }

   send(data:any){
     this.websocket.emit("sendDataToServer",data)
   }
}
