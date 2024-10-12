import {Role} from "../enum/Role";

export interface UserDTO {
  id:number;
  username: string;
  email: string;
  password: string;
  role: Role;
}
