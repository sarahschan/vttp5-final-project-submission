import { Component, Input} from '@angular/core'
import { User } from '../../../models'

@Component({
  selector: 'app-user-info',
  standalone: false,
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.scss'
})
export class UserInfoComponent {
  
  @Input()
  user: User | null = null
  
}
