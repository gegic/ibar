export class ChangePassword {
    oldPassword: string;
    newPassword: string;
    repeatedPassword: string;

    constructor(
        oldPassword: string,
        newPassword: string,
        repeatedPassword: string
    ) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repeatedPassword = repeatedPassword;
    }
}
