import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs'
import * as SockJS from 'sockjs-client'
import {environment} from "../../environments/environment";
@Injectable({
  providedIn: 'root',
})
export class WebSocketDeviceService {
  private stompClient!: Stomp.Client;
  private socket!: WebSocket;

  constructor() {
  }

  initializeWebSocketConnection() {
     this.socket = new SockJS(environment.webSocketUrlDevice);
     this.stompClient = Stomp.over(this.socket);
     return this.stompClient;
  }
}
