export interface AuthState {
  token: string | null
  isLoggedIn: boolean
  username: string | null
  userId: number | null
}

export const initialState: AuthState = {
  token: null,
  isLoggedIn: false,
  username: null,
  userId: null
}