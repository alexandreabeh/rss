package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

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

abstract class ExtensibleElementBuilder extends BuilderBase {
    private final Map<Class<? extends Module>, ModuleBuilder> modules;

    protected ExtensibleElementBuilder(final DateTimeFormatter parser) {
        super(parser);
        modules = new IdentityHashMap<>();
    }

    final <T extends ExtensibleElement> T extend(final T element) {
        for (final Entry<Class<? extends Module>, ModuleBuilder> module : modules.entrySet()) {
            element.addModule(module.getKey(), module.getValue().build());
        }

        return element;
    }

    abstract Set<Class<? extends Module>> getAllowedModules();

    public final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws Exception {
        try {
            final ModuleInformation info = ModuleInformation.fromUri(element.getName().getNamespaceURI());
            final Class<? extends Module> module = info.getModule();

            // check if this module can be here
            if (!getAllowedModules().contains(module)) {
                throw new IllegalStateException(format("the module %s can't be here", module));
            }

            ModuleBuilder builder = modules.get(module);
            if (builder == null) {
                builder = info.getBuilder().getConstructor(DateTimeFormatter.class).newInstance(parser);
                modules.put(module, builder);
            }

            builder.parse(reader, element);
        } catch (IllegalArgumentException ignored) {
            // ignore all the unknown modules
        }
    }
}
