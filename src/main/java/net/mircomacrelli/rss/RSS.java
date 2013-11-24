package net.mircomacrelli.rss;

import java.nio.charset.Charset;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * A RSS feed
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class RSS {
    private final Charset charset;
    private final Version version;
    private final Channel channel;

    /**
     * Creates a new RSS feed
     *
     * @param charset the charset used
     * @param version the version of RSS used
     * @param channel the channel contained
     */
    public RSS(final Charset charset, final Version version, final Channel channel) {
        this.charset = requireNonNull(charset);
        this.version = requireNonNull(version);
        this.channel = requireNonNull(channel);
    }

    /** @return the charset encoding used in the feed */
    public Charset getCharset() {
        return charset;
    }

    /** @return the version of RSS used by this feed */
    public Version getVersion() {
        return version;
    }

    /** @return the channel contained in this RSS feed */
    public Channel getChannel() {
        return channel;
    }

    @Override
    public int hashCode() {
        return hash(charset, version, channel);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RSS)) {
            return false;
        }

        final RSS other = (RSS)obj;
        return channel.equals(other.channel) && (version == other.version) && charset.equals(other.charset);
    }

    @Override
    public String toString() {
        return format("RSS{version='%s', charset=%s, channel=%s}", version, charset, channel);
    }

    /** RSS Version */
    public enum Version {
        /** Version 0.91 */
        RSS_0_91("0.91"),
        /** Version 0.92 */
        RSS_0_92("0.92"),
        /** Version 2.0.1 */
        RSS_2_0("2.0");
        private final String version;

        Version(final String version) {
            this.version = version;
        }

        /**
         * Get the version from the version number
         *
         * @param text the text with the version number
         * @return the version
         */
        public static Version from(final String text) {
            if (text.equals("2")) {
                return RSS_2_0;
            }
            return valueOf("RSS_" + text.replace('.', '_'));
        }

        @Override
        public String toString() {
            return version;
        }
    }
}
