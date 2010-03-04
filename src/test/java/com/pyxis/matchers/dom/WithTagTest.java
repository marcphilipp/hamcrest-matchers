package com.pyxis.matchers.dom;

import com.threelevers.css.Selector;
import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;
import org.w3c.dom.Element;

import static com.pyxis.matchers.dom.WithTag.withTag;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.core.IsEqual.equalTo;

public class WithTagTest extends AbstractMatcherTest {

    @Override
    protected Matcher<?> createMatcher() {
        return withTag("div");
    }

    public void testMatchesWhenElementHasGivenTagName() {
        assertMatches("correct tag", withTag(equalToIgnoringCase("div")), a("div"));
        assertDoesNotMatch("incorrect tag", withTag(equalTo("div")), a("span"));
    }

    public void testProvidesConvenientShortcutForTagNameIgnoringCase() {
        assertMatches("same lowercase tag", withTag("div"), a("div"));
        assertMatches("same uppercase tag", withTag("DIV"), a("DIV"));
        assertMatches("upper case tag", withTag("DIV"), a("div"));
        assertMatches("lowercase correct tag", withTag("div"), a("DIV"));
    }

    private Element a(String tag) {
        return element(String.format("<%s></%s>", tag, tag));
    }

    public void testHasAReadableDescription() {
        assertDescription("element with tag \"div\"", withTag(equalTo("div")));
    }

    public void testHasAReadableMismatchDescription() {
        assertMismatchDescription("element tag was \"SPAN\"", withTag(equalTo("div")), a("span"));
    }

    private Element element(String html) {
        return Selector.from(dom(html)).selectUnique("html > body > *");
    }

}
