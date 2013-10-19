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

public class UtilsTest {
    private DateTime date;

    private DateTime dateWithoutTime;

    private DateTime dateWithoutSeconds;

    @Before
    public void setup() {
        date = new DateTime(1382180943000L).withZone(DateTimeZone.UTC);
        dateWithoutTime = new DateTime(1382140800000L).withZone(DateTimeZone.UTC);
        dateWithoutSeconds = new DateTime(1382180940000L).withZone(DateTimeZone.UTC);
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
        final DateTime date = new DateTime(1381603826214L);
        assertEquals("Sat, 12 Oct 2013 18:50:26 UTC", formatDate(date));
    }

    @Test
    public void dateAreTrimmedBeforeParsing() {
        final DateTime date = new DateTime(1381603826000L).withZone(DateTimeZone.UTC);
        assertEquals(date, parseDate("   Sat, 12 Oct 2013 18:50:26 UTC  "));
    }

    @Test
    public void newLinesAreReplacesWithSpacesBeforeTrimming() {
        final DateTime date = new DateTime(1381603826000L).withZone(DateTimeZone.UTC);
        assertEquals(date, parseDate("Sat,\n 12 Oct 2013 18:50:26\n UTC"));
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

    @Test(expected = UnsupportedOperationException .class)
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

    @Test
    public void testDateFormat1() {
        assertEquals(date, parseDate("Sat, 19 Oct 2013 11:09:03 +0000"));
    }

    @Test
    public void testDateFormat2() {
        assertEquals(date, parseDate("Sat, 19 Oct 2013 11:09:03 UTC"));
    }

    @Test
    public void testDateFormat3() {
        assertEquals(date, parseDate("19 10 2013 11:09:03 +0000"));
    }

    @Test
    public void testDateFormat4() {
        assertEquals(dateWithoutSeconds, parseDate("Sat, 19 Oct 2013 11:09 UTC"));
    }

    @Test
    public void testDateFormat5() {
        assertEquals(date, parseDate("Sat, 19 Oct 2013 11:09:03"));
    }

    @Test
    public void testDateFormat6() {
        assertEquals(date, parseDate("2013-10-19T11:09:03+0000"));
    }

    @Test
    public void testDateFormat7() {
        assertEquals(date, parseDate("2013-10-19 11:09:03"));
    }

    @Test
    public void testDateFormat8() {
        assertEquals(dateWithoutTime, parseDate("19/10/2013"));
    }

    @Test
    public void testDateFormat10() {
        assertEquals(dateWithoutTime, parseDate("2013-10-19"));
    }

    @Test
    public void testDateFormat11() {
        assertEquals(date, parseDate("2013-10-19 11:09:03.0"));
    }

    @Test
    public void testDateFormat13() {
        assertEquals(dateWithoutSeconds, parseDate("2013-10-19T11:09+0000"));
    }

    @Test
    public void testDateFormat14() {
        assertEquals(date, parseDate("2013-10-19 11:09:03+0000"));
    }

    @Test
    public void testDateFormat15() {
        assertEquals(dateWithoutSeconds, parseDate("19/10/2013 11:09"));
    }

    @Test
    public void testDateFormat16() {
        assertEquals(dateWithoutTime, parseDate("19.10.2013"));
    }

    @Test
    public void testDateFormat17() {
        assertEquals(date, parseDate("19 10 2013 11:09:03 UTC"));
    }

    @Test
    public void testDateFormat18() {
        assertEquals(date, parseDate("2013.10.19 11:09:03.0"));
    }

    @Test
    public void testDateFormat19() {
        assertEquals(date, parseDate("2013-10-19T11:09:03.0"));
    }

    @Test
    public void testDateFormat20() {
        assertEquals(date, parseDate("2013-10-19 11:09.03"));
    }

    @Test
    public void testDateFormat21() {
        assertEquals(date, parseDate("19 Oct 2013 11:09:03 +0000"));
    }

    @Test
    public void testDateFormat22() {
        assertEquals(date, parseDate("10/19/2013 11:09:03 AM"));
    }

    @Test
    public void testDateFormat23() {
        assertEquals(date, parseDate("19 Oct 2013 11:09:03 UTC"));
    }

    @Test
    public void testDateFormat25() {
        assertEquals(date, parseDate("Saturday, October 19, 2013 11:09:03 UTC"));
    }

    @Test
    public void testDateFormat26() {
        assertEquals(date, parseDate("Sat, 2013 Oct 1911:09:03 UTC"));
    }

    @Test
    public void testDateFormat27() {
        assertEquals(dateWithoutSeconds, parseDate("Sat, 19 Oct 2013 11:09 +0000"));
    }

    @Test
    public void testDateFormat28() {
        assertEquals(dateWithoutTime, parseDate("Sat, 19 Oct 2013"));
    }

    @Test
    public void testDateFormat29() {
        assertEquals(dateWithoutTime, parseDate("Sat 19 Oct 2013"));
    }

    @Test
    public void testDateFormat30() {
        assertEquals(dateWithoutSeconds, parseDate("Sat Oct 19, 2013, 11:09 AM UTC"));
    }

    @Test
    public void testDateFormat31() {
        assertEquals(date, parseDate("Sat Oct 19 11:09:03 UTC 2013"));
    }

    @Test
    public void testDateFormat32() {
        assertEquals(dateWithoutTime, parseDate("20131019"));
    }
}
