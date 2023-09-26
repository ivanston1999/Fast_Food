package com.android.fastfood.data

import com.android.fastfood.model.FoodMenu

interface FoodMenuDataSource{
    fun getFood(): List<FoodMenu>
}

class FoodMenuDataSourceImpl(): FoodMenuDataSource {

    override fun getFood(): List<FoodMenu> = listOf(

        FoodMenu(
            foodName = "Sate Ayam",
            price = 28000.0,
            description = "Sate Ayam adalah hidangan Indonesia yang terdiri dari potongan daging ayam yang dimarinasi dengan rempah-rempah khas, ditusuk, dan dipanggang di atas bara api hingga kecokelatan.",
            location = "Jl. Biak No.48, RT.6/RW.11, Cideng, Kecamatan Gambir, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10150" ,
            imgFoodUrl = "https://images.unsplash.com/photo-1620088676099-c709b5838922?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
        ),

        FoodMenu(
            foodName = "Ayam Panggang",
            price = 45000.0,
            description = "Nikmati kenikmatan gurih dan kecokelatan dengan Ayam Panggang kami yang lembut dan kulitnya yang renyah. Hidangan sempurna untuk memanjakan lidah Anda!",
            imgFoodUrl = "https://images.unsplash.com/photo-1627799370307-9b2a689bb94f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8YXlhbSUyMHBhbmdnYW5nfGVufDB8fDB8fHww&auto=format&fit=crop&w=600&q=60",
            location = "Jl. Cipinang Baru Utara No.22, RT.11/RW.2, Cipinang, Kec. Pulo Gadung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13220"
        ),

        FoodMenu(
            foodName = "Dimsum",
            price = 25000.0,
            description = "Cicipi kelezatan Dimsum, sajian makanan Asia yang lezat dan beragam! Gigitan-gigitan kecil berisi rasa yang menggugah selera, siap memanjakan lidah Anda",
            imgFoodUrl = "https://images.unsplash.com/photo-1523905330026-b8bd1f5f320e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1779&q=80",
            location = "Jl. Tegal Parang Selatan I No.41 C, RT.2/RW.5, Tegal Parang, Kec. Mampang Prpt., Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12790"
        ),

        FoodMenu(
            foodName = "Nasi Goreng",
            price = 30000.0,
            description = "Nasi Goreng, sajian klasik Asia yang menggoda selera! Rasakan perpaduan sempurna antara nasi yang gurih, bumbu rempah yang khas, dan bahan tambahan favorit Anda dalam setiap suapan",
            imgFoodUrl = "https://images.unsplash.com/photo-1581184953963-d15972933db1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1930&q=80",
            location = "Jl. Kebon Sirih No.3, Kb. Sirih, Kec. Menteng, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10340"
        ),

        FoodMenu(
            foodName = "Sate Taichan",
            price = 20000.0,
            description = "Nikmati kelezatan Sate Taican yang renyah dan beraroma, sajian lezat yang akan menggoyang lidah Anda hingga menjilat jari!",
            imgFoodUrl = "https://images.unsplash.com/photo-1636301175218-6994458a4b0a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1887&q=80",
            location = "Jl. Terusan Hang Lekir II Blok Mushola No.28 A, RT.9/RW.6, Gunung, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12220"
        ),

        FoodMenu(
            foodName = "Kebab",
            price = 13000.0,
            description = "Selami kelezatan Timur Tengah dengan setiap suapan kebab kami. Rasakan paduan sempurna daging yang empuk, rempah-rempah khas, dan saus lezat yang akan membuat Anda ketagihan.",
            imgFoodUrl = "https://images.unsplash.com/photo-1579888944880-d98341245702?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
            location = "Pasar Mayestik, Lt. Dasar, Blok City Walk, Jalan Tebah 3, Kebayoran Baru, RT.1/RW.3, Gunung, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12120"
        )
    )


}