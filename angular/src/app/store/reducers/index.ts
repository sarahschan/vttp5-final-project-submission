import { ActionReducerMap } from '@ngrx/store'
import * as fromAuth from '../auth/auth.reducer' // Import the reducer
import { AuthState } from '../auth/auth.state'


// Define the structure of your application's state
export interface AppState {
  auth: AuthState // Referencing the state interface from auth.state.ts
}

// Combine reducers into a single reducer map
export const reducers: ActionReducerMap<AppState> = {
  auth: fromAuth.reducer // Referencing the reducer from auth.reducer
};