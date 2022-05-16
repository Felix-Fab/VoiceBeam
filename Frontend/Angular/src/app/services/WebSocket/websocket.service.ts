import { Injectable } from '@angular/core';
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import { io } from 'socket.io-client';
import UserInfo from 'src/app/classes/UserInfo';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  websocket!: any;

  constructor() {
  }

  init(){

    this.websocket = io("ws://localhost:8000", {
      extraHeaders: {
        Authorization: "Bearer " + UserInfo.getAccessToken()
      }
    });

    this.websocket.on('SendDataToClient', (data:any) => {
      console.log("Data received!");
      debugger;
    })
  }

   send(data:any){
     this.websocket.emit("sendDataToServer",data)
   }
}
