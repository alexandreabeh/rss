package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreativeCommonsTest {

    @Test(expected = NullPointerException.class)
    public void licensesIsRequired() {
        new CreativeCommons(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void licensesIsUnmodifiable() throws URISyntaxException {
        final List<URI> licenses = new ArrayList<>(1);
        licenses.add(new URI("http://www.google.it"));
        final CreativeCommons creativeCommons = new CreativeCommons(licenses);
        creativeCommons.getLicenses().clear();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CreativeCommons.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() throws URISyntaxException {
        final List<URI> licenses = new ArrayList<>(1);
        licenses.add(new URI("http://www.google.it"));
        final CreativeCommons creativeCommons = new CreativeCommons(licenses);

        assertEquals("CreativeCommons{licenses=[http://www.google.it]}", creativeCommons.toString());
    }
}
