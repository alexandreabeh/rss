# RSS
[![Build Status](https://travis-ci.org/mircomacrelli/rss.png?branch=master)](https://travis-ci.org/mircomacrelli/rss)
[![Coverage Status](https://coveralls.io/repos/mircomacrelli/rss/badge.png?branch=master)](https://coveralls.io/r/mircomacrelli/rss?branch=master)

## Rationale
Recently I needed a simple way to parse a lot (a few hundred thousand), of small RSS feeds from disk. I didn't like the way all the alternatives worked so I wrote my own library.

## How to compile
I used maven so, to compile the library from the source, just use the command `mvn package` and after a few seconds you'll have the `jar` in the directory `target/`.

## How to use it
Here's a small example of how to use the library to print all the title from a feed on the internet.

```java
package net.mircomacrelli.rss.example;

import net.mircomacrelli.rss.Item;
import net.mircomacrelli.rss.RSS;
import net.mircomacrelli.rss.RSSFactory;

import java.net.URL;
import java.net.URLConnection;

public class Example {
    public static void main(String... args) throws Exception {
        // create a new factory
        RSSFactory factory = RSSFactory.newFactory();

        // open a connection to the rss feed
        URL url = new URL("http://www.ansa.it/web/notizie/rubriche/spettacolo/spettacolo_rss.xml");
        URLConnection conn = url.openConnection();

        // parse the feed
        RSS feed = factory.parse(conn.getInputStream());

        // print all the item title
        for (Item item : feed.getChannel().getItems()) {
            System.out.println(item.getTitle());
        }
    }
}
```

## License
This is distributed under the MIT license. For the full text of the license see the [LICENSE](LICENSE) file.
