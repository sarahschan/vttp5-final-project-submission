import { inject, Injectable } from "@angular/core"
import { AuthenticationService } from "./authentication.service"
import { AsyncValidatorFn, AbstractControl, ValidationErrors } from "@angular/forms"
import { Observable, of, debounceTime, switchMap, catchError } from "rxjs"

@Injectable({
    providedIn: 'root'
})

export class AsyncValidatorService {

  private authService = inject(AuthenticationService)

  uniqueFieldValidator(field: 'username' | 'email'): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const value = control.value
      if (!value) {
        return of(null)
      }
  
      return this.authService.checkIfFieldExists(field, value).pipe(
        debounceTime(1000), // Debounce to reduce API calls
        switchMap(isAvailable => {
          return isAvailable ? of(null) : of({ notAvailable: true })
        }),
        catchError(() => of(null))
      )
    }
  }

}