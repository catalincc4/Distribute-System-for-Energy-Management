import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {UserDTO} from "../models/userDTO.model";
import {MessageDTO} from "../models/messageDTO";
import {TokenStorageService} from "./token-storage.service";

@Injectable({
  providedIn: "root"
})
export class MessageService{

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
  addConnection(message: MessageDTO){
    const url = environment.apiUrlChat + 'message/add-connection'
    return this.http.post<any>(url,message, this.httpOptions)
  }

  sendMessage(message: MessageDTO){
    const url = environment.apiUrlChat + 'message/add-message'
    return this.http.put<any>(url,message, this.httpOptions)
  }

  setAsSeenMessages(unSeenMessages: MessageDTO[]) {
    let unSeenMessagesIds: string[] = unSeenMessages.map( m => m.id);
    const url = environment.apiUrlChat + 'message/message-seen'
    return this.http.put<any>(url,unSeenMessagesIds, this.httpOptions)
  }
}
