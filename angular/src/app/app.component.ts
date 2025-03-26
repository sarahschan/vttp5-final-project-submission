import { Component, inject, OnInit } from '@angular/core'
import { Store } from '@ngrx/store'
import * as AuthActions from './store/auth/auth.actions'
import { CrossTabSyncService } from './services/cross-tab-sync.service'


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.scss'
})

export class AppComponent implements OnInit {
  
  title = 'angular'

  private store = inject(Store)
  private crossTabSync = inject(CrossTabSyncService)

  ngOnInit(): void {
    // dispatch initAuth action when app starts
    this.store.dispatch(AuthActions.initAuth())

    // crossTabSync service is automatically injected and initialized since it's in the 'constructor'
    
  }

}

