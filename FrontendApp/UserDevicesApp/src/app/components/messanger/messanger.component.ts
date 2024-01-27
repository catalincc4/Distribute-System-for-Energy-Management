import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnInit, SimpleChanges,
  ViewChild
} from '@angular/core';
import {ConnectionDTO} from "../../models/connectionDTO";
import {UserDTO} from "../../models/userDTO.model";
import {ConnectionService} from "../../services/connection.service";
import {take} from "rxjs";
import {MessageDTO} from "../../models/messageDTO";
import {WebsocketChatService} from "../../services/websocket-chat.service";
import {MessageService} from "../../services/message.service";
import {TypingService} from "../../services/typing.service";

@Component({
  selector: 'messanger',
  templateUrl: './messanger.component.html',
  styleUrls: ['./messanger.component.scss']
})
export class MessangerComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() user!: UserDTO
  @Input() users!: UserDTO[]
  connections!: ConnectionDTO[];
  isAdmin = false;
  newConnection = false;
  activeConnection!: ConnectionDTO;
  showMessages = false;
  message!: string
  showTypingInfo = false;
  selected!: boolean[];
  @ViewChild('messagesSection') messagesSection!: ElementRef<HTMLElement>;

  constructor(private connectionService: ConnectionService,
              private webSocketService: WebsocketChatService,
              private cdRef: ChangeDetectorRef,
              private messageService: MessageService,
              private typingService: TypingService
  ) {
  }

  ngOnInit() {
    if (this.user && this.user.role == "ADMIN") {
      this.isAdmin = true;
    }
    this.getConnectionsForUser();

    this.typingService.getTypingStatus().subscribe((typing) => {
      this.showTypingInfo = typing;
    });
    if (this.isAdmin) {
      const stompClient = this.webSocketService.initializeWebSocketConnection();
      stompClient.connect({}, (frame) => {
          stompClient.subscribe('/topic/messages', (response) => {
          let data: MessageDTO = JSON.parse(response.body);
          this.connections.push({id:data.connectionId, connectedUsers: [data.sender], messages:[data], adminUser: null});
          this.startNewWebSocketConnection(data.connectionId);
        });
      });
    } else {
      const stompClient = this.webSocketService.initializeWebSocketConnection();
      stompClient.connect({}, (frame) => {
        stompClient.subscribe('/topic/connection/user/' + this.user.id, (response) => {
          let data: ConnectionDTO = JSON.parse(response.body);
          this.connections.push(data);
          this.startNewWebSocketConnection(data.id);
        });
      });
    }
    this.selected = new Array(this.users.length).fill(false);
  }

  startNewWebSocketConnection(connectionId: string){
    const stompClient = this.webSocketService.initializeWebSocketConnection();
    const stompClient2 = this.webSocketService.initializeWebSocketConnection();
    stompClient.connect({}, (frame) => {
      stompClient.subscribe('/topic/connection/' + connectionId, (response) => {
        let data: MessageDTO = JSON.parse(response.body);
        if (data.sender.username != this.user.username) {
          this.connections[this.connections.length-1].messages.push(data);
          if (this.showMessages) {
            this.scrollToBottom();
          }
        }
      });
    });
    stompClient2.connect({}, (frame) => {
      stompClient2.subscribe('/topic/typing/' + connectionId, (response) => {
        let data: string = response.body;
        if (data != this.user.username) {
          this.showTypingForUser(this.connections.length-1);
        }
      });
    });
  }
  getConnectionName(connection: ConnectionDTO) {
    if (this.isAdmin) {
      var resultName = '';
      connection.connectedUsers.forEach((u) => {
        resultName += u.username + ','
      });
      return resultName.slice(0, -1);
    } else {
      if (connection.adminUser == null) {
        return 'New connection'
      }
      let anotherUsers = '('
      connection.connectedUsers.forEach( u => {if(u.username !== this.user.username){ anotherUsers = anotherUsers + u.username + ', '}})
      if(connection.connectedUsers.length > 1){
        return connection.adminUser.username + anotherUsers.slice(0, -2)+ ')';
      }
      else{
        return connection.adminUser.username;
      }
    }
  }

  getConnectionsForUser() {
    this.connectionService.getConnectionsForUser(this.user).pipe(take(1)).subscribe(data => {
      this.connections = data;
      for (let i = 0; i < this.connections.length; i++) {
        const stompClient = this.webSocketService.initializeWebSocketConnection();
        const stompClient2 = this.webSocketService.initializeWebSocketConnection();
        stompClient.connect({}, (frame) => {
          stompClient.subscribe('/topic/connection/' + this.connections[i].id, (response) => {
            let data: MessageDTO = JSON.parse(response.body);
            if (data.sender.username != this.user.username) {
              let message = this.connections[i].messages.find(m => m.id === data.id)
              if(message){
                message.status = data.status;
                this.connections[i].messages.forEach( m =>{
                  i
                } )
              }else {
              this.connections[i].messages.push(data);
              if (this.showMessages) {
                this.scrollToBottom();
              }
              }
            }
          });
        });
        stompClient2.connect({}, (frame) => {
          stompClient2.subscribe('/topic/typing/' + this.connections[i].id, (response) => {
            let data: string = response.body;
            if (data != this.user.username) {
              this.showTypingForUser(i);
            }
          });
        });
      }

    })
  }

  showTypingForUser(index: number) {
    if (this.showMessages && this.activeConnection.id === this.connections[index].id) {
      this.typingService.startTyping();
      if (this.showMessages) {
        this.scrollToBottom();
      }
    }
  }

  makeNewConnection() {
    this.newConnection = true;
    if (!this.isAdmin) {
      this.showMessages = true;
      this.activeConnection = {id: '0', connectedUsers: [this.user], adminUser: null, messages: []};
    }
  }

  createNewGroupConnection(){
    let users = this.users.filter((u,i) => this.selected[i])
    this.activeConnection = {id:'', connectedUsers: users, messages:[], adminUser:this.user}
    this.newConnection = false;
    this.showMessages = true;
    this.connectionService.createGroupConnection(this.activeConnection).pipe(take(1)).subscribe(data =>{
        this.activeConnection.id = data.id;
        this.connections.push(this.activeConnection);
        this.startNewWebSocketConnection(this.activeConnection.id);
    })
  }

  sendMessage() {
    if (this.message !== '') {
      if (this.newConnection) {
        this.newConnection = false;
        const messageDTO: MessageDTO = {id:'',message: this.message, sender: this.user, connectionId: '', status:'SEND'};
        this.activeConnection.messages.push(messageDTO)
        this.scrollToBottom();
        this.messageService.addConnection(messageDTO).pipe(take(1)).subscribe(
          data => {
            this.activeConnection.id = data.connectionId;
            this.connections.push(this.activeConnection)
            this.startNewWebSocketConnection(data.connectionId);
          }
        )
        this.webSocketService.awareAdmins(messageDTO);
      } else {
        const messageDTO: MessageDTO = {
          id:'',
          message: this.message,
          sender: this.user,
          connectionId: this.activeConnection.id,
          status:'SEND'
        };
        this.activeConnection.messages.push(messageDTO)
        this.scrollToBottom();
        this.messageService.sendMessage(messageDTO).pipe(take(1)).subscribe()
      }
      this.message = '';
    }
  }

  scrollToBottom(): void {
    this.cdRef.detectChanges()
    this.messagesSection.nativeElement.scrollTop = this.messagesSection.nativeElement.scrollHeight;
  }

  ngAfterViewInit(): void {
    this.cdRef.detectChanges()
  }

  ngOnChanges(): void {
    this.cdRef.detectChanges()
  }

  openConversation(connection: ConnectionDTO) {
    this.message = '';
    this.activeConnection = connection;

    let unSeenMessages = connection.messages.filter(m => m.status === 'SEND');
    this.messageService.setAsSeenMessages(unSeenMessages).pipe(take(1)).subscribe();

    if(this.activeConnection.messages == null){
      this.activeConnection.messages = [];
    }
    this.showMessages = true;
    if (connection.adminUser == null && this.isAdmin) {
      connection.adminUser = this.user;
      this.connectionService.takeConnection(connection.id, this.user.id).pipe(take(1)).subscribe();
    }
    this.scrollToBottom();
  }

  sendTypingInfo() {
    this.webSocketService.someoneIsTyping(this.user.username, this.activeConnection.id)
  }

  hasUnreadMessages(conn: ConnectionDTO) {
    for(let m of conn.messages){
      if(m.status == "SEND"){
        return true;
      }
    }
    return false;
  }
}
