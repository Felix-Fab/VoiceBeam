import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ReadVarExpr } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { RecordRTCPromisesHandler } from 'recordrtc';
import { timer } from 'rxjs';
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
export class AudioSendComponent implements OnInit,OnDestroy {
  MessageHistory: {from: string, to:string, audioLength:number, class: string, icon: string, text: string}[] = [

  ];

  recorder: any;
  audioChunks: any;

  Subscription:any
  To: string | null;

  constructor(public _webSocket: WebsocketService,private http: HttpClient,private dialog:MatDialog,private router: Router, public route: ActivatedRoute) {
    this.To = route.snapshot.paramMap.get("username")
  }

  async ngOnInit(): Promise<void> {

    document.getElementById("ButtonSend")!.addEventListener('mousedown', () => {

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

    document.getElementById("ButtonSend")!.addEventListener('touchstart', () => {

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

    document.getElementById("ButtonSend")!.addEventListener('mouseup', () => {

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
          to: this.To,
          accessToken: localStorage.getItem("accessToken"),
          data: audioBlob
        }
  
        debugger;
  
        this._webSocket.send(data);
      });
      this.recorder.stop();

    });

    document.getElementById("ButtonSend")!.addEventListener('touchend', () => {

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
          to: this.To,
          accessToken: localStorage.getItem("accessToken"),
          data: audioBlob
        }
  
        debugger;
  
        this._webSocket.send(data);
      });
      this.recorder.stop();

    });

    const TimerTask = timer(0,5000);
    this.Subscription = TimerTask.subscribe(() => {
      this.loadHistory();
    });
  }

  ngOnDestroy() {    
    this.Subscription.unsubscribe();
  }

  loadHistory() {
    
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("accessToken")}`
    }

    const body = {
      username1: localStorage.getItem("username"), username2: this.route.snapshot.paramMap.get("username")
    }

    this.http.post<Messages>(Http.getAPIUrl() + "/messages/getMessages", body, {headers}).subscribe({
      next: data => {
        this.MessageHistory = data.messages;

        this.MessageHistory.forEach(Message => {
          if(Message.from == localStorage.getItem("username")){
            Message.class = "MessageLeft";
            Message.icon = "send";
            Message.text = "Gesendet";
          }else{
            Message.class = "MessageRight";
            Message.icon = "reply";
            Message.text = "Empfangen";
          }
        });
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

  navigateToUserMenu(){
    this.router.navigate(["/UserMenu"]);
  }
}
