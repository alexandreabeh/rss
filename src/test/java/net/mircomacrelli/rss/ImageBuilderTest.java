package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Image.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ImageBuilderTest extends BuilderBaseTestBase<Image, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = ParserException.class)
    public void imageMustBeAValidUri() throws ParserException {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>:/gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = ParserException.class)
    public void linkMustBeAValidUri() throws ParserException {
        parse("<image>" +
              "<link>,,, *htt///</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = ParserException.class)
    public void widthMustBeANumber() throws ParserException {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>notanumber</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = ParserException.class)
    public void heightMustBeANumber() throws ParserException {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>notanumber</height>" +
              "</image>");
    }

    @Test
    public void validImage() throws ParserException {
        final Builder builder = parse("<image>" +
                                      "<link>http://www.google.it</link>" +
                                      "<title>titolo</title>" +
                                      "<url>http://www.google.it/logo.gif</url>" +
                                      "<description>descrizione</description>" +
                                      "<width>90</width>" +
                                      "<height>90</height>" +
                                      "</image>");
        assertNotNull(builder.build());
    }

    @Test
    public void widthIsOptional() throws ParserException {
        final Builder builder = parse("<image>" +
                                      "<link>http://www.google.it</link>" +
                                      "<title>titolo</title>" +
                                      "<url>http://www.google.it/logo.gif</url>" +
                                      "<description>descrizione</description>" +
                                      "<height>90</height>" +
                                      "</image>");
        assertNotNull(builder.build());
    }

    @Test
    public void heightIsOptional() throws ParserException {
        final Builder builder = parse("<image>" +
                                      "<link>http://www.google.it</link>" +
                                      "<title>titolo</title>" +
                                      "<url>http://www.google.it/logo.gif</url>" +
                                      "<description>descrizione</description>" +
                                      "<width>90</width>" +
                                      "</image>");
        assertNotNull(builder.build());
    }
}
