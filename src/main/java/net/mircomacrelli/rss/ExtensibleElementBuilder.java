package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.MalformedURLException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.format;
import static net.mircomacrelli.rss.Utils.isEndOfTag;

abstract class ExtensibleElementBuilder<T extends ExtensibleElement> extends BuilderBase<T> {
    private final Map<Class<? extends Module>, ModuleBuilder> modules;

    protected ExtensibleElementBuilder(final DateTimeFormatter parser) {
        super(parser);
        modules = new IdentityHashMap<>();
    }

    final <T extends ExtensibleElement> T extend(final T element) throws Exception {
        for (final Entry<Class<? extends Module>, ModuleBuilder> module : modules.entrySet()) {
            element.addModule(module.getKey(), module.getValue().build());
        }

        return element;
    }

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
                try {
                    builder = info.getBuilder().getConstructor(DateTimeFormatter.class).newInstance(parser);
                } catch (NoSuchMethodException ignored) {
                    builder = info.getBuilder().getConstructor().newInstance();
                }
                modules.put(module, builder);
            }

            builder.parse(reader, element);
        } catch (IllegalArgumentException ignored) {
            // ignore all the unknown modules
        }
    }

    abstract T buildElement();

    abstract void handleTag(XMLEventReader reader, StartElement element) throws Exception;

    @Override
    public final T build() throws Exception {
        return extend(buildElement());
    }

    @Override
    public final void parse(final XMLEventReader reader, final StartElement e) throws Exception {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();

            if (isEndOfTag(event, "item")) {
                break;
            }

            if (event.isStartElement()) {
                final StartElement el = event.asStartElement();

                // parse the extensions
                if (!el.getName().getPrefix().isEmpty()) {
                    passToModuleParser(reader, el);
                    continue;
                }

                handleTag(reader, el);
            }
        }
    }

    abstract Set<Class<? extends Module>> getAllowedModules();
}
