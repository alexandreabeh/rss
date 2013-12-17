package net.mircomacrelli.rss;

import org.junit.Test;

import static net.mircomacrelli.rss.Itunes.Explicit.CLEAN;
import static net.mircomacrelli.rss.Itunes.Explicit.NO;
import static net.mircomacrelli.rss.Itunes.Explicit.YES;
import static net.mircomacrelli.rss.Itunes.Explicit.from;
import static org.junit.Assert.assertEquals;

public class ItunesExplicitTest {

    @Test
    public void clean() {
        assertEquals(CLEAN, from("clean"));
    }

    @Test
    public void yes() {
        assertEquals(YES, from("yes"));
    }

    @Test
    public void noForAllOtherStrings() {
        assertEquals(NO, from("some other value"));
    }
}
