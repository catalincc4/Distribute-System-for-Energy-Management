import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs'
import * as SockJS from 'sockjs-client'
import {environment} from "../../environments/environment";
import {MessageDTO} from "../models/messageDTO";
@Injectable({
  providedIn: 'root',
})
export class WebsocketChatService {
  // @ts-ignore
  private stompClient: Stomp.Client;
  private socket!: WebSocket;

  constructor() {
  }

  initializeWebSocketConnection() {
    this.socket = new SockJS(environment.webSocketUrlChat);
    this.stompClient = Stomp.over(this.socket);
    return this.stompClient;
  }

  someoneIsTyping(message: string, connectionId:string) {
    this.stompClient.send('/message/' + connectionId, {}, message);
  }

  awareAdmins(message: MessageDTO){
    this.stompClient.send('/new-connection', {}, JSON.stringify({message}));
  }
}
