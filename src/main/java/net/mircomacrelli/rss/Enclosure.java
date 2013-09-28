package net.mircomacrelli.rss;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.net.URL;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.copyMimeType;

/**
 * An Enclosure is a way to attach a file to an Item. The file is not embedded in the feed. Only the URL to its actual
 * location, the size and the type are given.
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Enclosure {
    private final URL link;
    private final long length;
    private final MimeType type;

    /**
     * Creates a new Enclosure
     *
     * @param link the url to the file
     * @param length the length of the file. in bytes
     * @param type MIME Type of the linked file
     */
    Enclosure(final URL link, final long length, final MimeType type) throws MimeTypeParseException {
        requireNonNull(link);
        requireNonNull(type);
        lengthInvariant(length);
        linkInvariant(link);

        this.link = link;
        this.length = length;
        this.type = copyMimeType(type);
    }

    private static void lengthInvariant(final long length) {
        if (length < 0) {
            throw new IllegalArgumentException(format("length can't be negative. was %d", length));
        }
    }

    private static void linkInvariant(final URL link) {
        if (!link.getProtocol().equals("http")) {
            throw new IllegalArgumentException(format("only HTTP URLs are allowed. was %s", link.getProtocol()));
        }
    }

    /** @return the link to the file */
    public URL getLink() {
        return link;
    }

    /** @return the length of the linked files in bytes */
    public long getLength() {
        return length;
    }

    /** @return a copy of the MIME Type */
    public MimeType getType() throws MimeTypeParseException {
        return copyMimeType(type);
    }

    @Override
    public int hashCode() {
        return hash(link, length, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Enclosure)) {
            return false;
        }

        final Enclosure other = (Enclosure)obj;
        return link.toString().equals(other.link.toString()) && (length == other.length) &&
               type.getPrimaryType().equals(other.type.getPrimaryType()) &&
               type.getSubType().equals(other.type.getSubType());
    }

    @Override
    public String toString() {
        return format("Enclosure{link='%s', length=%d, type=%s}", link, length, type);
    }
}
