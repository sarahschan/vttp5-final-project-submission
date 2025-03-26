export interface User {
    userId: number | null
    name: string
    username: string
    email: string
    password: string
    confirm_password: string
    profile_picture: string | null
    short_bio: string | null
}


export interface FollowProfile {
    userId: number
    username: string
    profilePicture: string | null
}


export interface LoginCredentials {
    username: string
    password: string
}


export interface FilterCriteria {
    minCalories: number
    maxCalories: number
    minProtein: number
    maxProtein: number
    minCarbs: number
    maxCarbs: number
    minFats: number
    maxFats: number
    sortBy: string
}


export interface RecipeOverview {
    recipeId: number
    title: string
    protein: number
    carbs: number
    fats: number
    calories: number
    imageUrl: string
    username: string
    likes: number
}

export interface Page<T> {
    content: T[]
    pageable: {
        pageNumber: number
        pageSize: number
        sort: {
            sorted: boolean
            unsorted: boolean
            empty: boolean
        }
        offset: number
        paged: boolean
        unpaged: boolean
    }
    totalElements: number
    totalPages: number
    last: boolean
    first: boolean
    size: number
    number: number
    sort: {
        sorted: boolean
        unsorted: boolean
        empty: boolean
    }
    numberOfElements: number
    empty: boolean
}

export interface Recipe {
    recipe_id: number
    user_id: number
    username: string
    title: string
    description: string
    protein: number
    carbs: number
    fats: number
    calories: number
    servings: number
    prep_time: number
    cook_time: number
    image_url: string | null
    updated_at: string
    ingredients: Ingredient[]
    instructions: Instruction[]
}

export interface Ingredient {
    ingredient: string
    amount: string
    unit: string
    notes: string
}

export interface Instruction {
    step: string
}


export interface Comment {
    username: string
    profile_picture: string | null
    comment: string
}


export interface Activity {
    activityId: number
    activityType: string
    userId: number
    username: number
    userProfilePicture: string
    recipeId: number
    recipeTitle: string
    recipeImageUrl: string
    commentId: number
    commentText: string
    followedUserId: number
    followedUsername: string
    followedUserProfilePicture: string
    timestamp: string | Date
}


export interface Partner {
    partnerStoreId: number
    name: string
    addressLine1: string
    addressLine2: string
    postalCode: string
    phone: string
    latitude: number
    longitude: number
    website: string
}


export interface NutritionRec {
    carbs: number
    carbsRange: string
    protein: number
    proteinRange: string
    fat: number
    fatRange: string
    saturatedFat: string
    sugar: string
    calories: number
    tdee: number
    bmr: number
    weight: number
    height: number
    age: number
    sex: string
    activityLevel: string
    goal: string
}

export interface GeminiRecipe {
    title: string
    description: string
    ingredients: string[]
    prepTime: string
    cookTime: string
    steps: string[]
}