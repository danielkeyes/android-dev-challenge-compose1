package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class PetsViewModel: ViewModel() {
    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _pets = MutableLiveData(getPets())
    val pets: LiveData<List<Pet>> = _pets


    private fun getPets(): List<Pet> {
        val pet = Pet(id = 113321,
            photo = R.drawable.animal_dog_pet_cute,
            name = "Dolly",
            sex = Sex.FEMALE,
            isSpayedNeutered = true,
            breed = "Shepard Mix",
            ageMonth = 6,
            ageYear = 6)

        val pets = listOf(pet, pet, pet, pet, pet, pet, pet, pet)

        return pets
    }

    /**
     * Returns pet with petId. Returns null if Pet does not exist
     *
     * @param petId Id of pet
     */
    fun getPet(petId: Int): Pet? {
        for (pet in getPets()) {
            if(pet.id == petId){
                return pet
            }
        }
        return null
    }

    /**
     * Return a random pet from all pets
     */
    fun getRandomPet(): Pet? {
        val size = pets.value?.size

        // Return a random pet
        if (size != null && size > 0) {
            return pets.value?.get((size * Math.random()).toInt())
        }

        // If unable to get random pet, return null
        return null
    }
}