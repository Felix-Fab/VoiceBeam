import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import Http from 'src/app/classes/Http';
import * as $ from "jquery";
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import { DialogInfo } from 'src/app/dialogs/Info/dialog-info';
import { Router } from '@angular/router';
import { WebsocketService } from 'src/app/services/WebSocket/websocket.service';

interface Config{
  info: string,
}

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {

  constructor(private http: HttpClient, private dialog:MatDialog, private router: Router, private _webSocket: WebsocketService) { }

  ngOnInit(): void {
    this._webSocket.disconnect();
  }

  createUser(){

    var EmailInput = $("#Email")[0] as HTMLInputElement;
    var UsernameInput = $("#Username")[0] as HTMLInputElement;
    var PasswordInput = $("#Passwort")[0] as HTMLInputElement;
    var PasswordAgainInput = $("#PasswortAgain")[0] as HTMLInputElement;
    var LoadingWrapper = $("#LoadingWrapper")[0] as HTMLDivElement;
    var RegisterText = $("#RegisterText")[0] as HTMLParagraphElement;

    RegisterText.hidden = true;
    LoadingWrapper.hidden = false;

    if(EmailInput.value == "" || UsernameInput.value == "" || PasswordInput.value == "" || PasswordAgainInput.value == ""){
      $(".InputField").each(function(index){
        $(this).css({"border-color": "red", "border-width":"1px", "border-style": "solid"});
      });

      LoadingWrapper.hidden = true;
      RegisterText.hidden = false;

      return;
    }else{
      $(".InputField").each(function(index){
        $(this).css({"border":"none"});
      });
    }

    if(PasswordInput.value != PasswordAgainInput.value){
      this.dialog.open(DialogError, {
        data:{
          title: 'Register Error',
          message: 'Passwords do not match!'
        }
      });

      LoadingWrapper.hidden = true;
      RegisterText.hidden = false;

      return;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    const body = {
      username: UsernameInput.value,
      email: EmailInput.value,
      password: PasswordInput.value
    }

    this.http.post<Config>(Http.getAPIUrl() + "/auth/register", body, {headers}).subscribe({
      next: data => {
          this.dialog.open(DialogInfo, {
            data:{
              title: 'Register Success',
              message: 'Account created successfully'
            }
          });

          this.router.navigate(["/LoginMenu"]);

          LoadingWrapper.hidden = true;
          RegisterText.hidden = false;
      },
      error: error => {
        LoadingWrapper.hidden = true;
        RegisterText.hidden = false;

        if(error.status === 400 || error.status === 401){
          this.dialog.open(DialogError, {
            data:{
              title: 'Register Error',
              message: 'Invalid Credentials!'
            }
          });
        }else{
          this.dialog.open(DialogError, {
            data: {
              title: 'Login Error',
              message: error.message
            }
          });
          console.error('An error occurred:', error.error);
        }
      }
    });
  }
}
