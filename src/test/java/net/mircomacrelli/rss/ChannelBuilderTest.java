package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Builder;
import org.junit.Test;

import static net.mircomacrelli.rss.Utils.PARSER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChannelBuilderTest extends BuilderBaseTestBase<Channel, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder(PARSER);
    }

    @Test(expected = ParserException.class)
    public void onlyOneTitle() throws ParserException {
        parse("<channel>" +
              "<title>first</title>" +
              "<title>second</title>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneLink() throws ParserException {
        parse("<channel>" +
              "<link>http://www.google.it</link>" +
              "<link>http://mircomacrelli.net</link>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void linkMustBeAValidUri() throws ParserException {
        parse("<channel>" +
              "<link>,**/    /---</link>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneDescription() throws ParserException {
        parse("<channel>" +
              "<description>first</description>" +
              "<description>second</description>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneLanguage() throws ParserException {
        parse("<channel>" +
              "<language>it</language>" +
              "<language>en</language>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneCopyright() throws ParserException {
        parse("<channel>" +
              "<copyright>http://www.google.it</copyright>" +
              "<copyright>http://www.google.it</copyright>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneEditor() throws ParserException {
        parse("<channel>" +
              "<managingEditor>fake@email.it</managingEditor>" +
              "<managingEditor>other@email.it</managingEditor>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneWebmaster() throws ParserException {
        parse("<channel>" +
              "<webMaster>fake@email.it</webMaster>" +
              "<webMaster>other@email.it</webMaster>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOnePubDate() throws ParserException {
        parse("<channel>" +
              "<pubDate>Fri, 06 May 1983 09:00:00 +0000</pubDate>" +
              "<pubDate>Sun, 29 Jan 2006 05:00:00 +0000</pubDate>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneTtl() throws ParserException {
        parse("<channel>" +
              "<ttl>60</ttl>" +
              "<ttl>30</ttl>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneLastBuild() throws ParserException {
        parse("<channel>" +
              "<lastBuildDate>Fri, 06 May 1983 09:00:00 +0000</lastBuildDate>" +
              "<lastBuildDate>Sun, 29 Jan 2006 05:00:00 +0000</lastBuildDate>" +
              "</channel>");
    }

    @Test
    public void moreThanOneCategory() throws Exception {
        final Builder builder = parse("<channel>" +
                                      "<title>titolo</title>" +
                                      "<link>http://www.google.it</link>" +
                                      "<description>descrizione</description>" +
                                      "<category domain=\"dmoz.org\">news/italian</category>" +
                                      "<category domain=\"dmoz.org\">news/sport</category>" +
                                      "</channel>");
        assertEquals(2, builder.build().getCategories().size());
    }

    @Test(expected = ParserException.class)
    public void onlyOneGenerator() throws ParserException {
        parse("<channel>" +
              "<generator>some program a</generator>" +
              "<generator>other v1.2</generator>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneDocs() throws ParserException {
        parse("<channel>" +
              "<docs>http://www.google.it</docs>" +
              "<docs>http://mircomacrelli.net</docs>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneClouds() throws ParserException {
        parse("<channel>" +
              "<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />" +
              "<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneImage() throws Exception {
        parse("<channel>" +
              "<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>" +
              "<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyTextInput() throws ParserException {
        parse("<channel>" +
              "<textInput>" +
              "<description>descrizione</description>" +
              "<link>http://www.google.it</link>" +
              "<name>nome</name>" +
              "<title>titolo</title>" +
              "</textInput>" +
              "<textInput>" +
              "<description>descrizione</description>" +
              "<link>http://www.google.it</link>" +
              "<name>nome</name>" +
              "<title>titolo</title>" +
              "</textInput>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneRating() throws Exception {
        parse("<channel>" +
              "<rating>rating a</rating>" +
              "<rating>rating b</rating>" +
              "</channel>");
    }

    @Test
    public void moreThanOneItem() throws Exception {
        final Builder builder = parse("<channel>" +
                                      "<title>titolo</title>" +
                                      "<link>http://www.google.it</link>" +
                                      "<description>descrizione</description>" +
                                      "<item>" +
                                      "<title>first</title>" +
                                      "<link>http://www.google.it</link>" +
                                      "</item>" +
                                      "<item>" +
                                      "<title>second</title>" +
                                      "<link>http://mircomacrelli.net</link>" +
                                      "</item>" +
                                      "</channel>");
        assertEquals(2, builder.build().getItems().size());
    }

    @Test(expected = ParserException.class)
    public void onlyOneSkipDays() throws ParserException {
        parse("<channel>" +
              "<skipDays><Day>Saturday</Day><Day>Sunday</Day></skipDays>" +
              "<skipDays><Day>Sunday</Day></skipDays>" +
              "</channel>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneSkipHours() throws ParserException {
        parse("<channel>" +
              "<skipHours><Hour>5</Hour></skipHours>" +
              "<skipHours><Hour>6</Hour><Hour>4</Hour></skipHours>" +
              "</channel>");
    }

    @Test

    public void channelWithExtensions() throws Exception {
        final Builder builder = parse("<channel " +
                                      "xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\" " +
                                      "xmlns:cc=\"http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html\">" +
                                      "<title>titolo</title>" +
                                      "<description>descrizione</description>" +
                                      "<link>http://mircomacrelli.net</link>" +
                                      "<cc:license>http://mircomacrelli.net/license</cc:license>" +
                                      "<sy:updatePeriod>weekly</sy:updatePeriod>" +
                                      "<sy:updateFrequency>1</sy:updateFrequency>" +
                                      "<sy:updateBase>2013-12-21T12:21:00+0000</sy:updateBase>" +
                                      "</channel>");
        assertEquals(2, builder.build().getModules().size());
    }

    @Test
    public void channelAllowCreativeCommonsModule() {
        assertTrue(new Builder(PARSER).getAllowedModules().contains(CreativeCommons.class));
    }

    @Test
    public void channelAllowSyndicationModule() {
        assertTrue(new Builder(PARSER).getAllowedModules().contains(Syndication.class));
    }
}
