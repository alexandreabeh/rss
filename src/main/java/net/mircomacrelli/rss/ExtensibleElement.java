package net.mircomacrelli.rss;

import java.util.IdentityHashMap;
import java.util.Map;

abstract class ExtensibleElement {
    private final Map<Class<? extends Module>, Module> modules;

    ExtensibleElement() {
        modules = new IdentityHashMap<>();
    }

    public final boolean hasModule(final Class<? extends Module> clazz) {
        return modules.containsKey(clazz);
    }

    public final <T extends Module> T getModule(final Class<T> clazz) {
        return clazz.cast(modules.get(clazz));
    }

    final <T extends Module> void addModule(final Class<T> clazz, final Module module) {
        modules.put(clazz, module);
    }
}
