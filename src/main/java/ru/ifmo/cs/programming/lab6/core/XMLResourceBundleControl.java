package ru.ifmo.cs.programming.lab6.core;

/**
 * Created by саша on 26.08.2017.
 */
import java.io.*;
import java.net.*;
import java.util.*;

    public class XMLResourceBundleControl extends ResourceBundle.Control {
        private static String XML = "xml";

        public List getFormats(String baseName) {
            return Collections.singletonList(XML);
        }

        public ResourceBundle newBundle(String baseName, Locale locale,
                                        String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            if ((baseName == null) || (locale == null) || (format == null)
                    || (loader == null)) {
                throw new NullPointerException();
            }
            ResourceBundle bundle = null;
            if (format.equals(XML)) {
                String bundleName = toBundleName("res/" + baseName, locale);
//                System.out.println(System.getProperty("user.dir") + "\\src\\resources\\properties\\" + baseName);
                String resourceName = toResourceName(bundleName, format);
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        if (reload) {
                            connection.setUseCaches(false);
                        }
                        InputStream stream = connection.getInputStream();
                        if (stream != null) {
                            BufferedInputStream bis = new BufferedInputStream(
                                    stream);
                            bundle = new XMLResourceBundle(bis);
                            bis.close();
                        }
                    }
                }
            }
            return bundle;
        }

        private static class XMLResourceBundle extends ResourceBundle {
            private Properties props;

            XMLResourceBundle(InputStream stream) throws IOException {
                props = new Properties();
                props.loadFromXML(stream);
            }

            protected Object handleGetObject(String key) {
                return props.getProperty(key);
            }

            public Enumeration getKeys() {
                Set handleKeys = props.stringPropertyNames();
                return Collections.enumeration(handleKeys);
            }
        }
    }