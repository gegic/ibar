import { Component, OnInit } from '@angular/core';
import {TokenService} from '../../core/services/token.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthToken} from '../../core/model/auth-token';
import {AuthService} from '../../core/services/auth.service';
import {Authority} from '../../core/model/authority';
import {ADMIN, READER} from '../../core/utils/consts';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm: FormGroup;
  loading = false;

  constructor(private tokenService: TokenService,
              private authService: AuthService,
              private messageService: MessageService,
              private formBuilder: FormBuilder,
              private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]],
      password: ['', [Validators.required, Validators.pattern(new RegExp('\\S'))]]
    });
  }

  login(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authService.login(this.loginForm.value).subscribe(
      (token: AuthToken) => {
        this.loading = false;
        if (token) {
          if (token.authorities.some((au: Authority) => au.name === ADMIN || au.name === READER)) {
            this.tokenService.setToken(token);
            this.router.navigate(['']);
          }
          else {
            this.messageService.add({
              severity: 'error',
              summary: 'Invalid role',
              detail: 'Only super admins permitted.'
            });
          }
        }
      }, er => {
        console.log(er);
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: er?.error?.errorMessage ?? 'Please check your credentials'
        });
      });
  }
}
