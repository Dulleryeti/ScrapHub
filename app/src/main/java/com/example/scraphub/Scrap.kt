package com.example.scraphub

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Scrap(
    @PrimaryKey val id: UUID,
    val name: String, // name of employee selling
    //val rank: String, // rank of employee
    val title: String, // name of scrap
    val desc: String,
    val price: Int,
    val isFavorite: Boolean,
    val isBought: Boolean
) {
}