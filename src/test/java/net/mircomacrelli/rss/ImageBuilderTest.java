package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Image.Builder;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;

public class ImageBuilderTest extends BuilderBaseTestBase<Image, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = MalformedURLException.class)
    public void imageMustBeAValidUrl() throws Exception {
        parse("<image>" +
              "<link>http://www.google.it</link>" +
              "<title>titolo</title>" +
              "<url>:/gif</url>" +
              "<description>descrizione</description>" +
              "<width>90</width>" +
              "<height>90</height>" +
              "</image>");
    }

    @Test(expected = MalformedURLException.class)
    public void linkMustBeAValidUrl() throws Exception {
        parse("<image>" +
              "<link>htt///</link>" +
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
}
