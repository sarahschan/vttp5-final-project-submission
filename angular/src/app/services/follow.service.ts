import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { BehaviorSubject, Observable, tap } from 'rxjs'
import { FollowProfile } from '../models'

@Injectable({
  providedIn: 'root'
})
export class FollowService {

  private httpClient = inject(HttpClient)

  // Behaviour subject to track followed users
  private followedUsersSubject = new BehaviorSubject<FollowProfile[]>([])

  // Observable that components can subscribe to
  public followedUsers$ = this.followedUsersSubject.asObservable()

  constructor() {
    // console.log('FollowService constructor called')
    this.loadFollowedUsers()
  }

  loadFollowedUsers(): void {
    const userId = localStorage.getItem('userId')
    if (userId) {
      this.httpClient.get<FollowProfile[]>(`/api/private/follow/${userId}`)
        .subscribe({
          next: (profiles) => {
            this.followedUsersSubject.next(profiles)
            // console.log('Followed profiles loaded successfully:', this.followedUsersSubject.value)
          },
          error: (error) => {
            console.error('Error loading followed profiles:', error)
          }
        })
    }
  }


  followUser(myUserId: number, userToFollow: number): Observable<FollowProfile> {
    const payload = {
      myUserId: myUserId,
      userToFollow: userToFollow
    }
    // console.log('Sending follow request to springboot: ', payload)
    return this.httpClient.post<FollowProfile>('/api/private/follow/follow', payload)
      .pipe(
        tap( (newFollowProfile: FollowProfile) => {
          // Update the BehaviourSubject with the new follow profile
          const currentFollowProfiles = this.followedUsersSubject.value

          if (!currentFollowProfiles.some(profile => profile.userId === newFollowProfile.userId)) {
            this.followedUsersSubject.next([...currentFollowProfiles, newFollowProfile])
          }
            // console.log('Updated follow profiles', this.followedUsersSubject.value)
          
        })
      )
  }


  unfollowUser(myUserId: number, userToUnfollow: number): Observable<any> {
    // console.info('Sending unfollow request to springboot: ', { myUserId, userToUnfollow })
    return this.httpClient.delete(`/api/private/follow/unfollow/${myUserId}/${userToUnfollow}`)
      .pipe(
        tap(() => {
          // Get current followed users
          const currentFollowProfiles = this.followedUsersSubject.value
          
          // Filter out the unfollowed user
          const updatedProfiles = currentFollowProfiles.filter(
            profile => profile.userId !== userToUnfollow
          )
          
          // Update the BehaviorSubject with the new array
          this.followedUsersSubject.next(updatedProfiles)
          
          // console.log('Updated follow profiles after unfollow:', this.followedUsersSubject.value)
        })
      )
  }


}
