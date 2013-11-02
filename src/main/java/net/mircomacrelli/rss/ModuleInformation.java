package net.mircomacrelli.rss;

import static java.lang.String.format;

enum ModuleInformation {
    CREATIVE_COMMONS("http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html", CreativeCommons.class, CreativeCommons.Builder.class),
    SYNDICATION("http://purl.org/rss/1.0/modules/syndication/", Syndication.class, Syndication.Builder.class);

    private final String uri;
    private final Class<? extends Module> module;
    private final Class<? extends ModuleBuilder> builder;

    ModuleInformation(final String uri, final Class<? extends Module> module,
                      final Class<? extends ModuleBuilder> builder) {
        this.uri = uri;
        this.module = module;
        this.builder = builder;
    }

    public static ModuleInformation fromUri(final String uri) {
        for (final ModuleInformation module : ModuleInformation.values()) {
            if (module.uri.equals(uri)) {
                return module;
            }
        }
        throw new IllegalArgumentException(format("no module found for the uri \"%s\"", uri));
    }

    public Class<? extends Module> getModule() {
        return module;
    }

    public Class<? extends ModuleBuilder> getBuilder() {
        return builder;
    }
}
