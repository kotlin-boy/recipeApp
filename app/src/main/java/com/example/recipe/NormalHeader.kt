package com.example.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NormalHeader(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    selectedGenre: RecipeGenre?,
    onGenreSelected: (RecipeGenre?) -> Unit,
    selectedSort: SortOrder,
    onSortSelected: (SortOrder) -> Unit,
    favoriteOnly: Boolean,
    onFavoriteClick: () -> Unit
) {
    var genreExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    Text(
        text = "レシピ一覧",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 6.dp)
    )

    OutlinedTextField(
        value = searchKeyword,
        onValueChange = onSearchKeywordChange,
        label = {
            Text("レシピ名で検索")
        },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (searchKeyword.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchKeywordChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "検索文字をクリア"
                    )
                }
            }
        }
    )

    Spacer(modifier = Modifier.height(6.dp))

    Row {
        GenreDropDown(
            selectedGenre = selectedGenre,
            expanded = genreExpanded,
            onExpandClick = {
                genreExpanded = true
                sortExpanded = false
            },
            onDismissRequest = {
                genreExpanded = false
            },
            onGenreSelected = { genre ->
                onGenreSelected(genre)
                genreExpanded = false
            },
            showAllItem = true
        )

        Spacer(modifier = Modifier.width(6.dp))

        SortDropDown(
            selectedSort = selectedSort,
            expanded = sortExpanded,
            onExpandClick = {
                sortExpanded = true
                genreExpanded = false
            },
            onDismissRequest = {
                sortExpanded = false
            },
            onSortSelected = { sort ->
                onSortSelected(sort)
                sortExpanded = false
            }
        )

        IconButton(
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector =
                    if (favoriteOnly)
                        Icons.Default.Star
                    else
                        Icons.Default.StarBorder,
                contentDescription = "お気に入り"
            )
        }
    }
}