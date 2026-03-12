# Compose Training Plan — 7 Sessions
**Trainer:** HaiKM | **Học viên:** 5 juniors
**Format:** Sáng lý thuyết + demo live | Chiều bài tập thực hành

---

## 🎯 Tips Thuyết Trình Chung

1. **Live coding > Slide** — Mỗi concept, mở Android Studio code trực tiếp. Slide chỉ để giới thiệu concept, demo mới là lúc junior hiểu.
2. **Hỏi trước khi giảng** — "Theo mọi người, tại sao Column không dùng cho 1000 items?" → để junior suy nghĩ trước.
3. **Sai trước, đúng sau** — Cố tình code sai (quên `remember`, sai thứ tự Modifier), để junior thấy lỗi rồi fix. Nhớ lâu hơn.
4. **Pair up** — 5 người chia 2 cặp + 1 solo (hoặc 1 cặp + 1 nhóm 3). Rotate mỗi buổi.
5. **Check-in 3 phút** — Đầu mỗi buổi hỏi: "Buổi trước có gì chưa hiểu?"
6. **Time box** — Sáng 2.5h (9:00-11:30), Chiều 3h (13:30-16:30). Break 10 phút giữa mỗi session.

---

## Session 1: Compose Fundamentals — The Builder's Mindset

### 🎤 Cách Thuyết Trình
- **Mở đầu bằng so sánh** — Show 1 màn hình đơn giản bằng XML (20 dòng) vs Compose (8 dòng). Impact ngay.
- **Demo @Preview live** — Tạo composable, sửa text/color, thấy preview update real-time → "WOW moment".
- **Modifier order** — Demo trực tiếp `padding → background` vs `background → padding`. Cho junior đoán kết quả trước.
- **Đừng dành quá lâu vào lý thuyết Imperative vs Declarative** — 5 phút max, junior cần thấy code.

### 📝 Bài Tập Chiều (3 levels, mỗi level ~45 phút)

**Level 1: Greeting Card (Easy)** ⭐
```
Tạo 1 Card chứa:
- Icon (bất kỳ icon Material)
- Text "Hello, [tên mình]!" (fontSize 24sp, Bold)  
- Button "Say Hi" (click → đổi text thành "Hi back!")

Yêu cầu:
- Dùng Column layout
- Modifier: padding 16dp, fillMaxWidth
- Background color tùy chọn
```

**Level 2: Contact Card (Medium)** ⭐⭐
```
Tạo 1 Profile Card theo blueprint trong slide:
- Row chứa: Image (CircleShape, 60dp) + Column (Name bold + Bio regular)
- Button "Follow" ở dưới, fillMaxWidth
- Card có elevation, rounded corner 12dp

Yêu cầu:
- Modifier chain ít nhất 3 modifier
- Dùng cả Column, Row, Box
- @Preview với showBackground = true
```

**Level 3: Mini Profile Screen (Challenge)** ⭐⭐⭐
```
Tạo 1 màn hình hoàn chỉnh:
- Header: Avatar + Name + "Edit Profile" button
- Stats Row: 3 cột (Posts | Followers | Following) dùng Row + weight
- Bio section: Text nhiều dòng
- Action buttons: "Message" + "Follow" ngang hàng

Bonus: Thêm Spacer hợp lý, đúng alignment
```

**✅ Tiêu chí đánh giá:** Build thành công + Preview hiển thị đúng + Code sạch (không nest quá 4 level)

---

## Session 2: Layouts & Lazy Components

### 🎤 Cách Thuyết Trình
- **Demo giật lag** — Tạo Column với 500 Text items → scroll lag. Sau đó đổi sang LazyColumn → mượt. Visual proof.
- **Key parameter** — Demo reorder list KHÔNG có key (state mất) vs CÓ key (state giữ). Cho junior thấy bug thực tế.
- **Grid live** — Show `Fixed(2)` vs `Adaptive(128.dp)` trên cùng 1 data set, rotate thiết bị.
- **ConstraintLayout** — Chỉ giới thiệu 10 phút, đừng đào sâu. Junior cần LazyColumn nhiều hơn.

### 📝 Bài Tập Chiều

**Level 1: Todo List (Easy)** ⭐
```
Tạo LazyColumn hiển thị danh sách 20 todo items:
- Mỗi item: Checkbox + Text + Delete icon
- contentPadding = 16dp
- verticalArrangement = spacedBy(8dp)
- Key = todo.id

Data: hardcode list of data class Todo(id, title, isDone)
```

**Level 2: Movie App (Medium)** ⭐⭐
```
Màn hình Movie Browser:
- LazyRow trên cùng: "Trending" horizontal scroll (poster 120x180dp)
- LazyColumn phía dưới: "All Movies" vertical list
- Mỗi movie item: Row(Poster + Column(Title + Year + Rating))
- Scaffold với TopAppBar "🎬 Movies"

Data: 15 movies hardcode, dùng placeholder image
```

**Level 3: Photo Gallery (Challenge)** ⭐⭐⭐
```
Tạo Photo Gallery app:
- LazyRow tabs: "All", "Nature", "City", "People"  
- LazyVerticalStaggeredGrid hiển thị photos
- Mỗi photo card: Image + title overlay (Box)
- Grid: Adaptive(150.dp)
- Photos có chiều cao random (150-300dp) để thấy staggered effect

Bonus: Thêm Scaffold + FAB "Add Photo"
```

**✅ Tiêu chí:** Scroll mượt + Key đúng + Layout không bị overflow

---

## Session 3: State & Recomposition

### 🎤 Cách Thuyết Trình
- **"Bug" mở đầu** — Code counter KHÔNG có `remember` → click không đổi gì. Hỏi "Tại sao?" → giải thích recomposition.
- **Vẽ diagram** — Vẽ lên bảng: Function call → State reset → remember saves. Visual giúp nhiều.
- **State hoisting live** — Refactor 1 stateful component thành stateless trước mặt junior. Cho thấy reusability.
- **rememberSaveable** — Demo rotate screen, state mất → thêm Saveable → state giữ.

### 📝 Bài Tập Chiều

**Level 1: Interactive Counter (Easy)** ⭐
```
Counter app:
- Text hiển thị count (fontSize 48sp)
- Row: Button "-" | Button "+" | Button "Reset"
- Count không được < 0
- Dùng remember + mutableStateOf

Yêu cầu: Rotate thiết bị → count vẫn giữ (rememberSaveable)
```

**Level 2: Shopping Cart (Medium)** ⭐⭐
```
Mini shopping cart:
- LazyColumn: 5 products (Name + Price + Quantity selector [- count +])
- Bottom bar: "Total: $XXX" (derivedStateOf tính tổng)
- State hoisting: CartScreen(items, onQuantityChange)

Yêu cầu:
- Total tự update khi thay đổi quantity
- Quantity không < 0
- Stateless product item component
```

**Level 3: Search & Filter (Challenge)** ⭐⭐⭐
```
Contact Search screen:
- TextField search bar (state hoisted)
- LazyColumn: filtered contacts (derivedStateOf)
- Toggle: "Show favorites only" (Switch)
- Empty state: "No contacts found" khi list trống

Yêu cầu:
- UDF pattern: data flows down, events flow up
- derivedStateOf cho filtered list
- rememberSaveable cho search query
```

**✅ Tiêu chí:** State survive rotation + Derived state đúng + UDF pattern

---

## Session 4: Theming & Styling — Material 3

### 🎤 Cách Thuyết Trình
- **Before/After** — Show cùng 1 app KHÔNG theme vs CÓ theme. Dramatic difference.
- **Material Theme Builder** — Mở web tool (material-foundation.github.io), cho junior pick color → export code.
- **Dark mode toggle** — Demo switch dark/light real-time trong app.
- **CompositionLocal** — Giải thích như "biến global nhưng scoped". Demo nhỏ, đừng quá abstract.

### 📝 Bài Tập Chiều

**Level 1: Weather App Basic (Easy)** ⭐
```
Weather Card với MaterialTheme:
- Dùng MaterialTheme.colorScheme.primary/surface/onSurface
- Typography: headlineMedium cho temperature, bodyLarge cho description
- Shape: RoundedCornerShape(16.dp)
- Toggle dark mode bằng Switch

Data: hardcode "Hanoi, 32°C, Sunny"
```

**Level 2: Custom Theme (Medium)** ⭐⭐
```
Tạo custom color scheme cho 1 app tùy chọn:
- Định nghĩa lightColorScheme + darkColorScheme riêng
- Custom Typography (Google Font hoặc system font)
- Tạo wrapper MyAppTheme { content }
- Áp dụng lên 2 screens: Home + Detail

Yêu cầu: 
- Consistent color usage (không hardcode hex trong composable)
- isSystemInDarkTheme() tự switch
```

**Level 3: Design System (Challenge)** ⭐⭐⭐
```
Build mini Design System giống AI Outfit case study:
- data class AppColors (success, warning, error, gradient)
- CompositionLocalProvider
- AppTheme wrapper
- Áp dụng lên 1 Product Card:
  - "AI RECOMMENDED" badge (aiPrimary)
  - "TRENDING" pill (trending color)
  - Price text (custom priceTag typography)
  - Gradient bottom bar

Yêu cầu: AppTheme.colors.xxx syntax hoạt động
```

**✅ Tiêu chí:** Không hardcode color/size + Dark mode works + Custom theme applied

---

## Session 5: Navigation & Side Effects

### 🎤 Cách Thuyết Trình
- **Demo spaghetti trước** — Code `when(currentScreen)` switch → thêm 5 screens → "Thấy vấn đề chưa?" → Giới thiệu Navigation.
- **Back stack visual** — Dùng stack of cards/giấy thật trên bàn. Push = đặt lên, Pop = bỏ ra. Physical demo.
- **Type-safe route** — So sánh: typo "proflie/123" crash vs `Profile(id="123")` compiler check.
- **Deep link test** — `adb shell am start -d "myapp://product/123"` trên thiết bị → app mở đúng screen.

### 📝 Bài Tập Chiều

**Level 1: 2-Screen Flow (Easy)** ⭐
```
Welcome → Home:
- WelcomeScreen: Text + Button "Get Started"
- HomeScreen: Text "Welcome back!" + Button "Logout" (popBackStack)
- NavHost với 2 composable routes

Yêu cầu: Back button từ Home → Welcome. Logout clear stack.
```

**Level 2: Tab App (Medium)** ⭐⭐
```
3-tab app: Home / Search / Profile
- Scaffold + BottomNavigation
- Mỗi tab là 1 screen riêng
- Home: LazyColumn (items từ Session 2)
- Search: TextField + filtered list
- Profile: Static profile card

Yêu cầu:
- saveState = true (giữ scroll position khi switch tab)
- launchSingleTop = true
- Selected tab highlight
```

**Level 3: E-Commerce Flow (Challenge)** ⭐⭐⭐
```
Category → Product List → Product Detail → Cart:
- Type-safe routes: @Serializable data class
- Pass categoryId và productId qua navigation
- Cart screen: List products + Total
- Deep link: "myapp://product/{id}"

Yêu cầu:
- NavGraph: authGraph (Login) + mainGraph (Shop)
- popUpTo after login (clear login from stack)
- Argument passing hoạt động đúng
```

**✅ Tiêu chí:** Back stack đúng + Arguments pass đúng + Tab state preserved

---

## Session 6: Animation & Gesture

### 🎤 Cách Thuyết Trình  
- **Side-by-side** — App KHÔNG animation vs CÓ animation. Cùng feature, khác trải nghiệm hoàn toàn.
- **Spring vs Tween** — Demo cùng 1 animation với spring (bouncy) vs tween (linear). Cho junior chọn "cái nào feel tốt hơn?"
- **AnimatedVisibility** — Demo show/hide panel với fadeIn + slideIn combo.
- **Gesture interactive** — Code drag card trên máy thật, cho junior thử.

### 📝 Bài Tập Chiều

**Level 1: Animated Like Button (Easy)** ⭐
```
Like button:
- Icon heart: click toggle filled/outlined
- animateColorAsState: Gray → Red
- animateFloatAsState: scale 1.0 → 1.3 → 1.0 (graphicsLayer)

Yêu cầu: Spring animation, không tween
```

**Level 2: Expandable List (Medium)** ⭐⭐
```
FAQ Accordion:
- LazyColumn: 5 câu hỏi
- Click question → AnimatedVisibility (answer expand/collapse)
- Arrow icon rotate 0° → 180° (animateFloatAsState)
- animateContentSize trên answer text

Yêu cầu: Smooth animation, chỉ 1 item expand tại 1 thời điểm
```

**Level 3: Tinder Swipe Card (Challenge)** ⭐⭐⭐
```
Swipeable card stack:
- 5 profile cards stacked (Box)
- Drag gesture: detectDragGestures
- Swipe left = "Nope" (red overlay), right = "Like" (green overlay)
- Card rotation theo drag offset
- animateFloatAsState cho snap back khi thả giữa

Yêu cầu:
- Card tiếp theo slide lên khi top card bị swipe
- Drag amount > threshold → dismiss
- Drag amount < threshold → snap back với spring
```

**✅ Tiêu chí:** Animation smooth 60fps + Gesture responsive + Đúng physics (spring/tween)

---

## Session 7: Testing, Performance & KMP

### 🎤 Cách Thuyết Trình
- **Test demo đầu tiên** — Viết test cho Counter từ Session 3. Junior thấy familiar code → dễ hiểu testing.
- **Recomposition count** — Dùng Layout Inspector show component recompose 42 lần (bad) vs 1 lần (good sau optimize).
- **@Stable** — Demo `var` vs `val` + `List` vs `ImmutableList` → show recomposition difference.
- **KMP overview** — Chỉ giới thiệu 15-20 phút. Junior chưa cần deep dive, chỉ cần biết "tương lai là đây".

### 📝 Bài Tập Chiều

**Level 1: Counter Tests (Easy)** ⭐
```
Viết 5 UI tests cho Counter app (Session 3):
1. Initial state = 0
2. Click "+" → count = 1
3. Click "-" from 0 → count vẫn = 0
4. Click "+" 3 lần → count = 3
5. Click "Reset" → count = 0

Dùng: composeTestRule, onNodeWithText, performClick, assertIsDisplayed
```

**Level 2: Login Form Tests (Medium)** ⭐⭐
```
Login screen + tests:
- Email TextField + Password TextField + Login Button
- Validation: email not empty, password >= 6 chars
- Button disabled khi invalid

Tests (8 tests):
1. Initial: button disabled
2. Valid email + valid password → button enabled
3. Empty email → button disabled
4. Short password → button disabled  
5. Click login → loading indicator
6. Error state → error message displayed
7. testTag cho mỗi component
8. Semantic: contentDescription cho accessibility
```

**Level 3: Performance Audit (Challenge)** ⭐⭐⭐
```
Nhận 1 "bad code" sample có performance issues:
- List item dùng var thay val
- Không có key trong LazyColumn
- Heavy computation trong composable (không remember)
- Unstable data class

Nhiệm vụ:
1. Identify 5 performance issues
2. Fix từng issue
3. Giải thích tại sao fix đó improve performance
4. Bonus: Thêm @Stable annotation + derivedStateOf
```

**✅ Tiêu chí:** Tests pass green + Performance issues identified + Code stable

---

## 📊 Đánh Giá Học Viên

| Tiêu chí | Điểm |
|----------|------|
| Hoàn thành Level 1 (7 buổi) | 40% |
| Hoàn thành Level 2 (≥5 buổi) | 30% |
| Hoàn thành Level 3 (≥3 buổi) | 20% |
| Capstone project cuối khóa | 10% |

### 🏆 Capstone Project (Tuần cuối)
Mỗi junior/cặp build 1 mini app hoàn chỉnh trong 2 ngày:
- Ít nhất 3 screens + Navigation
- LazyColumn/Grid
- Custom Theme
- Ít nhất 1 animation
- 3 UI tests

Gợi ý: Recipe App, News Reader, Expense Tracker, Music Player UI

---

## 💡 Pro Tips Cho Trainer

1. **Code cùng nhau 15 phút đầu chiều** — Demo giải Level 1, junior code theo. Sau đó tự làm Level 2, 3.
2. **Review code cuối ngày** — Chọn 1 bài làm tốt, 1 bài cần improve, review trước lớp (15 phút).
3. **Slack/Zalo group** — Tạo group để junior hỏi ngoài giờ.
4. **Cheat sheet mỗi buổi** — 1 trang A4 tóm tắt syntax chính, junior giữ làm reference.
5. **Đừng skip Q&A** — Dành 15 phút cuối mỗi buổi sáng cho hỏi đáp.
