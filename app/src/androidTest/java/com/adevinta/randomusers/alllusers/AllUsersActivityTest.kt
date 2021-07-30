package com.adevinta.randomusers.alllusers

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.ui.AllUsersActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AllUsersActivityTest {

    @Rule
    @JvmField
    var allusersActivityRule = ActivityTestRule(AllUsersActivity::class.java)

    @Test
    fun checkView(){
        onView(withId(R.id.search_icon)).check(matches(isDisplayed()))
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun checkOnClickSearch(){
        onView(withId(R.id.search_icon)).perform(click())
        onView(withId(R.id.search_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun checkOnClickItem(){
        onView(withId(R.id.recycler_view)).perform(click())
    }
}