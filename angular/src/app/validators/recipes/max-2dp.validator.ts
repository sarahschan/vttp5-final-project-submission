import { ValidatorFn, AbstractControl, ValidationErrors } from "@angular/forms"

export function max2dpValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control.value == null || control.value === '') {
        return null
      }
  
      const valueStr = String(control.value)
      const parts = valueStr.split('.')
      
      if (parts.length > 1 && parts[1].length > 2) {
        return { 'maxTwoDecimalPlaces': { value: control.value } }
      }
      
      return null
    }
  }