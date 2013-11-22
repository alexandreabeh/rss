package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;

/**
 * Unique Identifier of an Item
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class UniqueId {
    private final String id;
    private final boolean isLink;

    /**
     * Creates a new UniqueId
     *
     * @param id the unique id
     * @param isLink true if id contains a link that can be opened in a browser
     */
    UniqueId(final String id, final boolean isLink) {
        this.id = requireNonNull(id);
        this.isLink = isLink;
    }

    /** @return the unique id */
    public String getId() {
        return id;
    }

    /** @return true if the id is a link that can be opened in the browser */
    public boolean isLink() {
        return isLink;
    }

    /**
     * @return the id as an URI if the UniqueId is a link
     * @throws URISyntaxException if the URI is malformed
     */
    public URI asURI() throws URISyntaxException {
        if (!isLink) {
            throw new IllegalStateException("the id is not a link");
        }
        return new URI(id);
    }

    @Override
    public int hashCode() {
        return hash(id, isLink);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof UniqueId)) {
            return false;
        }

        final UniqueId other = (UniqueId)obj;
        return (isLink == other.isLink) && id.equals(other.id);
    }

    @Override
    public String toString() {
        return format("UniqueId{id='%s', isLink=%s}", id, isLink);
    }

    static final class Builder extends BuilderBase<UniqueId> {
        boolean isLink = true;
        String id;

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws Exception {
            final Map<String, String> attributes = getAttributesValues(element);
            if (attributes.containsKey("isPermaLink")) {
                isLink = Boolean.parseBoolean(attributes.get("isPermaLink"));
            }
            id = getText(reader);
        }

        @Override
        public UniqueId build() {
            return new UniqueId(id, isLink);
        }
    }
}
