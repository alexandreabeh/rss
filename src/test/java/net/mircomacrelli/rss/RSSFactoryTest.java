package net.mircomacrelli.rss;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class RSSFactoryTest {
    private static RSSFactory factory;

    @BeforeClass
    public static void setup() {
        factory = RSSFactory.newFactory();
    }

    @Test
    public void testParse() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-complete.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void parseIgnoreAllTheExtensionsInChannelAndItem() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-with-extensions.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void useAnEmptyStringIfTagHasNoText() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-tags-without-text.xml")) {
            assertTrue(factory.parse(in).getChannel().getDescription().isEmpty());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void aTagInTheWrongPlaceWillCauseAnException() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-a-tag-in-the-wrong-place.xml")) {
            assertTrue(factory.parse(in).getChannel().getDescription().isEmpty());
        }
    }
}
