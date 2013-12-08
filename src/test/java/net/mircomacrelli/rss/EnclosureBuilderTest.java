package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Enclosure.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class EnclosureBuilderTest extends BuilderBaseTestBase<Enclosure, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = ParserException.class)
    public void urlMustBuValid() throws ParserException {
        parse("<enclosure url=\" f  i*=?le.mp3\" length=\"12\" type=\"audio/mp3\"/>");
    }

    @Test(expected = ParserException.class)
    public void lengthMustBeANumber() throws ParserException {
        parse("<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"notanumber\" type=\"audio/mp3\"/>");
    }

    @Test(expected = ParserException.class)
    public void typeMustBeAValidMimeType() throws ParserException {
        parse("<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"12\" type=\"uhm/not/a/mime\"/>");
    }

    @Test
    public void validEnclosure() throws Exception {
        final Builder builder = parse(
                "<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"12\" type=\"audio/mp3\"/>");
        assertNotNull(builder.build());
    }
}
