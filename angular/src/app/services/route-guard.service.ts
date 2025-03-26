import { inject, Injectable } from '@angular/core'
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router'
import { Store } from '@ngrx/store'
import { map, take } from 'rxjs/operators'
import { selectIsLoggedIn } from '../store/auth/auth.selectors'

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate {
  
  private store = inject(Store)
  private router = inject(Router)

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    
    // For pages that require authentication
    if (route.data['authRequired']) {
      return this.store.select(selectIsLoggedIn).pipe(
        take(1),
        map((isLoggedIn) => {
          if (!isLoggedIn) {
            console.info('Route guard caught non-logged in user accessing restricted resources, redirecting to login page')
            this.router.navigate(['/login'])
            return false
          }

          console.info('Route guard confirms user is authorized to view this page')
          return true
        })
      )
    }

    // For pages that don't require authentication
    console.info('Route guard confirms that no authorization is needed to view this page')
    return true
  }
}
