package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

@Composable
fun RecipeForm(
    recipeName: String,
    onRecipeNameChange: (String) -> Unit,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedGenre: RecipeGenre?,
    onGenreSelected: (RecipeGenre?) -> Unit,
    memo: String,
    onMemoValueChange: (String) -> Unit,
    ingredients: List<String>,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: (Int) -> Unit,
    onAddIngredient: () -> Unit,
    steps: List<String>,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: (Int) -> Unit,
    onAddStep: () -> Unit,
    isFavorite: Boolean,
    onFavoriteChange: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    hasChanges: Boolean,
    onImageSelectClick: () -> Unit,
    onRotateImageClick: () -> Unit,
    onDeleteImageClick: () -> Unit,
    imageUri: String,
){

    var showBackDialog by remember {
        mutableStateOf(false)
    }

    var showImageDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = recipeName,
        onValueChange = onRecipeNameChange,
        label = {
            Text("レシピ名")
        }
    )

    //ジャンル指定
    GenreDropDown(
        selectedGenre = selectedGenre,
        expanded = expanded,
        onExpandClick = onExpandClick,
        onDismissRequest = onDismissRequest,
        onGenreSelected = onGenreSelected
    )

    Spacer(modifier = Modifier.height(16.dp))

    //材料の記載・追加
    Text(
        text = "材料",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
    ingredients.forEachIndexed { index, ingredient ->
        Row {
            //材料欄
            OutlinedTextField(
                value = ingredient,
                onValueChange = {
                    onIngredientValueChange(index, it)
                },
                label = {
                    Text("材料${index + 1}")
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            //削除ボタン
            RecipeButton(
                text = "×",
                onClick = {
                    onRemoveIngredient(index)
                },
                modifier = Modifier.padding(top = 10.dp),
                containerColor = Red,
                contentColor = White
            )
        }
    }
    //材料追加
    RecipeButton(
        text = "＋",
        onClick = {
            onAddIngredient()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    //手順の記載・追加
    Text(
        text = "作り方",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
    steps.forEachIndexed { index, step ->

        Row {
            //手順欄
            OutlinedTextField(
                value = step,
                onValueChange = {
                    onStepValueChange(index, it)
                },
                label = {
                    Text("手順${index + 1}")
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            //削除ボタン
            RecipeButton(
                text = "×",
                onClick = {
                    onRemoveStep(index)
                },
                modifier = Modifier.padding(top = 10.dp),
                containerColor = Red,
                contentColor = White
            )
        }
    }

    //材料リストの追加
    RecipeButton(
        text = "＋",
        onClick = {
            onAddStep()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    //メモゾーン
    Text(
        text = "メモ",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
    OutlinedTextField(
        value = memo,
        onValueChange = onMemoValueChange,
        label = {
            Text("メモ")
        },
        minLines = 5
    )

    Spacer(modifier = Modifier.height(16.dp))

    //画像選択
    Text(
        text = buildAnnotatedString {
            append("画像　")

            withStyle(
                style = SpanStyle(color = Red)
            ) {
                append("※横向き推奨")
            }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
    Row {
        RecipeButton(
            text = "画像を選択",
            onClick = {
                showImageDialog = true
            }
        )

        if (imageUri.isNotBlank()) {

            Spacer(modifier = Modifier.width(8.dp))

            RecipeButton(
                text = "↻",
                onClick = onRotateImageClick,
                containerColor = Black,
                contentColor = White,
            )

            Spacer(modifier = Modifier.width(8.dp))

            RecipeButton(
                text = "削除",
                onClick = onDeleteImageClick,
                containerColor = Red,
                contentColor = White
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (imageUri.isNotBlank()) {
        AsyncImage(
            model = imageUri,
            contentDescription = "レシピ画像",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillBounds
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row {

        RecipeButton(
            text = "保存",
            onClick = onSaveClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        RecipeButton(
            text = "戻る",
            onClick = {
                if (hasChanges) {
                    showBackDialog = true
                } else {
                    onBackClick()
                }
            },
            containerColor = Red,
            contentColor = White
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onFavoriteChange
        ) {
            Icon(
                imageVector =
                    if (isFavorite)
                        Icons.Default.Star
                    else
                        Icons.Default.StarBorder,
                contentDescription = "お気に入り"
            )
        }
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = {
                showBackDialog = false
            },
            title = {
                Text("確認")
            },
            text = {
                Column {

                    Text("入力内容は保存されません。\n戻りますか？")

                    Spacer(modifier = Modifier.height(16.dp))

                    RecipeButton(
                        text = "戻る",
                        onClick = {
                            showBackDialog = false
                            onBackClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Red,
                        contentColor = White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RecipeButton(
                        text = "キャンセル",
                        onClick = {
                            showBackDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    //画像選択
    if (showImageDialog) {
        AlertDialog(
            onDismissRequest = {
                showImageDialog = false
            },
            title = {
                Text("画像を追加")
            },
            text = {
                Column {

                    RecipeButton(
                        text = "写真を撮る",
                        onClick = {
                            showImageDialog = false
                            // 後でカメラ起動
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RecipeButton(
                        text = "アルバムから選ぶ",
                        onClick = {
                            showImageDialog = false
                            onImageSelectClick()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}
