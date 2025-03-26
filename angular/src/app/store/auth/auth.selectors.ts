// Selectors for reading state
//      Selectors help retrieve data from the store efficiently
//      AppState ensures we access the correct part of the state

import { createSelector } from '@ngrx/store'
import { AppState } from '../reducers/index'  // Import your root state if necessary

export const selectAuthState = (state: AppState) => state.auth
export const selectIsLoggedIn = createSelector(selectAuthState, (auth) => auth.isLoggedIn)
export const selectToken = createSelector(selectAuthState, (auth) => auth.token)
export const selectUsername = createSelector(selectAuthState, (auth) => auth.username)
export const selectUserId = createSelector(selectAuthState, (auth) => auth.userId)