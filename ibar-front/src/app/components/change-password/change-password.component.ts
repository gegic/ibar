import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {ChangePassword} from 'src/app/core/model/changePassword';
import {AuthService} from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  @Input()
  isChangePasswordDialogOpen = false;

  @Output()
  closeDialog: EventEmitter<any> = new EventEmitter();

  changePasswordForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService) {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      repeatedPassword: ['', [Validators.required]]
    },
      {
        validators: (control) => {
          if (control.value.newPassword !== control.value.repeatedPassword) {
            control.get('repeatedPassword')?.setErrors({ notSame: true });
          }
          return null;
        },
      }
    );
  }


  ngOnInit(): void {
  }

  onHideChangePasswordDialog(): void {
    this.changePasswordForm.reset();

    this.closeDialog.emit();
  }

  changePassword(): void {
    const oldPassword: string = this.changePasswordForm.controls.oldPassword.value;
    const newPassword: string = this.changePasswordForm.controls.newPassword.value;
    const repeatedPassword: string = this.changePasswordForm.controls.repeatedPassword.value;

    const changePassword: ChangePassword = new ChangePassword(oldPassword, newPassword, repeatedPassword);

    this.authService.changePassword(changePassword).subscribe(res => {
      this.showMessageOnSuccessfullyChangedPassword();

      this.closeDialog.emit();
    },
      err => {
        this.showMessageOnUnsuccessfullyChangedPassword();
      });
  }

  private showMessageOnSuccessfullyChangedPassword(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `Changed password successfully`,
    });
  }

  private showMessageOnUnsuccessfullyChangedPassword(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `Changed password unsuccessfully`,
      detail: 'Please, provide valid password.'
    });
  }

}
