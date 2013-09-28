package net.mircomacrelli.rss;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.InternetAddress;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import static java.lang.String.format;

final class Utils {
    private Utils() {
        throw new AssertionError("do not instantiate this class");
    }

    public static <T> Set<T> copySet(final Set<T> source) {
        return (source == null) ? null : new HashSet<>(source);
    }

    public static <T> List<T> copyList(final List<T> source) {
        return (source == null) ? null : new ArrayList<>(source);
    }

    public static Date copyDate(final Date source) {
        return (source == null) ? null : (Date)source.clone();
    }

    public static <E extends Enum<E>> EnumSet<E> copyEnumSet(final EnumSet<E> set) {
        return (set == null) ? null : set.clone();
    }

    public static InternetAddress copyInternetAddress(final InternetAddress source) {
        return (source == null) ? null : (InternetAddress)source.clone();
    }

    public static MimeType copyMimeType(final MimeType original) {
        try {
            return new MimeType(original.getPrimaryType(), original.getSubType());
        } catch (MimeTypeParseException e) {
            throw new AssertionError("this should never happen", e);
        }
    }

    public static void append(final StringBuilder sb, final String fieldName, final Object field, final boolean quote) {
        if (field != null) {
            if ((field instanceof Collection) && ((Collection)field).isEmpty()) {
                return;
            }
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

    public static void append(final StringBuilder sb, final String fieldName, final Object field) {
        append(sb, fieldName, field, true);
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
                return values;
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                values.put(element.getName().getLocalPart(), getText(reader));
            }
        }

        throw new IllegalStateException(format("tag '%s' not terminated ", containerTagName));
    }

    static String getText(final XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (event.isCharacters()) {
                return event.asCharacters().getData();
            }

            if (event.isEndElement()) {
                return "";
            }
        }
        throw new IllegalStateException("tag not terminated");
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

    public static Date parseDate(String date) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        return dateFormat.parse(date);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}
