import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms"

export function fractionValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (control.value == null || control.value === '') {
            return null
        }

        const value = control.value
        const regex = /^[0-9\.\/ ]+$/

        if (!regex.test(value)) {
            return { invalidFraction: true}
        }

        return null
    }
}