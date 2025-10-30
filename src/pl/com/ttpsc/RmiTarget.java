package pl.com.ttpsc;

import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.method.RemoteAccess;
import wt.util.WTProperties;
import wt.util.WTProperties;

import java.io.IOException;

public class RmiTarget implements RemoteAccess {

    private static final Logger LOGGER = LogManager.getLogger(RmiTarget.class);

    public static String sayHelloTo(String name) {
        String message = "Windchill says hello, " + name;
        LOGGER.debug("Logger RmiTarget >> " + message);
        return message;
    }

    public static String getProperty(String key) throws IOException {
        WTProperties properties = WTProperties.getLocalProperties();
        String value = properties.getProperty(key);
        LOGGER.debug("Property for key '" + key + "' is: " + value);
        return value;
    }

    public static String displayMessage(String name) {
        String message = MessageProvider.getMessage(testResource.class, "MESSAGE_NAME");
        LOGGER.debug("MESSAGE_NAME: " + message);
        return message;
    }

    public static String displayMessageWithParam(String name) {
        String message = MessageProvider.getMessage(testResource.class, "HELLO_NUMBER_WORLD", new String[]{name});
        LOGGER.debug("HELLO_NUMBER_WORLD: " + message);
        return message;
    }
}