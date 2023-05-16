package log4jUniversal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationBuilder;

public class Application {
    public static final Logger logger = LogManager.getLogger(Application.class);
    private static String workingDirPathName = System.getProperty("user.dir");
    public static void main(String[] args) {
           initApp();
           logger.error("This is error message comming from ");
           logger.info("This is info message comming from ");
           logger.trace("This is trace message comming from ");
    }

    public static Properties convertResourceBundleToProperties(ResourceBundle resource) {
           Enumeration iter;
           if (resource == null) {
                  return null;
           }
           Properties props = new Properties();
           iter = resource.getKeys();
           while (iter.hasMoreElements()) {
                  String key = (String) iter.nextElement();
                  String value = resource.getString(key);
                  props.put(key, value);
           }
           return props;
    }

    public static void initApp() {
           
           File file = new File(workingDirPathName+"/resources");
           URI uri = file.toURI();
           URL url = null;;
           ClassLoader loader = null;
           Properties properties = null;
           try {
                  url = uri.toURL();
                  URL[] urls = { url };

                  loader = new URLClassLoader(urls);
           } catch (MalformedURLException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
           }

           if (null != loader) {
                  ResourceBundle bundle = ResourceBundle.getBundle("log4j", Locale.getDefault(), loader);

                  properties = convertResourceBundleToProperties(bundle);

           }
           if (null != properties) {
                  LoggerContext context = (LoggerContext) LogManager.getContext(false);
                  Configuration config = new PropertiesConfigurationBuilder()
                               .setConfigurationSource(ConfigurationSource.NULL_SOURCE).setRootProperties(properties)
                               .setLoggerContext(context).build();
                  context.setConfiguration(config);
                  Configurator.initialize(config);
           }

    }
}
