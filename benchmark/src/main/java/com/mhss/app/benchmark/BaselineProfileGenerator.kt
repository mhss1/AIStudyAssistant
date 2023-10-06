package com.mhss.app.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun addApiKeyAndChat() = baselineProfileRule.collectBaselineProfile(
        packageName = "com.mo.sh.studyassistant",
        profileBlock = {
            startActivityAndWait()
            addApiKeyAndChat()
        }
    )
}

fun MacrobenchmarkScope.addApiKeyAndChat() {

    device.wait(Until.hasObject(By.res("settings-button")), 5000)

    val settingsButton = device.findObject(By.res("settings-button"))

    settingsButton.click()

    device.wait(Until.hasObject(By.res("API Key")), 5000)

    device.findObject(By.text("API Key")).click()

    device.wait(Until.hasObject(By.res("api-key-text-field")), 5000)

    device.findObject(By.res("api-key-text-field")).text = BuildConfig.TEST_API_KEY

    device.waitForIdle()

    device.findObject(By.text("Save")).click()

    device.waitForIdle()

    device.pressBack()

    device.waitForIdle()

    device.findObject(By.text("Personal Tutor")).click()

    device.waitForIdle()

    device.wait(Until.hasObject(By.res("chat-text-field")), 5000)
    device.wait(Until.hasObject(By.res("send-button")), 5000)

    val chatTextField = device.findObject(By.res("chat-text-field"))
    val sendButton = device.findObject(By.res("send-button"))

    chatTextField.text = "Hello"

    device.waitForIdle()

    sendButton.click()

    device.waitForIdle()

    sendButton.wait(Until.enabled(true), 30000)

    chatTextField.text = "Compare Java and Kotlin briefly"

    device.waitForIdle()

    sendButton.click()

    device.waitForIdle()

    sendButton.wait(Until.enabled(true), 30000)

    chatTextField.text = "Which one came first?"

    device.waitForIdle()

    sendButton.click()

    device.waitForIdle()

    sendButton.wait(Until.enabled(true), 30000)

    device.wait(Until.hasObject(By.res("messages-list")), 5000)
    val list = device.findObject(By.res("messages-list"))

    list.setGestureMargin(device.displayWidth / 3)
    list.fling(Direction.UP, 1200)

    device.waitForIdle()

    device.pressBack()
}