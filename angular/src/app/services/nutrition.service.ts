import { HttpClient, HttpParams } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { NutritionRec } from '../models'

@Injectable({
  providedIn: 'root'
})
export class NutritionService {

  private httpClient = inject(HttpClient)

  getNutritionRec(searchValues: any) {
    
    let params = new HttpParams()
    .set('height', searchValues.height)
    .set('weight', searchValues.weight)
    .set('age', searchValues.age)
    .set('sex', searchValues.sex)
    .set('activityLevel', searchValues.activityLevel)
    .set('goal', searchValues.goal)

    return this.httpClient.get<NutritionRec>(`/api/private/nutrition`, { params })
  }
}
