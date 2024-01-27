import {AfterContentInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {UserDTO} from "../../models/userDTO.model";
import {take} from "rxjs";
import {TokenStorageService} from "../../services/token-storage.service";
import {TokenService} from "../../services/token.service";
import {DeviceDTO} from "../../models/deviceDTO.model";
import {DeviceService} from "../../services/device.service";
import {UserService} from "../../services/user.service";
@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit, AfterContentInit{
  connectedUser = new UserDTO();
  users: UserDTO[] = [];
  devices: DeviceDTO[] = [];
  showUsers: boolean = true;
  showDevices: boolean = false;
  showPopup: boolean = false;
  userToUse!: UserDTO;
  deviceToUse!: DeviceDTO;
  editMode: number = 1;
  showEditPopup: boolean = false;
  userSawWarning= false;
  showWarning= false;
  showMessanger = false;
  isAdmin = false;
  constructor(
    private tokenStorageService: TokenStorageService,
    private tokenService: TokenService,
    private deviceService: DeviceService,
    private userService: UserService,
    private cdRef: ChangeDetectorRef,
  ){}
  ngOnInit() {
    const token = this.tokenStorageService.getToken();
    if(token !== null) {
      const decodeToken = this.tokenService.getDecodedAccessToken(token);
      this.connectedUser.id = decodeToken.user.id;
      this.connectedUser.role = decodeToken.role;
      this.connectedUser.username = decodeToken.user.username;
    }
    if(this.connectedUser.role == "ADMIN") {
      this.isAdmin = true;
      this.getUsers();
      this.getDevices();
    }else{
      this.showUsers = false;
      this.showDevices = true;
      this.getDevicesForUser();
    }

  }
  getUsers(){
    this.userService.getUsers().pipe(take(1)).subscribe({
      next: data => {
        this.users = data;
      },
      error: error => { console.log('Error while fetching users')},
      complete: () => {this.users = this.users.filter(u => u.id !== this.connectedUser.id)}
    })
  }
  getDevices(){
    this.deviceService.getDevices().pipe(take(1)).subscribe({
      next: data => {
        this.devices = data;
      },
      error: error => { console.log('Error while fetching devices')},
      complete: () => {}
    })
  }

  getDevicesForUser(){
    this.deviceService.getDevicesForUser(this.connectedUser.id).pipe(take(1)).subscribe({
      next: data => {
        this.devices = data;
      },
      error: error => { console.log('Error while fetching devices for user')},
      complete: () => {}
    })
  }

  switchDataToDevices(){
    this.showUsers = false;
    this.showDevices = true;
  }

  switchDataToUsers(){
    this.showUsers = true;
    this.showDevices = false;
  }

  ngAfterContentInit(): void {
    this.cdRef.detectChanges();
  }

  editDevice(device : DeviceDTO){
    this.deviceToUse = device;
    this.showEditPopup = true;
    this.editMode = 2;
  }

  editUser(user: UserDTO){
    this.userToUse = user;
    this.showEditPopup = true;
    this.editMode = 1;
  }

  deleteDevice(device: DeviceDTO){
    this.deviceToUse = device;
    this.showPopup = true;
  }

  deleteUser(user: UserDTO){
    this.userToUse = user;
    this.showPopup = true;
  }

  deleteElement(){
    if(this.showDevices){
      this.deviceService.deleteDevice(this.deviceToUse.id).pipe(take(1)).subscribe()
      this.devices = this.devices.filter(d => d.id !== this.deviceToUse.id);
    }
    if(this.showUsers){
      this.userService.deleteUser(this.userToUse.username).pipe(take(1)).subscribe()
      this.users = this.users.filter(u => u.id !== this.userToUse.id);
      this.devices = this.devices.filter( d => d.userId !== this.userToUse.id);
    }
    this.showPopup = false
  }

  hidePopupEdit(){
    this.showEditPopup = false
  }

  addElement(){
    if(this.showUsers){
      this.userToUse = <UserDTO>{id:'',username:'',
      lastname:'',
      role:'',
        password:'',
      };
      this.editMode = 3;
      this.showEditPopup = true
    }
    if(this.showDevices){
      this.deviceToUse = {id:'',description:'', address: '', maxHourlyEnergyConsumption: 0, userId:''};
      this.editMode = 4;
      this.showEditPopup = true;
    }
  }

  getUsername(userId: string){
    const users = this.users.filter(u => u.id === userId);
    if(users.length === 0){
      return 'You';
    }
    return users[0].username;
  }

  handleWarning(warning: boolean, device: DeviceDTO){
    if(warning && !this.userSawWarning){
      this.showWarning = true;
    }else{
      this.showWarning = false;
    }
  }
}
