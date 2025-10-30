package pl.com.ttpsc;

import wt.util.WTMessage;
import wt.util.resource.WTListResourceBundle;

import java.util.Locale;

public class MessageProvider {

    public static String getMessage(Class<? extends WTListResourceBundle> bundleClass, String messageID) {
        return WTMessage.getLocalizedMessage(bundleClass.getName(), messageID);
    }

    public static String getMessage(Class<? extends WTListResourceBundle> bundleClass, String messageID, String[] params) {
        return WTMessage.getLocalizedMessage(bundleClass.getName(), messageID, params);
    }

    public static String getMessage(Class<? extends WTListResourceBundle> bundleClass, String messageID, String[] params, Locale locale) {
        return WTMessage.getLocalizedMessage(bundleClass.getName(), messageID, params, locale);
    }

}

