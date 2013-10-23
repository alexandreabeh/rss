package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

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

    private static Class<? extends Module> getModuleFromURI(final String uri) {
        switch (uri) {
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
        }
        throw new AssertionError("missing module builder");
    }

    abstract boolean canContainModule(Class<? extends Module> clazz);

    public final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws Exception {
        final String uri = element.getName().getNamespaceURI();
        final Class<? extends Module> clazz = getModuleFromURI(uri);

        // only process known modules
        if (clazz != null) {
            // check if this module can be here
            if (!canContainModule(clazz)) {
                throw new IllegalStateException(format("the module %s can't be here", clazz.getSimpleName()));
            }

            ModuleBuilder builder = modules.get(clazz);
            if (builder == null) {
                builder = (ModuleBuilder)getBuilderForModule(clazz).getDeclaredConstructors()[0].newInstance();
                modules.put(clazz, builder);
            }

            builder.parse(reader, element);
        }
    }
}
