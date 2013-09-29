package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;

final class ChannelBuilder {
    String title;
    URL link;
    String description;
    Locale language;
    String copyright;
    InternetAddress managingEditorEmail;
    InternetAddress webmasterEmail;
    Date publishDate;
    Date buildDate;
    Set<Category> categories;
    String generator;
    URL docs;
    Cloud cloud;
    Integer ttl;
    Image image;
    TextInput textInput;
    Set<Integer> skipHours;
    EnumSet<Day> skipDays;
    String rating;
    List<Item> items;

    public Channel build() {
        return new Channel(title, link, description, language, copyright, managingEditorEmail, webmasterEmail,
                           publishDate, buildDate, categories, generator, docs, cloud, ttl, image, textInput, skipHours,
                           skipDays, rating, items);
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

    public void setLanguage(final String language) {
        this.language = Locale.forLanguageTag(language);
    }

    public void setCopyright(final String copyright) {
        this.copyright = copyright;
    }

    public void setManagingEditorEmail(final String managingEditorEmail) throws AddressException {
        this.managingEditorEmail = new InternetAddress(managingEditorEmail);
    }

    public void setWebmasterEmail(final String webmasterEmail) throws AddressException {
        this.webmasterEmail = new InternetAddress(webmasterEmail);
    }

    public void setPublishDate(final String publishDate) throws ParseException {
        this.publishDate = parseDate(publishDate);
    }

    public void setBuildDate(final String buildDate) throws ParseException {
        this.buildDate = parseDate(buildDate);
    }

    public void addCategory(final Category category) {
        if (categories == null) {
            categories = new HashSet<>(6);
        }
        categories.add(category);
    }

    public void setGenerator(final String generator) {
        this.generator = generator;
    }

    public void setDocs(final String docs) throws MalformedURLException {
        this.docs = parseURL(docs);
    }

    public void setCloud(final Cloud cloud) {
        this.cloud = cloud;
    }

    public void setTtl(final String ttl) {
        this.ttl = Integer.parseInt(ttl);
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    public void setTextInput(final TextInput textInput) {
        this.textInput = textInput;
    }

    public void setRating(final String rating) {
        this.rating = rating;
    }

    public void addItem(final Item item) {
        if (items == null) {
            items = new ArrayList<>(25);
        }
        items.add(item);
    }

    public void setSkipDays(final EnumSet<Day> skipDays) {
        this.skipDays = skipDays.clone();
    }

    public void setSkipHours(final Set<Integer> skipHours) {
        this.skipHours = new HashSet<>(skipHours);
    }
}
