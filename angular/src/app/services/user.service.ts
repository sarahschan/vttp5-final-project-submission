import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { catchError, Observable, of, throwError } from 'rxjs'
import { User } from '../models'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private httpClient = inject(HttpClient)

  createNewUser(userData: FormData): Observable<Object> {
    return this.httpClient.post(('/api/public/users/register'), userData)
  }


  getUserByUsername(username: String): Observable<User | null> {
    return this.httpClient.get<User>(`/api/private/user/${username}`).pipe(
      catchError((error) => {
        console.error('Error fetching user details: ' + error)
        return throwError(() => new Error('Failed to fetch user details'))
      })
    )
  }


  getUserPublicInfo(username: String): Observable<User | null> {
    return this.httpClient.get<User>(`/api/public/users/${username}`).pipe(
      catchError((error) => {
        console.error('Error fetching user public details:' + error)
        return throwError(() => new Error('Failed to fetch user public details'))
      })
    )
  }


  updateProfilePicture(username: string, formData: FormData): Observable<any> {
    return this.httpClient.patch(`/api/private/user/updateProfilePicture/${username}`, formData).pipe(
      catchError((error) => {
        console.error('Error updating profile picture: ' + error)
        return throwError(() => new Error('Failed to update profile picture'))
      })
    )
  }


  updateUserBio(username: string, editedBio: string): Observable<any> {
    return this.httpClient.patch(`/api/private/user/updateBio/${username}`, { bio: editedBio }).pipe(
      catchError((error) => {
        console.error('Error updating user bio: ' + error)
        return throwError(() => new Error('Failed to update user bio'))
      })
    )
  }

  
}
