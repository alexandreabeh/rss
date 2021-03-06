package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.format;
import static net.mircomacrelli.rss.Utils.isEndOfTag;

abstract class ExtensibleElementBuilder<T extends ExtensibleElement> extends BuilderBase<T> {
    private final Map<Class<? extends Module>, ModuleBuilder> modules;

    ExtensibleElementBuilder(final String tagName, final DateTimeFormatter parser) {
        super(parser);
        this.tagName = tagName;
        modules = new IdentityHashMap<>();
    }

    private <T extends ExtensibleElement> T extend(final T element) throws ParserException {
        for (final Entry<Class<? extends Module>, ModuleBuilder> module : modules.entrySet()) {
            element.addModule(module.getKey(), module.getValue().build());
        }

        return element;
    }

    final void passToModuleParser(final XMLEventReader reader, final StartElement element) throws ParserException {
        final ModuleInformation info = ModuleInformation.fromUri(element.getName().getNamespaceURI());
        if (info == null) {
            return; // ignore all the unknown modules
        }

        final Class<? extends Module> module = info.getModule();

        // check if this module can be here
        if (!isModuleAllowed(module)) {
            throw new IllegalStateException(format("the module %s can't be here", module));
        }

        ModuleBuilder builder = modules.get(module);
        if (builder == null) {
            final Class<? extends ModuleBuilder> builderClass = info.getBuilder();
            final Constructor<?>[] constructors = builderClass.getConstructors();

            if (constructors.length != 1) {
                throw new AssertionError("a builder must have exactly one constructor");
            }

            final Constructor<?> constructor = constructors[0];
            final Class<?>[] parameters = constructor.getParameterTypes();

            try {
                if (parameters.length == 0) {
                    // normal constructor without parameter
                    builder = (ModuleBuilder)constructor.newInstance();
                } else {
                    // constructor with DateTimeFormatter;
                    builder = (ModuleBuilder)constructor.newInstance(parser);
                }
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError("error while calling the constructor", e);
            }

            modules.put(module, builder);
        }

        final String tag = element.getName().getLocalPart();
        if (!isTagAllowed(module, tag)) {
            throw new IllegalStateException(format("the module %s does not allow the tag %s here", module, tag));
        }

        builder.parse(reader, element);
    }

    private final String tagName;

    protected abstract T buildBase() throws ParserException;

    protected abstract void parseTag(XMLEventReader reader, StartElement element) throws ParserException;

    @Override
    public final T buildElement() throws ParserException {
        return extend(buildBase());
    }

    @Override
    final void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
        while (true) {
            final XMLEvent event;
            try {
                event = reader.nextEvent();
            } catch (final XMLStreamException cause) {
                throw new ParserException(cause);
            }

            if (isEndOfTag(event, tagName)) {
                break;
            }

            if (event.isStartElement()) {
                handleEvent(reader, event);
            }
        }
    }

    private void handleEvent(final XMLEventReader reader, final XMLEvent event) throws ParserException {
        final StartElement element = event.asStartElement();

        if (element.getName().getPrefix().isEmpty()) {
            parseTag(reader, element);
        } else {
            // parse the extensions
            passToModuleParser(reader, element);
        }
    }

    abstract boolean isModuleAllowed(Class<? extends Module> module);

    boolean isTagAllowed(final Class<? extends Module> module, final String tag) {
        return true;
    }
}
