package net.mircomacrelli.rss;

import java.net.MalformedURLException;
import java.net.URL;

final class ImageBuilder {
    URL image;
    URL link;
    String alt;
    String description;
    Integer width;
    Integer height;

    public void setImage(final String image) throws MalformedURLException {
        this.image = new URL(image);
    }

    public void setAlt(final String alt) {
        this.alt = alt;
    }

    public void setLink(final String link) throws MalformedURLException {
        this.link = new URL(link);
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setWidth(final String width) {
        if (width != null) {
            this.width = Integer.parseInt(width);
        }
    }

    public void setHeight(final String height) {
        if (height != null) {
            this.height = Integer.parseInt(height);
        }
    }

    public Image build() {
        return new Image(image, link, alt, description, width, height);
    }
}
