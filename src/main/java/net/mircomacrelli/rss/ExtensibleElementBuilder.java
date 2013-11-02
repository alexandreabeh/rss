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

    private static void requireModuleAnnotation(final Class<? extends Module> module) {
        for (final Class<?> clazz : module.getInterfaces()) {
            if (clazz.equals(Module.class)) {
                return;
            }
        }
        throw new IllegalArgumentException(format("the class %s does not implements the Module interface",
                                                  module.getSimpleName()));
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
        try {
            final ModuleInformation info = ModuleInformation.fromUri(element.getName().getNamespaceURI());
            final Class<? extends Module> module = info.getModule();

            // check if this module can be here
            if (!getAllowedModules().contains(module)) {
                throw new IllegalStateException(format("the module %s can't be here", module));
            }

            ModuleBuilder builder = modules.get(module);
            if (builder == null) {
                builder = (ModuleBuilder)info.getBuilder().getDeclaredConstructors()[0].newInstance();
                modules.put(module, builder);
            }

            builder.parse(reader, element);
        } catch (IllegalArgumentException ignored) {
            // ignore all the unknown modules
        }
    }
}
