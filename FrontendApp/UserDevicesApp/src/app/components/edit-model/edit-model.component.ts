import {
  AfterContentInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {UserDTO} from "../../models/userDTO.model";
import {DeviceDTO} from "../../models/deviceDTO.model";
import {DeviceService} from "../../services/device.service";
import {UserService} from "../../services/user.service";
import {take} from "rxjs";

@Component({
  selector: 'edit-model',
  templateUrl: './edit-model.component.html',
  styleUrls: ['./edit-model.component.scss']
})
export class EditModelComponent implements OnInit,AfterContentInit{
  @Input() user!: UserDTO;
  @Input() device!: DeviceDTO;
  @Input() devices: DeviceDTO[] = [];
  @Input() mode: number = 1;
  @Output() hidePopup: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() users: UserDTO[] = [];
  usernameCheck: boolean = true;
  firstnameCheck: boolean = true;
  lastnameCheck: boolean = true;
  roleCheck: boolean = true;
  descriptionCheck: boolean = true;
  addressCheck: boolean = true;
  energyConsumptionCheck: boolean = true;
  userCheck: boolean = true;
  passwordCheck: boolean = true;
  constructor(
    private cdRef: ChangeDetectorRef,
    private deviceService: DeviceService,
    private userService: UserService
    ) {
  }

  ngOnInit() {
  }

  save(){
    if(this.mode === 1 && this.validateUser(this.user)){
     this.userService.updateUser(this.user).pipe(take(1)).subscribe();
      this.hidePopup.emit(false);
    }
    if(this.mode === 2  && this.validateDevice(this.device)){
      this.deviceService.updateDevice(this.device).pipe(take(1)).subscribe();
      this.hidePopup.emit(false);
    }
    if(this.mode === 3  && this.validateUser(this.user)){
      this.userService.addUser(this.user).pipe(take(1)).subscribe(data =>{
        this.users.push(data);
        this.hidePopup.emit(false);
      });
    }
    if(this.mode === 4 && this.validateDevice(this.device)){
      this.deviceService.addDevice(this.device).pipe(take(1)).subscribe(data =>{
        var device : DeviceDTO = data;
        this.devices.push(device);
        this.hidePopup.emit(false);
      });
    }
  }

  cancel(){
    this.hidePopup.emit(false);
  }

  ngAfterContentInit() {
    this.cdRef.detectChanges();
  }
  validateUser(user : UserDTO){
    if(user.username.match('[a-zA-Z0-9]+.+') === null){
      this.usernameCheck = false;
      return false;
    }
    if(user.firstname.match('[a-zA-Z0-9]+.+') === null){
      this.firstnameCheck = false;
      return false;
    }
    if(user.lastname.match('[a-zA-Z0-9]+.+') === null){
      this.lastnameCheck = false;
      return false;
    }
    if(user.role.match('[a-zA-Z0-9]+.+') === null){
      this.roleCheck = false;
      return false;
    }

    if(user.password && user.password.match('[a-zA-Z0-9]+.+') === null && this.mode === 3){
      this.passwordCheck = false;
      return false;
    }
    this.roleCheck = true;
    this.usernameCheck = true;
    this.lastnameCheck = true;
    this.firstnameCheck = true;
    this.passwordCheck = true;
    return true;
  }

  validateDevice(device : DeviceDTO){
    if(device.description.match('[a-zA-Z0-9]+.+') === null){
      this.descriptionCheck = false;
      return false;
    }
    if(device.address.match('[a-zA-Z0-9]+.+') === null){
      this.addressCheck = false;
      return false;
    }
    if(device.maxHourlyEnergyConsumption.toString().match('[0-9]+(\.){1}[0.9]+') === null &&  device.maxHourlyEnergyConsumption.toString().match('[0-9]+') === null){
      this.energyConsumptionCheck = false;
      return false;
    }
/*    if(device.userId.match('[a-zA-Z0-9]+.+') === null){
      this.userCheck = false;
      return false;
    }*/
    this.descriptionCheck = true;
    this.addressCheck = true;
    this.energyConsumptionCheck = true;
    this.userCheck = true;
    return true;
  }

}
