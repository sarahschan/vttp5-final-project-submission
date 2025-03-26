import { Component, inject, OnInit } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { RecipeService } from '../../../services/recipe.service'
import { Recipe, Comment } from '../../../models'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { CommentService } from '../../../services/comment.service'

@Component({
  selector: 'app-view-recipe',
  standalone: false,
  templateUrl: './view-recipe.component.html',
  styleUrl: './view-recipe.component.scss'
})
export class ViewRecipeComponent implements OnInit {

  private route = inject(ActivatedRoute)
  private recipeService = inject(RecipeService)
  protected recipe!: Recipe

  private recipeId!: string

  private commentService = inject(CommentService)
  protected comments: Comment[] = []
  protected commentForm!: FormGroup
  private fb = inject(FormBuilder)

  protected isLoading = false
  protected error = false
  

  ngOnInit(): void {
    
    this.isLoading = true
    this.recipeId = this.route.snapshot.params['recipeId']
    // console.info(`Getting recipe ${this.recipeId}`)

    // display the recipe
    this.recipeService.getRecipeById(this.recipeId).subscribe({
      next: (data: Recipe) => {
          this.recipe = data
          // console.log("Got recipe")
          // console.log(this.recipe)
          this.isLoading = false
      },
      error: (error) => {
        console.error('Error fetching recipe data: ', error)
        this.isLoading = false
      }
    })

    // get comments
    this.fetchComments()

    // // create the comments form
    this.commentForm = this.createCommentForm()

  }

  fetchComments() {
    // console.log('Fetching comments')
    this.commentService.getComments(this.recipeId).subscribe({
      next: (data: Comment[] | null) => {
        this.comments = data || []
        console.info('Fetched comments:',this.comments)
      },
      error: (error) => {
        console.error('Error fetching comments', error)
      }
    })
  }

  createCommentForm(): FormGroup {
   return this.fb.group({
    comment: this.fb.control<string>('', [ Validators.required, Validators.maxLength(1000)] )
   })
  }

  isFormInvalid(): boolean {
    return this.commentForm.invalid
  }

  postComment() {
    const userId = localStorage.getItem('userId')
    if (!userId) {
      console.error('User ID is missing from localStorage')
      return
    }
    const recipeId = this.route.snapshot.params['recipeId']
    const comment = this.commentForm.value['comment']
    // console.log('Sending comment to service')
    this.commentService.postComment(userId, recipeId, comment).subscribe({
      next: (response) => {
        // console.log('Comment posted successfully', response)
        this.fetchComments()
        this.commentForm = this.createCommentForm()
      },
      error: (error) => {
        console.error('Error posting comment', error)
      }
    })
  }
   

}
