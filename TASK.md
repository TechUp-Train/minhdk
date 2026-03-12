# Task: Create Compose Training Android Project

Create a complete Android project for Jetpack Compose training course (7 sessions, 5 junior devs).

## Project Structure

```
compose-training/
├── app/                          # Main app module
│   └── src/main/
│       └── kotlin/com/apero/composetraining/
│           ├── MainActivity.kt    # Session selector menu
│           ├── session1/          # Each session = 1 package
│           │   ├── demos/         # Live demo code (trainer shows these)
│           │   └── exercises/     # Starter code for exercises
│           ├── session2/
│           │   ├── demos/
│           │   └── exercises/
│           ├── ... (session3-7)
│           └── common/            # Shared models, theme, utils
├── solutions/                     # Solutions module (separate, trainer-only)
│   └── src/main/kotlin/...
├── TRAINING_PLAN.md              # Already exists
└── README.md                     # How to use this repo
```

## Requirements

### 1. Android Project Setup
- **Min SDK:** 24, **Target SDK:** 35, **Compile SDK:** 35
- **Package:** `com.apero.composetraining`
- **Compose BOM:** latest stable (2025.01.01 or similar)
- **Dependencies:** Material3, Navigation Compose, Coil, ConstraintLayout Compose, kotlinx-serialization
- **Kotlin:** 2.0+
- **Build:** Kotlin DSL (build.gradle.kts)

### 2. MainActivity — Session Selector
A simple LazyColumn listing all 7 sessions. Click → navigate to that session's demo/exercise screen.

### 3. Per-Session Content

Read TRAINING_PLAN.md for detailed exercise specs. For each session create:

#### Demo Code (trainer shows live)
- **Annotated with comments** explaining each concept
- **@Preview** for every demo composable
- **Progressive complexity** — start simple, build up

#### Exercise Starter Code  
- **Skeleton with TODOs** — junior fills in the blanks
- **Clear instructions in comments**
- **Data models provided** (junior focuses on UI, not data)

### Session-by-Session Details:

**Session 1: Fundamentals**
- Demo: Basic composables (Text, Image, Button), Modifier chains, Modifier order demo, Column/Row/Box
- Exercises: GreetingCard (Easy), ContactCard (Medium), ProfileScreen (Challenge)

**Session 2: Layouts & Lazy**
- Demo: LazyColumn vs Column perf, LazyColumn with key, LazyRow, LazyVerticalGrid, Scaffold
- Exercises: TodoList (Easy), MovieBrowser (Medium), PhotoGallery (Challenge)

**Session 3: State & Recomposition**
- Demo: Counter without remember (broken), Counter with remember (fixed), State hoisting, rememberSaveable, derivedStateOf
- Exercises: InteractiveCounter (Easy), ShoppingCart (Medium), SearchFilter (Challenge)

**Session 4: Theming**
- Demo: MaterialTheme wrapper, Custom colors, Dark mode toggle, CompositionLocal custom theme
- Exercises: WeatherCard (Easy), CustomTheme (Medium), DesignSystem (Challenge)

**Session 5: Navigation**
- Demo: NavHost setup, Arguments, Type-safe routes, Bottom navigation, Deep links
- Exercises: TwoScreenFlow (Easy), TabApp (Medium), ECommerceFlow (Challenge)

**Session 6: Animation & Gesture**
- Demo: animate*AsState, AnimatedVisibility, AnimatedContent, Spring vs Tween, Gestures
- Exercises: AnimatedLikeButton (Easy), ExpandableList (Medium), SwipeCard (Challenge)

**Session 7: Testing & Performance**
- Demo: ComposeTestRule basics, Find/Act/Assert, @Stable demo, Performance comparison
- Exercises: CounterTests (Easy), LoginFormTests (Medium), PerformanceAudit (Challenge)

### 4. Common Module
- `SampleData.kt` — Fake data (movies, contacts, products, todos) for all sessions
- `AppTheme.kt` — Base theme for the training app
- `Navigation.kt` — Root navigation setup

### 5. Code Quality
- **Comments in Vietnamese** where explaining concepts (for Apero junior devs)
- **English for code** (function names, variables)  
- **Every demo has @Preview**
- **TODO markers** in exercises are clear: `// TODO: [Session X] Bài tập Y - Mô tả`
- Clean, readable code — this is teaching material

### 6. README.md
- How to open & run the project
- Session-by-session guide
- How to switch between demo/exercise code

## Important Notes
- Focus on DEMO code quality — this is what the trainer presents live
- Exercises should have clear TODOs but enough structure that junior isn't lost
- Don't over-engineer — keep it simple enough for beginners
- Use hardcoded sample data, no network calls needed
- Each session should be independently runnable (navigate from MainActivity)

When completely finished, run this command to notify me:
openclaw system event --text "Done: Compose Training repo created with 7 sessions of demos + exercises" --mode now
