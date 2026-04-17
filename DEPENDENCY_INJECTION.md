# Dependency Injection Setup

This project now uses **Hilt** for dependency injection.

## Setup Overview

### 1. Application Class (`CowpawApplication.kt`)
- Annotated with `@HiltAndroidApp`
- Entry point for Hilt dependency injection
- Registered in `AndroidManifest.xml`

### 2. Database Module (`DatabaseModule.kt`)
- Located in `com.inodaf.cowpaw.di` package
- Provides `SQLiteOpenHelper` instance (specifically `DbHelper`)
- The module is a singleton, so the database helper is shared across the app

### 3. Repository (`TransactionSqliteRepository.kt`)
- Uses constructor injection with `@Inject` annotation
- Receives `SQLiteOpenHelper` automatically from Hilt
- No need to manually instantiate `DbHelper`

## How to Use

### In Activities
Add `@AndroidEntryPoint` annotation to your activity:

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var repository: TransactionRepository
    
    // Use repository...
}
```

### In Fragments
Add `@AndroidEntryPoint` annotation to your fragment:

```kotlin
@AndroidEntryPoint
class MyFragment : Fragment() {
    @Inject lateinit var repository: TransactionRepository
    
    // Use repository...
}
```

### In ViewModels
Use `@HiltViewModel` and inject via constructor:

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    // Use repository...
}
```

## Dependencies Added

- Hilt Android: 2.52
- Hilt Compiler: 2.52 (kapt)
- Dagger Hilt Android Gradle Plugin: 2.52

## Note
The Kotlin version was updated to 2.1.0 for compatibility with Hilt 2.52.

