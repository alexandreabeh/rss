package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.mircomacrelli.rss.Utils.RFC822_DATE_FORMAT;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.canBeWrittenOnlyOnce;
import static net.mircomacrelli.rss.Utils.copyEnumSet;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.copySet;
import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public final class UtilsTest {
    private DateTime date;

    @Before
    public void setup() {
        date = new DateTime(1382180943000L).withZone(DateTimeZone.UTC);
    }

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
        assertEquals("Sat, 19 Oct 2013 11:09:03 +0000", formatDate(date));
    }

    @Test
    public void dateAreTrimmedBeforeParsing() {
        assertEquals(date, parseDate("    Sat, 19 Oct 2013 11:09:03 +0000    ", RFC822_DATE_FORMAT));
    }

    @Test
    public void newLinesAreReplacesWithSpacesBeforeTrimming() {
        assertEquals(date, parseDate("Sat,\n\n 19 Oct 2013 11:09:03 \n+0000", RFC822_DATE_FORMAT));
    }

    @Test
    public void emptyDatesReturnNull() {
        assertNull(parseDate("", RFC822_DATE_FORMAT));
    }

    @Test
    public void whenParsingMultipleSpacesAreIgnored() {
        assertEquals(date, parseDate("Sat,  19     Oct  2013  11:09:03    +0000", RFC822_DATE_FORMAT));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void copySetReturnAnUnmodifiableEmptySetWhenPassedNull() {
        final Set<Integer> set = copySet(null);
        assertTrue(set.isEmpty());
        set.add(23);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void copySetReturnAnUnmodifiableSet() {
        final Set<Integer> set = new HashSet<>(1);
        final Set<Integer> copy = copySet(set);
        assertEquals(set, copy);
        copy.add(12);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void copyListReturnAnUnmodifiableEmptyListWhenPassedNull() {
        final List<Integer> list = copyList(null);
        assertTrue(list.isEmpty());
        list.add(4);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void copyListReturnAnUnmodifiableList() {
        final List<Integer> list = new ArrayList<>(1);
        final List<Integer> copy = copyList(list);
        assertEquals(list, copy);
        copy.add(433);
    }

    @Test
    public void copyEnumSetReturnAnEmptySetIfPassedNull() {
        final EnumSet<Day> set = copyEnumSet(null, Day.class);
        assertTrue(set.isEmpty());
    }

    @Test
    public void copyEnumSetCopiesAreNotUnmodifiable() {
        final EnumSet<Day> set = EnumSet.of(Day.WEDNESDAY);
        final EnumSet<Day> copy = copyEnumSet(set, Day.class);
        assertEquals(set, copy);
        copy.add(Day.FRIDAY);
        assertNotEquals(set, copy);
    }

    @Test
    public void ifFieldIsNullAppendDoesNothing() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "name", null);
        assertEquals(0, sb.length());
    }

    @Test
    public void ifFieldIsAnEmptyCollectionAppendDoesNothing() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "name", Collections.emptyList());
        assertEquals(0, sb.length());
    }

    @Test
    public void ifTheBuilderIsEmptyCommaIsNotAdded() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "name", "value");
        assertEquals("name='value'", sb.toString());
    }

    @Test
    public void ifLastCharIsOpenBracketsCommaIsNotAppended() {
        final StringBuilder sb = new StringBuilder(100);
        sb.append('{');
        append(sb, "name", "value");
        assertEquals("{name='value'", sb.toString());
    }

    @Test
    public void appendAddsAComma() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "name", "value");
        append(sb, "other", "12");
        assertEquals("name='value', other='12'", sb.toString());
    }

    @Test
    public void appendAddsQuotesOnlyIfTold() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "name", "value", false);
        assertEquals("name=value", sb.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void canBeWrittenOnlyOnceThrowAnExceptionIfTheValueIsSet() {
        final String value = "12";
        canBeWrittenOnlyOnce(value);
    }
}
