import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { User } from 'src/app/core/model/user';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registrationForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router) {
    this.registrationForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      age: [0, [Validators.required, Validators.min(13), Validators.max(120)]],
      sex: ['', [Validators.required]],
    });
  }

  ngOnInit() { };

  registration(): void {
    if (this.registrationForm.invalid) {
      return;
    }

    const newUser = new User();

    newUser.email = this.registrationForm.controls['email'].value;
    newUser.firstName = this.registrationForm.controls['firstName'].value;
    newUser.lastName = this.registrationForm.controls['lastName'].value;
    newUser.age = this.registrationForm.controls['age'].value;
    newUser.male = this.registrationForm.controls['sex'].value === "Male" ? true : false;
    newUser.userType = 1;

    this.authService.registration(newUser).subscribe(res => {
      this.showSuccessMessageOnSignUp();

      this.router.navigateByUrl(`/login`);
    }, er => {
      this.showErrorMessageOnSignup();
    });
  };

  private showErrorMessageOnSignup(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `Signed up unsuccessful`,
      detail: 'Email address already in use.'
    });
  };

  private showSuccessMessageOnSignUp(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `Signed up successfully`,
      detail: `You have signed up successfully! We send you email with your password.`
    });
  };
}
