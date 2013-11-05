package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Builder;
import org.junit.Test;

import java.net.MalformedURLException;

import static net.mircomacrelli.rss.Utils.RFC822_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChannelBuilderTest extends BuilderBaseTestBase<Channel, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder(RFC822_DATE_FORMAT);
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneTitle() throws Exception {
        parse("<channel>" +
              "<title>first</title>" +
              "<title>second</title>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneLink() throws Exception {
        parse("<channel>" +
              "<link>http://www.google.it</link>" +
              "<link>http://mircomacrelli.net</link>" +
              "</channel>");
    }

    @Test(expected = MalformedURLException.class)
    public void linkMustBeAValidUrl() throws Exception {
        parse("<channel>" +
              "<link>---///---</link>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneDescription() throws Exception {
        parse("<channel>" +
              "<description>first</description>" +
              "<description>second</description>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneLanguage() throws Exception {
        parse("<channel>" +
              "<language>it</language>" +
              "<language>en</language>" +
              "</channel>");
    }

    @Test
    public void languageMustBeValid() throws Exception {
        parse("<channel>" +
              "<language>nonsochecosasia</language>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneCopyright() throws Exception {
        parse("<channel>" +
              "<copyright>http://www.google.it</copyright>" +
              "<copyright>http://www.google.it</copyright>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneEditor() throws Exception {
        parse("<channel>" +
              "<managingEditor>fake@email.it</managingEditor>" +
              "<managingEditor>other@email.it</managingEditor>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneWebmaster() throws Exception {
        parse("<channel>" +
              "<webMaster>fake@email.it</webMaster>" +
              "<webMaster>other@email.it</webMaster>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOnePubDate() throws Exception {
        parse("<channel>" +
              "<pubDate>Fri, 06 May 1983 09:00:00 +0000</pubDate>" +
              "<pubDate>Sun, 29 Jan 2006 05:00:00 +0000</pubDate>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneTtl() throws Exception {
        parse("<channel>" +
              "<ttl>60</ttl>" +
              "<ttl>30</ttl>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneLastBuild() throws Exception {
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

    @Test(expected = IllegalStateException.class)
    public void onlyOneGenerator() throws Exception {
        parse("<channel>" +
              "<generator>some program a</generator>" +
              "<generator>other v1.2</generator>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneDocs() throws Exception {
        parse("<channel>" +
              "<docs>http://www.google.it</docs>" +
              "<docs>http://mircomacrelli.net</docs>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneClouds() throws Exception {
        parse("<channel>" +
              "<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />" +
              "<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
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

    @Test(expected = IllegalStateException.class)
    public void onlyTextInput() throws Exception {
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

    @Test(expected = IllegalStateException.class)
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

    @Test(expected = IllegalStateException.class)
    public void onlyOneSkipDays() throws Exception {
        parse("<channel>" +
              "<skipDays><Day>Saturday</Day></skipDays>" +
              "<skipDays><Day>Sunday</Day></skipDays>" +
              "</channel>");
    }

    @Test(expected = IllegalStateException.class)
    public void onlyOneSkipHours() throws Exception {
        parse("<channel>" +
              "<skipHours><Hour>5</Hour></skipHours>" +
              "<skipHours><Hour>6</Hour></skipHours>" +
              "</channel>");
    }

    @Test
    public void channelAllowCreativeCommonsModule() {
        assertTrue(new Builder(RFC822_DATE_FORMAT).getAllowedModules().contains(CreativeCommons.class));
    }

    @Test
    public void channelAllowSyndicationModule() {
        assertTrue(new Builder(RFC822_DATE_FORMAT).getAllowedModules().contains(Syndication.class));
    }
}
