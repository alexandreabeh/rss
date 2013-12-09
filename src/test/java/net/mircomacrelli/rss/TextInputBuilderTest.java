package net.mircomacrelli.rss;

import net.mircomacrelli.rss.TextInput.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TextInputBuilderTest extends BuilderBaseTestBase<TextInput, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = ParserException.class)
    public void linkMustBeAValidUri() throws ParserException {
        parse("<textInput>" +
              "<description>descrizione</description>" +
              "<link>** http//..it</link>" +
              "<name>nome</name>" +
              "<title>titolo</title>" +
              "</textInput>");
    }

    @Test
    public void validTextInput() throws BuilderException, ParserException {
        final Builder builder = parse("<textInput>" +
                                      "<description>descrizione</description>" +
                                      "<link>http://www.google.it</link>" +
                                      "<name>nome</name>" +
                                      "<title>titolo</title>" +
                                      "</textInput>");
        assertNotNull(builder.build());
    }
}
