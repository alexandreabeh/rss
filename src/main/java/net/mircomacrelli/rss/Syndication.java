package net.mircomacrelli.rss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.util.Locale;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.canBeWrittenOnlyOnce;
import static net.mircomacrelli.rss.Utils.formatDate;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseDate;

/**
 * Implementation of the Syndication Module
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class Syndication implements Module {
    /** Type of periods */
    public enum Period {
        /** Hourly */
        HOURLY,
        /** Daily */
        DAILY,
        /** Weekly */
        WEEKLY,
        /** Monthly */
        MONTHLY,
        /** Yearly */
        YEARLY;

        /**
         * Parse a string and return the corresponding Period
         *
         * @param value the string to parse
         * @return the Period
         */
        public static Period from(final String value) {
            return valueOf(value.toUpperCase(Locale.ENGLISH));
        }
    }

    private final Period period;

    /** @return the period to used with frequency */
    public Period getPeriod() {
        return period;
    }

    private final int frequency;

    /** @return a positive number used with period to know when a feed should be checked */
    public int getFrequency() {
        return frequency;
    }

    private final DateTime base;

    /** @return the date and time that should be used as a base for update */
    public DateTime getBase() {
        return base;
    }

    @Override
    public String toString() {
        return format("Syndication{period=%s, frequency=%d, base='%s'}", period, frequency, formatDate(base));
    }

    @Override
    public int hashCode() {
        return hash(period, frequency, base);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Syndication)) {
            return false;
        }

        final Syndication other = (Syndication)obj;
        return (period == other.period) && (frequency == other.frequency) && base.equals(other.base);
    }

    Syndication(final Period period, final Integer frequency, final DateTime base) {
        requireNonNull(period);
        requireNonNull(base);
        requireNonNull(frequency);
        frequencyInvariant(frequency);

        this.period = period;
        this.frequency = frequency;
        this.base = base;
    }

    private static void frequencyInvariant(final int frequency) {
        if (frequency < 1) {
            throw new IllegalArgumentException(format("frequency must be positive. was %d", frequency));
        }
    }

    static final class Builder extends ModuleBuilder {
        Period period;
        DateTime base;
        Integer frequency;

        public Builder(final DateTimeFormatter parser) {
            super(ISODateTimeFormat.dateTimeParser());
        }

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws IllegalArgumentException,
                                                                                          XMLStreamException {
            final String name = element.getName().getLocalPart();

            switch (name) {
                case "updatePeriod":
                    canBeWrittenOnlyOnce(period);
                    period = Period.from(getText(reader));
                    break;
                case "updateFrequency":
                    canBeWrittenOnlyOnce(frequency);
                    frequency = Integer.parseInt(getText(reader));
                    break;
                case "updateBase":
                    canBeWrittenOnlyOnce(base);
                    base = parseDate(getText(reader), parser);
                    break;
            }
        }

        @Override
        public Module build() {
            return new Syndication(period, frequency, base);
        }
    }
}
