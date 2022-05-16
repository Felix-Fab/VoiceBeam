import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import UserInfo from 'src/app/classes/UserInfo';
import { MatDialog} from '@angular/material/dialog';
import { DialogError } from 'src/app/dialogs/Error/dialog-error';
import Http from 'src/app/classes/Http';
import { Router } from '@angular/router';

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
export class UserMenuComponent implements OnInit {
  users: { username: string}[] = [

  ];

  constructor(private http: HttpClient,private dialog:MatDialog, private router:Router) {
  }

  ngOnInit(): void {

    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${UserInfo.getAccessToken()}`
    }

    debugger;

    var hallo = this.http.post<Users>(Http.getServerUrl() + ":3000/manager/getUsers", {} , {headers}).subscribe({
      next: data => {
        debugger;
          this.users = data.users;
      },
      error: error => {

        if(error.status === 401){
          this.dialog.open(DialogError, {
            data:{
              title: 'Request Error',
              message: 'Not Authorized!'
            }
          });
        }else if(error.status === 403){
          this.dialog.open(DialogError, {
            data:{
              title: 'Request Error',
              message: 'Access Token Invalid or Expired!'
            }
          });
        }
      }
    })
  }

  onClickUser(username: string){
    this.router.navigateByUrl('/AudioSend', { state: { username: username } });
  }
}
