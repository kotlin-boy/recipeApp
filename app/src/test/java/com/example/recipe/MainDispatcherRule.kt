package com.example.recipe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    //テスト開始前にMain Dispatcherをテスト用に置き換える
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    //テスト終了後にMain Dispatcherを元に戻す
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}