import {Injectable} from "@angular/core";
import {AuthenticateRequest} from "../models/authenticate-request.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {TokenStorageService} from "./token-storage.service";

@Injectable({
  providedIn:'root'
})
export class AuthService {
  constructor(private http: HttpClient, private token: TokenStorageService) {
  }
  login(authenticateRequest: AuthenticateRequest){
    const url:string = environment.apiUrlUsers + 'auth/authenticate'
    return this.http.post<any>(url, authenticateRequest,{
      headers:{
        'Content-Type': 'application/json'
      }
    });
  }
}
