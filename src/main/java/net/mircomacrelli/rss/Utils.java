package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.InternetAddress;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

final class Utils {
    private static final DateTimeParser[] DATE_FORMATS = {
            DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z").getParser(),
            DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss z").getParser(),
            DateTimeFormat.forPattern("d MMM yyyy HH:mm:ss Z").getParser(),
            DateTimeFormat.forPattern("d MMM yyyy HH:mm:ss z").getParser(),
            DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss").getParser(),
            DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm Z").getParser(),
            DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm z").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser()};
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().append(null, DATE_FORMATS)
                                                                                  .toFormatter()
                                                                                  .withLocale(Locale.ENGLISH)
                                                                                  .withZoneUTC();

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss z")
                                                                       .withLocale(Locale.ENGLISH).withZoneUTC();

    private Utils() {
        throw new AssertionError("do not instantiate this class");
    }

    public static <T> Set<T> copySet(final Set<T> source) {
        return (source == null) ? null : unmodifiableSet(new HashSet<>(source));
    }

    public static <T> List<T> copyList(final List<T> source) {
        return (source == null) ? null : unmodifiableList(new ArrayList<>(source));
    }

    public static <E extends Enum<E>> EnumSet<E> copyEnumSet(final EnumSet<E> set) {
        return (set == null) ? null : set.clone();
    }

    public static InternetAddress copyInternetAddress(final InternetAddress source) {
        return (source == null) ? null : (InternetAddress)source.clone();
    }

    public static MimeType copyMimeType(final MimeType original) throws MimeTypeParseException {
        return new MimeType(original.getPrimaryType(), original.getSubType());
    }

    public static void append(final StringBuilder sb, final String fieldName, final Object field) {
        append(sb, fieldName, field, true);
    }

    public static void append(final StringBuilder sb, final String fieldName, final Object field, final boolean quote) {
        if (field != null) {
            if ((sb.length() > 0) && (sb.charAt(sb.length() - 1) != '{')) {
                sb.append(", ");
            }
            sb.append(fieldName).append('=');
            if (quote) {
                sb.append('\'');
            }
            sb.append(field.toString());
            if (quote) {
                sb.append('\'');
            }
        }
    }

    static boolean isStartOfTag(final XMLEvent event, final String tagName) {
        return event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(tagName);
    }

    static Map<String, String> getAllTagsValuesInside(final XMLEventReader reader, final String containerTagName) throws
                                                                                                                  XMLStreamException {
        final Map<String, String> values = new HashMap<>(5);

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, containerTagName)) {
                break;
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                values.put(element.getName().getLocalPart(), getText(reader));
            }
        }

        return values;
    }

    static String getText(final XMLEventReader reader) throws XMLStreamException {
        final XMLEvent event = reader.nextEvent();

        if (event.isCharacters()) {
            return event.asCharacters().getData();
        }

        if (event.isEndElement()) {
            return "";
        } else {
            throw new IllegalStateException("text not found");
        }
    }

    static boolean isEndOfTag(final XMLEvent event, final String tagName) {
        return event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(tagName);
    }

    static Map<String, String> getAttributesValues(final StartElement element) {
        final Map<String, String> values = new HashMap<>(4);

        @SuppressWarnings("unchecked")
        final Iterator<Attribute> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            final Attribute attr = attributes.next();
            values.put(attr.getName().getLocalPart(), attr.getValue());
        }

        return values;
    }

    private static final Pattern REPEATED_SPACES = Pattern.compile(" {2,}");

    public static DateTime parseDate(final String date) {
        final String trimmed = date.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        final Matcher matcher = REPEATED_SPACES.matcher(trimmed);
        return PARSER.parseDateTime(matcher.replaceAll(" "));
    }

    public static String formatDate(final DateTime date) {
        if (date == null) {
            return null;
        }

        return DATE_FORMAT.print(date);
    }

    public static URL parseURL(final String docs) throws MalformedURLException {
        if (docs.trim().isEmpty()) {
            return null;
        }
        return new URL(docs);
    }
}
