package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

final class Utils {
    public static final DateTimeFormatter ISO8601_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
                                                                              .withLocale(Locale.ENGLISH).withZoneUTC();
    public static final DateTimeFormatter RFC822_DATE_FORMAT = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z")
                                                                             .withLocale(Locale.ENGLISH).withZoneUTC();
    public static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .append(null, new DateTimeParser[]{ISO8601_DATE_FORMAT.getParser(), RFC822_DATE_FORMAT.getParser()})
            .toFormatter().withLocale(Locale.ENGLISH).withZoneUTC();
    private static final Pattern REPEATED_SPACES = Pattern.compile(" {2,}");

    private Utils() {
        throw new AssertionError("do not instantiate this class");
    }

    public static <T> Set<T> copySet(final Set<T> source) {
        return (source == null) ? Collections.<T>emptySet() : unmodifiableSet(new HashSet<>(source));
    }

    public static <T> List<T> copyList(final List<T> source) {
        return (source == null) ? Collections.<T>emptyList() : unmodifiableList(new ArrayList<>(source));
    }

    public static <E extends Enum<E>> EnumSet<E> copyEnumSet(final EnumSet<E> set, final Class<E> type) {
        return (set == null) ? EnumSet.noneOf(type) : set.clone();
    }

    public static MimeType copyMimeType(final MimeType original) throws MimeTypeParseException {
        return new MimeType(original.getPrimaryType(), original.getSubType());
    }

    public static void append(final StringBuilder sb, final String fieldName, final Object field) {
        append(sb, fieldName, field, true);
    }

    public static void append(final StringBuilder sb, final String fieldName, final Object field, final boolean quote) {
        if (field != null) {
            if (field instanceof Collection) {
                if (((Collection)field).isEmpty()) {
                    return;
                }
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

    static boolean isStartOfTag(final XMLEvent event, final String tagName) {
        return event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(tagName);
    }

    static Map<String, String> getAllTagsValuesInside(final XMLEventReader reader, final String containerTagName) throws
                                                                                                                  XMLStreamException {
        final Map<String, String> values = new HashMap<>(5);

        while (true) {
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

    public static DateTime parseDate(final String date, final DateTimeFormatter parser) {
        final String trimmed = date.replace('\n', ' ').trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        final Matcher matcher = REPEATED_SPACES.matcher(trimmed);
        return parser.parseDateTime(matcher.replaceAll(" "));
    }

    public static String formatDate(final DateTime date) {
        if (date == null) {
            return null;
        }

        return RFC822_DATE_FORMAT.print(date);
    }

    public static URI parseUri(final String uri) throws URISyntaxException {
        if (uri.trim().isEmpty()) {
            return null;
        }
        return new URI(uri.trim());
    }

    public static void crashIfAlreadySet(final Object obj) {
        if (obj != null) {
            throw new IllegalStateException("field already set");
        }
    }

    @SafeVarargs
    public static Set<Class<? extends Module>> allowedModules(final Class<? extends Module> module,
                                                              final Class<? extends Module>... others) {
        requireModuleInterface(module);

        final Set<Class<? extends Module>> set = new HashSet<>(1 + others.length);
        set.add(module);

        for (final Class<? extends Module> mod : others) {
            requireModuleInterface(mod);
            set.add(mod);
        }

        return unmodifiableSet(set);
    }

    private static void requireModuleInterface(final Class<? extends Module> module) {
        for (final Class<?> clazz : module.getInterfaces()) {
            if (clazz.equals(Module.class)) {
                return;
            }
        }
        throw new IllegalArgumentException(
                format("the class %s does not implements the Module interface", module.getSimpleName()));
    }
}
