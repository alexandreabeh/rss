package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Syndication.Period;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SyndicationTest {
    private DateTime date;
    private int frequency;
    private Period period;

    @Before
    public void setup() {
        date = new DateTime(1382454319798L).withZone(DateTimeZone.UTC);
        frequency = 2;
        period = Period.DAILY;
    }

    @Test
    public void periodHourly() {
        assertEquals(Period.HOURLY, Period.from("hourly"));
    }

    @Test
    public void periodDaily() {
        assertEquals(Period.DAILY, Period.from("daily"));
    }

    @Test
    public void periodWeekly() {
        assertEquals(Period.WEEKLY, Period.from("weekly"));
    }

    @Test
    public void periodMonthly() {
        assertEquals(Period.MONTHLY, Period.from("monthly"));
    }

    @Test
    public void periodYearly() {
        assertEquals(Period.YEARLY, Period.from("yearly"));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Syndication.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test(expected = NullPointerException.class)
    public void periodIsRequired() {
        new Syndication(null, frequency, date);
    }

    @Test(expected = NullPointerException.class)
    public void frequencyIsRequired() {
        new Syndication(period, null, date);
    }

    @Test(expected = NullPointerException.class)
    public void baseIsRequired() {
        new Syndication(period, frequency, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void frequencyMustBePositive() {
        new Syndication(period, 0, date);
    }

    @Test
    public void testToString() {
        assertEquals("Syndication{period=DAILY, frequency=2, base='Tue, 22 Oct 2013 15:05:19 UTC'}",
                     new Syndication(period, frequency, date).toString());
    }
}
