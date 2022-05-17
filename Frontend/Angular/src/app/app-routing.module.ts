import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AudioSendComponent } from './pages/audio-send/audio-send.component';
import { LoginMenuComponent } from './pages/login-menu/login-menu.component';
import { LoginRegisterComponent } from './pages/login-menu/register/login-register.component';
import { SettingsMenuComponent } from './pages/settings-menu/settings-menu.component';
import { UserMenuComponent } from './pages/user-menu/user-menu.component';

const routes: Routes = [
  { path:'', redirectTo: 'LoginMenu',pathMatch: 'full'},
  { path:'LoginMenu', component: LoginMenuComponent},
  { path: 'LoginMenu/register', component: LoginRegisterComponent},
  { path: 'UserMenu', component: UserMenuComponent},
  { path: 'AudioSend', component: AudioSendComponent},
  { path: 'SettingsMeni', component: SettingsMenuComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }