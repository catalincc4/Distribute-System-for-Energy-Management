import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {TokenService} from "../services/token.service";
import {TokenStorageService} from "../services/token-storage.service";

@Injectable({
  providedIn: "root"
})
export class TokenExpirationInterceptor implements HttpInterceptor {
  constructor(private router: Router, private tokenService: TokenService, private tokenStorageService: TokenStorageService)
  {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenStorageService.getToken();
    const decodedToken = this.tokenService.getDecodedAccessToken(token);
    if(decodedToken.expires){
      this.tokenStorageService.signOut();
      this.router.navigate(['/login']);
    }
    return next.handle(req);
  }
}
