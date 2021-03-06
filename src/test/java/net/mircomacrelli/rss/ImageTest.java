package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class ImageTest {

    private URI validImage;
    private URI validLink;
    private Image image;

    @Before
    public void setup() throws URISyntaxException {
        validImage = new URI("http://mircomacrelli.net/image.png");
        validLink = new URI("http://mircomacrelli.net");
        image = new Image(validImage, validLink, "logo", "Il mio logo", 80, 80);
    }

    @Test(expected = NullPointerException.class)
    public void imageIsRequired() {
        new Image(null, validLink, "logo", "Il mio logo", 80, 80);
    }

    @Test(expected = NullPointerException.class)
    public void linkIsRequired() {
        new Image(validImage, null, "logo", "Il mio logo", 80, 80);
    }

    @Test(expected = NullPointerException.class)
    public void altIsRequired() {
        new Image(validImage, validLink, null, "Il mio logo", 80, 80);
    }

    @Test
    public void descriptionCanBeNull() {
        assertNull(new Image(validImage, validLink, "logo", null, 80, 80).getDescription());
    }

    @Test
    public void ifWidthIsNullADefaultValueIsUsed() {
        assertEquals(88, new Image(validImage, validLink, "logo", "Il mio logo", null, 80).getWidth());
    }

    @Test
    public void ifHeightIsNullADefaultValueIsUsed() {
        assertEquals(31, new Image(validImage, validLink, "logo", "Il mio logo", 80, null).getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void widthCantBeNegative() {
        new Image(validImage, validLink, "logo", "Il mio logo", -1, 80);
    }

    @Test(expected = IllegalArgumentException.class)
    public void heightCantBeNegative() {
        new Image(validImage, validLink, "logo", "Il mio logo", 80, -1);
    }

    @Test
    public void image() {
        assertSame(validImage, image.getImage());
    }

    @Test
    public void link() {
        assertSame(validLink, image.getLink());
    }

    @Test
    public void description() {
        assertEquals("Il mio logo", image.getDescription());
    }

    @Test
    public void alt() {
        assertEquals("logo", image.getAlt());
    }

    @Test
    public void width() {
        assertEquals(80, image.getWidth());
    }

    @Test
    public void height() {
        assertEquals(80, image.getHeight());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Image.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Image{image='http://mircomacrelli.net/image.png', alt='logo', link='http://mircomacrelli.net', description='Il mio logo', width=80, height=80}",
                image.toString());
    }

    @Test
    public void testToStringSimple() {
        assertEquals("Image{image='http://mircomacrelli.net/image.png', alt='logo', link='http://mircomacrelli.net'}",
                     new Image(validImage, validLink, "logo", null, null, null).toString());
    }
}
