package net.mircomacrelli.rss;

import net.mircomacrelli.rss.RSS.Version;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.nio.charset.Charset;

import static net.mircomacrelli.rss.Utils.getAttributesValues;

/**
 * Factory used to Parse a RSS Feed
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class RSSFactory {
    private final XMLInputFactory factory;
    private final DateTimeFormatter parser;

    private RSSFactory(final DateTimeFormatter parser) {
        factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
        this.parser = parser == null ? Utils.PARSER : parser;
    }

    /** @return a new instance of the factory */
    public static RSSFactory newFactory() {
        return new RSSFactory(null);
    }

    /**
     * @param parser the parser used for the dates
     * @return a new instance of the factory
     */
    public static RSSFactory newFactory(final DateTimeFormatter parser) {
        return new RSSFactory(parser);
    }

    /** @return the DateTimeFormatter used in this factory */
    public DateTimeFormatter getDateTimeFormatter() {
        return parser;
    }

    /**
     * Create a new RSS from an InputStream
     *
     * @param is the InputStream
     * @return the RSS
     */
    public RSS parse(final InputStream is) throws ParserException {
        final XMLEventReader reader;
        try {
            reader = factory.createXMLEventReader(is);
        } catch (final XMLStreamException cause) {
            throw new ParserException(cause);
        }

        final Charset charset = getCharset(reader);
        final Version version = getVersion(reader);
        final Channel channel = getChannel(reader);

        return new RSS(charset, version, channel);
    }

    private static Charset getCharset(final XMLEventReader reader) throws ParserException {
        try {
            final StartDocument doc = (StartDocument)reader.nextEvent();
            return doc.encodingSet() ? Charset.forName(doc.getCharacterEncodingScheme()) : Charset.forName("UTF-8");
        } catch (final XMLStreamException cause) {
            throw new ParserException(cause);
        }
    }

    private static Version getVersion(final XMLEventReader reader) throws ParserException {
        while (true) {
            final XMLEvent event;
            try {
                event = reader.nextEvent();
            } catch (final XMLStreamException cause) {
                throw new ParserException(cause);
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String name = element.getName().getLocalPart();

                if (name.equals("rss")) {
                    return Version.from(getAttributesValues(element).get("version"));
                } else {
                    break;
                }
            }
        }

        throw new IllegalStateException("<rss> not found");
    }

    private Channel getChannel(final XMLEventReader reader) throws ParserException {
        while (reader.hasNext()) {
            final XMLEvent event;
            try {
                event = reader.nextEvent();
            } catch (final XMLStreamException cause) {
                throw new ParserException(cause);
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String name = element.getName().getLocalPart();

                if (name.equals("channel")) {
                    final Channel.Builder builder = new Channel.Builder(parser);
                    builder.parse(reader, null);
                    return builder.build();
                } else {
                    break;
                }
            }
        }

        throw new IllegalStateException("<channel> not found");
    }
}
