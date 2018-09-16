package com.crossover.crosschat.android;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.crossover.crosschat.android.activity.ChatActivity;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;

import static com.crossover.crosschat.android.utils.CustomMatchers.*;
import static com.crossover.crosschat.android.utils.OrientationChangeAction.*;
import static com.crossover.crosschat.android.utils.WaitUtils.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/12/18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatActivityTests {

    @Rule
    public ActivityTestRule<ChatActivity> activityRule =
            new ActivityTestRule<>(ChatActivity.class, true, false);

    private void startActivityWithCustomIntent() {
        Intent intent = new Intent();
        intent.putExtra(ChatActivity.EXTRA_USERNAME, "Mahmoud");
        activityRule.launchActivity(intent);
    }

    @Test
    public void testWelcomeMessage() {

        // Start Activity with Custom Intent
        // ----------------------
        startActivityWithCustomIntent();

        // Now we wait some time
        // ----------------------
        waitTime();

        // Verify we switched to Chat screen with Welcome Message displayed
        // ----------------------
        onView(withId(R.id.recycler))
                .check(matches(hasDescendant(withText(containsString("Hi Mahmoud,")))));

        // Clean up
        // ----------------------
        cleanupWaitTime();
    }

    @Test
    public void testMessageWithNoEntities() {

        // Start Activity with Custom Intent
        // ----------------------
        startActivityWithCustomIntent();

        // Now we wait some time
        // ----------------------
        waitTime();

        // Write a Message with no entities
        // ----------------------
        onView(withId(R.id.edit_message))
                .perform(typeText("Hello message, it should have no entities"));

        // Tap "Submit" button to send Message
        // ----------------------
        onView(withId(R.id.btn_submit)).perform(click());

        // Check if the reply is empty JSON only and no Formatted reply received
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 0),
                        hasDescendant(withText("{}")))
        ).check(matches(isDisplayed()));

        // Clean up
        // ----------------------
        cleanupWaitTime();
    }

    @Test
    public void testMessageWithDefaultText() {

        // Start Activity with Custom Intent
        // ----------------------
        startActivityWithCustomIntent();

        // Now we wait some time
        // ----------------------
        waitTime();

        // Write a Message with different entities
        // ----------------------
        onView(withId(R.id.edit_message))
                .perform(typeText("@bob @john (success) such a cool feature; " +
                        "https://twitter.com/jdorfman/status/430511497475670016"));

        // Tap "Submit" button to send Message
        // ----------------------
        onView(withId(R.id.btn_submit)).perform(click());

        // Check if the user Message is added to Chat view
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 0),
                        hasDescendant(withText("@bob @john (success) such a cool feature; " +
                                "https://twitter.com/jdorfman/status/430511497475670016")))
        ).check(matches(isDisplayed()));

        // Now we wait some time
        // ----------------------
        waitTime();

        // Check if a reply was added and contains a JSON text describing all the findings
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 1),
                        hasDescendant(withText("{\"emoticons\":[\"success\"],\"links\":" +
                                "[\"https://twitter.com/jdorfman/status/430511497475670016\"]," +
                                "\"mentions\":[\"bob\",\"john\"]}")))
        ).check(matches(isDisplayed()));

        // Moreover, check if a reply with the same original Message is added, and expected to be
        // formatted
        // TODO: Check if the Message were properly formatted as expected
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 0),
                        hasDescendant(withText("@bob @john (success) such a cool feature; " +
                                "https://twitter.com/jdorfman/status/430511497475670016")))
        ).check(matches(isDisplayed()));

        // Clean up
        // ----------------------
        cleanupWaitTime();
    }

    @Test
    public void testMessageWithDefaultTextWithSwitchingToLandscape() {

        // Start Activity with Custom Intent
        // ----------------------
        startActivityWithCustomIntent();

        // Now we wait some time
        // ----------------------
        waitTime();

        // Insure we're in Portrait mode
        // ----------------------
        onView(isRoot()).perform(orientationPortrait());

        // Write a Message with different entities
        // ----------------------
        onView(withId(R.id.edit_message))
                .perform(typeText("@bob @john (success) such a cool feature; " +
                        "https://twitter.com/jdorfman/status/430511497475670016"));

        // Tap "Submit" button to send Message
        // ----------------------
        onView(withId(R.id.btn_submit)).perform(click());

        // Check if the user Message is added to Chat view
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 0),
                        hasDescendant(withText("@bob @john (success) such a cool feature; " +
                                "https://twitter.com/jdorfman/status/430511497475670016")))
        ).check(matches(isDisplayed()));

        // Now we wait some time
        // ----------------------
        waitTime();

        // Switch to Landscape mode
        // ----------------------
        onView(isRoot()).perform(orientationLandscape());

        // Now we wait some time
        // ----------------------
        waitTime();

        // Check if a reply was added and contains a JSON text describing all the findings
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 1),
                        hasDescendant(withText("{\"emoticons\":[\"success\"],\"links\":" +
                                "[\"https://twitter.com/jdorfman/status/430511497475670016\"]," +
                                "\"mentions\":[\"bob\",\"john\"]}")))
        ).check(matches(isDisplayed()));

        // Moreover, check if a reply with the same original Message is added, and expected to
        // be formatted
        // TODO: Check if the Message were properly formatted as expected
        // ----------------------
        onView(
                allOf(nthChildOf(withId(R.id.recycler), 0),
                        hasDescendant(withText("@bob @john (success) such a cool feature; " +
                                "https://twitter.com/jdorfman/status/430511497475670016")))
        ).check(matches(isDisplayed()));

        // Clean up
        // ----------------------
        cleanupWaitTime();
    }

}
