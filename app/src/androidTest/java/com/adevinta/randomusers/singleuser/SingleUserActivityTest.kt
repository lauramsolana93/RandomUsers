package com.adevinta.randomusers.singleuser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.adevinta.randomusers.R
import com.adevinta.randomusers.singleuser.ui.SingleUserActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SingleUserActivityTest {

    @Rule
    @JvmField
    val singleUsersActivityTestRule = ActivityTestRule(SingleUserActivity::class.java)



    @Test
    fun checkView(){
        onView(withId(R.id.name)).check(matches(isDisplayed()))
        onView(withId(R.id.picture)).check(matches(isDisplayed()))
        onView(withId(R.id.loacation)).check(matches(isDisplayed()))
        onView(withId(R.id.gender)).check(matches(isDisplayed()))
        onView(withId(R.id.register_date)).check(matches(isDisplayed()))
        onView(withId(R.id.email)).check(matches(isDisplayed()))
        onView(withId(R.id.phone)).check(matches(isDisplayed()))

    }

    @Test
    fun clickClose(){
        onView(withId(R.id.close)).perform(click())
    }
}