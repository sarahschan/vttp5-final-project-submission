import { Component, inject, OnInit, ViewChild } from '@angular/core'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { Router } from '@angular/router'
import { Page, RecipeOverview } from '../../../../models'
import { RecipeService } from '../../../../services/recipe.service'

@Component({
  selector: 'app-liked-recipes',
  standalone: false,
  templateUrl: './liked-recipes.component.html',
  styleUrl: './liked-recipes.component.scss'
})
export class LikedRecipesComponent implements OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator

  private recipeService = inject(RecipeService)
  private router = inject(Router)

  protected recipes: RecipeOverview[] = []
  protected page: number = 0                // Backend API page (0-based)
  protected displayPage: number = 1         // UI display page (1-based)
  protected size: number = 3
  protected totalPages: number = 0
  protected totalElements: number = 0
  protected loading: boolean = false
  protected pageSizeOptions: number[] = [3, 6, 9, 12]
  private userId!: number

  ngOnInit(): void {
      const userIdString = localStorage.getItem("userId")
      this.userId = userIdString ? parseInt(userIdString, 10) : 0
      this.loadLikedRecipes()
  }

  loadLikedRecipes(): void {
    this.loading = true
    // console.log("Loading liked recipes for user", this.userId)

    this.recipeService.getLikedRecipesOverviews(this.userId, this.page, this.size)
      .subscribe({
        next: (response: Page<RecipeOverview>) => {
          // console.info(`Recieved ${response.totalElements} liked recipes`)
          this.recipes = response.content
          this.totalPages = response.totalPages
          this.totalElements = response.totalElements
          this.loading = false
          this.displayPage = this.page + 1
        },
        error: (error) => {
          console.error('Error loading recipes:', error)
          this.loading = false
        }
      })
  }

  handlePageEvent(event: PageEvent): void {
    // console.log('Page event:', event)
    this.size = event.pageSize
    this.page = event.pageIndex
    this.displayPage = this.page + 1
    this.loadLikedRecipes()
    window.scrollTo(0, 0)
  }

}
