import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RecordRTCPromisesHandler } from 'recordrtc';
import "src/app/classes/ArrayBufferConverter";
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import {WebsocketService} from "src/app/services/WebSocket/websocket.service";

@Component({
  selector: 'app-audio-send',
  templateUrl: './audio-send.component.html',
  styleUrls: ['./audio-send.component.css']
})
export class AudioSendComponent implements OnInit {
  recorder: any;
  audioChunks: any;

  constructor(private _webSocket: WebsocketService, private location:Location) {
    // Empty
  }

  async ngOnInit(): Promise<void> {
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
        const audioBlob = new Blob(this.audioChunks);
        if (audioBlob.size === 0) {
          return;
        }

        const audioUrl = URL.createObjectURL(audioBlob);
        const audio = new Audio(audioUrl);
        console.log()
        audio.play();
      });
      this.recorder.stop();
    });
  }
}
