import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';

import { User } from 'src/app/core/model/user';

import { AdminService } from 'src/app/core/services/admin.service';
import { TokenService } from 'src/app/core/services/token.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  isAddDialogOpen = false;

  private userId: string = "";

  emailControl = new FormControl('', [Validators.required]);
  firstNameControl = new FormControl('', [Validators.required]);
  lastNameControl = new FormControl('', [Validators.required]);

  constructor(
    private adminService: AdminService,
    private messageService: MessageService,
    private tokenService: TokenService
  ) {
    this.userId = tokenService.getToken().userId;
  }

  ngOnInit(): void {
    this.resetAdmins();
  }

  resetAdmins(): void {
    this.adminService.admins = [];

    this.emailControl.reset();
    this.firstNameControl.reset();
    this.lastNameControl.reset();

    this.getAdmins();
  }

  getAdmins(): void {
    this.adminService.getAdmins().subscribe(
      val => {
        for (const el of val) {
          if (this.adminService.admins.some(mod => mod.id === el.id) || el.id === this.userId) {
            continue;
          }
          this.adminService.admins.push(el);
        }
      }
    );
  };

  openAddDialog(): void {
    this.isAddDialogOpen = true;
  };

  onHideAddDialog(): void {
    this.emailControl.reset();
    this.firstNameControl.reset();
    this.lastNameControl.reset();
  };

  saveAdmin(): void {
    if (!this.emailControl.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Email is required.' }
      );
    }

    if (!this.firstNameControl.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'First name is required.' }
      );
    }

    if (!this.lastNameControl.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Last name is required.' }
      );
    }

    const email = this.emailControl.value;
    const firstName = this.firstNameControl.value;
    const lastName = this.lastNameControl.value;

    let admin: User = new User();

    admin.email = email;
    admin.firstName = firstName;
    admin.lastName = lastName;
    admin.userType = 0;

    this.adminService.create(admin).subscribe(res => {
      this.admins.push(res);

      this.showSuccessMessageOnUpdateOrCreateAdmin("Create");
    },
      err => {
        this.showErrorMessageOnUpdateOrCreateAdmin("Create");
      });
  };

  adminDeletionConfirmed(): void {
    this.resetAdmins();
  };

  get admins(): User[] {
    return this.adminService.admins;
  };

  private showErrorMessageOnUpdateOrCreateAdmin(operation: string) {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `${operation} unsuccessful`,
      detail: "User's email is already in use, please use a different email."
    });
  }

  private showSuccessMessageOnUpdateOrCreateAdmin(operation: string) {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `${operation} successfully`,
      detail: `The admin was ${operation.toLocaleLowerCase()}d successfully`
    });
  }

}
