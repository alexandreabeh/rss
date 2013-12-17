package net.mircomacrelli.rss;

import java.util.HashMap;
import java.util.Map;

enum ModuleInformation {
    CREATIVE_COMMONS("http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html", CreativeCommons.class,
                     CreativeCommons.Builder.class),
    SYNDICATION("http://purl.org/rss/1.0/modules/syndication/", Syndication.class, Syndication.Builder.class),
    ITUNES("http://www.itunes.com/dtds/podcast-1.0.dtd", Itunes.class, Itunes.Builder.class);


    private final String uri;
    private final Class<? extends Module> module;
    private final Class<? extends ModuleBuilder> builder;

    ModuleInformation(final String uri, final Class<? extends Module> module,
                      final Class<? extends ModuleBuilder> builder) {
        this.uri = uri;
        this.module = module;
        this.builder = builder;
    }

    private static final Map<String, ModuleInformation> MODULES;

    static {
        MODULES = new HashMap<>(2);
        for (final ModuleInformation module : ModuleInformation.values()) {
            MODULES.put(module.uri, module);
        }
    }

    public static ModuleInformation fromUri(final String uri) {
        return MODULES.get(uri);
    }

    public Class<? extends Module> getModule() {
        return module;
    }

    public Class<? extends ModuleBuilder> getBuilder() {
        return builder;
    }
}
