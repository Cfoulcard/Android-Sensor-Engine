package com.christianfoulcard.android.androidsensorengine

import com.christianfoulcard.android.androidsensorengine.sensors.SensorSoundActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
// @SmallTest
        public class TestHomeScreenActivity {

           private var homeScreenActivity: HomeScreenActivity? = null
           private var soundActivity: SensorSoundActivity? = null

    @Before
//    open fun setUp() { mainActivity = MainActivity() }
    fun setUp() { soundActivity = SensorSoundActivity() }

    @Test
    fun soundActivityTester() {
        soundActivity?.soundDb()
        assert(true)
    }
}



