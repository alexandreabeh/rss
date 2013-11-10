package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeFalse;

public final class UniqueIdTest {
    @Test(expected = NullPointerException.class)
    public void theIdIsRequired() {
        new UniqueId(null, false);
    }

    @Test
    public void idCanBeAnythingIfIsLinkIsFalse() {
        assertEquals("any string you can think of", new UniqueId("any string you can think of", false).getId());
    }

    @Test
    public void id() {
        assertEquals("this is the id", new UniqueId("this is the id", false).getId());
    }

    @Test
    public void isLink() {
        assertFalse(new UniqueId("this is the id", false).isLink());
    }

    @Test(expected = IllegalStateException.class)
    public void ifIsLinkIsFalseAsURLThrownAnException() throws MalformedURLException {
        final UniqueId id = new UniqueId("this is the id", false);
        assumeFalse(id.isLink());
        id.asURL();
    }

    @Test
    public void asURL() throws MalformedURLException {
        assertNotNull(new UniqueId("http://mircomacrelli.net", true).asURL());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(UniqueId.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals("UniqueId{id='id', isLink=false}", new UniqueId("id", false).toString());
    }
}
