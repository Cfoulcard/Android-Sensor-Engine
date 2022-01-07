//package com.christianfoulcard.android.androidsensorengine
//
//
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.Espresso.pressBack
//import androidx.test.espresso.action.ViewActions.*
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.filters.LargeTest
//import androidx.test.rule.ActivityTestRule
//import androidx.test.rule.GrantPermissionRule
//import androidx.test.runner.AndroidJUnit4
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//import org.hamcrest.Matchers.`is`
//import org.hamcrest.Matchers.allOf
//import org.hamcrest.TypeSafeMatcher
//import org.hamcrest.core.IsInstanceOf
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class SimpleTest2 {
//
//    @Rule
//    @JvmField
//    var mActivityTestRule = ActivityTestRule(HomeScreenActivity::class.java)
//
//    @Rule
//    @JvmField
//    var mGrantPermissionRule =
//            GrantPermissionRule.grant(
//                    "android.permission.ACCESS_FINE_LOCATION",
//                    "android.permission.RECORD_AUDIO")
//
//    @Test
//    fun simpleTest2() {
//        val viewGroup = onView(
//                allOf(withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()))
//        viewGroup.check(matches(isDisplayed()))
//
//        val appCompatImageView = onView(
//                allOf(withId(R.id.sound_icon), withContentDescription("Sound Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                1)))
//        appCompatImageView.perform(scrollTo(), click())
//
//        val textView = onView(
//                allOf(IsInstanceOf.instanceOf(android.widget.TextView::class.java), withText("Quick Sensor Access"),
//                        withParent(allOf(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
//                                withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
//                        isDisplayed()))
//
//
//        pressBack()
//
//        val appCompatImageView2 = onView(
//                allOf(withId(R.id.temp_icon), withContentDescription("Temp Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                2)))
//        appCompatImageView2.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView3 = onView(
//                allOf(withId(R.id.light_icon), withContentDescription("Light Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                3)))
//        appCompatImageView3.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView4 = onView(
//                allOf(withId(R.id.ram_icon), withContentDescription("Ram Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                4)))
//        appCompatImageView4.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView5 = onView(
//                allOf(withId(R.id.battery_icon), withContentDescription("Battery Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                5)))
//        appCompatImageView5.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView6 = onView(
//                allOf(withId(R.id.speed_icon), withContentDescription("Speed Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                6)))
//        appCompatImageView6.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView7 = onView(
//                allOf(withId(R.id.pressure_icon), withContentDescription("Pressure Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                8)))
//        appCompatImageView7.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView8 = onView(
//                allOf(withId(R.id.humidity_icon), withContentDescription("Humidity Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                7)))
//        appCompatImageView8.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView9 = onView(
//                allOf(withId(R.id.walk_icon), withContentDescription("Walk Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                9)))
//        appCompatImageView9.perform(scrollTo(), click())
//
//        pressBack()
//
//        val appCompatImageView10 = onView(
//                allOf(withId(R.id.sound_icon), withContentDescription("Sound Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                1)))
//        appCompatImageView10.perform(scrollTo(), click())
//
//        val appCompatImageView11 = onView(
//                allOf(withId(R.id.info_button),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                1)))
//        appCompatImageView11.perform(scrollTo(), click())
//
//        val appCompatButton = onView(
//                allOf(withId(R.id.dismiss_dialog), withText("X"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                3),
//                        isDisplayed()))
//        appCompatButton.perform(click())
//
//        val overflowMenuButton = onView(
//                allOf(withContentDescription("More options"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.action_bar),
//                                        1),
//                                0),
//                        isDisplayed()))
//        overflowMenuButton.perform(click())
//
//        val appCompatTextView = onView(
//                allOf(withId(R.id.title), withText("Preferences"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()))
//        appCompatTextView.perform(click())
//
//        val recyclerView = onView(
//                allOf(withId(R.id.recycler_view),
//                        childAtPosition(
//                                withId(android.R.id.list_container),
//                                0)))
//        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
//
//        val recyclerView2 = onView(
//                allOf(withId(R.id.recycler_view),
//                        childAtPosition(
//                                withId(android.R.id.list_container),
//                                0)))
//        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(3, click()))
//
//        val appCompatEditText = onView(
//                allOf(withId(android.R.id.edit),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                1)))
//        appCompatEditText.perform(scrollTo(), replaceText("32"), closeSoftKeyboard())
//
//        val appCompatButton2 = onView(
//                allOf(withId(android.R.id.button1), withText("OK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.buttonPanel),
//                                        0),
//                                3)))
//        appCompatButton2.perform(scrollTo(), click())
//
//        pressBack()
//
//        pressBack()
//
//        val appCompatImageView12 = onView(
//                allOf(withId(R.id.temp_icon), withContentDescription("Temp Button"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(`is`("android.widget.ScrollView")),
//                                        0),
//                                2)))
//        appCompatImageView12.perform(scrollTo(), click())
//
//        pressBack()
//    }
//
//    private fun childAtPosition(
//            parentMatcher: Matcher<View>, position: Int): Matcher<View> {
//
//        return object : TypeSafeMatcher<View>() {
//            override fun describeTo(description: Description) {
//                description.appendText("Child at position $position in parent ")
//                parentMatcher.describeTo(description)
//            }
//
//            public override fun matchesSafely(view: View): Boolean {
//                val parent = view.parent
//                return parent is ViewGroup && parentMatcher.matches(parent)
//                        && view == parent.getChildAt(position)
//            }
//        }
//    }
//}
