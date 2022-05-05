import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {MatDialog} from '@angular/material/dialog';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TopBarComponent } from './top-bar/top-bar.component';
import { LoginMenuComponent, DialogLoginInvalid, DialogLoginError } from './login-menu/login-menu.component';
import { LoginRegisterComponent } from './login-menu/register/login-register.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginMenuComponent,
    DialogLoginError,
    DialogLoginInvalid,
    TopBarComponent,
    LoginRegisterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatDialog
  ],
  providers: [],
  bootstrap: [AppComponent,LoginMenuComponent]
})
export class AppModule { }
