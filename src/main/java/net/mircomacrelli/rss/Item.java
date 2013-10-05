package net.mircomacrelli.rss;

import javax.mail.internet.InternetAddress;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.copyDate;
import static net.mircomacrelli.rss.Utils.copyInternetAddress;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.copySet;
import static net.mircomacrelli.rss.Utils.formatDate;

/**
 * An Item of the Feed
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Item {
    private final InternetAddress authorEmail;
    private final String title;
    private final String description;
    private final URL link;
    private final URL commentsLink;
    private final Set<Category> categories;
    private final UniqueId uniqueId;
    private final Date publishDate;
    private final Source source;
    private final List<Enclosure> enclosures;

    /**
     * Creates a new Item. All parameters are optional an can be null. But at least on of title or description must be
     * set when the item is created
     *
     * @param link link to the item
     * @param title title of the item
     * @param description the text describing the item
     * @param authorEmail the author email
     * @param publishDate the publication date
     * @param categories a set of categories
     * @param source the original source of the item
     * @param commentsLink link to a page with comments
     * @param enclosures an list with all the attached files
     * @param uniqueId the unique id of the item
     */
    Item(final URL link, final String title, final String description, final InternetAddress authorEmail,
         final Date publishDate, final Set<Category> categories, final Source source, final URL commentsLink,
         final List<Enclosure> enclosures, final UniqueId uniqueId) {
        itemInvariant(title, description);

        this.authorEmail = copyInternetAddress(authorEmail);
        this.title = title;
        this.description = description;
        this.link = link;
        this.commentsLink = commentsLink;
        this.categories = copySet(categories);
        this.uniqueId = uniqueId;
        this.publishDate = copyDate(publishDate);
        this.source = source;
        this.enclosures = copyList(enclosures);
    }

    private static void itemInvariant(final String title, final String description) {
        if ((title == null) && (description == null)) {
            throw new IllegalStateException("at least on of title or description must be not null");
        }
    }

    /** @return the email address of the author */
    public InternetAddress getAuthorEmail() {
        return copyInternetAddress(authorEmail);
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
    public URL getLink() {
        return link;
    }

    /** @return a link to a page containing comments on this item */
    public URL getCommentsLink() {
        return commentsLink;
    }

    /** @return a set of categories that contains this item */
    public Set<Category> getCategories() {
        return unmodifiableSet(categories);
    }

    /** @return the unique id of the item */
    public UniqueId getUniqueId() {
        return uniqueId;
    }

    /** @return when the item was published */
    public Date getPublishDate() {
        return copyDate(publishDate);
    }

    /** @return the original source of the file if present */
    public Source getSource() {
        return source;
    }

    /** @return the attached file if present */
    public List<Enclosure> getEnclosures() {
        return unmodifiableList(enclosures);
    }

    @Override
    public int hashCode() {
        return hash(authorEmail, title, description, link, commentsLink, categories, uniqueId, publishDate, source,
                    enclosures);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Item)) {
            return false;
        }

        final Item other = (Item)obj;
        return Objects.equals(authorEmail, other.authorEmail) && Objects.equals(title, other.title) &&
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
        append(sb, "authorEmail", authorEmail);
        append(sb, "commentsLink", commentsLink);
        append(sb, "uniqueId", uniqueId, false);
        append(sb, "publishDate", formatDate(publishDate));
        append(sb, "categories", categories, false);
        append(sb, "source", source, false);
        append(sb, "enclosures", enclosures, false);

        sb.append('}');
        return sb.toString();
    }
}
