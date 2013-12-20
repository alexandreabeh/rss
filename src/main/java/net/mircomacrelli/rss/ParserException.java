package net.mircomacrelli.rss;

/**
 * Exception thrown when something unexpected happens during the parsing of the feed
 *
 * @author Mirco Macrelli
 * @version 2.1
 */
public class ParserException extends Exception {
    /**
     * When rethrowing an exception
     * @param cause the original exception
     */
    public ParserException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create an exception with a message
     * @param message the message associated with the exception
     */
    public ParserException(final String message) {
        super(message);
    }
}
