import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core'
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'
import { Title } from '@angular/platform-browser'
import { wholeNumberValidator } from '../../../validators/recipes/whole-number.validator'
import { max2dpValidator } from '../../../validators/recipes/max-2dp.validator'
import { RecipeService } from '../../../services/recipe.service'
import { Router } from '@angular/router'
import { fractionValidator } from '../../../validators/recipes/fraction.validator'

@Component({
  selector: 'app-create-recipe',
  standalone: false,
  templateUrl: './create-recipe.component.html',
  styleUrl: './create-recipe.component.scss'
})
export class CreateRecipeComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>

  private formBuilder = inject(FormBuilder)
  private recipeService = inject(RecipeService)
  private router = inject(Router)
  
  protected recipeForm!: FormGroup
  protected ingredients!: FormArray
  protected instructions!: FormArray
  private imageFile: File | null = null
  
  protected selectedImage: boolean = false
  protected imagePreview: string | ArrayBuffer | null = null

  protected isLoading: boolean = false

  protected unitOptions: string[] = [
    'teaspoon (tsp)', 'tablespoon (tbsp)', 'fluid ounce (fl oz)', 'cup (cup)', 
    'milliliter (ml)', 'liter (l)', 'pint (pint)', 'quart (quart)', 'gallon (gallon)',
    'milligram (mg)', 'gram (g)', 'kilogram (kg)', 'ounce (oz)', 'pound (lb)',
    'whole', 'piece', 'slice', 'pinch', 'dash', 
    'to taste', 'bunch', 'clove', 'sprig'
  ]


  ngOnInit(): void {
    this.recipeForm = this.createRecipeForm()
  }

  private createRecipeForm(): FormGroup {
    
    this.ingredients = this.formBuilder.array([])
    this.instructions = this.formBuilder.array([])

    return this.formBuilder.group({
      title: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(10)] ),
      description: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(10)] ),
      protein: this.formBuilder.control<number | null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      carbs: this.formBuilder.control<number | null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      fats: this.formBuilder.control<number| null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      calories: this.formBuilder.control<number| null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      servings: this.formBuilder.control<number | null>(null, [ Validators.required, Validators.min(1), wholeNumberValidator()]),
      prep_time: this.formBuilder.control<number | null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      cook_time: this.formBuilder.control<number | null>(null, [ Validators.required, Validators.min(0), wholeNumberValidator()] ),
      ingredients: this.ingredients,
      instructions: this.instructions
    })
  }

  
  protected addIngredient() {
    this.ingredients.push(this.createIngredient())
  }

  private createIngredient(): FormGroup {
    return this.formBuilder.group({
      ingredient: this.formBuilder.control<string>('', [ Validators.required ]),
      amount: this.formBuilder.control<string>('', [ Validators.required , max2dpValidator(), fractionValidator()]),
      unit: this.formBuilder.control<string>('', [ Validators.required ]),
      notes: this.formBuilder.control<string>('')
    })
  }

  protected removeIngredient(index: number) {
    this.ingredients.removeAt(index)
  }


  protected addStep() {
    this.instructions.push(this.createStep())
  }

  private createStep(): FormGroup {
    return this.formBuilder.group({
      step: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(5) ])
    })
  }

  protected removeStep(index: number) {
    this.instructions.removeAt(index)
  }

  protected mainFieldError(fieldName: string): boolean {
    const field = this.recipeForm.get(fieldName) as FormControl
    return (field.dirty || field.touched) && field.invalid
  }

  protected ingredientFieldError(index: number, fieldName: string): boolean {
    const ingredientsArray = this.recipeForm.get('ingredients') as FormArray
    const ingredientGroup = ingredientsArray.at(index) as FormGroup
    const field = ingredientGroup.get(fieldName) as FormControl
    return (field?.dirty || field.touched) && (field?.invalid)
  }

  protected instructionsFieldError(index: number, fieldName: string): boolean {
    const instructionsArray = this.recipeForm.get('instructions') as FormArray
    const instructionsGroup = instructionsArray.at(index) as FormGroup
    const field = instructionsGroup.get(fieldName) as FormControl
    return (field?.dirty || field.touched) && (field?.invalid)
  }


  protected triggerFileInput(): void {
    this.fileInput.nativeElement.click()
  }


  protected onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement

    if (input.files && input.files.length > 0) {
      this.imageFile = input.files[0]
      this.selectedImage = true
      
      const reader = new FileReader()
      reader.onload = () => {
        this.imagePreview = reader.result
      }
      reader.readAsDataURL(this.imageFile)
    } else {
      this.resetImageSelection()
    }
  }

  protected resetImageSelection(): void {
    this.imageFile = null
    this.selectedImage = false
    this.imagePreview = null
    if (this.fileInput) {
      this.fileInput.nativeElement.value = ''
    }
  }

  protected isFormInvalid(): boolean {
    return this.recipeForm.invalid || this.ingredients.length <= 0 || this.instructions.length <= 0
  }


  protected submitRecipe() {

    this.isLoading = true

    const recipe = this.recipeForm.value

    this.recipeService.submitNewRecipe(recipe, this.imageFile)
    .subscribe({
      next: (response) => {
        // console.log('Success posting recipe, ID:', response.recipeId)
        this.router.navigate(['/recipes', response.recipeId])
        this.isLoading = false
      },
      error: (error) => {
        console.error('Failed to post recipe:', error)
        this.isLoading = false
      }
    })
  }
}