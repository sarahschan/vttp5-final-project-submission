import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { Activity } from '../models'
import { Observable, tap } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  private httpClient = inject(HttpClient)

  getActivity(followingList: number[], page: number = 0, size: number = 10): Observable<Activity[]> {
    
    const payload = {
      followingList: followingList,
      page: page,
      size: size
    }

    // console.log('ActivityService: Sending paginated getActivity request with payload:', payload)

    return this.httpClient.post<Activity[]>(`/api/private/activity`, payload)
  }
}