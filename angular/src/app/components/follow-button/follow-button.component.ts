import { Component, inject, Input, OnDestroy, OnInit, Output } from '@angular/core'
import { FollowService } from '../../services/follow.service'
import { Subscription } from 'rxjs'

@Component({
  selector: 'app-follow-button',
  standalone: false,
  templateUrl: './follow-button.component.html',
  styleUrl: './follow-button.component.scss'
})
export class FollowButtonComponent implements OnInit, OnDestroy {

  @Input()
  userIdToFollow!: number

  private followService = inject(FollowService)
  protected myUserId!: number
  protected isFollowing: boolean = false
  protected isSelf!: boolean
  protected isDisabled!: boolean
  protected isLoading: boolean = false
  private subscription!: Subscription

  ngOnInit(): void {

    this.isLoading = true

    const userId = localStorage.getItem('userId')
  
    if (userId) {
      this.myUserId = parseInt(userId)
      
      this.isSelf = this.myUserId === this.userIdToFollow
      
      if (this.isSelf) {
        this.isDisabled = true
        this.isFollowing = false
        this.isLoading = false
      } else {
        this.subscription = this.followService.followedUsers$.subscribe({
          next: (followedProfiles) => {
            this.isFollowing = followedProfiles.some(profile => profile.userId === this.userIdToFollow)
            // console.log(`User ${this.userIdToFollow} following status: ${this.isFollowing}`)
            this.isDisabled = false
            this.isLoading = false

          },
          error: (error) => {
            console.error('Error getting followed users:', error)
            this.isDisabled = true
            this.isLoading = false

          }
        })
      }
    } else {
      console.error("User ID cannot be found in local storage")
      this.isDisabled = true
      this.isLoading = false
    }
  }

  followChange(): void {

    this.isLoading = true

    if (!this.isFollowing) {
      // console.info(`Sending request for ${this.myUserId} to follow ${this.userIdToFollow}`)
      this.followService.followUser(this.myUserId, this.userIdToFollow).subscribe({
        next: () => {
          // console.info("follow ok")
          this.isFollowing = true
          this.isLoading = false

        },
        error: (error) => {
          console.error('Error following user:', error)
          this.isFollowing = false
          this.isLoading = false

        }
      })
    }

    if (this.isFollowing) {
      // console.info(`Sending request for ${this.myUserId} to unfollow ${this.userIdToFollow}`)
      this.followService.unfollowUser(this.myUserId, this.userIdToFollow).subscribe({
        next: () => {
          // console.info('unfollow ok')
          this.isFollowing = false
          this.isLoading = false

        },
        error: (error) => {
           console.error('Error unfollowing user:', error)
           this.isFollowing = true
           this.isLoading = false

        }
      })
    }
  }


  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe()
    }
  }

}
