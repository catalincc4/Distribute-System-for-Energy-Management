import {UserDTO} from "./userDTO.model";
import {MessageDTO} from "./messageDTO";

export class ConnectionDTO{
  id!: string;
  adminUser!: UserDTO | null;
  connectedUsers!:UserDTO[];
  messages!: MessageDTO[];
}
