package net.mircomacrelli.rss;

import java.util.IdentityHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

abstract class ExtensibleElement {
    private final Map<Class<? extends Module>, Module> modules;

    protected ExtensibleElement() {
        modules = new IdentityHashMap<>();
    }

    public final boolean hasModule(final Class<? extends Module> clazz) {
        return modules.containsKey(requireNonNull(clazz));
    }

    public final <T extends Module> T getModule(final Class<T> clazz) {
        return clazz.cast(modules.get(requireNonNull(clazz)));
    }

    final <T extends Module> void addModule(final Class<T> clazz, final Module module) {
        modules.put(requireNonNull(clazz), requireNonNull(module));
    }

    final Map<Class<? extends Module>, Module> getModules() {
        return unmodifiableMap(modules);
    }
}
