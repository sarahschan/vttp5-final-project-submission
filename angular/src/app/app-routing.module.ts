import { NgModule } from '@angular/core'
import { ExtraOptions, RouterModule, Routes } from '@angular/router'
import { HomeComponent } from './components/home/home.component'
import { AccountComponent } from './components/account/account.component'
import { RouteGuardService } from './services/route-guard.service'
import { ProfileComponent } from './components/profile/profile.component'
import { RecipesComponent } from './components/recipes/recipes.component'
import { CreateRecipeComponent } from './components/recipes/create-recipe/create-recipe.component'
import { ViewRecipeComponent } from './components/recipes/view-recipe/view-recipe.component'
import { RecipePreviewComponent } from './components/recipes/recipe-preview/recipe-preview.component'
import { MyRecipesComponent } from './components/recipes/my-recipes/my-recipes.component'
import { UsersComponent } from './components/users/users.component'
import { ActivityComponent } from './components/activity/activity.component'
import { PartnersComponent } from './components/partners/partners.component'
import { NutritionComponent } from './components/nutrition/nutrition.component'
import { GeminiRecipeComponent } from './components/gemini-recipe/gemini-recipe.component'


const routes: Routes = [
  // public routes
  { path: '', 
    component: HomeComponent, 
    canActivate: [ RouteGuardService ]
  },
  { path: 'recipe-preview',
    component: RecipePreviewComponent,
    canActivate: [ RouteGuardService ]
  },
  { path: 'users/:username',
    component: UsersComponent,
    canActivate: [ RouteGuardService ]
  },
  { path: 'login', 
    component: AccountComponent,
    canActivate: [ RouteGuardService ]
  },
  { path: 'partners', 
    component: PartnersComponent,
    canActivate: [ RouteGuardService ]
  },
  
  //private routes
  { path: 'activity',
    component: ActivityComponent,
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'recipes', 
    component: RecipesComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'recipes/my-recipes', 
    component: MyRecipesComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'recipes/new', 
    component: CreateRecipeComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'recipes/cookbook', 
    component: MyRecipesComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'nutrition',
    component: NutritionComponent,
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'clear-my-fridge',
    component: GeminiRecipeComponent,
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'recipes/:recipeId', 
    component: ViewRecipeComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },
  { path: 'account', 
    component: ProfileComponent, 
    canActivate: [ RouteGuardService ],
    data: { authRequired: true }
  },

  // redirect all non-routes to home
  { path: '**', redirectTo: '' }
];

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled'
};


@NgModule({
  imports: [
    RouterModule.forRoot(routes, routerOptions)
  ],
  exports: [
    RouterModule
  ]
})


export class AppRoutingModule { }
