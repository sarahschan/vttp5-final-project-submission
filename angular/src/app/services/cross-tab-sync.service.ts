import { inject, Injectable } from '@angular/core'
import { Store } from '@ngrx/store'
import * as AuthActions from '../store/auth/auth.actions'

@Injectable({
  providedIn: 'root'
})
@Injectable({
  providedIn: 'root'
})
export class CrossTabSyncService {

  private store = inject(Store)

  constructor() {
    window.addEventListener('storage', (event) => {
      
      // Handle logout in another tab
      if (event.key === 'token' && !event.newValue) {
        // console.log('User logged out in another tab')
        this.store.dispatch(AuthActions.logout())
      }
      
      // Handle login in another tab
      if (event.key === 'token' && event.newValue) {
        // console.log('User logged in in another tab')
        setTimeout(() => {
          const token = localStorage.getItem('token')
          const username = localStorage.getItem('username')
          const userIdStr = localStorage.getItem('userId')
          
          // console.log('Checking auth data:', { token: !!token, username, userId: userIdStr })
          
          if (token && username && userIdStr) {
            const userId = parseInt(userIdStr, 10)
            // console.log('Dispatching login success for cross-tab sync with userId:', userId)
            this.store.dispatch(AuthActions.loginSuccess({ token, username, userId }))
          }
        }, 50)
      }

      // Alternative login detection method
      // if token change event was missed but caught username/userId
      if ((event.key === 'username' || event.key === 'userId') && event.newValue) {
        // console.log('Auth-related data changed in another tab')
        setTimeout(() => {
          const token = localStorage.getItem('token')
          const username = localStorage.getItem('username')
          const userIdStr = localStorage.getItem('userId')
          
          if (token && username && userIdStr) {
            const userId = parseInt(userIdStr, 10)
            // console.log('Dispatching login success for cross-tab sync via alternative method')
            this.store.dispatch(AuthActions.loginSuccess({ token, username, userId }))
          }
        }, 50)
      }
      
    })
  }
}
