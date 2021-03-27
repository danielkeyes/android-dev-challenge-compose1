package com.example.androiddevchallenge

class PetUtils {
    companion object {

        fun getFormattedAge(years: Int = 0, months: Int = 0): String {
        val ageYears =  when(years) {
            0 -> ""
            1 -> "$years year"
            else -> "$years years"
        }

        val ageMonths =  when(months) {
            0 -> ""
            1 -> "$months month"
            else -> "$months months"
        }

        val formattedAge =
        "${if(!ageYears.isEmpty()){"$ageYears"}else{""}}" +
        "${if(!ageYears.isEmpty() && !ageMonths.isEmpty()){" "}else{""}}" +
        "${if(!ageMonths.isEmpty()){"$ageMonths"}else{""}}" +
        "${if(!ageYears.isEmpty() || !ageMonths.isEmpty()){" old"}else{""}}"

        return formattedAge
    }
}
}