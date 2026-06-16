package com.example.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.HOME.route
            ) {

                composable(
                    route = Screen.HOME.route
                ) {
                    HomeScreen(
                        navController = navController
                    )
                }

                composable(
                    route = Screen.ADD_RECIPE.route
                ) {
                    AddRecipeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "レシピアプリ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Button(
            onClick = {
                navController.navigate(
                    Screen.ADD_RECIPE.route
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("レシピ閲覧")
        }


        Button(
            onClick = { },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("レシピ閲覧")
        }
    }
}