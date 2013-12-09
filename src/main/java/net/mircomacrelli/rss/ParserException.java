package net.mircomacrelli.rss;

public class ParserException extends Exception {
    public ParserException(final Throwable cause) {
        super(cause);
    }

    public ParserException(final String message) {
        super(message);
    }
}
