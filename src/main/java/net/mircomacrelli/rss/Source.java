package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * Link to the original feed that first published the Item
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Source {
    private final URI link;
    private final String name;

    /**
     * Creates a new Source
     *
     * @param name the name of the source
     * @param link ths link to the original rss feed
     */
    Source(final String name, final URI link) {
        this.name = requireNonNull(name);
        this.link = requireNonNull(link);
    }

    /** @return the URI to the feed */
    public URI getLink() {
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
        if (!(obj instanceof Source)) {
            return false;
        }

        final Source other = (Source)obj;
        return name.equals(other.name) && link.equals(other.link);
    }

    @Override
    public String toString() {
        return format("Source{name='%s', link='%s'}", name, link);
    }

    static final class Builder extends BuilderBase<Source> {
        URI link;
        String title;

        @Override
        public void parseElement(final XMLEventReader reader, final StartElement element) throws URISyntaxException,
                                                                                          XMLStreamException {
            link = parseUri(getAttributesValues(element).get("url"));
            title = getText(reader);
        }

        @Override
        public Source build() {
            return new Source(title, link);
        }
    }
}
