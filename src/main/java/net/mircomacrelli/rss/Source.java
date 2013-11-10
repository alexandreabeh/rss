package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URL;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseURL;

/**
 * Link to the original feed that first published the Item
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Source {
    private final URL link;
    private final String name;

    /**
     * Creates a new Source
     *
     * @param name the name of the source
     * @param link ths link to the original rss feed
     */
    Source(final String name, final URL link) {
        this.name = requireNonNull(name);
        this.link = requireNonNull(link);
    }

    /** @return the URL to the feed */
    public URL getLink() {
        return link;
    }

    /** @return the name of the source */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return hash(link, name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Source)) {
            return false;
        }

        final Source other = (Source)obj;
        return name.equals(other.name) && link.toString().equals(other.link.toString());
    }

    @Override
    public String toString() {
        return format("Source{name='%s', link='%s'}", name, link);
    }

    static final class Builder extends BuilderBase<Source> {
        URL link;
        String title;

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws Exception {
            link = parseURL(getAttributesValues(element).get("url"));
            title = getText(reader);
        }

        @Override
        public Source build() {
            return new Source(title, link);
        }
    }
}
