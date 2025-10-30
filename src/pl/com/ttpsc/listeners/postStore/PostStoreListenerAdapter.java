package pl.com.ttpsc.listeners.postStore;

import wt.fc.PersistenceManagerEvent;
import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.part.WTPart;
import wt.services.ServiceEventListenerAdapter;

public class PostStoreListenerAdapter extends ServiceEventListenerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(PostStoreListenerAdapter.class);

    public PostStoreListenerAdapter(String className) {
        super(className);
    }

    @Override
    public void notifyVetoableEvent(Object event) throws Exception {
        PersistenceManagerEvent myEvent = (PersistenceManagerEvent) event;
        if(myEvent.getEventTarget() instanceof WTPart){
            WTPart somePart = (WTPart) myEvent.getEventTarget();
//            wt.fc.QueryResult qr = WTPartHelper.service.getUsedByWTParts(somePart.getMaster());
//            if (!qr.hasMoreElements()) {
//                WTPart childPart = ServiceHelper.service.createPart("Child of Nut");
//                // Check out the parent part
//                WTPart checkedOutParent = ServiceHelper.service.checkout(somePart);
//                // Add the child part as a used part to the parent part
//                WTPartUsageLink usageLink = ServiceHelper.service.createUsageLink(checkedOutParent, childPart);
//                // Check in the parent part
//                WTPart checkedInParent = ServiceHelper.service.checkin(checkedOutParent);
//                LOGGER.error("Usage Link created between parent part and child part. Link: " + usageLink);
//            }else{
//                LOGGER.error("Part already has used parts. No new usage link created.");
//            }
            LOGGER.debug("PostStoreListenerAdapter, WTPart stored: " + somePart.getNumber());
        }else{
            LOGGER.debug("PostStoreListenerAdapter, Non-WTPart object stored.");
        }
    }
}
