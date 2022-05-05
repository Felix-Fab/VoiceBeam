import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.css']
})
export class UserMenuComponent implements OnInit {
  users = [
    {
      username: "Test"
    },
    {
      username: "Test2"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    {
      username: "Test"
    },
    
  ]

  constructor() {}

  ngOnInit(): void {
  }

}
