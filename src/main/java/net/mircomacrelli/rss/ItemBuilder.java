package net.mircomacrelli.rss;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;

final class ItemBuilder {
    InternetAddress authorEmail;
    String title;
    String description;
    URL link;
    URL commentsLink;
    Set<Category> categories;
    UniqueId uniqueId;
    Date publishDate;
    Source source;
    Enclosure enclosure;

    public Item build() {
        return new Item(link, title, description, authorEmail, publishDate, categories, source, commentsLink, enclosure,
                        uniqueId);
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLink(final String link) throws MalformedURLException {
        this.link = new URL(link);
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setAuthorEmail(final String authorEmail) throws AddressException {
        this.authorEmail = new InternetAddress(authorEmail);
    }

    public void addCategory(final Category category) {
        if (categories == null) {
            categories = new HashSet<>(1);
        }
        categories.add(category);
    }

    public void setCommentsLink(final String commentsLink) throws MalformedURLException {
        this.commentsLink = parseURL(commentsLink);
    }

    public void setEnclosure(final Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public void setUniqueId(final UniqueId uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setPublishDate(final String publishDate) throws ParseException {
        this.publishDate = parseDate(publishDate);
    }

    public void setSource(final Source source) {
        this.source = source;
    }
}
