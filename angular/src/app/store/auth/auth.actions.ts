// Actions are dispatched by components/services to trigger state changes
//    createAction defines an action
//    props<{ token: string; username: string }>() allows the action to carry data (payload)
//
// Actions are like messages, they tell the store that something happened. They do not modify the state directly


import { createAction, props } from '@ngrx/store'

export const initAuth = createAction(
  '[Auth] Initialize Auth'
)

export const loginSuccess = createAction(         // triggered when a user logs in successfully
  '[Auth] Login Success',                         // payload contains token and username
  props<{ token: string; username: string; userId: number }>()
)

export const loginFailure = createAction(
  '[Auth] Login Failure'
)

export const logout = createAction(               // triggered when a user logs out
  '[Auth] Logout'                                 // no payload needed for this action to execute
)