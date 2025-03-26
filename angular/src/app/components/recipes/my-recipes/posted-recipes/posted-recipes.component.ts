import { Component, inject, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core'
import { RecipeService } from '../../../../services/recipe.service'
import { Page, RecipeOverview } from '../../../../models'
import { Router } from '@angular/router'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { HttpParams } from '@angular/common/http'

@Component({
  selector: 'app-posted-recipes',
  standalone: false,
  templateUrl: './posted-recipes.component.html',
  styleUrl: './posted-recipes.component.scss'
})
export class PostedRecipesComponent implements OnInit, OnChanges {
  
  @ViewChild(MatPaginator) paginator!: MatPaginator
  @Input() 
  profileUserId?: number           // Optional input for viewing other profiles

  private recipeService = inject(RecipeService)
  private router = inject(Router)

  protected recipes: RecipeOverview[] = []
  protected page: number = 0               // Backend API page (0-based)
  protected displayPage: number = 1        // UI display page (1-based)
  protected size: number = 3
  protected totalPages: number = 0
  protected totalElements: number = 0
  protected loading: boolean = false
  protected pageSizeOptions: number[] = [3, 6, 9, 12]
  private userId!: number

  ngOnInit(): void {
    this.determineUserId()
    this.loadRecipes()
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Reload when profileUserId input changes
    if (changes['profileUserId']) {
      this.determineUserId()
      // Reset pagination when switching between profiles
      this.page = 0
      this.displayPage = 1
      if (this.paginator) {
        this.paginator.pageIndex = 0
      }
      this.loadRecipes()
    }
  }

  private determineUserId(): void {
    // If profileUserId is available, use it. else use the logged-in user's ID
    if (this.profileUserId !== undefined) {
      this.userId = this.profileUserId
    } else {
      const userIdString = localStorage.getItem("userId")
      this.userId = userIdString ? parseInt(userIdString, 10) : 0
    }
    // console.log("Using userId:", this.userId)
  }

  loadRecipes(): void {
    this.loading = true
    // console.log("Loading recipes by user", this.userId)
    
    this.recipeService.getRecipesByUserId(this.userId, this.page, this.size)
      .subscribe({
        next: (response: Page<RecipeOverview>) => {
          // console.info(`Received ${response.totalElements} posted recipes`)
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
    this.loadRecipes()
    window.scrollTo(0, 0)    // Scroll to top when changing pages
  }

}