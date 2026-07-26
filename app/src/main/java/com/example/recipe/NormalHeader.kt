package com.example.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

    Text("レシピ一覧")

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