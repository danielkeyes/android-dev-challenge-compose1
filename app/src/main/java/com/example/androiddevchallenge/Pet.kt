package com.example.androiddevchallenge

data class Pet(
    val id: Int,
    val photo: Int,
    val name:String,
    val sex:Sex,
    val isSpayedNeutered: Boolean,
    val breed: String,
    val ageMonth: Int,
    val ageYear: Int,
)