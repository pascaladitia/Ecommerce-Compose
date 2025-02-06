package com.pascal.ecommercecompose.domain.model.dummy

import com.pascal.ecommercecompose.R

object DataDummy {
    val product = Product(
        1,
        "Nike Air Max 200",
        240.00,
        100,
        R.drawable.shooe_tilt_1,
        "Trending Now",
        description = "This Nike Air Max 200 comes constructed with transparent mesh on the upper, suede on the overlays and leather on the eyelets. Orange is then used on the tongue branding while a mixture of Blue and Grey runs throughout. Finishing the look is a White midsole and rubber outsole."
    )

    val productList = listOf(
        product,
        product.copy(
            2,
            "Nike Air Max 97",
            240.00,
            100,
            R.drawable.shoe_tilt_2,
            "Best Selling",
            description = "Despite what you might read, Japanese bullet trains weren’t the inspiration for the iconic “Silver Bullet” edition of the Air Max 97. Instead, designer Christian Tresser said his inspiration came from nature — specifically the ripples created when water drops into a pond."
        ),
        product.copy(
            3,
            "Nike Air Max 90",
            160.00,
            100,
            R.drawable.shoe_tilt_3,
            "Best Selling",
            description = "The Air Max 90 was originally known as the Air Max III until 2000 when it was reissued (taking a new name from the year of its launch). The original shoe’s radiant shade of red would later become known as “infrared.” The color remains synonymous with the Air Max 90 today."
        ),
        product.copy(
            3,
            "Nike Air Max 95",
            160.00,
            100,
            R.drawable.shoe_tilt_4,
            "Trending Now",
            description = "At first, the latest Air Max design hit resistance within Nike. It was unusual looking and featured the first-ever visible forefoot Air. Some loved the black midsole — the first time it was ever used in Nike running footwear — while others hated it. Which to designer Sergio Lozano, meant he was on to something."
        )
    )
}