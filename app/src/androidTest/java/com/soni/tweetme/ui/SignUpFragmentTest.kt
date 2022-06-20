package com.soni.tweetme.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.soni.tweetme.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignUpFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun signUpFragmentTest() {
        val appCompatTextView = onView(
            allOf(
                withId(R.id.signUp), withText("No Account? Go to Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())

        val frameLayout = onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        )
        frameLayout.check(matches(isDisplayed()))

        val appCompatButton = onView(
            allOf(
                withId(R.id.buttonSignup), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.nameEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nameLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("soni"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.emailEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("soni@gm.c"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.passwordEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("1234"), closeSoftKeyboard())

        pressBack()

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.buttonSignup), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.emailEditText), withText("soni@gm.c"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("soni@gmail.com"))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.emailEditText), withText("soni@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(closeSoftKeyboard())

        pressBack()

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.buttonSignup), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.passwordEditText), withText("1234"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("123456"))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.passwordEditText), withText("123456"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText7.perform(closeSoftKeyboard())

        pressBack()

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.buttonSignup), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.emailEditText), withText("soni@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("soni1@gmail.com"))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.emailEditText), withText("soni1@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        pressBack()

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.buttonSignup), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
