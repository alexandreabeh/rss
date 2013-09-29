package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class TextInputTest {
    private URL validUrl;
    private URL otherUrl;
    private TextInput input;

    @Before
    public void setup() throws MalformedURLException {
        validUrl = new URL("http://mircomacrelli.net/search.php");
        otherUrl = new URL("http://www.google.com");
        input = new TextInput("q", "search the feed", "Search", validUrl);
    }

    @Test(expected = NullPointerException.class)
    public void nameIsRequired() {
        new TextInput(null, "search the feed", "Search", validUrl);
    }

    @Test(expected = NullPointerException.class)
    public void labelIsRequired() {
        new TextInput("q", "search the feed", null, validUrl);
    }

    @Test(expected = NullPointerException.class)
    public void descriptionIsRequired() {
        new TextInput("q", null, "Search", validUrl);
    }

    @Test(expected = NullPointerException.class)
    public void scriptURLIsRequired() {
        new TextInput("q", "search the feed", "Search", null);
    }

    @Test
    public void name() {
        assertEquals("q", input.getName());
    }

    @Test
    public void label() {
        assertEquals("Search", input.getLabel());
    }

    @Test
    public void description() {
        assertEquals("search the feed", input.getDescription());
    }

    @Test
    public void scriptUrl() {
        assertSame(validUrl, input.getScriptUrl());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TextInput.class).withPrefabValues(URL.class, validUrl, otherUrl)
                      .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "TextInput{label='Search', description='search the feed', name='q', scriptUrl='http://mircomacrelli.net/search.php'}",
                input.toString());
    }
}
