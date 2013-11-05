package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.allowedModules;
import static net.mircomacrelli.rss.Utils.append;
import static net.mircomacrelli.rss.Utils.canBeWrittenOnlyOnce;
import static net.mircomacrelli.rss.Utils.copyEnumSet;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.copySet;
import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.isEndOfTag;
import static net.mircomacrelli.rss.Utils.isStartOfTag;
import static net.mircomacrelli.rss.Utils.parseDate;
import static net.mircomacrelli.rss.Utils.parseURL;

/**
 * Contains all the information regarding the rss and all the items published in this feed.
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Channel extends ExtensibleElement {
    private final String title;
    private final URL link;
    private final String description;
    private final Locale language;
    private final String copyright;
    private final String editor;
    private final String webmaster;
    private final DateTime publishDate;
    private final DateTime buildDate;
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
     * @param editor email address of the editor
     * @param webmaster email address of the web master
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
            final String editor, final String webmaster, final DateTime publishDate, final DateTime buildDate,
            final Set<Category> categories, final String generator, final URL documentation, final Cloud cloud,
            final Integer timeToLive, final Image image, final TextInput textInput, final Set<Integer> skipHours,
            final EnumSet<Day> skipDays, final String rating, final List<Item> items) {
        final Set<Integer> correctedSkipHours = correctSkipHours(skipHours);

        requireNonNull(title);
        requireNonNull(link);
        requireNonNull(description);
        ttlInvariant(timeToLive);
        skipHoursInvariant(correctedSkipHours);

        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.editor = editor;
        this.webmaster = webmaster;
        this.publishDate = publishDate;
        this.buildDate = buildDate;
        this.categories = copySet(categories);
        this.generator = generator;
        this.documentation = documentation;
        this.cloud = cloud;
        this.timeToLive = timeToLive;
        this.image = image;
        this.textInput = textInput;
        this.skipHours = copySet(correctedSkipHours);
        this.skipDays = copyEnumSet(skipDays, Day.class);
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

    private static Set<Integer> correctSkipHours(final Set<Integer> orig) {
        if (orig == null) {
            return null;
        }

        final Set<Integer> corrected = new HashSet<>(orig);
        if (corrected.contains(24)) {
            corrected.remove(24);
            corrected.add(0);
        }

        return corrected;
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
    public String getEditor() {
        return editor;
    }

    /**
     * @return return the edit as an email address
     * @throws AddressException if the content of the tag managingEditor is not a valid email address
     */
    public InternetAddress getEditorAsEmailAddress() throws AddressException {
        return new InternetAddress(editor);
    }

    /** @return the email address of the webmaster */
    public String getWebmaster() {
        return webmaster;
    }

    /**
     * @return return the webmaster as an email address
     * @throws AddressException if the content of the tag webMaster is not a valid email address
     */
    public InternetAddress getWebmasterAsEmailAddress() throws AddressException {
        return new InternetAddress(webmaster);
    }

    /** @return when the feed was published */
    public DateTime getPublishDate() {
        return publishDate;
    }

    /** @return when the feed was built */
    public DateTime getBuildDate() {
        return buildDate;
    }

    /** @return a set of categories that contains this feed */
    public Set<Category> getCategories() {
        return categories;
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

    public Integer getTimeToLive() {
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
        return skipHours;
    }

    /** @return a set of days that can be skipped when checking the feed for updates */
    public Collection<Day> getSkipDays() {
        return copyEnumSet(skipDays, Day.class);
    }

    /** @return the list with all the items published in this feed */
    public List<Item> getItems() {
        return items;
    }

    @Override
    public int hashCode() {
        return hash(title, link, description, language, copyright, editor, webmaster, publishDate, buildDate,
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
               Objects.equals(editor, other.editor) &&
               Objects.equals(webmaster, other.webmaster) && Objects.equals(publishDate, other.publishDate) &&
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
        append(sb, "editor", editor);
        append(sb, "webmaster", webmaster);
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

    static final class Builder extends ExtensibleElementBuilder<Channel> {
        private static final Set<Class<? extends Module>> ALLOWED_MODULES = allowedModules(CreativeCommons.class,
                                                                                           Syndication.class);
        String title;
        URL link;
        String description;
        Locale language;
        String copyright;
        String editor;
        String webmaster;
        DateTime publishDate;
        DateTime buildDate;
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

        Builder(final DateTimeFormatter parser) {
            super("channel", parser);
        }

        @Override
        Channel buildElement() {
            return new Channel(title, link, description, language, copyright, editor, webmaster, publishDate,
                               buildDate, categories, generator, docs, cloud, ttl, image, textInput, skipHours,
                               skipDays, rating, items);
        }

        @Override
        void handleTag(final XMLEventReader reader, final StartElement element) throws Exception {
            final String name = element.getName().getLocalPart();
            switch (name) {
                case "title":
                    canBeWrittenOnlyOnce(title);
                    title = getText(reader);
                    break;
                case "link":
                    canBeWrittenOnlyOnce(link);
                    link = parseURL(getText(reader));
                    break;
                case "description":
                    canBeWrittenOnlyOnce(description);
                    description = getText(reader);
                    break;
                case "language":
                    canBeWrittenOnlyOnce(language);
                    language = Locale.forLanguageTag(getText(reader));
                    break;
                case "copyright":
                    canBeWrittenOnlyOnce(copyright);
                    copyright = getText(reader);
                    break;
                case "managingEditor":
                    canBeWrittenOnlyOnce(editor);
                    editor = getText(reader);
                    break;
                case "webMaster":
                    canBeWrittenOnlyOnce(webmaster);
                    webmaster = getText(reader);
                    break;
                case "pubDate":
                    canBeWrittenOnlyOnce(publishDate);
                    publishDate = parseDate(getText(reader), parser);
                    break;
                case "lastBuildDate":
                    canBeWrittenOnlyOnce(buildDate);
                    buildDate = parseDate(getText(reader), parser);
                    break;
                case "category":
                    if (categories == null) {
                        categories = new HashSet<>(6);
                    }
                    categories.add(parseCategory(reader, element));
                    break;
                case "generator":
                    canBeWrittenOnlyOnce(generator);
                    generator = getText(reader);
                    break;
                case "docs":
                    canBeWrittenOnlyOnce(docs);
                    docs = parseURL(getText(reader));
                    break;
                case "cloud":
                    canBeWrittenOnlyOnce(cloud);
                    cloud = parseCloud(element);
                    break;
                case "ttl":
                    canBeWrittenOnlyOnce(ttl);
                    ttl = Integer.parseInt(getText(reader));
                    break;
                case "image":
                    canBeWrittenOnlyOnce(image);
                    image = parseImage(reader);
                    break;
                case "textInput":
                    canBeWrittenOnlyOnce(textInput);
                    textInput = parseTextInput(reader);
                    break;
                case "rating":
                    canBeWrittenOnlyOnce(rating);
                    rating = getText(reader);
                    break;
                case "item":
                    if (items == null) {
                        items = new ArrayList<>(25);
                    }
                    items.add(parseItem(reader, parser));
                    break;
                case "skipDays":
                    canBeWrittenOnlyOnce(skipDays);
                    skipDays = parseSkipDays(reader);
                    break;
                case "skipHours":
                    canBeWrittenOnlyOnce(skipHours);
                    skipHours = parseSkipHours(reader);
                    break;
            }
        }

        private static Image parseImage(final XMLEventReader reader) throws Exception {
            final Image.Builder builder = new Image.Builder();
            builder.parse(reader, null);
            return builder.build();
        }

        private static Cloud parseCloud(final StartElement element) throws Exception {
            final Cloud.Builder builder = new Cloud.Builder();
            builder.parse(null, element);
            return builder.build();
        }

        private static TextInput parseTextInput(final XMLEventReader reader) throws Exception {
            final TextInput.Builder builder = new TextInput.Builder();
            builder.parse(reader, null);
            return builder.build();
        }

        private static EnumSet<Day> parseSkipDays(final XMLEventReader reader) throws XMLStreamException {
            final EnumSet<Day> days = EnumSet.noneOf(Day.class);

            while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();

                if (isEndOfTag(event, "skipDays")) {
                    break;
                }

                if (isStartOfTag(event, "Day")) {
                    days.add(Day.from(getText(reader)));
                }
            }

            return days;
        }

        private static Category parseCategory(final XMLEventReader reader, final StartElement element) throws Exception {
            final Category.Builder builder = new Category.Builder();
            builder.parse(reader, element);
            return builder.build();
        }

        private static Set<Integer> parseSkipHours(final XMLEventReader reader) throws XMLStreamException {
            final Set<Integer> hours = new HashSet<>(24);

            while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();

                if (isEndOfTag(event, "skipHours")) {
                    break;
                }

                if (isStartOfTag(event, "Hour")) {
                    hours.add(Integer.parseInt(getText(reader)));
                }
            }

            return hours;
        }

        private static Item parseItem(final XMLEventReader reader, final DateTimeFormatter parser) throws Exception {
            final Item.Builder builder = new Item.Builder(parser);
            builder.parse(reader, null);
            return builder.build();
        }

        @Override
        Set<Class<? extends Module>> getAllowedModules() {
            return ALLOWED_MODULES;
        }
    }
}
