package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Image.Builder;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class ImageBuilderTest extends BuilderBaseTestBase<Image, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = URISyntaxException.class)
    public void imageMustBeAValidUri() throws Exception {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>:/gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = URISyntaxException.class)
    public void linkMustBeAValidUri() throws Exception {
        parse("<image>" +
              "<link>,,, *htt///</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = NumberFormatException.class)
    public void widthMustBeANumber() throws Exception {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>http://www.google.it/logo.gif</url>" +
              "<description>descrizione</description>" +
              "<width>notanumber</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = NumberFormatException.class)
    public void heightMustBeANumber() throws Exception {
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
    public void validImage() throws Exception {
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
    public void widthIsOptional() throws Exception {
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
    public void heightIsOptional() throws Exception {
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
