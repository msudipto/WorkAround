package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.view.MotionEventBuilder;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class EthanSystemTestAdmin {

    @Rule
    public ActivityScenarioRule<loginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(loginActivity.class);

    // Test for the Employer login and initial Clock In button display
    @Test
    public void testEmployerLoginAndClockInButton() throws InterruptedException {
        // Simulate the login process
        Thread.sleep(1000);
        onView(withId(R.id.usernameInput)).perform(typeText("dianap"));
        onView(withId(R.id.passwordInput)).perform(typeText("Encodedpassword3!"));
        onView(withId(R.id.submitButton)).perform(click());

        // Wait for the transition to the employer activity
        Thread.sleep(1000);

        // Check that the initial button text is "Clock In"
        onView(withId(R.id.checkText)).check(matches(withText("Clock In")));
        Thread.sleep(1000);
    }

    // Test for clicking the Clock In button and ensuring it changes to "Clock Out"
    @Test
    public void testEmployerClockIn() throws InterruptedException {
        // Simulate the login process
        Thread.sleep(1000);
        onView(withId(R.id.usernameInput)).perform(typeText("dianap"));
        onView(withId(R.id.passwordInput)).perform(typeText("Encodedpassword3!"));
        onView(withId(R.id.submitButton)).perform(click());

        // Wait for the transition to the employer activity
        Thread.sleep(1000);

        // Click the Clock In button
        onView(withId(R.id.frameChange)).perform(click());

        // Check that the button text changes to "Clock Out"
        Thread.sleep(1000);
        onView(withId(R.id.checkText)).check(matches(withText("Clock Out")));

        onView(withId(R.id.frameChange)).perform(click());
        Thread.sleep(1000);
    }

    // Test for clicking the Clock Out button and ensuring the button text changes back to "Clock In"
    @Test
    public void testEmployerClockOut() throws InterruptedException {
        // Simulate the login process
        Thread.sleep(1000);
        onView(withId(R.id.usernameInput)).perform(typeText("dianap"));
        onView(withId(R.id.passwordInput)).perform(typeText("Encodedpassword3!"));
        onView(withId(R.id.submitButton)).perform(click());

        // Wait for the transition to the employer activity
        Thread.sleep(1000);

        // Click the Clock In button to first clock in
        onView(withId(R.id.frameChange)).perform(click());

        // Wait for the Clock In action to complete
        Thread.sleep(1000);

        // Click the Clock Out button
        onView(withId(R.id.frameChange)).perform(click());
        Thread.sleep(1000);


        onView(withText("OK")).perform(click());

        // Check that the button text changes back to "Clock In"
        onView(withId(R.id.checkText)).check(matches(withText("Clock In")));
        Thread.sleep(1000);
    }

    // Test for ensuring the Chronometer stops when clocked out
    @Test
    public void testChronometerStopsWhenClockedOut() throws InterruptedException {
        // Simulate the login process
        Thread.sleep(1000);
        onView(withId(R.id.usernameInput)).perform(typeText("dianap"));
        onView(withId(R.id.passwordInput)).perform(typeText("Encodedpassword3!"));
        onView(withId(R.id.submitButton)).perform(click());

        // Wait for the transition to the employer activity
        Thread.sleep(1000);

        // Click the Clock In button to start the chronometer
        onView(withId(R.id.frameChange)).perform(click());

        // Wait for the chronometer to start
        Thread.sleep(1000);

        // Click the Clock Out button to stop the chronometer
        onView(withId(R.id.frameChange)).perform(click());
        Thread.sleep(1000);

        // Verify that the dialog is displayed
        onView(withText("Clock Out Summary")).check(matches(isDisplayed()));
        // Close the dialog
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);
    }

    // Test for ensuring the Chronometer stops when clocked out
    @Test
    public void clockResetTest() throws InterruptedException {
        // Simulate the login process
        Thread.sleep(1000);
        onView(withId(R.id.usernameInput)).perform(typeText("dianap"));
        onView(withId(R.id.passwordInput)).perform(typeText("Encodedpassword3!"));
        onView(withId(R.id.submitButton)).perform(click());

        // Wait for the transition to the employer activity
        Thread.sleep(1000);

        // Click the Clock In button to start the chronometer
        onView(withId(R.id.frameChange)).perform(click());

        // Wait for the chronometer to start
        Thread.sleep(1000);

        // Click the Clock Out button to stop the chronometer
        onView(withId(R.id.frameChange)).perform(click());
        Thread.sleep(1000);

        // Verify that the dialog is displayed
        onView(withText("Clock Out Summary")).check(matches(isDisplayed()));
        // Close the dialog
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.checkText)).check(matches(withText("Clock In")));
    }
}
