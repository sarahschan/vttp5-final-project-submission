import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { BehaviorSubject, Observable, tap } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  private httpClient = inject(HttpClient)

  // Behaviour subject to track liked recipes
  private likedRecipesSubject = new BehaviorSubject<number[]>([])

  // Observable that components can subscribe to
  public likedRecipes$ = this.likedRecipesSubject.asObservable()

  // Runs the first time the LikeService is called
  constructor() {
    // console.log('LikeService constructor called')
    this.loadLikedRecipes()
  }

  loadLikedRecipes(): void {
    const userId = localStorage.getItem("userId")
    if (userId) {
      this.httpClient.get<number[]>(`/api/private/likes/${userId}`)
        .subscribe({
          next: (recipes) => {
            this.likedRecipesSubject.next(recipes)
            // console.log('Liked recipes loaded successfully:', this.likedRecipesSubject.value)
          },
          error: (error) => {
            console.error('Error loading liked recipes: ', error)
          }
        })
    }
  }


  likeRecipe(userId: string, recipeId: number): Observable<any> {
    const payload = {
      userId: userId,
      recipeId: recipeId
    }
    // console.info('Sending like payload to springboot: ', payload)
    return this.httpClient.post('/api/private/likes/like', payload)
      .pipe(
        tap(() => {
          // Update the BehaviorSubject with the new liked recipe
          const currentLikes = this.likedRecipesSubject.value
          if (!currentLikes.includes(recipeId)) {
            this.likedRecipesSubject.next([...currentLikes, recipeId])
          }
          // console.log('Updated likes: ', this.likedRecipesSubject.value)
        })
      )
  }


  unlikeRecipe(userId: string, recipeId: number): Observable<any> {
    // console.info('Sending unlike payload to springboot: ', { userId, recipeId })
    return this.httpClient.delete(`/api/private/likes/unlike/${userId}/${recipeId}`)
      .pipe(
        tap(() => {
          // Update the BehaviorSubject by removing the unliked recipe
          const currentLikes = this.likedRecipesSubject.value
          this.likedRecipesSubject.next(
            currentLikes.filter(id => id !== recipeId)
          )
        })
      )
  }


  isRecipeLiked(recipeId: number): boolean {
    return this.likedRecipesSubject.value.includes(recipeId)
  }

}
