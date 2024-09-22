import {Role} from "../enum/Role";

export interface UserDTO {
  username: string;
  email: string;
  password: string;
  role: Role;
}
