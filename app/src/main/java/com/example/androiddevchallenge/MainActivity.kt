/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.theme.MyTheme

enum class Sex {
    MALE, FEMALE
}

class MainActivity : AppCompatActivity() {
    private val petsViewModel: PetsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(petsViewModel)
            }
        }
    }
}

@Composable
fun MyApp(petsViewModel: PetsViewModel) {

    val pets : List<Pet> by petsViewModel.pets.observeAsState(listOf())

    // https://developer.android.com/jetpack/compose/navigation
    val navController = rememberNavController()
    NavHost(navController, startDestination = "petlist") {
        composable("petlist") {
            PetListScreen(
                pets = pets,
                navController = navController,
            )
        }
        composable("petdetail/{petId}") { backStackEntry ->
            PetDetailScreen(
                pet = backStackEntry.arguments?.getString("petId")?.let { petsViewModel.getPet(it.toInt())},
                navController = navController,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetListScreen(pets: List<Pet>, navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Compose Pet Adoption") }) },
        backgroundColor = MaterialTheme.colors.background,
    )
    {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(pets) { pet ->
                PetCard(
                    pet = pet,
                    onClick = { petId ->
                        navController.navigate("petdetail/$petId")
                    }
                )
            }
        }
    }
}

@Composable
fun PetDetailScreen(navController: NavController, pet: Pet?) {

    val title = if(pet?.name?.isEmpty() == true) {
        pet.name
    } else {
        "Compose Pet Adoption"
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = title)}) },
        backgroundColor = MaterialTheme.colors.background,
    ) {
        if (pet != null) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
            ) {
                Image(
                    painter = painterResource(pet.photo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .weight(1F),
                    contentScale = ContentScale.Fit
                )


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    val textPadding = 8.dp
                    item {
                        Text(
                            text = pet.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(textPadding),
                        )
                    }

                    item {
                    Text(
                        text = pet.id.toString(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(textPadding),
                    )
                }

                    item {
                    Text(
                        text = "${pet.sex.toString().toLowerCase().capitalize()}" +
                                "/" +
                                "${if (pet.isSpayedNeutered) "" else "Not "}${if (pet.sex == Sex.FEMALE) "Spayed" else "Nuetered"}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(textPadding),
                    )}

                    item {
                    Text(
                        text = pet.breed,
                        modifier = Modifier.padding(textPadding),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                    )}

                    item {
                        if (pet.ageMonth != 0 && pet.ageYear != 0) {
                            Text(
                                text = PetUtils.getFormattedAge(pet.ageYear, pet.ageMonth),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(textPadding),
                            )
                        }
                    }
                }

            }
            // TODO update to not just reuse the PetCard
//            PetCard(pet = pet, onClick = { /*TODO*/ })
        } else {
            Text(text = "Pet not found")
        }
    }
}

@Composable
fun PetCard(pet: Pet, onClick: (Int) -> Unit) {
    val textPadding = 4.dp

    Column(
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClick(pet.id) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(pet.photo),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = pet.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(textPadding),
        )

        Text(
            text = pet.id.toString(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(textPadding),
        )

        Text(
            text = "${pet.sex.toString().toLowerCase().capitalize()}" +
                    "/" +
                    "${if (pet.isSpayedNeutered) "" else "Not "}${if (pet.sex == Sex.FEMALE) "Spayed" else "Nuetered"}",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(textPadding),
        )

        Text(
            text = pet.breed,
            modifier = Modifier.padding(textPadding),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )

        if (pet.ageMonth != 0 && pet.ageYear != 0) {
            Text(
                text = PetUtils.getFormattedAge(pet.ageYear, pet.ageMonth),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(textPadding),
            )
        }
    }
}

@Preview
@Composable
fun PreviewPetCard() {
    PetCard(getPreviewPet()) {}
}

@Preview
@Composable
fun PreviewPetListScreen() {
    PetListScreen(pets = MutableList(14) { getPreviewPet() }, rememberNavController())
}

@Preview
@Composable
fun PreviewPetDetailScreen() {
    PetDetailScreen(pet = getPreviewPet(), navController = rememberNavController())
}

private fun getPreviewPet(): Pet {
    return Pet(id = 113321,
        photo = R.drawable.animal_dog_pet_cute,
        name = "Dolly",
        sex = Sex.FEMALE,
        isSpayedNeutered = true,
        breed = "Shepard Mix",
        ageMonth = 6,
        ageYear = 6)
}