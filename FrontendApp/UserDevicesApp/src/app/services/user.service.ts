import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {TokenStorageService} from "./token-storage.service";
import {UserDTO} from "../models/userDTO.model";

@Injectable({
  providedIn: "root"
})
export class UserService{

  constructor(private http: HttpClient,
              private token: TokenStorageService
  ){
  }

  httpOptions = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.token.getToken()
    }
  }
  getUsers(){
    const url = environment.apiUrlUsers + 'user/all'
    return this.http.get<any>(url, this.httpOptions);
  }

  deleteUser(username : string){
    const url = environment.apiUrlUsers + 'user/deleteUser/' + username;
    return this.http.delete<any>(url, this.httpOptions);
  }
  updateUser(userDTO: UserDTO){
    const url = environment.apiUrlUsers + 'user/updateUser';
    return this.http.put<any>(url, userDTO, this.httpOptions);
  }

  addUser(userDTO: UserDTO){
    const url = environment.apiUrlUsers + 'user/addUser';
    return this.http.post<any>(url, userDTO, this.httpOptions);
  }
}
