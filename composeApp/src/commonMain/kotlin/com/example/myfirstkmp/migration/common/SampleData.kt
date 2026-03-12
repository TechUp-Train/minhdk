package com.apero.composetraining.common

/**
 * Dữ liệu mẫu dùng chung cho tất cả sessions.
 * Hardcoded — không cần network call.
 */

// === Models ===

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val rating: Float,
    val genre: String,
    val posterUrl: String = "https://picsum.photos/seed/movie$id/200/300",
    val description: String = ""
)

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val avatarUrl: String = "https://i.pravatar.cc/150?u=$id",
    val isFavorite: Boolean = false,
    val bio: String = ""
)

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "https://picsum.photos/seed/product$id/200/200",
    val description: String = "",
    val isAiRecommended: Boolean = false,
    val isTrending: Boolean = false
)

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)

data class FaqItem(
    val id: Int,
    val question: String,
    val answer: String
)

data class ProfileCard(
    val id: Int,
    val name: String,
    val age: Int,
    val bio: String,
    val imageUrl: String = "https://i.pravatar.cc/300?u=swipe$id"
)

data class Photo(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String = "https://picsum.photos/seed/photo$id/400/300",
    val heightDp: Int = (150..300).random()
)

// === Sample Data ===

object SampleData {

    val movies = listOf(
        Movie(1, "The Shawshank Redemption", 1994, 9.3f, "Drama", description = "Two imprisoned men bond over a number of years."),
        Movie(2, "The Godfather", 1972, 9.2f, "Crime", description = "The aging patriarch of an organized crime dynasty."),
        Movie(3, "The Dark Knight", 2008, 9.0f, "Action", description = "Batman must accept one of the greatest psychological tests."),
        Movie(4, "Pulp Fiction", 1994, 8.9f, "Crime", description = "The lives of two mob hitmen intertwine."),
        Movie(5, "Forrest Gump", 1994, 8.8f, "Drama", description = "The life story of a slow-witted but kind-hearted man."),
        Movie(6, "Inception", 2010, 8.8f, "Sci-Fi", description = "A thief who steals secrets through dream-sharing technology."),
        Movie(7, "The Matrix", 1999, 8.7f, "Sci-Fi", description = "A hacker discovers the true nature of reality."),
        Movie(8, "Interstellar", 2014, 8.7f, "Sci-Fi", description = "Explorers travel through a wormhole in space."),
        Movie(9, "Parasite", 2019, 8.5f, "Thriller", description = "Greed and class discrimination threaten a symbiotic relationship."),
        Movie(10, "Spirited Away", 2001, 8.6f, "Animation", description = "A girl wanders into a world of gods and spirits."),
        Movie(11, "Avengers: Endgame", 2019, 8.4f, "Action", description = "The Avengers assemble once more to reverse Thanos' actions."),
        Movie(12, "Your Name", 2016, 8.4f, "Animation", description = "Two strangers find themselves linked in a bizarre way."),
        Movie(13, "Joker", 2019, 8.4f, "Drama", description = "A mentally troubled comedian embarks on a downward spiral."),
        Movie(14, "Whiplash", 2014, 8.5f, "Music", description = "A drummer enrolls at a cutthroat music conservatory."),
        Movie(15, "Coco", 2017, 8.4f, "Animation", description = "A boy journeys to the Land of the Dead.")
    )

    val contacts = listOf(
        Contact(1, "Nguyễn Văn An", "0901234567", "an.nguyen@email.com", isFavorite = true, bio = "Android Developer tại Apero"),
        Contact(2, "Trần Thị Bình", "0912345678", "binh.tran@email.com", bio = "UI/UX Designer"),
        Contact(3, "Lê Hoàng Cường", "0923456789", "cuong.le@email.com", isFavorite = true, bio = "Backend Engineer"),
        Contact(4, "Phạm Minh Duy", "0934567890", "duy.pham@email.com", bio = "Product Manager"),
        Contact(5, "Hoàng Thị Em", "0945678901", "em.hoang@email.com", bio = "QA Engineer"),
        Contact(6, "Ngô Đức Phú", "0956789012", "phu.ngo@email.com", isFavorite = true, bio = "iOS Developer"),
        Contact(7, "Vũ Thị Giang", "0967890123", "giang.vu@email.com", bio = "Data Analyst"),
        Contact(8, "Đỗ Quang Hùng", "0978901234", "hung.do@email.com", bio = "DevOps Engineer"),
        Contact(9, "Bùi Thị Oanh", "0989012345", "oanh.bui@email.com", bio = "Scrum Master"),
        Contact(10, "Lý Văn Khánh", "0990123456", "khanh.ly@email.com", bio = "Tech Lead")
    )

    val products = listOf(
        Product(1, "iPhone 16 Pro", 999.0, "Electronics", description = "Latest Apple smartphone", isAiRecommended = true),
        Product(2, "MacBook Air M3", 1099.0, "Electronics", description = "Ultra-thin laptop", isTrending = true),
        Product(3, "AirPods Pro 3", 249.0, "Electronics", description = "Noise-cancelling earbuds"),
        Product(4, "Nike Air Max", 159.0, "Fashion", description = "Classic running shoes", isTrending = true),
        Product(5, "Kindle Paperwhite", 139.0, "Electronics", description = "E-reader with warm light"),
        Product(6, "Lego Technic", 89.0, "Toys", description = "Building set for adults", isAiRecommended = true),
        Product(7, "Uniqlo T-Shirt", 19.0, "Fashion", description = "Basic cotton tee"),
        Product(8, "Sony WH-1000XM5", 349.0, "Electronics", description = "Premium headphones", isAiRecommended = true),
        Product(9, "Moleskine Notebook", 25.0, "Stationery", description = "Classic ruled notebook"),
        Product(10, "Anker PowerBank", 45.0, "Electronics", description = "20000mAh portable charger")
    )

    val todos = (1..20).map { i ->
        Todo(
            id = i,
            title = listOf(
                "Học Jetpack Compose", "Review pull request", "Fix bug login screen",
                "Viết unit test", "Update README", "Refactor ViewModel",
                "Design new feature", "Code review session", "Deploy staging",
                "Họp sprint planning", "Cập nhật documentation", "Tối ưu performance",
                "Test trên nhiều devices", "Viết release notes", "Setup CI/CD",
                "Học Kotlin coroutines", "Implement dark mode", "Add animations",
                "Xử lý edge cases", "Prepare demo cho khách hàng"
            )[i - 1],
            isDone = i % 3 == 0
        )
    }

    val faqItems = listOf(
        FaqItem(1, "Jetpack Compose là gì?", "Jetpack Compose là toolkit UI hiện đại của Android, sử dụng Kotlin để xây dựng giao diện theo cách khai báo (declarative). Thay vì dùng XML layout, bạn viết các hàm composable mô tả UI."),
        FaqItem(2, "Tại sao nên dùng Compose thay vì XML?", "Compose giảm boilerplate code, hỗ trợ preview real-time, dễ test hơn, và tích hợp tốt với Kotlin. Google đang chuyển hướng hoàn toàn sang Compose."),
        FaqItem(3, "remember và rememberSaveable khác gì nhau?", "remember giữ state qua recomposition nhưng mất khi configuration change (xoay màn hình). rememberSaveable giữ state qua cả configuration change."),
        FaqItem(4, "State hoisting là gì?", "State hoisting là pattern đưa state lên component cha, component con chỉ nhận state và callback. Giúp component con stateless, dễ test và reuse."),
        FaqItem(5, "LazyColumn khác gì Column?", "Column render tất cả items cùng lúc. LazyColumn chỉ render items đang hiển thị trên màn hình (giống RecyclerView). Dùng LazyColumn cho list dài.")
    )

    val profileCards = listOf(
        ProfileCard(1, "Minh Anh", 25, "Coffee lover ☕ | Traveler ✈️ | Cat person 🐱"),
        ProfileCard(2, "Hồng Nhung", 23, "Bookworm 📚 | Yoga enthusiast 🧘 | Foodie 🍜"),
        ProfileCard(3, "Đức Thắng", 27, "Gym rat 💪 | Music producer 🎵 | Dog dad 🐕"),
        ProfileCard(4, "Thu Hà", 24, "Photography 📸 | Hiking 🏔️ | Plant mom 🌿"),
        ProfileCard(5, "Quang Minh", 26, "Gamer 🎮 | Coder by day 💻 | Chef by night 👨‍🍳")
    )

    val photos = listOf(
        Photo(1, "Sunset Beach", "Nature", heightDp = 200),
        Photo(2, "City Skyline", "City", heightDp = 250),
        Photo(3, "Mountain Peak", "Nature", heightDp = 180),
        Photo(4, "Street Market", "City", heightDp = 220),
        Photo(5, "Happy Family", "People", heightDp = 160),
        Photo(6, "Waterfall", "Nature", heightDp = 280),
        Photo(7, "Neon Signs", "City", heightDp = 190),
        Photo(8, "Portrait", "People", heightDp = 240),
        Photo(9, "Forest Path", "Nature", heightDp = 210),
        Photo(10, "Coffee Shop", "City", heightDp = 170),
        Photo(11, "Group Photo", "People", heightDp = 200),
        Photo(12, "Ocean Waves", "Nature", heightDp = 260)
    )

    val categories = listOf("Electronics", "Fashion", "Toys", "Stationery")
}
