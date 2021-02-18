package com.example.reminderappremindme;

import android.app.AlertDialog;
import android.widget.Button;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class MainActivityTest {

    public String StrToInput;



    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp()  {
        StrToInput = "Task Try";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onCreate() {
    }

    @Test
    public void FAbtnClick(){
        onView(withId(R.id.FAbtn)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText(StrToInput));
        onView(withText("Add")).perform(click());
    }

    @Test
    public void FAbtnAbort(){
        onView(withId(R.id.FAbtn)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText(StrToInput));
        onView(withText("Abort")).perform(click());
    }

    @Test public void TrashTest(){
        onView(withId(R.id.delID)).perform(click());
    }
}