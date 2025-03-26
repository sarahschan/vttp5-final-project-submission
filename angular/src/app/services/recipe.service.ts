import { HttpClient, HttpParams } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { Observable, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators'
import { FilterCriteria, Page, Recipe, RecipeOverview } from '../models'

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private httpClient = inject(HttpClient)

  submitNewRecipe(recipe: any, imageFile: File | null): Observable<any> {

    const formData = new FormData
    
    const userId = localStorage.getItem("userId")

    if (userId) {
      formData.append('userId', userId)
    } else {
      console.error('User ID is null')
    }
    
    formData.append('recipe', JSON.stringify(recipe))

    if (imageFile) {
      formData.append('imageFile', imageFile)

    } else {
      formData.append('imageFile', new Blob())
    }

    // console.log('Sending post recipe request to springboot')

    return this.httpClient.post('/api/private/recipes/new', formData).pipe(
      catchError((error) => {
        console.error('Error sending recipe post request: ' + error)
        return throwError(() => new Error('Failed to submit recipe'))
      })
    )
  }


  getRecipeById(recipeId: string): Observable<Recipe> {
    return this.httpClient.get<Recipe>(`/api/private/recipes/${recipeId}`)
  }


  getFilteredRecipes(page: number, size: number, filters: any): Observable<Page<RecipeOverview>> {
    const defaultFilters: FilterCriteria = {
      minCalories: 0,
      maxCalories: 10000,
      minProtein: 0,
      maxProtein: 1000,
      minCarbs: 0,
      maxCarbs: 1000,
      minFats: 0,
      maxFats: 1000,
      sortBy: 'timestamp'
    }
    
    if (filters.minCalories !== null && filters.minCalories !== undefined) defaultFilters.minCalories = filters.minCalories
    if (filters.maxCalories !== null && filters.maxCalories !== undefined) defaultFilters.maxCalories = filters.maxCalories
    if (filters.minProtein !== null && filters.minProtein !== undefined) defaultFilters.minProtein = filters.minProtein
    if (filters.maxProtein !== null && filters.maxProtein !== undefined) defaultFilters.maxProtein = filters.maxProtein
    if (filters.minCarbs !== null && filters.minCarbs !== undefined) defaultFilters.minCarbs = filters.minCarbs
    if (filters.maxCarbs !== null && filters.maxCarbs !== undefined) defaultFilters.maxCarbs = filters.maxCarbs
    if (filters.minFats !== null && filters.minFats !== undefined) defaultFilters.minFats = filters.minFats
    if (filters.maxFats !== null && filters.maxFats !== undefined) defaultFilters.maxFats = filters.maxFats
    if (filters.sortBy !== null && filters.sortBy !== undefined) defaultFilters.sortBy = filters.sortBy
    
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', defaultFilters.sortBy)
      .set('minCalories', defaultFilters.minCalories)
      .set('maxCalories', defaultFilters.maxCalories)
      .set('minProtein', defaultFilters.minProtein)
      .set('maxProtein', defaultFilters.maxProtein)
      .set('minCarbs', defaultFilters.minCarbs)
      .set('maxCarbs', defaultFilters.maxCarbs)
      .set('minFats', defaultFilters.minFats)
      .set('maxFats', defaultFilters.maxFats)
      
    // console.log('Request params:', params)

    return this.httpClient.get<Page<RecipeOverview>>(`/api/private/recipes/overview`, { params })
  }


  getRecipesByUserId(userId: number, page: number, size: number) {
    
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)

    return this.httpClient.get<Page<RecipeOverview>>(`/api/private/recipes/postedBy/${userId}`, { params })
  }


  getLikedRecipesOverviews(userId: number, page: number, size: number) {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size)

    return this.httpClient.get<Page<RecipeOverview>>(`/api/private/recipes/likedRecipes/${userId}`, { params })
  }
}