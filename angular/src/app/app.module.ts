import { NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import { AppRoutingModule } from './app-routing.module'
import { AppComponent } from './app.component'
import { HomeComponent } from './components/home/home.component'
import { MaterialModule } from './material/material.module'
import { AccountComponent } from './components/account/account.component'
import { NewAccountComponent } from './components/account/new-account/new-account.component'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import { EffectsModule } from '@ngrx/effects'
import { StoreModule } from '@ngrx/store'
import { AuthEffects } from './store/auth/auth.effects'
import { reducer as authReducer } from './store/auth/auth.reducer'
import { HeaderComponent } from './components/skeleton/header/header.component'
import { FooterComponent } from './components/skeleton/footer/footer.component'
import { RouteGuardService } from './services/route-guard.service'
import { LoginComponent } from './components/account/login/login.component'
import { ProfileComponent } from './components/profile/profile.component'
import { AuthInterceptor } from './interceptors/auth.interceptor'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'
import { RecipesComponent } from './components/recipes/recipes.component'
import { CreateRecipeComponent } from './components/recipes/create-recipe/create-recipe.component'
import { ViewRecipeComponent } from './components/recipes/view-recipe/view-recipe.component'
import { OverviewComponent } from './components/recipes/overview/overview.component'
import { LikeButtonComponent } from './components/like-button/like-button.component'
import { MatPaginatorModule } from '@angular/material/paginator'
import { GoogleMapsModule } from '@angular/google-maps'
import { RecipePreviewComponent } from './components/recipes/recipe-preview/recipe-preview.component'
import { MyRecipesComponent } from './components/recipes/my-recipes/my-recipes.component'
import { PostedRecipesComponent } from './components/recipes/my-recipes/posted-recipes/posted-recipes.component'
import { LikedRecipesComponent } from './components/recipes/my-recipes/liked-recipes/liked-recipes.component'
import { UsersComponent } from './components/users/users.component'
import { UserInfoComponent } from './components/users/user-info/user-info.component'
import { FollowButtonComponent } from './components/follow-button/follow-button.component'
import { ActivityComponent } from './components/activity/activity.component'
import { PartnersComponent } from './components/partners/partners.component'
import { NutritionComponent } from './components/nutrition/nutrition.component'
import { GeminiRecipeComponent } from './components/gemini-recipe/gemini-recipe.component'


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AccountComponent,
    LoginComponent,
    NewAccountComponent,
    HeaderComponent,
    FooterComponent,
    ProfileComponent,
    RecipesComponent,
    CreateRecipeComponent,
    ViewRecipeComponent,
    OverviewComponent,
    LikeButtonComponent,
    RecipePreviewComponent,
    MyRecipesComponent,
    PostedRecipesComponent,
    LikedRecipesComponent,
    UsersComponent,
    UserInfoComponent,
    FollowButtonComponent,
    ActivityComponent,
    PartnersComponent,
    NutritionComponent,
    GeminiRecipeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    StoreModule.forRoot({ auth: authReducer }),
    EffectsModule.forRoot([AuthEffects]),
    NgbModule,
    FormsModule,
    MatPaginatorModule,
    GoogleMapsModule
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    RouteGuardService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
