import { Authority } from './authority';

export enum UserType {
  ADMIN,
  READER
}

export class User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  enabled: boolean;
  userType: UserType;
  authorities: Authority[];
  age: Number;
  male: boolean;
}
