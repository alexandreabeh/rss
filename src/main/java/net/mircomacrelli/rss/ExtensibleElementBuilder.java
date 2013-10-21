package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

abstract class ExtensibleElementBuilder {
    private final Map<Class<? extends Module>, ModuleBuilder> modules;

    ExtensibleElementBuilder() {
        modules = new IdentityHashMap<>();
    }

    final <T extends ExtensibleElement> T extend(final T element) {
        for (final Entry<Class<? extends Module>, ModuleBuilder> module : modules.entrySet()) {
            element.addModule(module.getKey(), module.getValue().build());
        }

        return element;
    }


    private static Class<? extends Module> getModuleFromURI(final String prefix) {
        switch (prefix) {
            case "http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html":
                return CreativeCommons.class;
            case "http://purl.org/rss/1.0/modules/syndication/":
                return Syndication.class;
            default:
                return null;
        }
    }

    private static Class<? extends ModuleBuilder> getBuilderForModule(final Class<? extends Module> clazz) {
        switch (clazz.getName()) {
            case "net.mircomacrelli.rss.Syndication":
                return Syndication.Builder.class;
            case "net.mircomacrelli.rss.CreativeCommons":
                return CreativeCommons.Builder.class;
            default:
                return null;
        }
    }

    public final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws Exception {
        final Class<? extends Module> clazz = getModuleFromURI(element.getName().getNamespaceURI());

        // only process known modules
        if (clazz != null) {
            ModuleBuilder builder = modules.get(clazz);
            if (builder == null) {
                builder = getBuilderForModule(clazz).newInstance();
                modules.put(clazz, builder);
            }

            builder.parse(reader, element);
        }
    }
}
