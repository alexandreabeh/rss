package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

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

    private static void requireModuleAnnotation(final Class<? extends Module> clazz) {
        for (final Class<?> iface : clazz.getInterfaces()) {
            if (iface.equals(Module.class)) {
                return;
            }
        }
        throw new IllegalArgumentException(format("the class %s does not implements the Module interface",
                                                  clazz.getSimpleName()));
    }

    @SafeVarargs
    protected static Set<Class<? extends Module>> allowedModules(final Class<? extends Module> module,
                                                                 final Class<? extends Module>... others) {
        requireNonNull(module);
        requireModuleAnnotation(module);

        final Set<Class<? extends Module>> set = new HashSet<>(1);
        set.add(module);

        if (others != null) {
            for (final Class<? extends Module> mod : others) {
                requireNonNull(mod);
                requireModuleAnnotation(mod);
                set.add(mod);
            }
        }

        return unmodifiableSet(set);
    }

    abstract Set<Class<? extends Module>> getAllowedModules();

    public final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws Exception {
        final String uri = element.getName().getNamespaceURI();
        final Class<? extends Module> clazz = getModuleFromURI(uri);

        // only process known modules
        if (clazz != null) {
            // check if this module can be here
            if (!getAllowedModules().contains(clazz)) {
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
