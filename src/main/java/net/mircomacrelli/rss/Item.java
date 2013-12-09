package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;
import static net.mircomacrelli.rss.Utils.allowedModules;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.crashIfAlreadySet;
import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * An Item of the Feed
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Item extends ExtensibleElement {
    private final String author;
    private final String title;
    private final String description;
    private final URI link;
    private final URI commentsLink;
    private final Set<Category> categories;
    private final UniqueId uniqueId;
    private final DateTime publishDate;
    private final Source source;
    private final List<Enclosure> enclosures;

    /**
     * Creates a new Item. All parameters are optional an can be null. But at least on of title or description must be
     * set when the item is created
     *
     * @param link link to the item
     * @param title title of the item
     * @param description the text describing the item
     * @param author the author email
     * @param publishDate the publication date
     * @param categories a set of categories
     * @param source the original source of the item
     * @param commentsLink link to a page with comments
     * @param enclosures an list with all the attached files
     * @param uniqueId the unique id of the item
     */
    Item(final URI link, final String title, final String description, final String author, final DateTime publishDate,
         final Set<Category> categories, final Source source, final URI commentsLink, final List<Enclosure> enclosures,
         final UniqueId uniqueId) {
        itemInvariant(title, description);

        this.author = author;
        this.title = title;
        this.description = description;
        this.link = link;
        this.commentsLink = commentsLink;
        this.categories = categories;
        this.uniqueId = uniqueId;
        this.publishDate = publishDate;
        this.source = source;
        this.enclosures = enclosures;
    }

    private static void itemInvariant(final String title, final String description) {
        if ((title == null) && (description == null)) {
            throw new IllegalStateException("at least on of title or description must be not null");
        }
    }

    /** @return the email address of the author */
    public String getAuthor() {
        return author;
    }

    /**
     * @return return the author as an email address
     * @throws AddressException if the content of the tag author is not a valid email address
     */
    public InternetAddress getAuthorAsEmailAddress() throws AddressException {
        return new InternetAddress(author);
    }

    /** @return the title of the item */
    public String getTitle() {
        return title;
    }

    /** @return the text describing the item */
    public String getDescription() {
        return description;
    }

    /** @return the link to the item */
    public URI getLink() {
        return link;
    }

    /** @return a link to a page containing comments on this item */
    public URI getCommentsLink() {
        return commentsLink;
    }

    /** @return a set of categories that contains this item */
    public Set<Category> getCategories() {
        if (categories == null) {
            return emptySet();
        }

        return unmodifiableSet(categories);
    }

    /** @return the unique id of the item */
    public UniqueId getUniqueId() {
        return uniqueId;
    }

    /** @return when the item was published */
    public DateTime getPublishDate() {
        return publishDate;
    }

    /** @return the original source of the file if present */
    public Source getSource() {
        return source;
    }

    /** @return the attached file if present */
    public List<Enclosure> getEnclosures() {
        if (enclosures == null) {
            return emptyList();
        }

        return unmodifiableList(enclosures);
    }

    @Override
    public int hashCode() {
        return hash(author, title, description, link, commentsLink, categories, uniqueId, publishDate, source,
                    enclosures);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }

        final Item other = (Item)obj;
        return Objects.equals(author, other.author) && Objects.equals(title, other.title) &&
               Objects.equals(description, other.description) && Objects.equals(link, other.link) &&
               Objects.equals(commentsLink, other.commentsLink) && Objects.equals(categories, other.categories) &&
               Objects.equals(uniqueId, other.uniqueId) && Objects.equals(publishDate, other.publishDate) &&
               Objects.equals(source, other.source) && Objects.equals(enclosures, other.enclosures);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("Item{");

        append(sb, "title", title);
        append(sb, "link", link);
        append(sb, "description", description);
        append(sb, "author", author);
        append(sb, "commentsLink", commentsLink);
        append(sb, "uniqueId", uniqueId, false);
        append(sb, "publishDate", formatDate(publishDate));
        append(sb, "categories", categories, false);
        append(sb, "source", source, false);
        append(sb, "enclosures", enclosures, false);

        sb.append('}');
        return sb.toString();
    }

    static final class Builder extends ExtensibleElementBuilder<Item> {
        private static final Set<Class<? extends Module>> ALLOWED_MODULES = allowedModules(CreativeCommons.class);
        String author;
        String title;
        String description;
        URI link;
        URI commentsLink;
        final Set<Category> categories;
        UniqueId uniqueId;
        DateTime publishDate;
        Source source;
        final List<Enclosure> enclosures;

        Builder(final DateTimeFormatter parser) {
            super("item", parser);
            categories = new HashSet<>(1);
            enclosures = new ArrayList<>(1);
        }

        @Override
        protected Item buildBase() {
            return new Item(link, title, description, author, publishDate, categories, source, commentsLink, enclosures,
                            uniqueId);
        }

        @Override
        protected void parseTag(final XMLEventReader reader, final StartElement element) throws ParserException {
            final String name = element.getName().getLocalPart();
            switch (name) {
                case "title":
                    crashIfAlreadySet(title);
                    title = getText(reader);
                    break;
                case "link":
                    crashIfAlreadySet(link);
                    link = parseUri(getText(reader));
                    break;
                case "description":
                    crashIfAlreadySet(description);
                    description = getText(reader);
                    break;
                case "author":
                    crashIfAlreadySet(author);
                    author = getText(reader);
                    break;
                case "category":
                    categories.add(parseCategory(reader, element));
                    break;
                case "comments":
                    crashIfAlreadySet(commentsLink);
                    commentsLink = parseUri(getText(reader));
                    break;
                case "enclosure":
                    enclosures.add(parseEnclosure(reader, element));
                    break;
                case "guid":
                    crashIfAlreadySet(uniqueId);
                    uniqueId = parseUniqueId(reader, element);
                    break;
                case "pubDate":
                    crashIfAlreadySet(publishDate);
                    publishDate = parseDate(getText(reader), parser);
                    break;
                case "source":
                    crashIfAlreadySet(source);
                    source = parseSource(reader, element);
                    break;
            }
        }

        private static Source parseSource(final XMLEventReader reader, final StartElement element) throws
                                                                                                   ParserException {
            final Source.Builder builder = new Source.Builder();
            builder.parse(reader, element);
            return builder.build();
        }

        private static Enclosure parseEnclosure(final XMLEventReader reader, final StartElement element) throws
                                                                                                         ParserException {
            final Enclosure.Builder builder = new Enclosure.Builder();
            builder.parse(reader, element);
            return builder.build();
        }

        private static UniqueId parseUniqueId(final XMLEventReader reader, final StartElement element) throws
                                                                                                       ParserException {
            final UniqueId.Builder builder = new UniqueId.Builder();
            builder.parse(reader, element);
            return builder.build();
        }

        private static Category parseCategory(final XMLEventReader reader, final StartElement element) throws
                                                                                                       ParserException {
            final Category.Builder builder = new Category.Builder();
            builder.parse(reader, element);
            return builder.build();
        }

        @Override
        protected Set<Class<? extends Module>> getAllowedModules() {
            return ALLOWED_MODULES;
        }
    }
}
