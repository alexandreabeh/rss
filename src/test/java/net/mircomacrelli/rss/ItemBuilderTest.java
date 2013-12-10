package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Item.Builder;
import org.junit.Test;

import static net.mircomacrelli.rss.Utils.PARSER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemBuilderTest extends BuilderBaseTestBase<Item, Builder> {

    @Test(expected = ParserException.class)
    public void onlyOneTitle() throws ParserException {
        parse("<item>" +
              "<title>first</title>" +
              "<title>second</title>" +
              "</item>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneLink() throws ParserException {
        parse("<item>" +
              "<link>http://mircomacrelli.net</link>" +
              "<link>http://mircomacrelli.net</link>" +
              "</item>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneDescription() throws ParserException {
        parse("<item>" +
              "<description>first</description>" +
              "<description>second</description>" +
              "</item>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneAuthor() throws ParserException {
        parse("<item>" +
              "<author>first</author>" +
              "<author>second</author>" +
              "</item>");
    }

    @Test
    public void moreThanOneCategory() throws ParserException {
        final Builder builder = parse("<item>" +
                                      "<title>Titolo</title>" +
                                      "<link>http://www.google.it</link>" +
                                      "<category domain=\"dmoz.org\">news/italian</category>" +
                                      "<category domain=\"dmoz.org\">news/sport</category>" +
                                      "</item>");
        assertEquals(2, builder.build().getCategories().size());
    }

    @Test(expected = ParserException.class)
    public void onlyOneCommentLink() throws ParserException {
        parse("<item>" +
              "<comments>http://mircomacrelli.net</comments>" +
              "<comments>http://www.google.it</comments>" +
              "</item>");
    }

    @Test
    public void moreThanOneEnclosure() throws ParserException {
        final Builder builder = parse("<item>" +
                                      "<title>Titolo</title>" +
                                      "<link>http://www.google.it</link>" +
                                      "<enclosure url=\"http://mircomacrelli.net/file.mp3\" length=\"12\" type=\"audio/mp3\"/>" +
                                      "<enclosure url=\"http://mircomacrelli.net/other.mp3\" length=\"21\" type=\"audio/mp3\"/>" +
                                      "</item>");
        assertEquals(2, builder.build().getEnclosures().size());
    }

    @Test(expected = ParserException.class)
    public void onlyOneGuid() throws ParserException {
        parse("<item>" +
              "<guid>http://mircomacrelli.net/abcde</guid>" +
              "<guid>http://www.google.it/123</guid>" +
              "</item>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneDate() throws ParserException {
        parse("<item>" +
              "<pubDate>Sun, 29 Jan 2006 17:17:44 +0000</pubDate>" +
              "<pubDate>Sun, 29 Jan 2006 05:00:00 +0000</pubDate>" +
              "</item>");
    }

    @Test(expected = ParserException.class)
    public void onlyOneSource() throws ParserException {
        parse("<item>" +
              "<source url=\"http://www.google.it\">google</source>" +
              "<source url=\"http://mircomacrelli.net\">mircomacrelli</source>" +
              "</item>");
    }

    @Override
    Builder newBuilder() {
        return new Builder(PARSER);
    }

    @Test
    public void itemAllowCreativeCommonsModule() {
        assertTrue(new Builder(PARSER).isModuleAllowed(CreativeCommons.class));
    }
}
