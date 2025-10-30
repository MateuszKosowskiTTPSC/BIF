package pl.com.ttpsc;

import wt.util.resource.*;

@RBUUID("pl.com.ttpsc.testResource")
public class testResource extends WTListResourceBundle{

    @RBEntry("Message Text")
    public static final String MESSAGE_NAME = "MESSAGE_NAME";

    @RBEntry("Hello {0} World")
    @RBComment("Greet entire world of some name")
    @RBArgComment0("World's name")
    public static final String HELLO_NUMBER_WORLD = "HELLO_NUMBER_WORLD";

}

