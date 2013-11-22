package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SourceTest {
    private URI validLink;
    private Source source;

    @Before
    public void setup() throws URISyntaxException {
        validLink = new URI("http://mircomacrelli.net");
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
        EqualsVerifier.forClass(Source.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals("Source{name='Mirco Macrelli', link='http://mircomacrelli.net'}", source.toString());
    }
}
