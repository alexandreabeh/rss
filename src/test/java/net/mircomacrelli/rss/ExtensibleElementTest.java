package net.mircomacrelli.rss;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExtensibleElementTest {
    private MockElement element;
    private CreativeCommons creativeCommons;

    @Before
    public void setup() throws MalformedURLException, URISyntaxException {
        element = new MockElement();
        creativeCommons = new CreativeCommons(singletonList(new URI("http://www.google.it")));
    }

    @Test(expected = NullPointerException.class)
    public void classIsRequiredInAddModule() {
        element.addModule(null, creativeCommons);
    }

    @Test(expected = NullPointerException.class)
    public void moduleIsRequiredInAddModule() {
        element.addModule(CreativeCommons.class, null);
    }

    @Test
    public void falseWhenTheModuleIsMissing() {
        assertFalse(element.hasModule(CreativeCommons.class));
    }

    @Test
    public void trueIfTheModuleIsPresent() {
        element.addModule(CreativeCommons.class, creativeCommons);
        assertTrue(element.hasModule(CreativeCommons.class));
    }

    @Test(expected = NullPointerException.class)
    public void classIsRequiredOnHasModule() {
        element.hasModule(null);
    }

    @Test(expected = NullPointerException.class)
    public void classIsRequiredOnGetModule() {
        element.getModule(null);
    }

    @Test
    public void returnNullIfTheModuleIsMissing() {
        assertNull(element.getModule(CreativeCommons.class));
    }

    @Test
    public void returnTheModuleIfPresent() {
        element.addModule(CreativeCommons.class, creativeCommons);
        assertEquals(creativeCommons, element.getModule(CreativeCommons.class));
    }

    @Test
    public void getModulesReturnAnEmptyMapIfNoModules() {
        assertTrue(element.getModules().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getModulesReturnAnUnmodifiableMap() {
        element.getModules().clear();
    }

    private static class MockElement extends ExtensibleElement {

    }
}
