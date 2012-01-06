package org.testinfected.hamcrest.spring;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.testinfected.hamcrest.AbstractMatcherTest;

import static org.hamcrest.Matchers.equalTo;
import static org.testinfected.hamcrest.spring.HasAttribute.containsAttribute;
import static org.testinfected.hamcrest.spring.HasAttribute.hasAttribute;
import static org.testinfected.hamcrest.spring.HasAttribute.hasAttributeValue;

public class HasAttributeTest extends AbstractMatcherTest {

    ExtendedModelMap shouldMatch = new ExtendedModelMap() {{
        addAttribute("key", "matches");
    }};

    ExtendedModelMap shouldNotMatch = new ExtendedModelMap() {{
        addAttribute("key", "does not match");
    }};

    ExtendedModelMap missingKey = new ExtendedModelMap() {{
        addAttribute("missing", "value");
    }};

    @Override protected Matcher<? super Model> createMatcher() {
        return hasAttributeValue("key", equalTo("matches"));
    }
    
    @Test public void
    matchesAttributeValueUsingProvidedMatcher() {
        assertMatches("correct value", createMatcher(), shouldMatch);
        assertDoesNotMatch("incorrect value", createMatcher(), shouldNotMatch);
        assertDoesNotMatch("missing key", createMatcher(), missingKey);
    }
    
    @Test public void
    providesConvenientShortcutForMatchingValueUsingEquals() {
        assertMatches("equal value", hasAttribute("key", "matches"), shouldMatch);
    }
    
    @Test public void
    providesConvenientShortcutForCheckingIfAttributeExists() {
        assertMatches("key match", containsAttribute("key"), shouldMatch);
        assertDoesNotMatch("no key match", containsAttribute("key"), missingKey);
    }

    @Test public void
    hasHumanReadableDescription() {
        assertDescription("with \"key\" \"matches\"", createMatcher());
    }

    @Test public void
    hasHumanReadableMismatchDescription() {
        assertMismatchDescription("\"key\" was \"does not match\"", createMatcher(), shouldNotMatch);
    }
}
