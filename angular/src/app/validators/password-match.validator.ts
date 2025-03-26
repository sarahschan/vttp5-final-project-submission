import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms"

export function passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const password = control.get('password')
        const confirmPassword = control.get('confirm_password')
        if (password?.value !== confirmPassword?.value) {
            return { passwordMatch: true }
        }
        return null
    }
}