package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.net.MalformedURLException;

import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilsTest {
    @Test
    public void emptyUrlsAreIgnored() throws MalformedURLException {
        assertNull(parseURL(""));
    }

    @Test
    public void spacesAreTrimmedWhenParsingUrls() throws MalformedURLException {
        assertNull(parseURL("       "));
    }

    @Test
    public void validUrl() throws MalformedURLException {
        assertEquals("http://www.google.it", parseURL("    http://www.google.it   ").toString());
    }

    @Test
    public void formattingANullDateReturnNull() {
        assertNull(formatDate(null));
    }

    @Test
    public void dateAreRFC822WithEnglishLocaleAndUTCTimeZone() {
        final DateTime date = new DateTime(1381603826214L);
        assertEquals("Sat, 12 Oct 2013 18:50:26 UTC", formatDate(date));
    }

    @Test
    public void dateAreTrimmedBeforeParsing() {
        assertNull(parseDate("     "));
    }

    @Test
    public void newLinesAreReplacesWithSpacesBeforeTrimming() {
        assertNull(parseDate("\n   \n\n\n\n\n"));
    }

    @Test
    public void emptyDatesReturnNull() {
        assertNull(parseDate(""));
    }

    @Test
    public void whenParsingMultipleSpacesAreIgnored() {
        final DateTime date = new DateTime(1381603826000L).withZone(DateTimeZone.UTC);
        assertEquals(date, parseDate("Sat,    12   Oct    2013    18:50:26    UTC"));
    }
}
