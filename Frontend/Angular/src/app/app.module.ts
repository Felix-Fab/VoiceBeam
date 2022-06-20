import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { MatDialogModule, } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AudioContextModule } from 'angular-audio-context';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TopBarComponent } from './top-bar/top-bar.component';
import { LoginMenuComponent} from './pages/login-menu/login-menu.component';
import { LoginRegisterComponent } from './pages/login-menu/register/login-register.component';
import { UserMenuComponent } from './pages/user-menu/user-menu.component';
import { DialogError } from './dialogs/Error/dialog-error';
import { DialogWarning } from './dialogs/Warning/dialog-warning';
import { DialogInfo } from './dialogs/Info/dialog-info';
import { AudioSendComponent } from './pages/audio-send/audio-send.component';
import { SettingsMenuComponent } from './pages/settings-menu/settings-menu.component'

@NgModule({
  declarations: [
    AppComponent,
    LoginMenuComponent,
    DialogError,
    DialogInfo,
    DialogWarning,
    LoginRegisterComponent,
    UserMenuComponent,
    TopBarComponent,
    AudioSendComponent,
    SettingsMenuComponent
  ],
  imports: [
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDialogModule,
    AudioContextModule.forRoot('balanced')
  ],
  exports: [],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule { }
