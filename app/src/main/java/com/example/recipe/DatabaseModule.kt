package com.example.recipe

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//hiltの部品作成宣言
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    //この関数でオブジェクトを作る宣言(DB)
    @Provides
    @Singleton
    fun provideDatabase(
        //composeではないから下記のアノテで状態取得
        @ApplicationContext context: Context
    ): RecipeDatabase {
        return RecipeDatabase.create(context)
    }

    //この関数でオブジェクトを作る宣言(DB操作)
    @Provides
    @Singleton
    fun provideRecipeDao(
        database: RecipeDatabase
    ): RecipeDao {
        return database.recipeDao()
    }
}