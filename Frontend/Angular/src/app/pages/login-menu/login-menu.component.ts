import { HttpClient, HttpContext, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import * as $ from "jquery";
import { MatDialog} from '@angular/material/dialog';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import { Router } from '@angular/router';
import { WebsocketService } from "src/app/services/WebSocket/websocket.service";
import Http from 'src/app/classes/Http';
import { CookieService } from 'ngx-cookie';

interface LoginConfig{
  username: string,
  email: string,
  accessToken: string
}

@Component({
  selector: 'app-login-menu',
  templateUrl: './login-menu.component.html',
  styleUrls: ['./login-menu.component.css']
})
export class LoginMenuComponent implements OnInit {
  Subscription: any;

  constructor(private http: HttpClient,private dialog:MatDialog,private router: Router, private _webSocket: WebsocketService, private cookieService: CookieService) { }

  ngOnInit(): void {

    var hallo = this.cookieService.get("VoiceBeam-Email");

    this._webSocket.disconnect();
  }

  onButtonLoginClick(event: MouseEvent){
    var EmailInput = $("#Email")[0] as HTMLInputElement;
    var PasswordInput = $("#Passwort")[0] as HTMLInputElement;
    var LoadingWrapper = $("#LoadingWrapper")[0] as HTMLDivElement;
    var LoginText = $("#LoginText")[0] as HTMLParagraphElement;

    LoginText.hidden = true;
    LoadingWrapper.hidden = false;

    if(EmailInput.value == "" || PasswordInput.value == ""){
      $(".InputField").each(function(index){
        $(this).css({"border-color": "red", "border-width":"1px", "border-style": "solid"});
      });

      LoadingWrapper.hidden = true;
      LoginText.hidden = false;

      return;
    }else{
      $(".InputField").each(function(index){
        $(this).css({"border":"none"});
      });
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    const body = {
      email: EmailInput.value, password: PasswordInput.value
    }

    this.http.post<LoginConfig>(Http.getAPIUrl() + "/auth/login", body, {headers}).subscribe({
      next: data => {
        localStorage.setItem("username", data.username);
        localStorage.setItem("email", data.email);
        localStorage.setItem("accessToken", data.accessToken);

        this._webSocket.init();

        this.router.navigate(["/UserMenu"]);

        LoadingWrapper.hidden = true;
        LoginText.hidden = false;
      },
      error: error => {
        LoadingWrapper.hidden = true;
        LoginText.hidden = false;
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
              message: error.message
            }
          });
          console.error('An error occurred:', error.error);
        }
      }
    })
  }
}
