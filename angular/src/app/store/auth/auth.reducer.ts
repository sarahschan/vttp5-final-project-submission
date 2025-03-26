// Defies how state changes when an action is dispatched
//    the reducer listens to actions and returns a new state
// > imports AuthState and initialState from auth.state.ts
// > imports actions (loginSuccess, logout) to listen for them


import { Action, createReducer, on } from '@ngrx/store'
import { AuthState, initialState } from './auth.state'
import * as AuthActions from './auth.actions'

const authReducer = createReducer(
  initialState,
  on(AuthActions.loginSuccess, (state, { token, username, userId }) => ({
    ...state,
    token,
    isLoggedIn: true,
    username,
    userId
  })),
  on(AuthActions.loginFailure, () => ({
    ...initialState
  })),
  on(AuthActions.logout, () => ({
    ...initialState
  }))
)

export function reducer(state: AuthState | undefined, action: Action) {
  return authReducer(state, action)
}