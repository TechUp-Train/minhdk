package com.apero.composetraining.session5.exercises.data

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val specs: List<ProductSpec>
)

data class ProductSpec(
    val label: String,
    val value: String
)

data class CartItem(
    val product: Product,
    val quantity: Int,
    val specLine: String // ví dụ: "Size: M, Color: Black"
)

val sampleProductList = listOf(
    Product(
        id = 0,
        name = "Premium Wireless Headphones",
        price = 299.99,
        description = "High-fidelity audio with ANC and 40-hour battery life. Comfortable, durable, and ideal for commuting or focused work.",
        specs = listOf(
            ProductSpec("Color", "Midnight Black"),
            ProductSpec("Connectivity", "Bluetooth 5.3"),
            ProductSpec("Weight", "250g")
        )
    ),
    Product(
        id = 1,
        name = "Smart Fitness Watch Pro",
        price = 199.49,
        description = "Track your heart rate, sleep, steps, and receive notifications with a vibrant AMOLED display.",
        specs = listOf(
            ProductSpec("Color", "Space Gray"),
            ProductSpec("Battery Life", "10 Days"),
            ProductSpec("Water Resistance", "5 ATM")
        )
    ),
    Product(
        id = 2,
        name = "Portable Bluetooth Speaker",
        price = 129.00,
        description = "Rich bass, clear mids, and crisp highs with 18 hours of continuous playback. Perfect for outdoor activities.",
        specs = listOf(
            ProductSpec("Color", "Ocean Blue"),
            ProductSpec("Connectivity", "Bluetooth 5.0"),
            ProductSpec("Weight", "620g")
        )
    ),
    Product(
        id = 3,
        name = "Mechanical Gaming Keyboard",
        price = 159.99,
        description = "Tactile switches, RGB lighting, and durable aluminum frame for high-performance gaming sessions.",
        specs = listOf(
            ProductSpec("Switch Type", "Tactile Brown"),
            ProductSpec("Backlight", "RGB"),
            ProductSpec("Connection", "USB-C")
        )
    ),
    Product(
        id = 4,
        name = "4K Ultra HD Monitor 27\"",
        price = 349.00,
        description = "Crisp 4K visuals with HDR support and ultra-thin bezels. Ideal for productivity and creative tasks.",
        specs = listOf(
            ProductSpec("Resolution", "3840 × 2160"),
            ProductSpec("Refresh Rate", "75Hz"),
            ProductSpec("Panel", "IPS")
        )
    ),
    Product(
        id = 5,
        name = "Noise Cancelling Earbuds",
        price = 149.90,
        description = "Compact, lightweight earbuds with hybrid ANC and up to 28 hours of total playtime.",
        specs = listOf(
            ProductSpec("Color", "Pearl White"),
            ProductSpec("Connectivity", "Bluetooth 5.2"),
            ProductSpec("Weight", "58g (case included)")
        )
    ),
    Product(
        id = 6,
        name = "Ergonomic Office Chair",
        price = 269.50,
        description = "Adaptive lumbar support, breathable mesh, and smooth 4D armrests designed for long work sessions.",
        specs = listOf(
            ProductSpec("Color", "Graphite"),
            ProductSpec("Material", "Mesh + Steel"),
            ProductSpec("Weight Capacity", "135kg")
        )
    ),
    Product(
        id = 7,
        name = "Smart Home Air Purifier",
        price = 189.00,
        description = "HEPA-13 filtration removes 99.97% of allergens and pollutants. App-controlled with real-time air stats.",
        specs = listOf(
            ProductSpec("Coverage", "45 m²"),
            ProductSpec("Filter Type", "HEPA-13"),
            ProductSpec("Noise Level", "23–48 dB")
        )
    ),
    Product(
        id = 8,
        name = "Ultra-Slim Laptop Stand",
        price = 39.90,
        description = "Lightweight, adjustable aluminum stand improves airflow and posture for comfortable laptop use.",
        specs = listOf(
            ProductSpec("Material", "Aluminum"),
            ProductSpec("Height Levels", "6 Levels"),
            ProductSpec("Weight", "190g")
        )
    ),
    Product(
        id = 9,
        name = "Pro Webcam 1080p",
        price = 89.99,
        description = "Crystal-clear Full HD video with dual-noise-reduction microphones. Excellent for meetings and streaming.",
        specs = listOf(
            ProductSpec("Resolution", "1080p / 30fps"),
            ProductSpec("Field of View", "78°"),
            ProductSpec("Connection", "USB-A")
        )
    )
)