import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DeviceDTO} from "../../models/deviceDTO.model";
import {UserDTO} from "../../models/userDTO.model";

@Component({
  selector: 'user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent {
  @Input() user!: UserDTO
  @Output() delete: EventEmitter<any> = new EventEmitter<any>();
  @Output() edit: EventEmitter<any> = new EventEmitter<any>();

  editDevice() {
    this.edit.emit();
  }

  deleteDevice() {
    this.delete.emit();
  }
}
