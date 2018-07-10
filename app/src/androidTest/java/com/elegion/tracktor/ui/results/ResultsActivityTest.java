package com.elegion.tracktor.ui.results;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.elegion.tracktor.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Azret Magometov
 */
@RunWith(AndroidJUnit4.class)
public class ResultsActivityTest {

    @Rule
    public ActivityTestRule<ResultsActivity> mResultsActivityActivityTestRule = new ActivityTestRule<>(ResultsActivity.class);

    @Test
    public void checkListShown() {
        onView(withId(R.id.recycler))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickListItem() {
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tvTime))
                .check(matches(isDisplayed()));

    }

}