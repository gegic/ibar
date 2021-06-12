import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function matchPassword(oldPassword: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const isMatching: boolean = control.value === oldPassword;

      return isMatching ? {repeatedPassword: {value: control.value}} : null;
    };
  }