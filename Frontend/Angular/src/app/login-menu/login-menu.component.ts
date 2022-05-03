import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login-menu',
  templateUrl: './login-menu.component.html',
  styleUrls: ['./login-menu.component.css']
})
export class LoginMenuComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  changeColor(event: MouseEvent){
    const button = event.target as HTMLButtonElement;
    button.style.color = "Green";
    debugger;
  }

}
