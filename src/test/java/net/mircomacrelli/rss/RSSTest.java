package net.mircomacrelli.rss;

import net.mircomacrelli.rss.RSS.Version;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class RSSTest {
    private Charset validCharset;
    private Charset otherCharset;
    private Version validVersion;
    private Channel validChannel;
    private RSS rss;

    @Before
    public void setup() throws URISyntaxException {
        validCharset = Charset.forName("UTF-8");
        otherCharset = Charset.forName("ISO-8859-1");
        validVersion = Version.RSS_2_0;
        validChannel = new Channel("titolo", new URI("http://mircomacrelli.net"), "Mirco Macrelli", null, null, null,
                                   null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        rss = new RSS(validCharset, validVersion, validChannel);
    }

    @Test(expected = NullPointerException.class)
    public void charsetIsRequired() {
        new RSS(null, validVersion, validChannel);
    }

    @Test(expected = NullPointerException.class)
    public void versionIsRequired() {
        new RSS(validCharset, null, validChannel);
    }

    @Test(expected = NullPointerException.class)
    public void channelIsRequired() {
        new RSS(validCharset, validVersion, null);
    }

    @Test
    public void charset() {
        assertSame(validCharset, rss.getCharset());
    }

    @Test
    public void version() {
        assertSame(validVersion, rss.getVersion());
    }

    @Test
    public void channel() {
        assertSame(validChannel, rss.getChannel());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(RSS.class).withPrefabValues(Charset.class, validCharset, otherCharset)
                      .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "RSS{version='2.0', charset=UTF-8, channel=Channel{title='titolo', link='http://mircomacrelli.net', description='Mirco Macrelli'}}",
                rss.toString());
    }

    @Test
    public void fromRss20() {
        assertEquals(Version.RSS_2_0, Version.from("2.0"));
    }

    @Test
    public void fromRss091() {
        assertEquals(Version.RSS_0_91, Version.from("0.91"));
    }

    @Test
    public void fromRss092() {
        assertEquals(Version.RSS_0_92, Version.from("0.92"));
    }

    @Test
    public void fromRss2IncompleteVersion() {
        assertEquals(Version.RSS_2_0, Version.from("2"));
    }
}
