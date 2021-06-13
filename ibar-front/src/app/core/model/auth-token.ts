import {Authority} from './authority';

export class AuthToken {
  userId?: string;
  accessToken?: string;
  authorities?: Authority[];
  userInitials?: string;
}
