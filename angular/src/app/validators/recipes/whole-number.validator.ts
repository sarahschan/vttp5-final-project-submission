import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms"

export function wholeNumberValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control.value == null || control.value === '') {
        return null
      }
  
      const value = Number(control.value)
      
      const isWholeNumber = Number.isInteger(value)
      
      return isWholeNumber ? null : { 'wholeNumber': { value: control.value } }
    }
  }