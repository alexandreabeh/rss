package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Enclosure.Builder;
import org.junit.Test;

import javax.activation.MimeTypeParseException;
import java.net.MalformedURLException;

public class EnclosureBuilderTest extends BuilderBaseTestBase<Enclosure, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = MalformedURLException.class)
    public void urlMustBuValid() throws Exception {
        parse("<enclosure url=\" f  i*=?le.mp3\" length=\"12\" type=\"audio/mp3\"/>");
    }

    @Test(expected = NumberFormatException.class)
    public void lengthMustBeANumber() throws Exception {
        parse("<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"notanumber\" type=\"audio/mp3\"/>");
    }

    @Test(expected = MimeTypeParseException.class)
    public void typeMustBeAValidMimeType() throws Exception {
        parse("<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"12\" type=\"uhm/not/a/mime\"/>");
    }
}
