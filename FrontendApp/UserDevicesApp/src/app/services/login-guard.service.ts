import {inject, Injectable} from "@angular/core";
import {TokenStorageService} from "./token-storage.service";
import {ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot} from "@angular/router";
import {AuthGuardService} from "./auth-guard.service";

@Injectable({
  providedIn: "root"
})
export class LoginGuardService{
  constructor(private tokenStorageService: TokenStorageService) {
  }
  canActivate(){
    return this.tokenStorageService.getToken() === ''
  }
}

export const canActivateLoginPage : CanActivateFn =
  (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    return inject(LoginGuardService).canActivate();
  };
