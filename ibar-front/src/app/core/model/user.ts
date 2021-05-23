import {Authority} from './authority';

export enum UserType {
  ADMIN,
  READER
}

export class User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  enabled: boolean;
  userType: UserType;
  authorities: Authority[];
}
