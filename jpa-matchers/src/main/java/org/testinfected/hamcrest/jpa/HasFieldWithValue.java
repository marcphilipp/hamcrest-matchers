package org.testinfected.hamcrest.jpa;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Field;

import static org.testinfected.hamcrest.jpa.Reflection.readField;
import static org.hamcrest.Matchers.anything;

public class HasFieldWithValue<T, U> extends TypeSafeDiagnosingMatcher<T> {

    private final String fieldName;
    private final Matcher<? super U> valueMatcher;

    public HasFieldWithValue(String fieldName, Matcher<? super U> valueMatcher) {
        this.fieldName = fieldName;
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(T argument, Description mismatchDescription) {
        Field field = getField(argument, mismatchDescription);
        if (field == null) return false;

        Object fieldValue = readField(argument, field);
        boolean valueMatches = valueMatcher.matches(fieldValue);
        if (!valueMatches) {
            mismatchDescription.appendText("\"" + fieldName + "\" ");
            valueMatcher.describeMismatch(fieldValue, mismatchDescription);
        }
        return valueMatches;
    }

    private Field getField(T argument, Description mismatchDescription) {
        try {
            return argument.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            mismatchDescription.appendText("no field \"" + fieldName + "\"");
            return null;
        }
    }

    public void describeTo(Description description) {
        description.appendText("has field \"");
        description.appendText(fieldName);
        description.appendText("\": ");
        description.appendDescriptionOf(valueMatcher);
    }

    @Factory
    public static <T, U> Matcher<T> hasField(String field, Matcher<? super U> value) {
        return new HasFieldWithValue<T, U>(field, value);
    }

    @Factory
    public static <T> Matcher<T> hasField(String field) {
        return new HasFieldWithValue<T, Object>(field, anything());
    }
}
