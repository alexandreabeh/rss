package net.mircomacrelli.rss;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
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
    public void dateWithoutDayName() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-date-without-day-name.xml")) {
            final RSS rss = factory.parse(in);
            assertEquals(new Date(1138510800000L), rss.getChannel().getPublishDate());
        }
    }

    @Test
    public void dateWithoutATimezone() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-date-without-timezone.xml")) {
            final RSS rss = factory.parse(in);
            assertEquals(new Date(1138555064000L), rss.getChannel().getPublishDate());
        }
    }

    @Test
    public void dateWithoutSeconds() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-date-without-seconds.xml")) {
            final RSS rss = factory.parse(in);
            assertEquals(new Date(1138555020000L), rss.getChannel().getPublishDate());
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
    public void allowHttpsInEnclosureLinks() throws Exception {
        try (InputStream in = RSS.class.getResourceAsStream("/rss-2.0-allow-https-urls.xml")) {
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
}
