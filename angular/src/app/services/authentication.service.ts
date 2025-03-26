import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { Store } from '@ngrx/store'
import { catchError, Observable, of } from 'rxjs'
import * as AuthActions from '../store/auth/auth.actions'
import { jwtDecode } from 'jwt-decode'


@Injectable({
  providedIn: 'root'
})

export class AuthenticationService {

  private httpClient = inject(HttpClient)
  private store = inject(Store)

  // check if username or email already exists in the database - for sign up form
  checkIfFieldExists(field: 'username' | 'email', value: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`/api/public/auth/check/${field}/${value}`).pipe(
      catchError(() => {
        console.error('API call failed')
        return of(false)  // Return false if error occurs
      })
    )
  }


  // login method
  login(credentials: { username: string, password: string }): Observable<any> {
    return this.httpClient.post('/api/public/auth/login', credentials)
  }

  // logout method
  logout(): void {
    this.store.dispatch(AuthActions.logout())
  }



  // check for expired tokens
  isTokenExpired(token: string): boolean {
    try {
      const decodedToken: any = jwtDecode(token)
      const currentTime = Date.now() / 1000 // Convert current time to seconds
      return decodedToken.exp < currentTime
    } catch (error) {
      console.error('Error decoding token:', error)
      return true // Treat invalid tokens as expired
    }
  }

}
