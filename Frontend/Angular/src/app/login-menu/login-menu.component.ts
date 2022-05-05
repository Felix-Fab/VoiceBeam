import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { disableDebugTools } from '@angular/platform-browser';
import * as $ from "jquery";
import { catchError, retry, throwError } from 'rxjs';
import {MatDialog} from '@angular/material/dialog';

interface Config{
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

  constructor(private http: HttpClient,private dialog:MatDialog) { }

  ngOnInit(): void {
  }

  onButtonLoginClick(event: MouseEvent){
    var EmailInput = $("#email")[0] as HTMLInputElement;
    var PasswordInput = $("#password")[0] as HTMLInputElement;

    debugger;

    const headers = { 'Content-Type': 'application/json'};

    var data;

    this.http.patch<Config>("http://37.114.34.153:3000/manager/login",{ email: EmailInput.value, password: PasswordInput.value},{ headers }).subscribe({
      next: data => {
        debugger;
      },
      error: error => {
        if(error.status === 400 || error.status === 401){
          this.dialog.open(DialogLoginInvalid);
        }else{
          this.dialog.open(DialogLoginError);
          console.error('An error occurred:', error.error);
        }
      }
    })
  }
}

@Component({
  selector: 'dialog-login-invalid',
  template: 'dialog-login-invalid.html'
})
export class DialogLoginInvalid {}

@Component({
  selector: 'dialog-login-error',
  template: 'dialog-login-error.html'
})
export class DialogLoginError {}
