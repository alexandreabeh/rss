package net.mircomacrelli.rss;

import net.mircomacrelli.rss.RSS.Version;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static net.mircomacrelli.rss.RSSFactory.newFactory;
import static net.mircomacrelli.rss.Utils.PARSER;
import static org.junit.Assert.assertEquals;

public class RSSFactoryTest {
    @Test
    public void normalFactoryUsesTheStandardParser() {
        assertEquals(PARSER, newFactory().getDateTimeFormatter());
    }

    @Test
    public void factoryCanUseACustomParser() {
        final DateTimeFormatter custom = ISODateTimeFormat.dateTimeParser();
        assertEquals(custom, newFactory(custom).getDateTimeFormatter());
    }

    @Test(expected = IllegalStateException.class)
    public void oneChannelIsRequired() throws Exception {
        newFactory().parse(toInputStream("<rss version=\"2.0\"></rss>"));
    }

    @Test
    public void onlyTheFirstChannelIsParsed() throws Exception {
        final Channel channel = newFactory().parse(toInputStream("<rss version=\"2.0\">" +
                                                                 "<channel>" +
                                                                 "<title>first</title>" +
                                                                 "<link>http://mircomacrelli.net</link>" +
                                                                 "<description>desc</description>" +
                                                                 "</channel>" +
                                                                 "<channel>" +
                                                                 "<title>second</title>" +
                                                                 "<link>http://mircomacrelli.net</link>" +
                                                                 "<description>desc</description>" +
                                                                 "</channel>" +
                                                                 "</rss>")).getChannel();
        assertEquals("first", channel.getTitle());
    }

    @Test
    public void customEncoding() throws Exception {
        final RSS feed = newFactory().parse(toInputStream("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                                                          "<rss version=\"2.0\">" +
                                                          "<channel>" +
                                                          "<title>first</title>" +
                                                          "<link>http://mircomacrelli.net</link>" +
                                                          "<description>desc</description>" +
                                                          "</channel>" +
                                                          "</rss>"));
        assertEquals(Charset.forName("ISO-8859-1"), feed.getCharset());
    }

    @Test
    public void withoutEncodingUTF8IsUsed() throws Exception {
        final RSS feed = newFactory().parse(toInputStream("<rss version=\"2.0\">" +
                                                          "<channel>" +
                                                          "<title>first</title>" +
                                                          "<link>http://mircomacrelli.net</link>" +
                                                          "<description>desc</description>" +
                                                          "</channel>" +
                                                          "</rss>"));
        assertEquals(Charset.forName("UTF-8"), feed.getCharset());
    }

    @Test
    public void rssVersion() throws Exception {
        final RSS feed = newFactory().parse(toInputStream("<rss version=\"2.0\">" +
                                                          "<channel>" +
                                                          "<title>first</title>" +
                                                          "<link>http://mircomacrelli.net</link>" +
                                                          "<description>desc</description>" +
                                                          "</channel>" +
                                                          "</rss>"));
        assertEquals(Version.RSS_2_0, feed.getVersion());
    }

    @Test(expected = IllegalStateException.class)
    public void rssMustBeTheRoot() throws Exception {
        newFactory().parse(toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed><!-- other tags --></feed>"));
    }

    @Test
    public void findRssEvenWithComments() throws Exception {
        final RSS feed = newFactory().parse(toInputStream("<!-- comments --><!-- other comments -->" +
                                                          "<rss version=\"2.0\">" +
                                                          "<channel>" +
                                                          "<title>first</title>" +
                                                          "<link>http://mircomacrelli.net</link>" +
                                                          "<description>desc</description>" +
                                                          "</channel>" +
                                                          "</rss>"));
        assertEquals("first", feed.getChannel().getTitle());
    }

    @Test(expected = IllegalStateException.class)
    public void rssCanContainOnlyChannel() throws Exception {
        newFactory().parse(toInputStream("<rss version=\"2.0\">" +
                                         "<tag>" +
                                         "<!-- some other content -->" +
                                         "</tag>" +
                                         "</rss>"));
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static InputStream toInputStream(final String arg) {
        return new ByteArrayInputStream(arg.getBytes(UTF8));
    }
}
