import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginMenuComponent } from './login-menu/login-menu.component';
import { LoginRegisterComponent } from './login-menu/register/login-register.component';
import { UserMenuComponent } from './user-menu/user-menu.component';

const routes: Routes = [
  { path:'', redirectTo: 'LoginMenu',pathMatch: 'full'},
  { path:'LoginMenu', component: LoginMenuComponent},
  { path: 'LoginMenu/register', component: LoginRegisterComponent},
  { path: 'UserMenu', component: UserMenuComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }