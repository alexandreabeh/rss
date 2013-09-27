package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SourceTest {
    private URL validLink;

    private URL otherLink;

    private Source source;

    @Before
    public void setup() throws MalformedURLException {
        validLink = new URL("http://mircomacrelli.net");
        otherLink = new URL("http://www.google.com");
        source = new Source("Mirco Macrelli", validLink);
    }

    @Test(expected = NullPointerException.class)
    public void nameIsRequired() {
        new Source(null, validLink);
    }

    @Test(expected = NullPointerException.class)
    public void linkIsRequired() {
        new Source("Mirco Macrelli", null);
    }

    @Test
    public void url() {
        assertSame(validLink, source.getLink());
    }

    @Test
    public void name() {
        assertEquals("Mirco Macrelli", source.getName());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Source.class).withPrefabValues(URL.class, validLink, otherLink)
                      .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals("Source{name='Mirco Macrelli', link='http://mircomacrelli.net'}", source.toString());
    }
}
