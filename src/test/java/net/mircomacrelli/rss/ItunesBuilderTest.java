package net.mircomacrelli.rss;

import org.junit.Before;
import org.junit.Test;

import static net.mircomacrelli.rss.Itunes.Builder;
import static org.junit.Assert.assertEquals;

public class ItunesBuilderTest extends ModuleBuilderTestBase {
    @Before
    public void setup() {
        uri = "http://www.itunes.com/dtds/podcast-1.0.dtd";
        prefix = "itunes";
    }

    @Test(expected = ParserException.class)
    public void authorCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:author>primo</<itunes:author>");
        step(builder, "<itunes:author>secondo</<itunes:author>");
    }

    @Test(expected = ParserException.class)
    public void blockCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:block>yes</<itunes:block>");
        step(builder, "<itunes:block>yes</<itunes:block>");
    }

    @Test(expected = ParserException.class)
    public void imageCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:image href=\"http://mircomacrelli.net/immagine.png\" />");
        step(builder, "<itunes:image href=\"http://mircomacrelli.net/immagine.png\" />");
    }

    @Test(expected = ParserException.class)
    public void ccCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:isClosedCaptioned>yes</<itunes:isClosedCaptioned>");
        step(builder, "<itunes:isClosedCaptioned>no</<itunes:isClosedCaptioned>");
    }

    @Test(expected = ParserException.class)
    public void summaryCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:summary>first</<itunes:summary>");
        step(builder, "<itunes:summary>second</<itunes:summary>");
    }

    @Test(expected = ParserException.class)
    public void subtitleCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:subtitle>first</<itunes:subtitle>");
        step(builder, "<itunes:subtitle>second</<itunes:subtitle>");
    }

    @Test(expected = ParserException.class)
    public void newFeedUrlCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:new-feed-url>http://www.google.it/feed.xml</<itunes:new-feed-url>");
        step(builder, "<itunes:new-feed-url>http://www.google.it/feed.xml</<itunes:new-feed-url>");
    }

    @Test(expected = ParserException.class)
    public void orderCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:order>1</<itunes:order>");
        step(builder, "<itunes:order>2</<itunes:order>");
    }

    @Test(expected = ParserException.class)
    public void completeCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:complete>yes</<itunes:complete>");
        step(builder, "<itunes:complete>no</<itunes:complete>");
    }

    @Test(expected = ParserException.class)
    public void ownerCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:owner>" +
                      "<itunes:name>owner</itunes:name>" +
                      "<itunes:email>email@domain.com</itunes:email>" +
                      "</itunes:owner>");
        step(builder, "<itunes:owner>" +
                      "<itunes:name>owner</itunes:name>" +
                      "<itunes:email>email@domain.com</itunes:email>" +
                      "</itunes:owner>");
    }

    @Test(expected = ParserException.class)
    public void explicitCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:explicit>yes</<itunes:explicit>");
        step(builder, "<itunes:explicit>no</<itunes:explicit>");
    }

    @Test(expected = ParserException.class)
    public void durationCantBeRepeated() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:duration>12:34</<itunes:duration>");
        step(builder, "<itunes:duration>2:15</<itunes:duration>");
    }

    @Test(expected = ParserException.class)
    public void ownerEmailMustBeValid() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:owner>" +
                      "<itunes:name>owner</itunes:name>" +
                      "<itunes:email>- .......domai-</itunes:email>" +
                      "</itunes:owner>");
    }

    @Test
    public void categories() throws ParserException {
        final Builder builder = new Builder();
        step(builder, "<itunes:category text=\"Society &amp; Culture\">" +
                      "<itunes:category text=\"History\" />" +
                      "</itunes:category>");
        step(builder, "<itunes:category text=\"Technology\">" +
                      "<itunes:category text=\"Gadgets\" />" +
                      "</itunes:category>");
        assertEquals(2, ((Itunes)builder.build()).getCategories().size());
    }
}
