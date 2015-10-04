package com.fewlaps.flone;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.fewlaps.flone.view.activity.DronesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HelloWorldEspressoTest {

    @Rule
    public ActivityTestRule<DronesListActivity> mActivityRule = new ActivityTestRule(DronesListActivity.class);

    @Test
    public void addDroneButtonOpensAddDroneScreen() {
        onView(withId(R.id.bt_add_drone)).perform(click());

        onView(withId(R.id.tv_instructions)).check(matches(withText(containsString("Select the drone"))));
    }
}