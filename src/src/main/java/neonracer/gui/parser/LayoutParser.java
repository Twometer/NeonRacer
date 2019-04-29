package neonracer.gui.parser;

import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.ParserMethod;
import neonracer.gui.screen.Screen;
import neonracer.gui.util.PropertyList;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


class LayoutParser {

    private static final String ROOT_ELEMENT = "Screen";

    private Document document;

    private LayoutParser(Document document) {
        this.document = document;
    }

    static LayoutParser fromStream(InputStream inputStream) {
        if (inputStream == null) throw new LayoutParserException("Could not find the layout file");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return new LayoutParser(builder.parse(inputStream));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new LayoutParserException("Failed to read the layout file", e);
        }
    }

    void loadInto(Screen screen) {
        Element root = document.getDocumentElement();
        if (root.getTagName().equals(ROOT_ELEMENT)) {
            traverseDocument(root, screen);
        } else
            throw new LayoutParserException(String.format("Invalid layout file: Root tag is %s, expected %s", root.getTagName(), ROOT_ELEMENT));
        bindFields(screen);
        bindMethods(screen);
    }

    private void bindMethods(Screen screen) {
        for (Method method : screen.getClass().getDeclaredMethods()) {
            EventHandler annotation = null;
            for (Annotation candidate : method.getAnnotations()) {
                if (candidate instanceof EventHandler) {
                    annotation = (EventHandler) candidate;
                    break;
                }
            }
            if (annotation != null) {
                Widget ctrl = screen.getChildById(annotation.value());
                if (method.getParameterCount() > 0) {
                    Class clazz = method.getParameterTypes()[0];
                    if (ctrl != null) {
                        ctrl.addEventHandler(clazz, event -> {
                            try {
                                boolean accessible = method.isAccessible();
                                method.setAccessible(true);
                                method.invoke(screen, event);
                                method.setAccessible(accessible);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

            }
        }
    }

    private void bindFields(Screen screen) {
        for (Field field : screen.getClass().getDeclaredFields()) {
            BindWidget annotation = null;
            for (Annotation candidate : field.getAnnotations()) {
                if (candidate instanceof BindWidget) {
                    annotation = (BindWidget) candidate;
                    break;
                }
            }
            if (annotation != null) {
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(screen, screen.getChildById(annotation.value()));
                    field.setAccessible(accessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void traverseDocument(Node root, Container parent) {
        loadAttributes(root.getAttributes(), parent);
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);
            String prefix = parent.getClass().getSimpleName() + ".";
            if (node.getNodeName().startsWith(prefix)) {
                loadSublist(node, prefix, parent);
                continue;
            }
            if (node.getNodeType() == Node.TEXT_NODE) // Skip "#text" nodes
                continue;

            Widget widget = WidgetFactory.create(node.getNodeName());
            if (node.getAttributes() != null)
                loadAttributes(node.getAttributes(), widget);
            if (widget instanceof Container)
                traverseDocument(node, (Container) widget);
            parent.addChild(widget);
        }
    }

    private void loadSublist(Node root, String prefix, Widget parent) {
        String name = root.getNodeName().substring(prefix.length());
        Method listGetterMethod = findMethod(parent.getClass().getMethods(), "get" + name, false);
        if (listGetterMethod != null) {
            PropertyList list = null;
            try {
                list = (PropertyList) listGetterMethod.invoke(parent);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if (list != null) {
                for (int j = 0; j < root.getChildNodes().getLength(); j++) {
                    Node subNode = root.getChildNodes().item(j);
                    if (subNode.getNodeName().equals(list.getClazz().getSimpleName())) {
                        list.getList().add(invokeParser(list.getClazz(), subNode.getTextContent()));
                    }
                }
            }
        }
    }

    private Object invokeParser(Class clazz, String name) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(ParserMethod.class)
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0] == String.class
                    && Modifier.isStatic(method.getModifiers())
                    && method.getReturnType() == clazz) {
                try {
                    return method.invoke(null, name);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new LayoutParserException(String.format("For %s to automatically be parsed from string, %s has to declare a static method annotated with @ParserMethod that takes a String as input and %s as output.", clazz.getSimpleName(), clazz.getSimpleName(), clazz.getSimpleName()));
    }

    private void loadAttributes(NamedNodeMap attributes, Widget widget) {
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            String name = node.getNodeName();
            String value = node.getNodeValue();

            String expectedSetter = "set" + name;
            Method setterMethod = findMethod(widget.getClass().getMethods(), expectedSetter, true);
            if (setterMethod != null) {
                Object o = cast(setterMethod.getParameterTypes()[0], value);
                try {
                    setterMethod.invoke(widget, o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                if (name.contains("."))
                    widget.getForeignParameters().put(name, value);
            }
        }
    }

    private Method findMethod(Method[] methods, String name, boolean requireParams) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                if (!requireParams || method.getParameterCount() > 0) {
                    return method;
                }
            }
        }
        return null;
    }

    private Object cast(Class<?> param, String val) {
        if (param.getName().equals(String.class.getName())) return val;
        else if (param.getName().equals(int.class.getName())) return Integer.parseInt(val);
        else if (param.getName().equals(double.class.getName())) return Double.parseDouble(val);
        else if (param.getName().equals(float.class.getName())) return Float.parseFloat(val);
        else if (param.getName().equals(long.class.getName())) return Long.parseLong(val);
        else if (param.getName().equals(boolean.class.getName())) return Boolean.parseBoolean(val);
        else if (param.isEnum()) return Enum.valueOf((Class<? extends Enum>) param, val);
        else return invokeParser(param, val);
    }
}
