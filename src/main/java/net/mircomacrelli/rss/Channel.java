package net.mircomacrelli.rss;

import javax.mail.internet.InternetAddress;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.copyDate;
import static net.mircomacrelli.rss.Utils.copyEnumSet;
import static net.mircomacrelli.rss.Utils.copyInternetAddress;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.copySet;
import static net.mircomacrelli.rss.Utils.formatDate;

/**
 * Contains all the information regarding the rss and all the items published in this feed.
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Channel {
    private final String title;
    private final URL link;
    private final String description;
    private final Locale language;
    private final String copyright;
    private final InternetAddress editorEmail;
    private final InternetAddress webmasterEmail;
    private final Date publishDate;
    private final Date buildDate;
    private final Set<Category> categories;
    private final String generator;
    private final URL documentation;
    private final Cloud cloud;
    private final Integer timeToLive;
    private final Image image;
    private final TextInput textInput;
    private final Set<Integer> skipHours;
    private final EnumSet<Day> skipDays;
    private final String rating;
    private final List<Item> items;

    /**
     * Creates a new Channel. title, link and description are required all the other fields can be null
     *
     * @param title the title of the feed
     * @param link the link to the web site
     * @param description a description of this feed
     * @param language the language of the items
     * @param copyright copyright information for this feed
     * @param editorEmail email address of the editor
     * @param webmasterEmail email address of the web master
     * @param publishDate when the feed was published
     * @param buildDate when the feed was built
     * @param categories a set of categories that contains the feed
     * @param generator the name of the program used to generate the feed
     * @param documentation a link to a page with information on this feed
     * @param cloud information used to subscribe to this feed
     * @param timeToLive how many seconds can the feed be cached
     * @param image an image associated with this feed
     * @param textInput a text input to search the feed or provide feedback
     * @param skipHours a set of hours that can be skipped when checking for updates
     * @param skipDays a set of days that can be skipped when checking for updates
     * @param items a list with all the items published in the feed
     */
    Channel(final String title, final URL link, final String description, final Locale language, final String copyright,
            final InternetAddress editorEmail, final InternetAddress webmasterEmail, final Date publishDate,
            final Date buildDate, final Set<Category> categories, final String generator, final URL documentation,
            final Cloud cloud, final Integer timeToLive, final Image image, final TextInput textInput,
            final Set<Integer> skipHours, final EnumSet<Day> skipDays, final String rating, final List<Item> items) {
        requireNonNull(title);
        requireNonNull(link);
        requireNonNull(description);
        ttlInvariant(timeToLive);
        skipHoursInvariant(skipHours);

        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.editorEmail = copyInternetAddress(editorEmail);
        this.webmasterEmail = copyInternetAddress(webmasterEmail);
        this.publishDate = copyDate(publishDate);
        this.buildDate = copyDate(buildDate);
        this.categories = copySet(categories);
        this.generator = generator;
        this.documentation = documentation;
        this.cloud = cloud;
        this.timeToLive = timeToLive;
        this.image = image;
        this.textInput = textInput;
        this.skipHours = copySet(skipHours);
        this.skipDays = copyEnumSet(skipDays);
        this.rating = rating;
        this.items = copyList(items);
    }

    private static void skipHoursInvariant(final Set<Integer> skipHours) {
        if (skipHours != null) {
            for (final Integer hour : skipHours) {
                if ((hour < 0) || (hour > 23)) {
                    throw new IllegalArgumentException(
                            format("skipHours can contain only values from 0 to 23. was: %s", skipHours));
                }
            }
        }
    }

    private static void ttlInvariant(final Integer ttl) {
        if ((ttl != null) && (ttl < 0)) {
            throw new IllegalArgumentException(format("timeToLive can't be negative. was %d", ttl));
        }
    }

    /** @return the string containing the PICS rating of the channel */
    public String getRating() {
        return rating;
    }

    /** @return the title of the feed */
    public String getTitle() {
        return title;
    }

    /** @return a link to the web site */
    public URL getLink() {
        return link;
    }

    /** @return a description of this feed */
    public String getDescription() {
        return description;
    }

    /** @return the language of this feed */
    public Locale getLanguage() {
        return language;
    }

    /** @return copyright information for the items in the feed */
    public String getCopyright() {
        return copyright;
    }

    /** @return the email address of the editor */
    public InternetAddress getEditorEmail() {
        return copyInternetAddress(editorEmail);
    }

    /** @return the email address of the webmaster */
    public InternetAddress getWebmasterEmail() {
        return copyInternetAddress(webmasterEmail);
    }

    /** @return when the feed was published */
    public Date getPublishDate() {
        return copyDate(publishDate);
    }

    /** @return when the feed was built */
    public Date getBuildDate() {
        return copyDate(buildDate);
    }

    /** @return a set of categories that contains this feed */
    public Set<Category> getCategories() {
        return unmodifiableSet(categories);
    }

    /** @return the name of the program that created this feed */
    public String getGenerator() {
        return generator;
    }

    /** @return a link to a page that contains information about this feed */
    public URL getDocumentation() {
        return documentation;
    }

    /** @return information used to subscribe to this feed */
    public Cloud getCloud() {
        return cloud;
    }

    /** @return the number of seconds that the feed can be cached */
    public int getTimeToLive() {
        return timeToLive;
    }

    /** @return the image associated with this feed */
    public Image getImage() {
        return image;
    }

    /** @return a text input that can be used to search the feed */
    public TextInput getTextInput() {
        return textInput;
    }

    /** @return a set of hours that can be skipped when checking the feed for updates */
    public Set<Integer> getSkipHours() {
        return unmodifiableSet(skipHours);
    }

    /** @return a set of days that can be skipped when checking the feed for updates */
    public Collection<Day> getSkipDays() {
        return copyEnumSet(skipDays);
    }

    /** @return the list with all the items published in this feed */
    public List<Item> getItems() {
        return unmodifiableList(items);
    }

    @Override
    public int hashCode() {
        return hash(title, link, description, language, copyright, editorEmail, webmasterEmail, publishDate, buildDate,
                    categories, generator, documentation, cloud, timeToLive, image, textInput, skipHours, skipDays,
                    rating, items);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Channel)) {
            return false;
        }

        final Channel other = (Channel)obj;
        return title.equals(other.title) && link.toString().equals(other.link.toString()) &&
               description.equals(other.description) && Objects.equals(language, other.language) &&
               Objects.equals(copyright, other.copyright) &&
               Objects.equals(editorEmail, other.editorEmail) &&
               Objects.equals(webmasterEmail, other.webmasterEmail) && Objects.equals(publishDate, other.publishDate) &&
               Objects.equals(buildDate, other.buildDate) && Objects.equals(categories, other.categories) &&
               Objects.equals(generator, other.generator) && Objects.equals(documentation, other.documentation) &&
               Objects.equals(cloud, other.cloud) && Objects.equals(timeToLive, other.timeToLive) &&
               Objects.equals(image, other.image) &&
               Objects.equals(textInput, other.textInput) && Objects.equals(skipHours, other.skipHours) &&
               Objects.equals(skipDays, other.skipDays) && Objects.equals(rating, other.rating) &&
               Objects.equals(items, other.items);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append("Channel{");

        append(sb, "title", title);
        append(sb, "link", link);
        append(sb, "description", description);
        append(sb, "language", language);
        append(sb, "copyright", copyright);
        append(sb, "editorEmail", editorEmail);
        append(sb, "webmasterEmail", webmasterEmail);
        append(sb, "publishDate", formatDate(publishDate));
        append(sb, "buildDate", formatDate(buildDate));
        append(sb, "categories", categories, false);
        append(sb, "generator", generator);
        append(sb, "documentation", documentation);
        append(sb, "cloud", cloud, false);
        append(sb, "timeToLive", timeToLive);
        append(sb, "image", image, false);
        append(sb, "textInput", textInput, false);
        append(sb, "skipHours", skipHours, false);
        append(sb, "skipDays", skipDays, false);
        append(sb, "items", items, false);

        sb.append('}');
        return sb.toString();
    }

    /** Day of the Week */
    @SuppressWarnings("UnusedDeclaration")
    public enum Day {
        /** Monday */
        MONDAY,
        /** Tuesday */
        TUESDAY,
        /** Wednesday */
        WEDNESDAY,
        /** Thursday */
        THURSDAY,
        /** Friday */
        FRIDAY,
        /** Saturday */
        SATURDAY,
        /** Sunday */
        SUNDAY;

        /**
         * Get a Day from the text
         *
         * @param text the name of the day
         * @return the day
         */
        public static Day from(final String text) {
            return valueOf(text.toUpperCase(Locale.ENGLISH));
        }
    }
}
