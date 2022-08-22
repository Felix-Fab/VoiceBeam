import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatDialog} from '@angular/material/dialog';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import Http from 'src/app/classes/Http';
import { timer, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import * as $ from "jquery";

interface Users{
  users: [
    
  ]
}

interface User{
  _uid: string,
  username: string
}

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.css']
})
export class UserMenuComponent implements OnInit,OnDestroy {
  Subscription:any
  users: { username: string}[] = [

  ];

  constructor(private http: HttpClient,private dialog:MatDialog, private router:Router) {
  }

  ngOnInit(): void {
    const TimerTask = timer(0,5000);
    this.Subscription = TimerTask.subscribe(() => {
      this.updateUserMenu();
    });
  }

  ngOnDestroy() {    
    this.Subscription.unsubscribe();
  }

  updateUserMenu(){
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("accessToken")}`
    }

    this.http.post<Users>(Http.getAPIUrl() + "/auth/getUsers", {} , { headers }).subscribe({
      next: data => {
          this.users = data.users;

          var NoUsersTag = $("#NoUsers")[0] as HTMLParagraphElement;
          if(this.users.length <= 0){
            NoUsersTag.hidden = false;
          }else{
            NoUsersTag.hidden = true;
          }
      },
      error: error => {
        if(error.status === 401) {
          this.dialog.open(DialogError, {
            data:{
              title: 'Request Error',
              message: 'Not Authorized!'
            }
          });
        } else if(error.status === 403) {
          this.dialog.open(DialogError, {
            data:{
              title: 'Request Error',
              message: 'Access Token Invalid or Expired!'
            }
          });

          this.router.navigate(["/LoginMenu"]);
        }
      }
    });
  }

  onClickUser(username: string){
    this.router.navigate(["/AudioSend", username]);
  }
}
