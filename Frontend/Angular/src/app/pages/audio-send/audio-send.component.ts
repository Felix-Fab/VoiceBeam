import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RecordRTCPromisesHandler } from 'recordrtc';
import "src/app/classes/ArrayBufferConverter";
import ArrayBufferConverter from 'src/app/classes/ArrayBufferConverter';
import UserInfo from 'src/app/classes/UserInfo';
import {WebsocketService} from "src/app/services/WebSocket/websocket.service";

@Component({
  selector: 'app-audio-send',
  templateUrl: './audio-send.component.html',
  styleUrls: ['./audio-send.component.css']
})
export class AudioSendComponent implements OnInit {
  recorder: any;

  constructor(private _webSocket: WebsocketService, private location:Location) {
    this.recorder = null;
  }

  async ngOnInit(): Promise<void> {
    let stream = await navigator.mediaDevices.getUserMedia({audio:true, video: false});
    this.recorder = new RecordRTCPromisesHandler(stream, {
      type: 'audio'
    });

    document.getElementById("ButtonSend")!.addEventListener('mousedown', (event) => {
      this.recorder.startRecording();
    });

    document.getElementById("ButtonSend")!.addEventListener('mouseup', async (event) => {
      await this.recorder.stopRecording();

      let AudioBlob = await this.recorder.getBlob();

      var Data = {
        Key: "message",
        from: UserInfo.getUsername,
        to: 'admin',
        data: await AudioBlob.text()
      }

      this._webSocket.send(JSON.stringify(Data));

      console.log("***Audio Blob Sended");

    });
  }
}
