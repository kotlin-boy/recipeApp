package com.example.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun RecipeCalendar(
    currentMonth: YearMonth,
    selectedDate: String,
    monthMealRecords: List<MealRecordWithRecipe>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = (1..currentMonth.lengthOfMonth()).map { day ->
        currentMonth.atDay(day)
    }

    val firstDay = currentMonth.atDay(1)
    val emptyDays = firstDay.dayOfWeek.value % 7

    val weekDays = listOf(
        "日", "月", "火", "水", "木", "金", "土"
    )

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onPreviousMonth
            ) {
                Text("＜")
            }

            Text(
                text = "${currentMonth.year}年${currentMonth.monthValue}月",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onNextMonth
            ) {
                Text("＞")
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
        ) {
            items(weekDays) { weekDay ->
                val textColor = when (weekDay) {
                    "日" -> Color.Red
                    "土" -> Color.Blue
                    else -> Color.Black
                }

                Text(
                    text = weekDay,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }

            items(emptyDays) {
                Text(text = "")
            }

            items(days) { date ->
                val backgroundColor = when {
                    date.toString() == selectedDate ->
                        Color.Gray.copy(alpha = 0.4f)

                    date.dayOfWeek == DayOfWeek.SUNDAY ->
                        Color.Red.copy(alpha = 0.2f)

                    date.dayOfWeek == DayOfWeek.SATURDAY ->
                        Color.Blue.copy(alpha = 0.2f)

                    else -> Color.Transparent
                }

                val recordsForDate = monthMealRecords.filter {
                    it.date == date.toString()
                }

                Column(
                    modifier = Modifier
                        .height(74.dp)
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .border(
                            width = 1.dp,
                            color = Color.Gray
                        )
                        .clickable {
                            onDateClick(date.toString())
                        }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfMonth.toString()
                    )

                    recordsForDate.take(3).forEach { mealRecord ->
                        Text(
                            text = mealRecord.recipeName,
                            fontSize = 11.sp,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}