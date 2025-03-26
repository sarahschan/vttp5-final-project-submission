import { Component, inject, OnInit, TemplateRef, ViewChild } from '@angular/core'
import { User } from '../../models'
import { UserService } from '../../services/user.service'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { AuthenticationService } from '../../services/authentication.service'

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  private userService = inject(UserService)
  private modalService = inject(NgbModal)
  private authService = inject(AuthenticationService)

  @ViewChild('profilePictureModal') profilePictureModal!: TemplateRef<any>
  @ViewChild('bioModal') bioModal!: TemplateRef<any>

  protected user!: User
  selectedFile: Blob | null = null
  imagePreview: string | null = null
  editedBio: string = ''

  ngOnInit(): void {
    const username = localStorage.getItem('username')!
    this.userService.getUserByUsername(username).subscribe({
      next: (data: User | null) => {
        if (data) {
          this.user = data
          // console.info(this.user)
        } else {
          console.warn('User not found')
        }
      },
      error: (error) => {
        console.error('Error fetching user details: ', error)
      }
    })
  }

  logout() {
    this.authService.logout()
  }

  // Profile picture methods
  openProfilePictureModal(): void {
    this.selectedFile = null
    this.imagePreview = null
    this.modalService.open(this.profilePictureModal, { centered: true })
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] as File

    // create preview
    if (this.selectedFile) {
      const reader = new FileReader()
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result
      }
      reader.readAsDataURL(this.selectedFile)
    }
  }

  uploadProfilePicture(): void {
    if (this.selectedFile) {
      const formData = new FormData()
      formData.append('profile_picture', this.selectedFile)

      this.userService.updateProfilePicture(this.user.username, formData).subscribe({
        next: () => {
          // console.log('Profile picture updated successfully')
          window.location.reload()
        },
        error: (error) => {
          console.error('An unknown error occurred: ', error)
        }
      })
    }
  }

  // Bio methods
  openBioModal(): void {
    this.editedBio = this.user.short_bio || ''
    this.modalService.open(this.bioModal, { centered: true })
  }

  updateBio(): void {
    this.userService.updateUserBio(this.user.username, this.editedBio).subscribe({
      next: () => {
        // console.log('Bio updated successfully')
        window.location.reload()
      },
      error: (error) => {
        console.error('An unknown error occurred: ', error)
      }
    })
  }
}