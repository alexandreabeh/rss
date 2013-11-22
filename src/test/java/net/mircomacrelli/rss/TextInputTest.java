package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class TextInputTest {
    private URI validUri;
    private URI otherUri;
    private TextInput input;

    @Before
    public void setup() throws URISyntaxException {
        validUri = new URI("http://mircomacrelli.net/search.php");
        otherUri = new URI("http://www.google.com");
        input = new TextInput("q", "search the feed", "Search", validUri);
    }

    @Test(expected = NullPointerException.class)
    public void nameIsRequired() {
        new TextInput(null, "search the feed", "Search", validUri);
    }

    @Test(expected = NullPointerException.class)
    public void labelIsRequired() {
        new TextInput("q", "search the feed", null, validUri);
    }

    @Test(expected = NullPointerException.class)
    public void descriptionIsRequired() {
        new TextInput("q", null, "Search", validUri);
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
    public void scriptUri() {
        assertSame(validUri, input.getScriptUri());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TextInput.class).withPrefabValues(URI.class, validUri, otherUri)
                      .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "TextInput{label='Search', description='search the feed', name='q', scriptUri='http://mircomacrelli.net/search.php'}",
                input.toString());
    }
}
