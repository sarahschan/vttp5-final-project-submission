import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core'
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'
import { GeminiService } from '../../services/gemini.service'
import { GeminiRecipe } from '../../models'

@Component({
  selector: 'app-gemini-recipe',
  standalone: false,
  templateUrl: './gemini-recipe.component.html',
  styleUrl: './gemini-recipe.component.scss'
})
export class GeminiRecipeComponent implements OnInit {

  private fb = inject(FormBuilder)
  protected ingredients!: FormArray
  protected form! : FormGroup

  private geminiServie = inject(GeminiService)
  protected geminiRecipe: GeminiRecipe | null = null
  protected geminiError: boolean = false

  @ViewChild('generatedRecipe') generatedRecipeElement?: ElementRef

  protected isLoading: boolean = false
  
  // Common ingredients list
  protected commonIngredients = [
    { name: 'Cooking Oil', value: 'cooking_oil' },
    { name: 'Olive Oil', value: 'olive_oil' },
    { name: 'Soy Sauce', value: 'soy_sauce' },
    { name: 'Salt', value: 'salt' },
    { name: 'Pepper', value: 'pepper' },
    { name: 'Onions', value: 'onions' },
    { name: 'Garlic', value: 'garlic' },
    { name: 'Rice', value: 'rice'}
  ]

  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm(): FormGroup {
    this.ingredients = this.fb.array([])
    return this.fb.group({
      ingredients: this.ingredients,
      commonIngredients: this.fb.group(
        this.commonIngredients.reduce((acc, ingredient) => {
          acc[ingredient.value] = [false]
          return acc
        }, {} as {[key: string]: any})
      )
    })
  }

  protected addIngredient() {
    this.ingredients.push(this.createIngredient())
  }

  createIngredient(): FormGroup {
    return this.fb.group({
      ingredient: this.fb.control<string>('', [ Validators.required ]),
      amount: this.fb.control<string>('', [ Validators.required ])
    })
  }

  protected removeIngredient(index: number) {
    this.ingredients.removeAt(index)
  }

  protected ingredientFieldError(index: number, fieldName: string): boolean {
    const ingredientsArray = this.form.get('ingredients') as FormArray
    const ingredientGroup = ingredientsArray.at(index) as FormGroup
    const field = ingredientGroup.get(fieldName) as FormControl
    return (field?.dirty || field.touched) && (field.invalid)
  }

  protected formInvalid(): boolean {
    return this.ingredients.length === 0 || this.form.invalid
  }

  submitForm() {

    this.isLoading = true
    this.geminiRecipe = null
  
    const allIngredients: string[] = []
  
    this.ingredients.value.forEach((item: {ingredient: string, amount: string}) => {
      allIngredients.push(`${item.ingredient} (${item.amount})`)
    })
      
    const commonIngredientsGroup = this.form.get('commonIngredients')?.value || {}
    Object.keys(commonIngredientsGroup)
      .filter(key => commonIngredientsGroup[key])
      .forEach(key => {
        const ingredientInfo = this.commonIngredients.find(i => i.value === key)
        allIngredients.push(`${ingredientInfo?.name || key} (as needed)`)
      })
      
    // console.log('Recipe ingredients:', allIngredients)
      
    this.geminiServie.getGeminiRecipe(allIngredients).subscribe({
      next: (data) => {
        this.geminiRecipe = data
        // console.log(data)
        setTimeout(() => {
          this.scrollToGeneratedRecipe()
        }, 100)
        this.isLoading = false
        this.geminiError = false
      },
      error: (error) => {
        console.error('Unable to generate gemini recipe')
        this.isLoading = false
        this.geminiError = true
      }
    })
  }

  private scrollToGeneratedRecipe() {
    if (this.generatedRecipeElement) {
      this.generatedRecipeElement.nativeElement.scrollIntoView({ 
        behavior: 'smooth', 
        block: 'start' 
      })
    } else {
      const element = document.querySelector('.generated-recipe')
      if (element) {
        element.scrollIntoView({ 
          behavior: 'smooth', 
          block: 'start' 
        })
      }
    }
  }
  
}