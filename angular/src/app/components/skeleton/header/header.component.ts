import { Component, inject, OnInit } from '@angular/core'
import { Store } from '@ngrx/store'
import { selectIsLoggedIn } from '../../../store/auth/auth.selectors'

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {

  private store = inject(Store)
  isLoggedIn: boolean = false

  isCollapsed = false;


  ngOnInit(): void {
    this.store.select(selectIsLoggedIn).subscribe((isLoggedIn) => {
      this.isLoggedIn = isLoggedIn
    })
  }

}
