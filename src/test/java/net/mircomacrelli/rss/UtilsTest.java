package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.mircomacrelli.rss.Utils.PARSER;
import static net.mircomacrelli.rss.Utils.allowedModules;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.copyEnumSet;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.copySet;
import static net.mircomacrelli.rss.Utils.crashIfAlreadySet;
import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.isEndOfTag;
import static net.mircomacrelli.rss.Utils.isStartOfTag;
import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public final class UtilsTest extends XmlTestBase {
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
        assertEquals(date, parseDate("    Sat, 19 Oct 2013 11:09:03 +0000    ", PARSER));
    }

    @Test
    public void newLinesAreReplacesWithSpacesBeforeTrimming() {
        assertEquals(date, parseDate("Sat,\n\n 19 Oct 2013 11:09:03 \n+0000", PARSER));
    }

    @Test
    public void emptyDatesReturnNull() {
        assertNull(parseDate("", PARSER));
    }

    @Test
    public void whenParsingMultipleSpacesAreIgnored() {
        assertEquals(date, parseDate("Sat,  19     Oct  2013  11:09:03    +0000", PARSER));
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
    public void copyListOfNull() {
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
    public void appendCollection() {
        final StringBuilder sb = new StringBuilder(100);
        append(sb, "list", Arrays.asList(12, 14), false);
        assertEquals("list=[12, 14]", sb.toString());
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
    public void tryToWriteTwoValues() {
        final String value = "12";
        crashIfAlreadySet(value);
    }

    @Test(expected = NullPointerException.class)
    public void firstModuleCantBeNull() {
        allowedModules(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firstModuleMustImplementModule() {
        final Class<?> stringClazz = String.class;
        @SuppressWarnings("unchecked")
        final Class<? extends Module> clazz = (Class<? extends Module>)stringClazz;
        allowedModules(clazz);
    }

    @Test(expected = NullPointerException.class)
    public void othersModulesCantBeNull() {
        allowedModules(CreativeCommons.class, Syndication.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void othersModulesMustImplementModule() {
        final Class<?> stringClazz = String.class;
        @SuppressWarnings("unchecked")
        final Class<? extends Module> clazz = (Class<? extends Module>)stringClazz;
        allowedModules(CreativeCommons.class, Syndication.class, clazz);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void allowedModulesReturnUnmodifiableSet() {
        allowedModules(Syndication.class).clear();
    }

    @Test
    public void allowOneModule() {
        assertTrue(allowedModules(CreativeCommons.class).contains(CreativeCommons.class));
    }

    @Test
    public void allowTwoModules() {
        final Set<Class<? extends Module>> modules = allowedModules(CreativeCommons.class, Syndication.class);
        assertTrue(modules.contains(CreativeCommons.class));
        assertTrue(modules.contains(Syndication.class));
    }

    @Test
    public void isStartOfTagCheckIfIsStartTagEvent() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        reader.nextEvent();
        assertFalse(isStartOfTag(reader.nextEvent(), "tag"));
    }

    @Test
    public void isStartOfTagCheckTheTagName() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        assertFalse(isStartOfTag(reader.nextEvent(), "other"));
    }

    @Test
    public void isReallyTheStartOfTag() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        assertTrue(isStartOfTag(reader.nextEvent(), "tag"));
    }

    @Test
    public void isEndOfTagCheckIfIsEndOfTagEvent() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        assertFalse(isEndOfTag(reader.nextEvent(), "tag"));
    }

    @Test
    public void isEndOfTagCheckTheTagName() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        reader.nextEvent();
        reader.nextEvent();
        assertFalse(isEndOfTag(reader.nextEvent(), "other"));
    }

    @Test
    public void isReallyTheEndOfTag() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        reader.nextEvent();
        reader.nextEvent();
        assertTrue(isEndOfTag(reader.nextEvent(), "tag"));
    }

    @Test
    public void getTextReturnTheImmediateNextTextEvent() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo completo</tag>");
        reader.nextEvent();
        assertEquals("testo completo", getText(reader));
    }

    @Test
    public void getTextReturnEmptyStringOnEmptyTags() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag/>");
        reader.nextEvent();
        assertEquals("", getText(reader));
    }

    @Test
    public void getTextReturnEmptyStringOnEmptyTagTwo() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag></tag>");
        reader.nextEvent();
        assertEquals("", getText(reader));
    }

    @Test(expected = IllegalStateException.class)
    public void getTextThrowAnExceptionIfCantFindText() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag><other>value</other></tag>");
        reader.nextEvent();
        getText(reader);
    }

    @Test
    public void getAttributesReturnAnEmptyMapWhenThereAreNone() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>testo</tag>");
        assertTrue(getAttributesValues((StartElement)reader.nextEvent()).isEmpty());
    }

    @Test
    public void getAttributesOnTagThatHasTwo() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag a=\"1\" b=\"2\">testo</tag>");
        assertEquals(2, getAttributesValues((StartElement)reader.nextEvent()).size());
    }

    @Test
    public void copyMimeType() throws MimeTypeParseException {
        final MimeType mime = new MimeType("application", "javascript");
        assertTrue(mime.match(Utils.copyMimeType(mime)));
    }

    @Test
    public void getTagsReturnAnEmptyMapWhenNoChildTagsAreFound() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag>\n\n\n</tag>");
        reader.nextEvent();
        assertTrue(getAllTagsValuesInside(reader, "tag").isEmpty());
    }

    @Test
    public void getTagsReturnMapsWithTwoValues() throws XMLStreamException {
        final XMLEventReader reader = parseString("<tag><a>uno</a><b>due</b></tag>");
        reader.nextEvent();
        final Map<String, String> tags = getAllTagsValuesInside(reader, "tag");

        assertTrue(tags.keySet().contains("a"));
        assertTrue(tags.keySet().contains("b"));
        assertTrue(tags.values().contains("uno"));
        assertTrue(tags.values().contains("due"));
    }
}
