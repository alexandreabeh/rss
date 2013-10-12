package net.mircomacrelli.rss;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class RSSFactoryTest {
    private RSSFactory factory;

    @Before
    public void setup() {
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
            factory.parse(in);
        }
    }

    @Test
    public void emptyDatesAreIgnored() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-empty-date.xml")) {
            final RSS rss = factory.parse(in);
            assertNull(rss.getChannel().getPublishDate());
            assertNull(rss.getChannel().getBuildDate());
            assertNull(rss.getChannel().getItems().iterator().next().getPublishDate());
        }
    }

    @Test
    public void incompleteVersionNumberDoNotCauseError() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-incomplete-version-number.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void emptyLinksAreIgnored() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-empty-urls-are-ignored.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void isoDateFormat() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-iso-date-format.xml")) {
            assertNotNull(factory.parse(in));
        }
    }

    @Test
    public void inRSS09124IsMidnight() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-0.91-24-is-midnight.xml")) {
            assertThat(factory.parse(in).getChannel().getSkipHours(), contains(0));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenADateCantBeParsedThrowAnException() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-malformed-date.xml")) {
            factory.parse(in);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void rssMustBeTheRootElement() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/root-tag-is-not-rss.xml")) {
            factory.parse(in);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void onlyChanCanBeInsideRss() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/only-channel-can-be-inside-rss.xml")) {
            factory.parse(in);
        }
    }
}
