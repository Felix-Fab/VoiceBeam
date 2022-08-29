import { Injectable } from '@angular/core';
import { io, Socket } from 'socket.io-client';
import Http from 'src/app/classes/Http';
import { MatDialog } from '@angular/material/dialog';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  websocket!: Socket;

  constructor(private dialog:MatDialog, private router: Router) { }

  init(){

    this.websocket = io(Http.getWebSocketUrl(), {
      extraHeaders: {
        Authorization: `${localStorage.getItem("accessToken")}`
      }
    });
    console.log("Connecting to WebSocket...");

    this.websocket.on('SendDataToClient', (data:any) => {

      console.log("Nachricht empfangen");

      var blob = new Blob([data.data], { type: 'audio/webm;codecs=opus' });

      const audioUrl = URL.createObjectURL(blob);
      const audio = new Audio(audioUrl);
      audio.play();
    });

    this.websocket.on("ServerStatusResponse", (data:any) => {

      this.dialog.open(DialogError, {
        data:{
          title: 'Audio Send Error',
          message: data.message,
        }
      });

      if(data.status == 301){
        this.router.navigate(["/UserMenu"]);
      }
    });

    this.websocket.on('disconnected', () => {
      console.log("WebSocket disconnect");
      /* TODO: Insecure! You could potentially kill other sessions! */
      this.websocket.emit('ClientDisconnect', localStorage.getItem("username"));
      this.router.navigate(["/LoginMenu"]);
    });

    this.websocket.on("connect", () => {
       console.log("Connected to WebSocket");
    });
  }

  disconnect(){
    if(this.websocket != undefined){
      this.websocket.disconnect();
      console.log("Disconnected from WebSocket Server");
    }
  }

  send(data:any){
    if(this.websocket != undefined && !this.websocket.disconnected){
      this.websocket.emit("sendDataToServer",data)
    }else{
      this.router.navigate(["/LoginMenu"]);
    }
  }
}
