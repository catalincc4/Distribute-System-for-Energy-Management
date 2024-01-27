import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {UserDTO} from "../models/userDTO.model";
import {ConnectionDTO} from "../models/connectionDTO";
import {TokenStorageService} from "./token-storage.service";

@Injectable({
  providedIn: "root"
})
export class ConnectionService{

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
  getConnectionsForUser(user: UserDTO){
    const url = environment.apiUrlChat + 'connection/get-for-user/' + user.id
    return this.http.get<any>(url, this.httpOptions)
  }

  takeConnection(connectionId: string, userId: string){
    const url = environment.apiUrlChat + 'connection/take-connection/' + userId + '/' + connectionId;
    return this.http.patch<any>(url,{}, this.httpOptions)
  }

  createGroupConnection(activeConnection: ConnectionDTO) {
    const url = environment.apiUrlChat + 'connection/create-group-connection';
    return this.http.post<any>(url,activeConnection, this.httpOptions)
  }
}
