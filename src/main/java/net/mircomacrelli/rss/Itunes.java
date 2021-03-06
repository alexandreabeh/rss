package net.mircomacrelli.rss;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
import static net.mircomacrelli.rss.Utils.crashIfAlreadySet;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.isEndOfTag;
import static net.mircomacrelli.rss.Utils.isStartOfTag;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * Module used for parsing the itunes/podcast module
 *
 * @author Mirco Macrelli
 * @version 2.1
 */
public final class Itunes implements Module {

    /**
     * A category used in iTunes
     *
     * @author Mirco Macrelli
     * @version 2.1
     */
    public static final class Category {
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

        /**
         * Create a new Category
         *
         * @param name the name of the category
         * @param subCategories a list of sub categories
         */
        public Category(final String name, final List<Category> subCategories) {
            this.name = requireNonNull(name);
            this.subCategories = subCategories;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, subCategories);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Category)) {
                return false;
            }

            final Category other = (Category)obj;
            return name.equals(other.name) && Objects.equals(subCategories, other.subCategories);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Category{");

            append(sb, "name", name);
            append(sb, "subCategories", subCategories, false);

            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Enum with all the possibile levels of Explicit for a podcast
     *
     * @author Mirco Macrelli
     * @version 2.1
     */
    public enum Explicit {
        /**
         * Yes the podcast has explicit content
         */
        YES,
        /**
         * No, the podcast does not contain explicit materials
         */
        NO,
        /**
         * All the content is clean and not explicit
         */
        CLEAN;

        /**
         * Return the value of Explicit from a string
         *
         * @param value the string value
         * @return the Explicit enum value
         */
        public static Explicit from(final String value) {
            if (value.equalsIgnoreCase("yes")) {
                return YES;
            } else {
                return value.equalsIgnoreCase("clean") ? CLEAN : NO;
            }
        }
    }

    private final String author;
    private final Boolean block;
    private final URI image;
    private final Boolean closedCaptioned;
    private final String summary;
    private final String subtitle;
    private final URI newFeedUrl;
    private final Integer order;
    private final Boolean complete;
    private final String ownerName;
    private final InternetAddress ownerEmail;
    private final Explicit explicit;
    private final Period duration;
    private final List<Category> categories;


    Itunes(final String author, final Boolean block, final URI image, final Boolean closedCaptioned, final String summary, final String subtitle,
           final URI newFeedUrl, final Integer order, final Boolean complete, final String ownerName, final InternetAddress ownerEmail,
           final Explicit explicit, final Period duration, final List<Category> categories) {
        this.author = author;
        this.block = block;
        this.image = image;
        this.closedCaptioned = closedCaptioned;
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
        return unmodifiableList(categories);
    }

    public Period getDuration() {
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
        return (complete == null) ? false : complete;
    }

    public Integer getOrder() {
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
        return (closedCaptioned == null) ? false : closedCaptioned;
    }

    public boolean isBlocked() {
        return (block == null) ? false : block;
    }

    public URI getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, block, image, closedCaptioned, summary, subtitle, newFeedUrl, order, complete, ownerName,
                            ownerEmail, explicit, duration, categories);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Itunes)) {
            return false;
        }

        final Itunes other = (Itunes)obj;
        return Objects.equals(author, other.author) && Objects.equals(block, other.block) &&
               Objects.equals(image, other.image) && Objects.equals(closedCaptioned, other.closedCaptioned) &&
               Objects.equals(summary, other.summary) && Objects.equals(subtitle, other.subtitle) &&
               Objects.equals(newFeedUrl, other.newFeedUrl) && Objects.equals(order, other.order) &&
               Objects.equals(complete, other.complete) && Objects.equals(ownerName, other.ownerName) &&
               Objects.equals(ownerEmail, other.ownerEmail) && Objects.equals(explicit, other.explicit) &&
               Objects.equals(duration, other.duration) && Objects.equals(categories, other.categories);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(256);
        sb.append("iTunes{");

        append(sb, "author", author);
        append(sb, "block", block, false);
        append(sb, "image", image);
        append(sb, "closedCaptioned", closedCaptioned, false);
        append(sb, "summary", summary);
        append(sb, "subtitle", subtitle);
        append(sb, "newFeedUrl", newFeedUrl);
        append(sb, "order", order, false);
        append(sb, "complete", complete, false);
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
        private Boolean closedCaptioned;
        private String summary;
        private String subtitle;
        private URI newFeedUrl;
        private Integer order;
        private Boolean complete;
        private String ownerName;
        private InternetAddress ownerEmail;
        private Explicit explicit;
        private Period duration;
        private final List<Category> categories;

        public static final PeriodFormatter DURATION = new PeriodFormatterBuilder()
                .append(null, new PeriodFormatterBuilder().appendHours()
                                                          .appendSeparator(":")
                                                          .appendMinutes()
                                                          .appendSeparator(":")
                                                          .appendSeconds().toParser())
                .append(null, new PeriodFormatterBuilder().appendMinutes()
                                                          .appendSeparator(":")
                                                          .appendSeconds().toParser()).toFormatter();

        Builder() {
            super(null);
            categories = new ArrayList<>(1);
        }

        @Override
        void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            final String name = element.getName().getLocalPart();

            switch (name) {
                case "author":
                    crashIfAlreadySet(author);
                    author = getText(reader);
                    break;
                case "block":
                    crashIfAlreadySet(block);
                    block = getText(reader).equalsIgnoreCase("yes");
                    break;
                case "image":
                    crashIfAlreadySet(image);
                    image = parseUri(getAttributesValues(element).get("href"));
                    break;
                case "isClosedCaptioned":
                    crashIfAlreadySet(closedCaptioned);
                    closedCaptioned = getText(reader).equalsIgnoreCase("yes");
                    break;
                case "summary":
                    crashIfAlreadySet(summary);
                    summary = getText(reader);
                    break;
                case "subtitle":
                    crashIfAlreadySet(subtitle);
                    subtitle = getText(reader);
                    break;
                case "new-feed-url":
                    crashIfAlreadySet(newFeedUrl);
                    newFeedUrl = parseUri(getText(reader));
                    break;
                case "order":
                    crashIfAlreadySet(order);
                    order = Integer.parseInt(getText(reader));
                    break;
                case "complete":
                    crashIfAlreadySet(complete);
                    complete = getText(reader).equals("yes");
                    break;
                case "owner":
                    final Map<String,String> values = getAllTagsValuesInside(reader, "owner");
                    crashIfAlreadySet(ownerName);
                    ownerName = values.get("name");
                    try {
                        crashIfAlreadySet(ownerEmail);
                        ownerEmail = new InternetAddress(values.get("email"));
                    } catch (final AddressException cause) {
                        throw new ParserException(cause);
                    }
                    break;
                case "explicit":
                    crashIfAlreadySet(explicit);
                    explicit = Explicit.from(getText(reader));
                    break;
                case "duration":
                    crashIfAlreadySet(duration);
                    duration = DURATION.parsePeriod(getText(reader));
                    break;
                case "category":
                    categories.add(parseCategory(reader, element));
                    break;
            }
        }

        private static Category parseCategory(final XMLEventReader reader, final StartElement element) throws
                                                                                                 ParserException {
            final String name = getAttributesValues(element).get("text");
            final List<Category> subCategories = new ArrayList<>(0);

            while (true) {
                try {
                    final XMLEvent event = reader.nextEvent();

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
        Module buildElement() {
            return new Itunes(author, block, image, closedCaptioned, summary, subtitle, newFeedUrl, order, complete,
                              ownerName, ownerEmail, explicit, duration, categories);
        }
    }
}
