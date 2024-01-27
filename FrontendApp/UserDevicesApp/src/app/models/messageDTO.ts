import {UserDTO} from "./userDTO.model";

export interface MessageDTO{
  id :string,
  message: string,
  sender: UserDTO,
  connectionId: string,
  status: string
}
