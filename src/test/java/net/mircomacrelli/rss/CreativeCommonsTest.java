package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreativeCommonsTest {

    @Test(expected = NullPointerException.class)
    public void licensesIsRequired() {
        new CreativeCommons(null);
    }

    @Test
    public void licensesIsCopiedByCtor() throws MalformedURLException {
        final List<URL> licenses = new ArrayList<>(1);
        licenses.add(new URL("http://www.google.it"));
        final CreativeCommons cc = new CreativeCommons(licenses);
        licenses.clear();
        assertEquals(1, cc.getLicenses().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void licensesIsUnmodifiable() throws MalformedURLException {
        final List<URL> licenses = new ArrayList<>(1);
        licenses.add(new URL("http://www.google.it"));
        final CreativeCommons cc = new CreativeCommons(licenses);
        cc.getLicenses().clear();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CreativeCommons.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() throws MalformedURLException {
        final List<URL> licenses = new ArrayList<>(1);
        licenses.add(new URL("http://www.google.it"));
        final CreativeCommons cc = new CreativeCommons(licenses);

        assertEquals("CreativeCommons{licenses=[http://www.google.it]}", cc.toString());
    }
}
