import {Role} from "../enum/Role";

export class User {
  id: number;
  username: string;
  email: string;
  password: string;
  role: Role;


  constructor(
    id: number,
    username: string,
    email: string,
    password: string,
    role: Role
  ) {
  this.id = id;
  this.username = username;
  this.email = email;
  this.password = password;
  this.role = role;
}

// The equivalent methods for the UserDetails interface methods in Java
getAuthorities(): string[] {
  return [`ROLE_${this.role}`];
}

isAccountNonExpired(): boolean {
  return true;
}

isAccountNonLocked(): boolean {
  return true;
}

isCredentialsNonExpired(): boolean {
  return true;
}

isEnabled(): boolean {
  return true;
}
}
