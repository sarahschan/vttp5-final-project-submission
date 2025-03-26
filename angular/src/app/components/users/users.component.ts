import { Component, inject, OnInit } from '@angular/core'
import { UserService } from '../../services/user.service'
import { ActivatedRoute } from '@angular/router'
import { User } from '../../models'

@Component({
  selector: 'app-users',
  standalone: false,
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {

  private userService = inject(UserService)
  private activatedRoute = inject(ActivatedRoute)

  private username!: string
  protected user!: User
  protected userId?: number
  protected loading: boolean = true

  ngOnInit(): void {
      this.username = this.activatedRoute.snapshot.params['username']
      this.fetchUserData()
  }

  private fetchUserData(): void {
    this.loading = true
    // console.info('Getting info for user', this.username)
    this.userService.getUserPublicInfo(this.username).subscribe({
      next: (data) => {
        if (data) {
          this.user = data
          this.userId = data.userId !== null ? data.userId : undefined
        } else {
          console.warn('User not found')
        }
        this.loading = false
      },
      error: (error) => {
        console.error('Error fetching user info:', error)
        this.loading = false
      }
    })
  }
}
