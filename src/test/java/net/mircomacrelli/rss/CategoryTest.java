package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public final class CategoryTest {
    private Category withDomain;
    private Category withoutDomain;

    @Before
    public void setup() {
        withDomain = new Category("wp", "blog/it");
        withoutDomain = new Category(null, "review");
    }

    @Test
    public void domainCanBeNull() {
        assertNull(new Category(null, "security").getDomain());
    }

    @Test(expected = NullPointerException.class)
    public void locationIsRequired() {
        new Category(null, null);
    }

    @Test
    public void domain() {
        assertNull(withoutDomain.getDomain());
        assertEquals("wp", withDomain.getDomain());
    }

    @Test
    public void location() {
        assertEquals("review", withoutDomain.getLocation());
    }

    @Test
    public void hasDomain() {
        assertFalse(withoutDomain.hasDomain());
        assertTrue(withDomain.hasDomain());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Category.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals("Category{domain='wp', location='blog/it'}", withDomain.toString());
        assertEquals("Category{location='review'}", withoutDomain.toString());
    }
}
