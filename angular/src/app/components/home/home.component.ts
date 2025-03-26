import { Component, inject, OnInit } from '@angular/core'
import { Store } from '@ngrx/store'
import { selectIsLoggedIn } from '../../store/auth/auth.selectors'

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  private store = inject(Store)
    isLoggedIn: boolean = false
  
    isCollapsed = false
  
  
    ngOnInit(): void {
      this.store.select(selectIsLoggedIn).subscribe((isLoggedIn) => {
        this.isLoggedIn = isLoggedIn
      })
    }
    
}
