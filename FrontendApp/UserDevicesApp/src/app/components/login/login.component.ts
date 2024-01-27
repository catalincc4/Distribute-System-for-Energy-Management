import { Component } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {AuthenticateRequest} from "../../models/authenticate-request.model";
import {take} from "rxjs";
import {TokenStorageService} from "../../services/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username : string ="";
  password : string ="";
  usernameInputEmpty: boolean = false;
  passwordInputEmpty: boolean = false;
  loginFailed: boolean = false;

  constructor(
    private authService: AuthService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {
  }

  submit(){
    if(this.username !== '' && this.password !== ''){
      const authRequest = new AuthenticateRequest();
      authRequest.password = this.password;
      authRequest.username = this.username;
      this.authService.login(authRequest).pipe(take(1)).subscribe(
        {next: data => {
        this.tokenStorageService.saveToken(data.token);
        this.router.navigate(['/home']);
      },
          error: error => { this.loginFailed = true},
          complete: () => {}})
    }else{
      if(this.username === ''){
        this.usernameInputEmpty = true;
      }
      if(this.password === ''){
        this.passwordInputEmpty = true;
      }
    }
  }

  usernameChanged(){
    if(this.username !== ''){
      this.usernameInputEmpty = false;
    }
  }
  passwordChanged(){
    if(this.password !== ''){
      this.passwordInputEmpty = false;
    }
  }
}
