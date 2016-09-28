package com.example.a00227178.myapp;
import android.support.test.rule.ActivityTestRule;

import com.example.a00227178.myapp.MatchDay;
import com.example.a00227178.myapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

// Tests for MainActivity
public class MatchDayHomePointTest {
    private String mString;

    @Rule
    public ActivityTestRule<MatchDay> mActivityRule = new ActivityTestRule<>(MatchDay.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mString = "1";
    }
    @Test
    public void changeText_sameActivity() {
        // Click Button To Start Game
        onView(withId(R.id.timeManagement)).perform(click());

        onView(withId(R.id.addHomePoint)).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.homePoints))
                .check(matches(withText(mString)));
    }
}