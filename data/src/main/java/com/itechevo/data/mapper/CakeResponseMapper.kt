package com.itechevo.data.mapper

import com.itechevo.data.model.CakeResponse
import com.itechevo.domain.model.Cake

fun CakeResponse.toDomain(): Cake =
    Cake(
        title,
        desc,
        image
    )