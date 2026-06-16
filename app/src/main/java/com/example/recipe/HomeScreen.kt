package com.example.recipe

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
import androidx.navigation.NavController
import androidx.compose.foundation.layout.safeDrawingPadding

@Composable
fun HomeScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
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
            Text("レシピ追加")
        }

        Button(
            onClick = {
                // 後でレシピ閲覧画面へ遷移
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("レシピ閲覧")
        }
    }
}