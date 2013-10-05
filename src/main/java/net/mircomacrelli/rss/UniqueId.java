package net.mircomacrelli.rss;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * Unique Identifier of an Item
 *
 * @author Mirco Macrelli
 * @version 1.0
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
    UniqueId(final String id, final boolean isLink) throws MalformedURLException {
        requireNonNull(id);
        this.id = isLink ? new URL(id).toString() : id;
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
     * @return the id as an URL if the UniqueId is a link
     * @throws MalformedURLException this should never happen
     */
    public URL asURL() throws MalformedURLException {
        if (!isLink) {
            throw new IllegalStateException("the id is not a link");
        }
        return new URL(id);
    }

    @Override
    public int hashCode() {
        return hash(id, isLink);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UniqueId)) {
            return false;
        }

        final UniqueId other = (UniqueId)obj;
        return id.equals(other.id) && (isLink == other.isLink);
    }

    @Override
    public String toString() {
        return format("UniqueId{id='%s', isLink=%s}", id, isLink);
    }
}
