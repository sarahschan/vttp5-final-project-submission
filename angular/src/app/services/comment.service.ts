import { HttpClient } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'
import { Observable, catchError, throwError } from 'rxjs'
import { Comment } from '../models'


@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private httpClient = inject(HttpClient)

  getComments(recipeId: string): Observable<Comment[] | null> {
    return this.httpClient.get<Comment[]>(`/api/private/comments/${recipeId}`).pipe(
      catchError((error) => {
        console.error('HTTP Error Details:', error)
        return throwError(() => new Error('Failed to fetch comments'))
      })
    )
  }


  postComment(userId: string, recipeId: number, comment: string): Observable<any> {

    const payload = {
      userId: userId,
      recipeId: recipeId,
      comment: comment
    }

    // console.log('Sending comment payload to springboot', payload)

    return this.httpClient.post('/api/private/comments/new', payload).pipe(
      catchError((error) => {
        return throwError(() => new Error('Failed to submit recipe'))
      })
    )

  }

}
