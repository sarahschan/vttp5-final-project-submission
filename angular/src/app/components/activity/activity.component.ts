import { Component, HostListener, inject, OnDestroy, OnInit } from '@angular/core'
import { FollowService } from '../../services/follow.service'
import { ActivityService } from '../../services/activity.service'
import { Activity } from '../../models'
import { of, Subscription, switchMap, tap, finalize, catchError } from 'rxjs'

@Component({
  selector: 'app-activity',
  standalone: false,
  templateUrl: './activity.component.html',
  styleUrl: './activity.component.scss'
})
export class ActivityComponent implements OnInit, OnDestroy {
  
  private followService = inject(FollowService)
  private activityService = inject(ActivityService)

  private followerListSubscription$!: Subscription
  private loadMoreSubscription$?: Subscription
  protected followingList: number[] = []
  protected activityFeed: Activity[] = []
  
  // Pagination properties
  protected currentPage = 0
  protected pageSize = 10
  protected isLoading = false
  protected hasMoreActivities = true
  protected showBackToTop = false

  ngOnInit(): void {

    this.followingList = []
    this.activityFeed = []  

    this.followerListSubscription$ = this.followService.followedUsers$.pipe(
      tap((followedProfiles) => {
        this.followingList = followedProfiles.map(profile => profile.userId)
        // console.log('Retrieved following accounts:', this.followingList)
        
        if (followedProfiles.length === 0) {
          this.followingList = []
          this.activityFeed = []
          this.hasMoreActivities = false
        }
      }),
      switchMap((followedProfiles) => {
        const userIds = followedProfiles.map(profile => profile.userId)
        if (userIds.length > 0) {
          this.isLoading = true
          return this.activityService.getActivity(userIds, this.currentPage, this.pageSize).pipe(
            catchError(error => {
              console.error('Error fetching activities:', error)
              this.isLoading = false
              return of([])
            })
          )
        } else {
          return of([])
        }
      }),
      tap(() => {
        this.isLoading = false
      }),
      finalize(() => {
        this.isLoading = false
      })
    ).subscribe({
      next: (activities) => {
        this.activityFeed = activities.map(activity => {
          const utcTimestamp = new Date(activity.timestamp + 'Z')
          return {
            ...activity,
            timestamp: utcTimestamp
          }
        })
        this.hasMoreActivities = activities.length === this.pageSize
        // console.log('Initial activity feed populated:', activities.length, 'items')
        // console.log('Has more activities?', this.hasMoreActivities)
        this.isLoading = false
      },
      error: (error) => {
        console.error('Error in follower/activity chain:', error)
        this.isLoading = false
      },
      complete: () => {
        this.isLoading = false
      }
    })

    // ensure loading indicator doesn't get stuck
    setTimeout(() => {
      if (this.isLoading) {
        this.isLoading = false
      }
    }, 10000) // 10 seconds timeout
  }

  // Load more activities when user scrolls to bottom
  loadMoreActivities(): void {
    if (this.isLoading || !this.hasMoreActivities || this.followingList.length === 0) {
      return
    }

    this.isLoading = true
    this.currentPage++

    // Cancel any existing subscription before making a new one
    if (this.loadMoreSubscription$) {
      this.loadMoreSubscription$.unsubscribe()
    }

    this.loadMoreSubscription$ = this.activityService
      .getActivity(this.followingList, this.currentPage, this.pageSize)
      .pipe(
        tap(() => {
          this.isLoading = false
        }),
        catchError(error => {
          console.error('Error loading more activities:', error)
          this.currentPage--
          this.isLoading = false
          return of([])
        }),
        finalize(() => {
          this.isLoading = false
        })
      )
      .subscribe({
        next: (newActivities) => {
          const convertedActivities = newActivities.map(activity => ({
            ...activity,
            timestamp: new Date(activity.timestamp + 'Z')
          }))
          this.hasMoreActivities = newActivities.length === this.pageSize
          // console.log('More activities loaded:', newActivities.length, 'items')
          // console.log('Has more activities?', this.hasMoreActivities)
          this.isLoading = false
        },
        error: (error) => {
          console.error('Error loading more activities:', error)
          this.currentPage--
          this.isLoading = false
        },
        complete: () => {
          this.isLoading = false
        }
      })
      

    setTimeout(() => {
      if (this.isLoading) {
        console.log('LoadMore safety timeout')
        this.isLoading = false
      }
    }, 10000) // 10 seconds timeout
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    this.showBackToTop = window.scrollY > 500
    
    const windowHeight = window.innerHeight
    const documentHeight = document.documentElement.scrollHeight
    const scrollTop = window.scrollY
    
    if (scrollTop + windowHeight >= documentHeight - 200) {
      this.loadMoreActivities()
    }
  }


  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  ngOnDestroy(): void {
    if (this.followerListSubscription$) {
      this.followerListSubscription$.unsubscribe()
    }
    if (this.loadMoreSubscription$) {
      this.loadMoreSubscription$.unsubscribe()
    }
  }
}