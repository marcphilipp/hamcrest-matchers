package org.testinfected.hamcrest.dom;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.hamcrest.AbstractMatcherTest;

import static org.hamcrest.Matchers.equalTo;
import static org.testinfected.hamcrest.dom.Documents.toElement;
import static org.testinfected.hamcrest.dom.HasSelector.hasSelector;
import static org.testinfected.hamcrest.dom.WithTag.withTag;

public class HasSelectorTest extends AbstractMatcherTest {

    @Override
    protected Matcher<?> createMatcher() {
        return hasSelector("#content");
    }

    @Test public void
    matchesWhenAtLeastOneChildMatchesSelector() {
        assertMatches("single element", hasSelector("#content"), toElement("<div id='content'>content</div>"));
        assertMatches("multiple elements", hasSelector("li"), toElement("<ol><li>first</li><li>second</li></ol>"));
        assertDoesNotMatch("element not found", hasSelector("#content"), toElement("<div>content</div>"));
    }

    @Test public void
    matchSelectedChildrenAgainstGivenMatcher() {
        assertMatches("matching child", hasSelector("#content", withTag("div")), toElement("<div id='content'>content</div>"));
        assertMatches("matching children", hasSelector("ol > li", withTag("li")), toElement("<ol><li>first</li><li>second</li></ol>"));
        assertDoesNotMatch("child does not match", hasSelector("#content", withTag("div")), toElement("<span id='content'>content</span>"));
    }

    @Test public void
    hasAReadableDescription() {
        assertDescription("has selector \"#content\" (a collection containing element with tag \"div\")", hasSelector("#content", withTag(equalTo("div"))));
    }

    @Test public void
    hasAReadableMismatchDescription() {
        assertMismatchDescription("no selector \"ul li\"", hasSelector("ul li"), toElement("<ol><li>first</li><li>second</li></ol>"));
        assertMismatchDescription("#content a collection containing element with tag \"div\" element tag was \"span\"", hasSelector("#content", withTag(equalTo("div"))), toElement("<span id='content'>content</span>"));
    }
}