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
    public void theIdIsRequired() throws MalformedURLException {
        new UniqueId(null, false);
    }

    @Test
    public void idCanBeAnythingIfIsLinkIsFalse() throws MalformedURLException {
        new UniqueId("any string you can think of", false);
    }

    @Test(expected = MalformedURLException.class)
    public void ifIsLinkIsTrueThenIdMustBeAValidURL() throws MalformedURLException {
        new UniqueId("this will throw an exception", true);
    }

    @Test
    public void id() throws MalformedURLException {
        assertEquals("this is the id", new UniqueId("this is the id", false).getId());
    }

    @Test
    public void isLink() throws MalformedURLException {
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
    public void testToString() throws MalformedURLException {
        assertEquals("UniqueId{id='id', isLink=false}", new UniqueId("id", false).toString());
    }
}
