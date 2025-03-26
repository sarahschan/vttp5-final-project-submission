import { Component, inject, OnInit } from '@angular/core'
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'
import { passwordMatchValidator } from '../../../validators/password-match.validator'
import { AsyncValidatorService } from '../../../services/async-validator.service'
import { UserService } from '../../../services/user.service'

@Component({
  selector: 'app-new-account',
  standalone: false,
  templateUrl: './new-account.component.html',
  styleUrl: './new-account.component.scss'
})

export class NewAccountComponent implements OnInit {

  private formBuilder = inject(FormBuilder)
  private asyncValidatorService = inject(AsyncValidatorService)
  private userService = inject(UserService)

  protected newUserForm!: FormGroup

  protected registrationSuccess: boolean = false
  protected registrationError: boolean = false

  protected isLoading: boolean = false

  ngOnInit(): void {
    this.newUserForm = this.formBuilder.group({
      name: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(3), Validators.maxLength(125) ]),
      username: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(3), Validators.maxLength(50), Validators.pattern(/^[a-zA-Z0-9_-]*$/) ], [this.asyncValidatorService.uniqueFieldValidator('username')]),
      email: this.formBuilder.control<string>('', [ Validators.required, Validators.email, Validators.maxLength(125) ], [this.asyncValidatorService.uniqueFieldValidator('email')]),
      password: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(8), Validators.maxLength(50) ]),
      confirm_password: this.formBuilder.control<string>(''),
      short_bio: this.formBuilder.control<string>('')
    },
    { validator: passwordMatchValidator() })
  }

  // field validation
  protected fieldError(fieldName: string): boolean {
    const field = this.newUserForm.get(fieldName) as FormControl
    return (field?.dirty || field?.touched) && (field?.invalid || field?.hasError('notAvailable')) // checking for native validators and uniqueFieldValidator
  }

  // password match validation
  protected passwordMatchError(): boolean {
    return this.newUserForm.hasError('passwordMatch') && this.newUserForm.get('confirm_password')?.dirty || false
  }

  // form validation
  protected isFormInvalid() {
    return this.newUserForm.invalid
  }
  

  // form submission
  protected submitForm() {

    this.isLoading = true
    // console.log('Form submitted:', this.newUserForm.value)
    
    // need to use this version because we're handing a file upload
    const formData = new FormData()

    // handle the regular stuff
    formData.set('name', this.newUserForm.get('name')?.value)
    formData.set('username', this.newUserForm.get('username')?.value)
    formData.set('email', this.newUserForm.get('email')?.value)
    formData.set('password', this.newUserForm.get('password')?.value)
    formData.set('short_bio', this.newUserForm.get('short_bio')?.value)

    // console.log('Registering new user')

    // Send form data to backend via user service
    this.userService.createNewUser(formData).subscribe({
      next: () => {
        // console.log('User created successfully')
        this.registrationSuccess = true
        this.registrationError = false
        this.newUserForm.reset()
        this.isLoading = false
      },
      error: (error) => {
        console.error('Error creating user:', error.error.error)
        this.registrationSuccess = false
        this.registrationError = true
        this.isLoading = false
      }
    })

  }

}
