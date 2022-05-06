import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { MatDialogModule, } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TopBarComponent } from './top-bar/top-bar.component';
import { LoginMenuComponent} from './pages/login-menu/login-menu.component';
import { LoginRegisterComponent } from './pages/login-menu/register/login-register.component';
import { UserMenuComponent } from './pages/user-menu/user-menu.component';
import { DialogLoginError } from './dialogs/Error/dialog-login-error';
import { DialogLoginInvalid } from './dialogs/Invalid/dialog-login-invalid';

@NgModule({
  declarations: [
    AppComponent,
    LoginMenuComponent,
    DialogLoginError,
    DialogLoginInvalid,
    TopBarComponent,
    LoginRegisterComponent,
    UserMenuComponent
  ],
  imports: [
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDialogModule
  ],
  exports: [],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
