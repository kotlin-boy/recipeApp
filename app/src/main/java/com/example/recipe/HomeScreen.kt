package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    navController: NavController,
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {

    val currentMonth by calendarViewModel.currentMonth.collectAsState()
    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val monthMealRecords by calendarViewModel.monthMealRecords.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
    ) {

        Text(
            text = "レシピ帳",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(Screen.ADD_RECIPE.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                "レシピ追加",
                fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                navController.navigate(Screen.RECIPE_LIST.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                "レシピ一覧",
                fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        RecipeCalendar(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            monthMealRecords = monthMealRecords,
            onPreviousMonth = calendarViewModel::previousMonth,
            onNextMonth = calendarViewModel::nextMonth,
            onDateClick = {
                calendarViewModel.selectDate(it)
                navController.navigate(Screen.CALENDAR.route)
            }
        )
    }
}