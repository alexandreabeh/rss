package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.format;
import static net.mircomacrelli.rss.Utils.isEndOfTag;

abstract class ExtensibleElementBuilder<T extends ExtensibleElement> extends BuilderBase<T> {
    private final Map<Class<? extends Module>, ModuleBuilder> modules;

    protected ExtensibleElementBuilder(final String tagName, final DateTimeFormatter parser) {
        super(parser);
        this.tagName = tagName;
        modules = new IdentityHashMap<>();
    }

    final <T extends ExtensibleElement> T extend(final T element) throws Exception {
        for (final Entry<Class<? extends Module>, ModuleBuilder> module : modules.entrySet()) {
            element.addModule(module.getKey(), module.getValue().build());
        }

        return element;
    }

    public final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws Exception {
        final ModuleInformation info = ModuleInformation.fromUri(element.getName().getNamespaceURI());
        if (info == null) {
            return; // ignore all the unknown modules
        }

        final Class<? extends Module> module = info.getModule();

        // check if this module can be here
        if (!getAllowedModules().contains(module)) {
            throw new IllegalStateException(format("the module %s can't be here", module));
        }

        ModuleBuilder builder = modules.get(module);
        if (builder == null) {
            try {
                builder = info.getBuilder().getConstructor(DateTimeFormatter.class).newInstance(parser);
            } catch (NoSuchMethodException ignored) {
                builder = info.getBuilder().getConstructor().newInstance();
            }
            modules.put(module, builder);
        }

        builder.parse(reader, element);
    }

    final String tagName;

    abstract T buildElement();

    abstract void handleTag(XMLEventReader reader, StartElement element) throws Exception;

    @Override
    public final T build() throws Exception {
        return extend(buildElement());
    }

    @Override
    public final void parse(final XMLEventReader reader, final StartElement element) throws Exception {
        while (true) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, tagName)) {
                break;
            }

            if (event.isStartElement()) {
                handleEvent(reader, event);
            }
        }
    }

    private void handleEvent(final XMLEventReader reader, final XMLEvent event) throws Exception {
        final StartElement element = event.asStartElement();

        if (!element.getName().getPrefix().isEmpty()) {
            // parse the extensions
            passToModuleParser(reader, element);
        } else {
            handleTag(reader, element);
        }
    }

    abstract Set<Class<? extends Module>> getAllowedModules();
}
