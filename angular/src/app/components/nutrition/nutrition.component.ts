import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core'
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'
import { wholeNumberValidator } from '../../validators/recipes/whole-number.validator'
import { max2dpValidator } from '../../validators/recipes/max-2dp.validator'
import { NutritionService } from '../../services/nutrition.service'
import { NutritionRec } from '../../models'

@Component({
  selector: 'app-nutrition',
  standalone: false,
  templateUrl: './nutrition.component.html',
  styleUrl: './nutrition.component.scss'
})
export class NutritionComponent implements OnInit {

  private formBuilder = inject(FormBuilder)
  protected form!: FormGroup

  private nutritionService = inject(NutritionService)
  protected nutritionRec!: NutritionRec

  @ViewChild('resultsContainer') resultsContainer!: ElementRef

  protected isLoading: boolean = false


  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      height: this.formBuilder.control<number | null> (null, [ Validators.required, Validators.min(60), Validators.max(225), max2dpValidator()]),
      weight: this.formBuilder.control<number | null> (null, [ Validators.required, Validators.min(9), Validators.max(500), max2dpValidator()]),
      age: this.formBuilder.control<number | null> (null, [ Validators.required, Validators.min(18), Validators.max(120), wholeNumberValidator()]),
      sex: this.formBuilder.control<string>('female', [ Validators.required ]),
      activityLevel: this.formBuilder.control<string>('sedentary', [ Validators.required ]),
      goal: this.formBuilder.control<string>('lose', [ Validators.required ])
    })
  }

  protected fieldError(fieldName: string): boolean {
    const field = this.form.get(fieldName) as FormControl
    return (field.dirty || field.touched) && field.invalid
  }

  protected invalidForm(): boolean {
    return this.form.invalid
  }


  protected processForm(): void {
    this.isLoading = true
    this.nutritionService.getNutritionRec(this.form.value).subscribe({
      next: (response: NutritionRec) => {
        this.nutritionRec = response
        // console.log(this.nutritionRec)
        setTimeout(() => {
          this.scrollToResults()
        }, 100)
        this.isLoading = false
      },
      error: (error) => {
        console.error('Error fetching nutrition rec: ', error)
        this.isLoading = false
      }
    
    })

  }

  private scrollToResults(): void {
    if (this.resultsContainer && this.nutritionRec) {
      this.resultsContainer.nativeElement.scrollIntoView({ 
        behavior: 'smooth', 
        block: 'start' 
      })
    }
  }


}
