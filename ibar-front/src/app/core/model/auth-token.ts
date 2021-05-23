import {Authority} from './authority';

export class AuthToken {
  accessToken: string;
  authorities: Authority[];
  userInitials: string;
}
