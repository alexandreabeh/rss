package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class EnclosureTest {
    private URI validLink;
    private URI badLink;
    private URI otherLink;
    private MimeType validMimeType;
    private Enclosure enclosure;

    @Before
    public void setup() throws MimeTypeParseException, URISyntaxException {
        validLink = new URI("http://mircomacrelli.net/audio.mp3");
        badLink = new URI("ftp://mircomacrelli.net/archive.zip");
        otherLink = new URI("https://www.google.com");
        validMimeType = new MimeType("audio/mp3");
        enclosure = new Enclosure(validLink, 1024, validMimeType);
    }

    @Test(expected = NullPointerException.class)
    public void linkIsRequired() {
        new Enclosure(null, 1024, validMimeType);
    }

    @Test(expected = NullPointerException.class)
    public void typeIsRequired() {
        new Enclosure(validLink, 1024, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lengthCantBeNegative() {
        new Enclosure(validLink, -1, validMimeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onlyHTTPLinkArePermitted() {
        new Enclosure(badLink, 1024, validMimeType);
    }

    @Test
    public void link() {
        assertSame(validLink, enclosure.getLink());
    }

    @Test
    public void length() {
        assertEquals(1024, enclosure.getLength());
    }

    @Test
    public void type() throws MimeTypeParseException {
        assertTrue(validMimeType.match(enclosure.getType()));
    }

    @Test
    public void typeIsCopiedByGetter() throws MimeTypeParseException {
        enclosure.getType().setSubType("aac");
        assertEquals("mp3", enclosure.getType().getSubType());
    }

    @Test
    public void httpsIsAValidProtocol() {
        assertEquals(otherLink, new Enclosure(otherLink, 12, validMimeType).getLink());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Enclosure.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals("Enclosure{link='http://mircomacrelli.net/audio.mp3', length=1024, type=audio/mp3}",
                     enclosure.toString());
    }
}
