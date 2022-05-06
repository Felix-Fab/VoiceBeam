import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import * as $ from "jquery";
import { MatDialog} from '@angular/material/dialog';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';

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

    const headers = { 'Content-Type': 'application/json'};

    this.http.patch<Config>("http://37.114.34.153:3000/manager/login",{ email: EmailInput.value, password: PasswordInput.value},{ headers }).subscribe({
      next: data => {
        debugger;
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
    })
  }
}
