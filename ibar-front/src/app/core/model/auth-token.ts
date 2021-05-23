import {Authority} from './authority';

export class AuthToken {
  userId: number;
  accessToken: string;
  authorities: Authority[];
  userInitials: string;
}
