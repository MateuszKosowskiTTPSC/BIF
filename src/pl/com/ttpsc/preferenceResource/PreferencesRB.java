package pl.com.ttpsc.preferenceResource;

import wt.util.resource.RBEntry;
import wt.util.resource.RBUUID;
import wt.util.resource.WTListResourceBundle;

@RBUUID("pl.com.ttpsc.preferenceResource.PreferencesRB")
public class PreferencesRB extends WTListResourceBundle{

    @RBEntry("Custom Part Category")
    public static final String CUSTOM_PART_PREFERENCE_NODE_displayName = "CUSTOM_PART_PREFERENCE_NODE.displayName";
    @RBEntry("Custom Part Category description")
    public static final String CUSTOM_PART_PREFERENCE_NODE_description = "CUSTOM_PART_PREFERENCE_NODE.description";

    @RBEntry("Test Reference 1")
    public static final String CUSTOM_PART_PREFERENCE1_displayName = "CUSTOM_PART_PREFERENCE1.displayName";
    @RBEntry("Test Reference 1 description")
    public static final String CUSTOM_PART_PREFERENCE1_description = "CUSTOM_PART_PREFERENCE1.description";

    @RBEntry("Validate Release Reference ")
    public static final String VALIDATE_RELEASE_PREFERENCE_displayName = "VALIDATE_RELEASE_PREFERENCE.displayName";
    @RBEntry("Validate Release description")
    public static final String VALIDATE_RELEASE_PREFERENCE_description = "VALIDATE_RELEASE_PREFERENCE.description";
}
