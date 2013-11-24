package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * An image that can be displayed with the feed
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Image {
    private final URI image;
    private final URI link;
    private final String alt;
    private final String description;
    private final Integer width;
    private final Integer height;

    /**
     * Create a new Image. image, link and alt are required. The other can be null. If width and height are not
     * specified the default values of 88 and 31 will be used. width can't be greater than 144 and height can't be
     * greater than 400.
     *
     * @param image the url of an png, jpeg or gif image
     * @param link the link of the site
     * @param alt string to use when the image can't be displayed
     * @param description a description of the image
     * @param width width of the image
     * @param height height of the image
     */
    Image(final URI image, final URI link, final String alt, final String description, final Integer width,
          final Integer height) {
        widthInvariant(width);
        heightInvariant(height);

        this.image = requireNonNull(image);
        this.link = requireNonNull(link);
        this.alt = requireNonNull(alt);
        this.description = description;
        this.width = width;
        this.height = height;
    }

    private static void widthInvariant(final Integer width) {
        if (width != null) {
            if (width < 0) {
                throw new IllegalArgumentException(format("width can't be negative. was %d", width));
            }
        }
    }

    private static void heightInvariant(final Integer height) {
        if (height != null) {
            if (height < 0) {
                throw new IllegalArgumentException(format("height can't be negative. was %d", height));
            }
        }
    }

    /** @return the url of an png, jpeg or gif image */
    public URI getImage() {
        return image;
    }

    /** @return the link of the site */
    public URI getLink() {
        return link;
    }

    /** @return string to use when the image can't be displayed */
    public String getAlt() {
        return alt;
    }

    /** @return a description of the image */
    public String getDescription() {
        return description;
    }

    /** @return width of the image */
    public int getWidth() {
        return (width == null) ? 88 : width;
    }

    /** @return height of the image */
    public int getHeight() {
        return (height == null) ? 31 : height;
    }

    @Override
    public int hashCode() {
        return hash(image, alt, link, width, height, description);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Image)) {
            return false;
        }

        final Image other = (Image)obj;
        return image.equals(other.image) && link.equals(other.link) && alt.equals(other.alt) &&
               Objects.equals(width, other.width) && Objects.equals(height, other.height) &&
               Objects.equals(description, other.description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(200);
        sb.append("Image{image='").append(image).append("', alt='").append(alt).append("', link='").append(link)
          .append('\'');

        if (description != null) {
            sb.append(", description='").append(description).append('\'');
        }

        if (width != null) {
            sb.append(", width=").append(width);
        }

        if (height != null) {
            sb.append(", height=").append(height);
        }

        sb.append('}');
        return sb.toString();
    }

    static final class Builder extends BuilderBase<Image> {
        URI image;
        URI link;
        String alt;
        String description;
        Integer width;
        Integer height;

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws Exception {
            final Map<String, String> values = getAllTagsValuesInside(reader, "image");

            image = parseUri(values.get("url"));
            alt = values.get("title");
            link = parseUri(values.get("link"));
            description = values.get("description");
            if (values.containsKey("width")) {
                width = Integer.parseInt(values.get("width"));
            }
            if (values.containsKey("height")) {
                height = Integer.parseInt(values.get("height"));
            }
        }

        @Override
        public Image build() {
            return new Image(image, link, alt, description, width, height);
        }
    }
}
