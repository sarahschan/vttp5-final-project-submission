// auth.effects.ts
import { inject, Injectable } from '@angular/core'
import { Actions, createEffect, ofType } from '@ngrx/effects'
import { tap, map } from 'rxjs/operators'
import * as AuthActions from './auth.actions'
import { Router } from '@angular/router'
import { AuthenticationService } from '../../services/authentication.service'

@Injectable()
export class AuthEffects {

    private actions$ = inject(Actions)
    private router = inject(Router)
    private authService = inject(AuthenticationService)

    // Save to localStorage when login
    saveAuthData$ = createEffect(() => 
        this.actions$.pipe(
        ofType(AuthActions.loginSuccess),
        tap(({ token, username, userId }) => {
            localStorage.setItem('token', token)
            localStorage.setItem('username', username)
            localStorage.setItem('userId', userId.toString())
        })
        ),
        { dispatch: false }
    )
    
  // In auth.effects.ts
    clearAuthData$ = createEffect(() => 
    this.actions$.pipe(
      ofType(AuthActions.logout),
      tap(() => {
        if (localStorage.getItem('token')) {
          localStorage.removeItem('token')
        }
        if (localStorage.getItem('username')) {
          localStorage.removeItem('username')
        }
        if (localStorage.getItem('userId')) {
          localStorage.removeItem('userId')
        }
        this.router.navigate(['/login'])
      })
    ),
    { dispatch: false }
  )
  
    // Initialize auth state from localStorage on app startup
    initAuth$ = createEffect(() => 
        this.actions$.pipe(
        ofType(AuthActions.initAuth),
        map(() => {
            const token = localStorage.getItem('token')
            const username = localStorage.getItem('username')
            const userIdStr = localStorage.getItem('userId')
            
            if (token && username && userIdStr) {
            const userId = parseInt(userIdStr, 10)
            
            if (!this.authService.isTokenExpired(token)) {
                return AuthActions.loginSuccess({ token, username, userId })
            }
            
            localStorage.removeItem('token')
            localStorage.removeItem('username')
            localStorage.removeItem('userId')
            
            return AuthActions.loginFailure()
            }
            
            return AuthActions.loginFailure()
        })
        )
    )
  
}