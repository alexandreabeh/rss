package net.mircomacrelli.rss;

import java.net.URL;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * An image that can be displayed with the feed
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Image {
    private final URL image;
    private final URL link;
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
    Image(final URL image, final URL link, final String alt, final String description, final Integer width,
          final Integer height) {
        requireNonNull(image);
        requireNonNull(link);
        requireNonNull(alt);
        widthInvariant(width);
        heightInvariant(height);

        this.image = image;
        this.link = link;
        this.alt = alt;
        this.description = description;
        this.width = width;
        this.height = height;
    }

    private static void widthInvariant(final Integer width) {
        if (width != null) {
            if ((width < 0) || (width > 144)) {
                throw new IllegalArgumentException(format("width must be between 0 and 144. was %d", width));
            }
        }
    }

    private static void heightInvariant(final Integer height) {
        if (height != null) {
            if ((height < 0) || (height > 400)) {
                throw new IllegalArgumentException(format("height must be between 0 and 400. was %d", height));
            }
        }
    }

    /** @return the url of an png, jpeg or gif image */
    public URL getImage() {
        return image;
    }

    /** @return the link of the site */
    public URL getLink() {
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
        return hash(image, alt, link);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Image)) {
            return false;
        }

        final Image other = (Image)obj;
        return image.toString().equals(other.image.toString()) && link.toString().equals(other.link.toString()) &&
               alt.equals(other.alt);
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
}
