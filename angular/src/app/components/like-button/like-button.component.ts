import { Component, inject, Input, OnDestroy, OnInit } from '@angular/core'
import { Subscription } from 'rxjs'
import { LikeService } from '../../services/like.service'

@Component({
  selector: 'app-like-button',
  standalone: false,
  templateUrl: './like-button.component.html',
  styleUrl: './like-button.component.scss'
})

export class LikeButtonComponent implements OnInit, OnDestroy {

  @Input()
  recipeId!: number

  private likeService = inject(LikeService)
  protected isLiked!: boolean
  private userId!: string
  private subscription!: Subscription
  protected isLoading: boolean = false

  ngOnInit(): void {
    const userId = localStorage.getItem('userId')
    if (userId) {
      this.userId = userId;
      // subscribe to the likedRecipes$ observable
      this.subscription = this.likeService.likedRecipes$.subscribe({
        next: (likedRecipes) => {
          this.isLiked = likedRecipes.includes(this.recipeId)
        },
        error: (error) => {
          console.error(`Error checking if recipe ${this.recipeId} is liked: `, error)
        }
      })

    } else {
      console.error("No userId found in local storage")
    }
  }

  toggleLike(): void {

    this.isLoading = true

    if (this.isLiked) {
      // console.info(`Sending request for ${this.userId} to unlike recipe ${this.recipeId}`)
      this.likeService.unlikeRecipe(this.userId, this.recipeId).subscribe({
        next: () => {
          this.isLiked = false
          this.isLoading = false
        },
        error: (error) => {
          console.error('Error unliking recipe: ', error)
          this.isLoading = false
        }
      })

    } else {
      // console.info(`Sending request for ${this.userId} to like recipe ${this.recipeId}`)
      this.likeService.likeRecipe(this.userId, this.recipeId).subscribe({
        next: () => {
          this.isLiked = true
          this.isLoading = false
        },
        error: (error) => {
          console.error('Error liking recipe: ', error)
          this.isLoading = false
        }
      })
    }
  }


  ngOnDestroy(): void {
    this.subscription.unsubscribe()
  }

}
