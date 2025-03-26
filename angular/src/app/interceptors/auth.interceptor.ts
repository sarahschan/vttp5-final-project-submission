import { inject, Injectable } from "@angular/core"
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http"
import { catchError, Observable, throwError } from "rxjs"
import { Store } from "@ngrx/store"
import * as AuthActions from "../store/auth/auth.actions"
import { AuthenticationService } from "../services/authentication.service"

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private store = inject(Store)
  private authService = inject(AuthenticationService)

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip token check for public APIs or login/register
    if (request.url.includes('/api/public/')){
        return next.handle(request)
    }

    const token = localStorage.getItem("token")


    
    // For protected APIs, check if token valid
    if (token && this.authService.isTokenExpired(token)) {
        // console.log('Token expired during request, logging out')
        // use the logout action, which will handle clearing localStorage and navigation
        this.store.dispatch(AuthActions.logout())
        return throwError(() => new Error('Token expired'))
    }

    // Add token to the header
    const clonedRequest = request.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`
        }
    })
    

    return next.handle(clonedRequest).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
                const authErrorMessage = error.error?.message || ''
                
                if (authErrorMessage.includes('invalid_token') || 
                    authErrorMessage.includes('expired_token') || 
                    authErrorMessage.includes('unauthorized')) {
                    this.store.dispatch(AuthActions.logout())
                }

            }
            return throwError(() => error)
        })
    )
  }
}