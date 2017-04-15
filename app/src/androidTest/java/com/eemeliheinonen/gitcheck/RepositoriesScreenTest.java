package com.eemeliheinonen.gitcheck;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Eemeli on 15/04/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RepositoriesScreenTest {
    private final String username = "Vincit";

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void noSearchwithoutUsername() throws Exception {
//        // Click on the add note button
    onView(withId(R.id.editTextSearch)).check(matches(withText("")));

    onView(withId(R.id.buttonSearch)).perform(click());

    // Check that no recyclerView list items exist
    onView(withId(R.id.repository_list_item)).check(doesNotExist());
    }

    @Test
    public void changeTitleWhenSearching(){
        onView(withId(R.id.editTextSearch)).perform(typeText(username));

        onView(withId(R.id.buttonSearch)).perform(click());

        matchToolbarTitle(username+" - Repositories");
    }

    @Test
    public void persistRecyclerViewOnOrientationChange(){
        onView(withId(R.id.editTextSearch)).perform(typeText(username));

        onView(withId(R.id.buttonSearch)).perform(click());

        onView(withId(R.id.recycler_repositories))
                .check(matches(hasDescendant(withId(R.id.repository_list_item))));

        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView(withId(R.id.recycler_repositories))
                .check(matches(hasDescendant(withId(R.id.repository_list_item))));
    }

    @Test
    public void moveToNextFragment(){
        onView(withId(R.id.editTextSearch)).perform(typeText(username));

        onView(withId(R.id.buttonSearch)).perform(click());

        onView(withId(R.id.recycler_repositories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.fragment_commits_swipe_refresh_layout)).check(matches(isDisplayed()));

    }

    //Method for checking the toolbar title
    private static ViewInteraction matchToolbarTitle(
            CharSequence title) {
        return onView(
                allOf(
                        isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }
}