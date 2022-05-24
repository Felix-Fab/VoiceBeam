import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import Http from 'src/app/classes/Http';
import * as $ from "jquery";
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import { DialogInfo } from 'src/app/dialogs/Info/dialog-info';
import { Router } from '@angular/router';

interface Config{
  info: string,
}

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {

  constructor(private http: HttpClient, private dialog:MatDialog, private router: Router) { }

  ngOnInit(): void {
  }

  createUser(){

    var EmailInput = $("#email")[0] as HTMLInputElement;
    var UsernameInput = $("#username")[0] as HTMLInputElement;
    var PasswordInput = $("#password")[0] as HTMLInputElement;

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
          debugger;

          this.dialog.open(DialogInfo, {
            data:{
              title: 'Register Success',
              message: 'Account created successfully'
            }
          });

          this.router.navigate(["/LoginMenu"]);
      },
      error: error => {
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
              message: 'Unknown Error'
            }
          });
          console.error('An error occurred:', error.error);
        }
      }
    });
  }
}
