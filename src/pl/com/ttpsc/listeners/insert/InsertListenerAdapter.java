package pl.com.ttpsc.listeners.insert;

import pl.com.ttpsc.service.ServiceHelper;
import wt.events.KeyedEvent;
import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.part.WTPart;
import wt.services.ServiceEventListenerAdapter;
import wt.session.SessionServerHelper;
import wt.vc.wip.WorkInProgressHelper;


public class InsertListenerAdapter extends ServiceEventListenerAdapter {

    private static final Logger logger = LogManager.getLogger(InsertListenerAdapter.class);

    public InsertListenerAdapter(String className) {
        super(className);
    }

    @Override
    public void notifyVetoableEvent(Object event) throws Exception {
        KeyedEvent event1 = (KeyedEvent) event;
        if (event1.getEventTarget() instanceof WTPart) {
//            WTPart part = (WTPart) event1.getEventTarget();
//            if (!WorkInProgressHelper.isCheckedOut(part) && !WorkInProgressHelper.isWorkingCopy(part)) {
//                ServiceHelper.service.modifyPart((WTPart) event1.getEventTarget());
//            }
            logger.debug("InsertListenerAdapter, WTPart inserted in session");
        }
    }
}
