package net.mircomacrelli.rss;

import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

public class BuilderBaseTest {

    private static class Mock {   }

    private static class MockBuilder extends BuilderBase<Mock> {
        @Override
        void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            throw new RuntimeException("catch me!");
        }

        @Override
        Mock buildElement() throws ParserException {
            throw new RuntimeException("catch me!");
        }
    }

    @Test(expected = ParserException.class)
    public void parseCatchRuntimeException() throws ParserException {
        new MockBuilder().parse(null, null);
    }

    @Test(expected = ParserException.class)
    public void buildCatchRuntimeException() throws ParserException {
        new MockBuilder().build();
    }

}
