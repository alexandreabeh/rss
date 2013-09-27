package net.mircomacrelli.rss;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public final class RSSFactoryTest {
    @Test
    public void testParse() throws Exception {
        final RSSFactory factory = RSSFactory.newFactory();
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-complete.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void parseIgnoreAllTheExtensionsInChannelAndItem() throws Exception {
        final RSSFactory factory = RSSFactory.newFactory();
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-complete-with-extensions.xml")) {
            assertNotNull(factory.parse(in));
        }
    }
}
