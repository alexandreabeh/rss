package net.mircomacrelli.rss;

import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.isEndOfTag;
import static net.mircomacrelli.rss.Utils.isStartOfTag;
import static net.mircomacrelli.rss.Utils.parseUri;

public class Itunes implements Module {

    public static class Category {
        private final String name;

        public String getName() {
            return name;
        }

        private final List<Category> subCategories;

        public List<Category> getSubCategories() {
            if (subCategories == null) {
                return emptyList();
            }
            return unmodifiableList(subCategories);
        }

        public Category(String name, List<Category> subCategories) {
            this.name = requireNonNull(name);
            this.subCategories = subCategories;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, subCategories);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Category)) {
                return false;
            }

            Category other = (Category)obj;
            return name.equals(other.name) && Objects.equals(subCategories, other.subCategories);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Category{");

            append(sb, "name", name);
            append(sb, "subCategories", subCategories);

            sb.append('}');
            return sb.toString();
        }
    }

    public static enum Explicit {
        YES,
        NO,
        CLEAN;

        public static Explicit from(String value) {
            if (value.equalsIgnoreCase("yes")) {
                return YES;
            } else {
                if (value.equalsIgnoreCase("clean")) {
                    return CLEAN;
                } else {
                    return NO;
                }
            }
        }
    }

    private final String author;
    private final Boolean block;
    private final URI image;
    private final Boolean cc;
    private final String summary;
    private final String subtitle;
    private final URI newFeedUrl;
    private final Integer order;
    private final Boolean complete;
    private final String ownerName;
    private final InternetAddress ownerEmail;
    private final Explicit explicit;
    private final Duration duration;
    private final List<Category> categories;


    private Itunes(final String author, final Boolean block, URI image, Boolean cc, String summary, String subtitle,
                   URI newFeedUrl, Integer order, Boolean complete, String ownerName, InternetAddress ownerEmail,
                   Explicit explicit, Duration duration, List<Category> categories) {
        this.author = author;
        this.block = block;
        this.image = image;
        this.cc = cc;
        this.summary = summary;
        this.subtitle = subtitle;
        this.newFeedUrl = newFeedUrl;
        this.order = order;
        this.complete = complete;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.explicit = explicit;
        this.duration = duration;
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Duration getDuration() {
        return duration;
    }

    public Explicit getExplicit() {
        return explicit;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public InternetAddress getOwnerEmail() {
        return ownerEmail;
    }

    public boolean isComplete() {
        return complete == null ? false : complete;
    }

    public Integer getOreder() {
        return order;
    }

    public URI getNewFeedUrl() {
        return newFeedUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isClosedCaptioned() {
        return cc == null ? false : cc;
    }

    public boolean isBlocked() {
        return block == null ? false : block;
    }

    public URI getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, block, image, cc, summary, subtitle, newFeedUrl, order, complete, ownerName,
                            ownerEmail, explicit, duration, categories);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Itunes)) {
            return false;
        }

        final Itunes other = (Itunes)obj;
        return Objects.equals(author, other.author) && Objects.equals(block, other.block) &&
               Objects.equals(image, other.image) && Objects.equals(cc, other.cc) &&
               Objects.equals(summary, other.summary) && Objects.equals(subtitle, other.subtitle) &&
               Objects.equals(newFeedUrl, other.newFeedUrl) && Objects.equals(order, other.order) &&
               Objects.equals(complete, other.complete) && Objects.equals(ownerName, other.ownerName) &&
               Objects.equals(ownerEmail, other.ownerEmail) && Objects.equals(explicit, other.explicit) &&
               Objects.equals(duration, other.duration) && Objects.equals(categories, other.categories);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("iTunes{");

        append(sb, "author", author);
        append(sb, "block", block, false);
        append(sb, "image", image);
        append(sb, "cc", cc, false);
        append(sb, "summary", summary);
        append(sb, "subtitle", subtitle);
        append(sb, "newFeedUrl", newFeedUrl);
        append(sb, "order", order, false);
        append(sb, "compelte", complete, false);
        append(sb, "owner.name", ownerName);
        append(sb, "owner.email", ownerEmail);
        append(sb, "explicit", explicit, false);
        append(sb, "duration", duration);
        append(sb, "categories", categories, false);

        sb.append('}');

        return sb.toString();
    }

    static final class Builder extends ModuleBuilder {
        private String author;
        private Boolean block;
        private URI image;
        private Boolean cc;
        private String summary;
        private String subtitle;
        private URI newFeedUrl;
        private Integer order;
        private Boolean complete;
        private String ownerName;
        private InternetAddress ownerEmail;
        private Explicit explicit;
        private Duration duration;
        private final List<Category> categories;

        Builder(final DateTimeFormatter parser) {
            super(parser);
            categories = new ArrayList<>(1);
        }

        @Override
        void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            final String name = element.getName().getLocalPart();

            switch (name) {
                case "author":
                    author = getText(reader);
                    break;
                case "block":
                    block = getText(reader).equalsIgnoreCase("yes");
                    break;
                case "image":
                    image = parseUri(getAttributesValues(element).get("href"));
                    break;
                case "isClosedCaptioned":
                    cc = getText(reader).equalsIgnoreCase("yes");
                    break;
                case "summary":
                    summary = getText(reader);
                    break;
                case "subtitle":
                    subtitle = getText(reader);
                    break;
                case "new-feed-url":
                    newFeedUrl = parseUri(getText(reader));
                    break;
                case "order":
                    order = Integer.parseInt(getText(reader));
                    break;
                case "complete":
                    complete = getText(reader).equals("yes");
                    break;
                case "owner":
                    Map<String,String> values = getAllTagsValuesInside(reader, "owner");
                    ownerName = values.get("name");
                    try {
                        ownerEmail = new InternetAddress(values.get("email"));
                    } catch (final AddressException cause) {
                        throw new ParserException(cause);
                    }
                    break;
                case "explicit":
                    explicit = Explicit.from(getText(reader));
                    break;
                case "duration":
                    duration = Duration.parse(getText(reader));
                    break;
                case "category":
                    categories.add(parseCategory(reader, element));
                    break;
            }
        }

        private static final Category parseCategory(XMLEventReader reader, StartElement element) throws
                                                                                                 ParserException {
            final String name = getAttributesValues(element).get("text");
            final List<Category> subCategories = new ArrayList<>(0);

            while (true) {
                try {
                    XMLEvent event = reader.nextEvent();

                    if (isEndOfTag(event, "category")) {
                        break;
                    }

                    if (isStartOfTag(event, "category")) {
                        subCategories.add(parseCategory(reader, event.asStartElement()));
                    }
                } catch (final XMLStreamException cause) {
                    throw new ParserException(cause);
                }
            }

            return new Category(name, subCategories);
        }

        @Override
        Module buildElement() throws ParserException {
            return new Itunes(author, block, image, cc, summary, subtitle, newFeedUrl, order, complete, ownerName,
                              ownerEmail, explicit, duration, categories);
        }
    }
}
