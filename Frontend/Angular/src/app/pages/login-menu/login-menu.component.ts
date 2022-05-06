import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { disableDebugTools } from '@angular/platform-browser';
import * as $ from "jquery";
import { catchError, retry, throwError } from 'rxjs';
import { MAT_DIALOG_DATA,MatDialogModule, MatDialog ,MatDialogRef} from '@angular/material/dialog';
import { DialogLoginInvalid } from 'src/app/dialogs/Invalid/dialog-login-invalid';
import { DialogLoginError } from 'src/app/dialogs/Error/dialog-login-error';

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
          this.dialog.open(DialogLoginInvalid);
        }else{
          this.dialog.open(DialogLoginError);
          console.error('An error occurred:', error.error);
        }
      }
    })
  }
}
