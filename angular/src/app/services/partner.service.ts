import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { BehaviorSubject } from 'rxjs'
import { Partner } from '../models'

@Injectable({
  providedIn: 'root'
})
export class PartnerService {

  private httpClient = inject(HttpClient)

  // Behaviour subject to track the partner stores
  private partnerStoresSubject = new BehaviorSubject<Partner[]>([])

  // Observable that components can subscribe to
  public partnerStores$ = this.partnerStoresSubject.asObservable()

  constructor() {
    // console.log('PartnerService constructor called')
    this.loadPartnerStores()
  }

  loadPartnerStores(): void {
    this.httpClient.get<Partner[]>(`/api/public/partners`)
      .subscribe({
        next: (partners) => {
          this.partnerStoresSubject.next(partners)
          // console.log('Partner stores loaded successfully: ', this.partnerStoresSubject.value)
        },
        error: (error) => {
          console.error('Error loading partner stores: ', error)
        }
      })
  }
  
}
