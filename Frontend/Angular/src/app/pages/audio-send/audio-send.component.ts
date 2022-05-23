import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ReadVarExpr } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RecordRTCPromisesHandler } from 'recordrtc';
import "src/app/classes/ArrayBufferConverter";
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import Http from 'src/app/classes/Http';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import {WebsocketService} from "src/app/services/WebSocket/websocket.service";

interface Messages{
  messages: [
    
  ]
}

interface Message{
  to: string,
  from: string
}

@Component({
  selector: 'app-audio-send',
  templateUrl: './audio-send.component.html',
  styleUrls: ['./audio-send.component.css']
})
export class AudioSendComponent implements OnInit {
  MessageHistory: {from: string, to:string, audioLength:number}[] = [

  ];

  recorder: any;
  audioChunks: any;

  constructor(public _webSocket: WebsocketService,private http: HttpClient,private dialog:MatDialog) {
    // Empty
  }

  async ngOnInit(): Promise<void> {
    const To = window.history.state.username;

    document.getElementById("ButtonSend")!.addEventListener('mousedown', (event) => {
      console.log("Recording...");
      navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
          this.recorder = new MediaRecorder(stream);
          this.recorder.start();

          this.audioChunks = [];

          this.recorder.addEventListener("dataavailable", (event: { data: any; }) => {
            this.audioChunks.push(event.data);
          });
        });
    });

    document.getElementById("ButtonSend")!.addEventListener('mouseup', async (event) => {

      if (typeof this.recorder === "undefined") {
        return;
      }

      this.recorder.addEventListener("stop", () => {
        console.log(this.audioChunks);
        const audioBlob = new Blob(this.audioChunks);
        if (audioBlob.size === 0) {
          return;
        }

        var data = {
          from: localStorage.getItem("username"),
          to: localStorage.getItem("username"),
          accessToken: localStorage.getItem("accessToken"),
          data: audioBlob
        }

        debugger;

        this._webSocket.send(data);
      });
      this.recorder.stop();
    });

    this.loadHistory();
  }

  loadHistory() {
    
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("accessToken")}`
    }

    const body = {
      username1: localStorage.getItem("username"), username2: window.history.state.username
    }

    this.http.post<Messages>(Http.getAPIUrl() + "/messages/getMessages", body, {headers}).subscribe({
      next: data => {
        this.MessageHistory = data.messages;
      },
      error: error => {
        if(error.status === 400 || error.status === 401){
          this.dialog.open(DialogError, {
            data:{
              title: 'Login Error',
              message: 'Invalid Credentials!'
            }
          });
        }else{
          this.dialog.open(DialogError, {
            data: {
              title: 'Login Error',
              message: 'Unknown Error'
            }
          });
          console.error('An error occurred:', error.error);
        }
      }
    });
  }
}
