import { Component, inject, OnInit } from '@angular/core'
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms'
import { AuthenticationService } from '../../../services/authentication.service'
import { LoginCredentials } from '../../../models'
import { Store } from '@ngrx/store'
import { AuthState } from '../../../store/auth/auth.state'
import * as AuthActions from '../../../store/auth/auth.actions'
import { Router } from '@angular/router'

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  private formBuilder = inject(FormBuilder)
  private authService = inject(AuthenticationService)
  private store = inject(Store<{ auth: AuthState}>)
  private router = inject(Router)

  protected loginForm!: FormGroup
  private loginCredentials!: LoginCredentials
  protected wrongUsernameOrPassword = false
  protected isLoading = false
  protected serverError = false

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(3), Validators.maxLength(50) ]),
      password: this.formBuilder.control<string>('', [ Validators.required, Validators.minLength(8), Validators.maxLength(50) ])
    })
  }

  protected fieldError(fieldName: string): boolean {
      const field = this.loginForm.get(fieldName) as FormControl
      return field.dirty && field.invalid
  }

  protected isFormInvalid() {
    return this.loginForm.invalid
  }

  protected handleLogin() {
    
    this.loginCredentials = this.loginForm.value
    // console.log('Login credentials:', this.loginCredentials)
  
    this.isLoading = true;

    this.authService.login(this.loginCredentials).subscribe({
      next: (response) => {
        if (response && response.authenticated) {
          const token = response.authenticated
          const username = response.username
          const userId = response.userId
  
          this.store.dispatch(AuthActions.loginSuccess({ token, username, userId }))
          console.log('Login successful')
          
          this.router.navigate([''])
        }
      },
      error: (error) => {

        if (error.status === 401) {
          console.error('Login failed: ' + error.error.error)
          this.wrongUsernameOrPassword = true
          this.serverError = false
          this.isLoading = false

        } else {
          console.error('Login failed: Server error')
          this.serverError = true
          this.wrongUsernameOrPassword = false
          this.isLoading = false
        }
      }
    })
  }

}
