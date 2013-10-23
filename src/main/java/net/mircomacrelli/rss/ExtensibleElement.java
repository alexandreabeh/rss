package net.mircomacrelli.rss;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

abstract class ExtensibleElement {
    private final Map<Class<? extends Module>, Module> modules;

    ExtensibleElement() {
        modules = new IdentityHashMap<>();
    }

    public final boolean hasModule(final Class<? extends Module> clazz) {
        requireNonNull(clazz);
        return modules.containsKey(clazz);
    }

    public final <T extends Module> T getModule(final Class<T> clazz) {
        requireNonNull(clazz);
        return clazz.cast(modules.get(clazz));
    }

    final <T extends Module> void addModule(final Class<T> clazz, final Module module) {
        requireNonNull(clazz);
        requireNonNull(module);
        modules.put(clazz, module);
    }
}
