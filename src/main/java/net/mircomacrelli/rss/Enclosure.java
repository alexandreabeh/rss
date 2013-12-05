package net.mircomacrelli.rss;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.copyMimeType;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * An Enclosure is a way to attach a file to an Item. The file is not embedded in the feed. Only the URI to its actual
 * location, the size and the type are given.
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Enclosure {
    private final URI link;
    private final long length;
    private final MimeType type;

    /**
     * Creates a new Enclosure
     *
     * @param link the url to the file
     * @param length the length of the file. in bytes
     * @param type MIME Type of the linked file
     */
    Enclosure(final URI link, final long length, final MimeType type) {
        lengthInvariant(length);
        linkInvariant(link);

        this.link = requireNonNull(link);
        this.length = length;
        this.type = requireNonNull(type);
    }

    private static void linkInvariant(final URI link) {
        if (!(link.getScheme().equals("http") || link.getScheme().equals("https"))) {
            throw new IllegalArgumentException(format("only HTTP URIs are allowed. was %s", link.getScheme()));
        }
    }

    private static void lengthInvariant(final long length) {
        if (length < 0) {
            throw new IllegalArgumentException(format("length can't be negative. was %d", length));
        }
    }

    /** @return the link to the file */
    public URI getLink() {
        return link;
    }

    /** @return the length of the linked files in bytes */
    public long getLength() {
        return length;
    }

    /**
     * @return a copy of the MIME Type
     * @throws MimeTypeParseException if the mime type is not valid
     */
    public MimeType getType() throws MimeTypeParseException {
        return copyMimeType(type);
    }

    @Override
    public int hashCode() {
        return hash(link, length, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Enclosure)) {
            return false;
        }

        final Enclosure other = (Enclosure)obj;
        return (length == other.length) && link.equals(other.link) && type.match(other.type);
    }

    @Override
    public String toString() {
        return format("Enclosure{link='%s', length=%d, type=%s}", link, length, type);
    }

    static final class Builder extends BuilderBase<Enclosure> {
        URI uri;
        Long length;
        MimeType type;

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws MimeTypeParseException,
                                                                                          URISyntaxException {
            final Map<String, String> attributes = getAttributesValues(element);

            uri = parseUri(attributes.get("url"));
            length = Long.parseLong(attributes.get("length"));
            type = new MimeType(attributes.get("type"));
        }

        @Override
        public Enclosure build() {
            return new Enclosure(uri, length, type);
        }
    }
}
