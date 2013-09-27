package net.mircomacrelli.rss;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.net.MalformedURLException;
import java.net.URL;

final class EnclosureBuilder {
    URL url;
    long length = -1;
    MimeType type;

    public void setUrl(final String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public void setLength(final String length) {
        this.length = Long.valueOf(length);
    }

    public void setType(final String type) throws MimeTypeParseException {
        this.type = new MimeType(type);
    }

    public Enclosure build() {
        return new Enclosure(url, length, type);
    }
}
