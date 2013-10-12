package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;
import net.mircomacrelli.rss.RSS.Version;

import javax.activation.MimeTypeParseException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.isEndOfTag;
import static net.mircomacrelli.rss.Utils.isStartOfTag;

/**
 * Factory used to Parse a RSS Feed
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class RSSFactory {
    private final XMLInputFactory factory;

    RSSFactory() {
        factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
    }

    /** @return a new instance of the factory */
    public static RSSFactory newFactory() {
        return new RSSFactory();
    }

    /**
     * Create a new RSS from an InputStream
     *
     * @param is the InputStream
     * @return the RSS
     * @throws XMLStreamException in case of some xml errors
     * @throws MalformedURLException if some of the links are wrong
     * @throws MimeTypeParseException if the mime type are malformed
     * @throws URISyntaxException if some of the domain are wrong
     */
    public RSS parse(final InputStream is) throws XMLStreamException, MalformedURLException, MimeTypeParseException,
                                                  URISyntaxException {
        final XMLEventReader reader = factory.createXMLEventReader(is);

        final Charset charset = getCharset(reader);
        final Version version = getVersion(reader);
        final Channel channel = getChannel(reader);

        return new RSS(charset, version, channel);
    }

    private static Channel getChannel(final XMLEventReader reader) throws XMLStreamException, MalformedURLException,
                                                                              URISyntaxException,
                                                                              MimeTypeParseException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String name = element.getName().getLocalPart();

                if (name.equals("channel")) {
                    return parseChannel(reader);
                } else {
                    break;
                }
            }
        }

        throw new IllegalStateException("<channel> not found");
    }

    private static Version getVersion(final XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

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

    private static Charset getCharset(final XMLEventReader reader) throws XMLStreamException {
        final StartDocument doc = (StartDocument)reader.nextEvent();
        return doc.encodingSet() ? Charset.forName(doc.getCharacterEncodingScheme()) : Charset.forName("UTF-8");
    }

    private static Channel parseChannel(final XMLEventReader reader) throws XMLStreamException, MalformedURLException,
                                                                            URISyntaxException, MimeTypeParseException {
        final Channel.Builder builder = new Channel.Builder();

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, "channel")) {
                break;
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String name = element.getName().getLocalPart();

                // skip all the extensions
                if (!element.getName().getPrefix().isEmpty()) {
                    continue;
                }

                switch (name) {
                    case "title":
                        builder.setTitle(getText(reader));
                        break;
                    case "link":
                        builder.setLink(getText(reader));
                        break;
                    case "description":
                        builder.setDescription(getText(reader));
                        break;
                    case "language":
                        builder.setLanguage(getText(reader));
                        break;
                    case "copyright":
                        builder.setCopyright(getText(reader));
                        break;
                    case "managingEditor":
                        builder.setEditor(getText(reader));
                        break;
                    case "webMaster":
                        builder.setWebmaster(getText(reader));
                        break;
                    case "pubDate":
                        builder.setPublishDate(getText(reader));
                        break;
                    case "lastBuildDate":
                        builder.setBuildDate(getText(reader));
                        break;
                    case "category":
                        builder.addCategory(parseCategory(reader, element));
                        break;
                    case "generator":
                        builder.setGenerator(getText(reader));
                        break;
                    case "docs":
                        builder.setDocs(getText(reader));
                        break;
                    case "cloud":
                        builder.setCloud(parseCloud(element));
                        break;
                    case "ttl":
                        builder.setTtl(getText(reader));
                        break;
                    case "image":
                        builder.setImage(parseImage(reader));
                        break;
                    case "textInput":
                        builder.setTextInput(parseTextInput(reader));
                        break;
                    case "rating":
                        builder.setRating(getText(reader));
                        break;
                    case "item":
                        builder.addItem(parseItem(reader));
                        break;
                    case "skipDays":
                        builder.setSkipDays(parseSkipDays(reader));
                        break;
                    case "skipHours":
                        builder.setSkipHours(parseSkipHours(reader));
                        break;
                }
            }
        }

        return builder.build();
    }

    private static EnumSet<Day> parseSkipDays(final XMLEventReader reader) throws XMLStreamException {
        final EnumSet<Day> days = EnumSet.noneOf(Day.class);

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, "skipDays")) {
                break;
            }

            if (isStartOfTag(event, "day")) {
                days.add(Day.from(getText(reader)));
            }
        }

        return days;
    }

    private static Set<Integer> parseSkipHours(final XMLEventReader reader) throws XMLStreamException {
        final Set<Integer> hours = new HashSet<>(24);

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, "skipHours")) {
                break;
            }

            if (isStartOfTag(event, "hour")) {
                hours.add(Integer.parseInt(getText(reader)));
            }
        }

        return hours;
    }

    private static Item parseItem(final XMLEventReader reader) throws XMLStreamException, MalformedURLException,
                                                                      MimeTypeParseException {
        final Item.Builder builder = new Item.Builder();

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, "item")) {
                break;
            }

            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String name = element.getName().getLocalPart();

                // skip all the extensions
                if (!element.getName().getPrefix().isEmpty()) {
                    continue;
                }

                switch (name) {
                    case "title":
                        builder.setTitle(getText(reader));
                        break;
                    case "link":
                        builder.setLink(getText(reader));
                        break;
                    case "description":
                        builder.setDescription(getText(reader));
                        break;
                    case "author":
                        builder.setAuthor(getText(reader));
                        break;
                    case "category":
                        builder.addCategory(parseCategory(reader, element));
                        break;
                    case "comments":
                        builder.setCommentsLink(getText(reader));
                        break;
                    case "enclosure":
                        builder.addEnclosure(parseEnclosure(element));
                        break;
                    case "guid":
                        builder.setUniqueId(parseUniqueId(reader, element));
                        break;
                    case "pubDate":
                        builder.setPublishDate(getText(reader));
                        break;
                    case "source":
                        builder.setSource(parseSource(reader, element));
                        break;
                }
            }
        }

        return builder.build();
    }

    private static Source parseSource(final XMLEventReader reader, final StartElement element) throws
                                                                                               XMLStreamException,
                                                                                               MalformedURLException {
        final URL link = new URL(getAttributesValues(element).get("url"));
        final String title = getText(reader);
        return new Source(title, link);
    }

    private static UniqueId parseUniqueId(final XMLEventReader reader, final StartElement element) throws
                                                                                                   XMLStreamException,
                                                                                                   MalformedURLException {
        boolean isLink = true;
        final Map<String, String> attributes = getAttributesValues(element);
        if (attributes.containsKey("isPermaLink")) {
            isLink = Boolean.parseBoolean(attributes.get("isPermaLink"));
        }
        final String id = getText(reader);
        return new UniqueId(id, isLink);
    }

    private static Enclosure parseEnclosure(final StartElement element) throws MimeTypeParseException,
                                                                               MalformedURLException {
        final Map<String, String> attributes = getAttributesValues(element);

        final Enclosure.Builder builder = new Enclosure.Builder();
        builder.setUrl(attributes.get("url"));
        builder.setLength(attributes.get("length"));
        builder.setType(attributes.get("type"));

        return builder.build();
    }

    private static TextInput parseTextInput(final XMLEventReader reader) throws XMLStreamException,
                                                                                MalformedURLException {
        final Map<String, String> values = getAllTagsValuesInside(reader, "textInput");

        final TextInput.Builder builder = new TextInput.Builder();
        builder.setLabel(values.get("title"));
        builder.setDescription(values.get("description"));
        builder.setName(values.get("name"));
        builder.setCgiScriptURL(values.get("link"));

        return builder.build();
    }

    private static Image parseImage(final XMLEventReader reader) throws XMLStreamException, MalformedURLException {
        final Map<String, String> values = getAllTagsValuesInside(reader, "image");

        final Image.Builder builder = new Image.Builder();
        builder.setImage(values.get("url"));
        builder.setAlt(values.get("title"));
        builder.setLink(values.get("link"));
        builder.setDescription(values.get("description"));
        builder.setWidth(values.get("width"));
        builder.setHeight(values.get("height"));

        return builder.build();
    }

    private static Category parseCategory(final XMLEventReader reader, final StartElement element) throws
                                                                                                   XMLStreamException {
        final String domain = getAttributesValues(element).get("domain");
        final String location = getText(reader);
        return new Category(domain, location);
    }

    private static Cloud parseCloud(final StartElement element) throws URISyntaxException {
        final Map<String, String> attributes = getAttributesValues(element);

        final Cloud.Builder builder = new Cloud.Builder();
        builder.setDomain(attributes.get("domain"));
        builder.setPath(attributes.get("path"));
        builder.setProcedureName(attributes.get("registerProcedure"));
        builder.setPort(attributes.get("port"));
        builder.setProtocol(attributes.get("protocol"));

        return builder.build();
    }
}
