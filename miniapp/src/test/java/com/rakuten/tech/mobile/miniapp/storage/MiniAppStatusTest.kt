package com.rakuten.tech.mobile.miniapp.storage

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rakuten.tech.mobile.miniapp.*
import com.rakuten.tech.mobile.miniapp.TEST_BASE_PATH
import com.rakuten.tech.mobile.miniapp.TEST_ID_MINIAPP
import com.rakuten.tech.mobile.miniapp.TEST_ID_MINIAPP_VERSION
import com.rakuten.tech.mobile.miniapp.TEST_MA
import com.rakuten.tech.mobile.miniapp.TEST_MA_ID
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MiniAppStatusTest {
    private lateinit var context: Context
    private lateinit var miniAppStatus: MiniAppStatus

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        miniAppStatus = MiniAppStatus(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun `isVersionDownloaded should be false if no app has been saved`() {
        miniAppStatus.isVersionDownloaded(
            TEST_ID_MINIAPP, TEST_ID_MINIAPP_VERSION, TEST_BASE_PATH) shouldBe false
    }

    @Test
    fun `setVersionDownloaded and isVersionDownloaded should correspond with each other`() {
        miniAppStatus.setVersionDownloaded(TEST_ID_MINIAPP, TEST_ID_MINIAPP_VERSION, true)
        miniAppStatus.isVersionDownloaded(
            TEST_ID_MINIAPP, TEST_ID_MINIAPP_VERSION, context.filesDir.path) shouldBe true
    }

    @Test
    fun `downloaded miniapp should be saved in storage`() {
        MiniAppStatus(context).getDownloadedMiniApp(TEST_MA_ID) shouldBe null
        miniAppStatus.saveDownloadedMiniApp(TEST_MA)
        MiniAppStatus(context).getDownloadedMiniApp(TEST_MA_ID)?.id shouldEqual TEST_MA_ID
    }
}
