import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core'
import { RecipeService } from '../../../services/recipe.service'
import { Page, RecipeOverview } from '../../../models'
import { Router } from '@angular/router'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { FormBuilder, FormGroup } from '@angular/forms'

@Component({
  selector: 'app-overview',
  standalone: false,
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.scss'
})
export class OverviewComponent implements OnInit {
  
  @ViewChild(MatPaginator) paginator!: MatPaginator
  @ViewChild('filterContainerRef') filterContainerRef!: ElementRef

  private recipeService = inject(RecipeService)
  private router = inject(Router)
  private fb = inject(FormBuilder)

  protected recipes: RecipeOverview[] = []
  protected page: number = 0                // Backend API page (0-based)
  protected displayPage: number = 1         // UI display page (1-based)
  protected size: number = 12
  protected totalPages: number = 0
  protected totalElements: number = 0
  protected direction: string = 'desc'
  protected loading: boolean = false
  protected pageSizeOptions: number[] = [6, 12, 18, 24, 30]
  protected filterForm!: FormGroup

  // Options for sorting
  sortOptions = [
    { value: 'timestamp', label: 'Recently added' },
    { value: 'likes', label: 'Popularity' }
  ]

  ngOnInit(): void {
    this.initFilterForm()
    this.loadRecipes()
  }

  initFilterForm(): void {
    this.filterForm = this.fb.group({
      minCalories: [null],
      maxCalories: [null],
      minProtein: [null],
      maxProtein: [null],
      minCarbs: [null],
      maxCarbs: [null],
      minFats: [null],
      maxFats: [null],
      sortBy: ['timestamp']
    })
  }

  loadRecipes(): void {
    this.loading = true
    const filters = this.getFilters()
    
    // console.log(`Loading recipes with filters:`, filters)
    
    this.recipeService.getFilteredRecipes(this.page, this.size, filters)
      .subscribe({
        next: (response: Page<RecipeOverview>) => {
          // console.log('API Response:', response)
          this.recipes = response.content
          this.totalPages = response.totalPages
          this.totalElements = response.totalElements
          this.loading = false
          // Update display page whenever we load recipes
          this.displayPage = this.page + 1
        },
        error: (error) => {
          console.error('Error loading recipes:', error)
          this.loading = false
        }
      })
  }

  getFilters(): any {
    const formValues = this.filterForm.value
    return {
      minCalories: formValues.minCalories || null,
      maxCalories: formValues.maxCalories || null,
      minProtein: formValues.minProtein || null,
      maxProtein: formValues.maxProtein || null,
      minCarbs: formValues.minCarbs || null,
      maxCarbs: formValues.maxCarbs || null,
      minFats: formValues.minFats || null,
      maxFats: formValues.maxFats || null,
      sortBy: formValues.sortBy
    }
  }

  applyFilters(): void {
    this.page = 0            // Reset to first page when applying filters
    this.displayPage = 1     // Also reset display page
    if (this.paginator) {
      this.paginator.pageIndex = 0
    }
    this.loadRecipes()
    setTimeout(() => {
      this.scrollToFilter()
    }, 100)
  }

  onSortChange(): void {
    // Reset to first page when sorting changes
    this.page = 0
    this.displayPage = 1
    if (this.paginator) {
      this.paginator.pageIndex = 0
    }
    this.loadRecipes()
    setTimeout(() => {
      this.scrollToFilter()
    }, 100)
  }
  
  resetFilters(): void {
    this.filterForm.reset({
      sortBy: 'timestamp'
    })
    this.page = 0
    this.displayPage = 1
    if (this.paginator) {
      this.paginator.pageIndex = 0
    }
    this.loadRecipes()
    setTimeout(() => {
      this.scrollToFilter()
    }, 100)
  }

  hasActiveFilters(): boolean {
    const formValues = this.filterForm.value
    return !!(formValues.minCalories || formValues.maxCalories || 
              formValues.minProtein || formValues.maxProtein || 
              formValues.minCarbs || formValues.maxCarbs || 
              formValues.minFats || formValues.maxFats)
  }

  getSortLabel(value: string): string {
    const option = this.sortOptions.find(opt => opt.value === value)
    return option ? option.label : ''
  }

  handlePageEvent(event: PageEvent): void {
    console.log('Page event:', event)
    this.size = event.pageSize
    this.page = event.pageIndex
    this.displayPage = this.page + 1
    this.loadRecipes()
    setTimeout(() => {
      this.scrollToFilter()
    }, 100)
  }

  navigateToCreateRecipe(): void {
    this.router.navigate(['/recipes/create'])
  }

  private scrollToFilter(): void {
    if (this.filterContainerRef) {
      this.filterContainerRef.nativeElement.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start',
        inline: 'nearest'
      });
    }
  }
}