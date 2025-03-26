import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { GeminiRecipe } from '../models'
import { Observable } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class GeminiService {

  private httpClient = inject(HttpClient)

  getGeminiRecipe(ingredients: string[]): Observable<GeminiRecipe> {
    return this.httpClient.post<GeminiRecipe>(`/api/private/gemini/generateRecipe`, ingredients)
  }

}
