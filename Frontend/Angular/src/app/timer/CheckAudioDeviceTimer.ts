import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { AudioContext } from 'angular-audio-context';
import Http from '../classes/Http';
import { DialogError } from '../dialogs/Error/dialog-error';

interface StatusConfig{
    username:string,
    email:string,
    status:boolean
  }

export default class CheckAudioDeviceListener {

    public static startCheckStatus(navigator: Navigator,http:HttpClient,dialog:MatDialog){
        
        navigator.mediaDevices.addEventListener('devicechange', () => {
            var mics = 0;
            var micStatus = false;

            navigator.mediaDevices.enumerateDevices()
            .then(function(devices){
              devices.forEach(device => {
                if(device.kind == 'audioinput'){
                  mics++;
                }
              });
            });

            if(mics >= 2){
              micStatus = true;
            }

            const headers = new HttpHeaders({
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${localStorage.getItem("accessToken")}`
            });
        
            const body = {
              status: micStatus
            }

            http.post<StatusConfig>(Http.getAPIUrl() + "/auth/login", body, {headers}).subscribe({
              next: data => {
                debugger;
              },
              error: error => {
                console.error('An error occurred:', error.error);

                switch(error.status){
                  case 400:
                    dialog.open(DialogError, {
                      data: {
                        title: 'Status Change Error',
                        message: 'Invalid Credentials!'
                      }
                    });
                  break;

                  case 401:
                    dialog.open(DialogError, {
                      data: {
                        title: 'Status Change Error',
                        message: 'Not Authorized!'
                      }
                    });
                  break;

                  case 403:
                    dialog.open(DialogError, {
                      data:{
                        title: 'Status Change Error',
                        message: 'Access Token Invalid or Expired!'
                      }
                    });
                  break;

                  default:
                    dialog.open(DialogError, {
                      data: {
                        title: 'Status Change Error',
                        message: 'Unknown Error'
                      }
                    });
                  break;
                }
              }
            });
        });
    }

    public static stopCheckStatus(navigator: Navigator){
        navigator.mediaDevices!.removeEventListener("devicechange", (ev) => {

        });
    }
}