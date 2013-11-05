package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import static net.mircomacrelli.rss.Utils.RFC822_DATE_FORMAT;
import static org.junit.Assert.assertEquals;

public class RSSFactoryTest {
    @Test
    public void normalFactoryUsesTheStandardParser() {
        assertEquals(RFC822_DATE_FORMAT, RSSFactory.newFactory().getDateTimeFormatter());
    }

    @Test
    public void factoryCanUseACustomParser() {
        final DateTimeFormatter custom = ISODateTimeFormat.dateTimeParser();
        assertEquals(custom, RSSFactory.newFactory(custom).getDateTimeFormatter());
    }
}
