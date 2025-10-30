package pl.com.ttpsc.listeners.template;

import wt.fc.PersistenceManagerEvent;
import wt.services.ManagerException;
import wt.services.StandardManager;
import wt.util.WTException;

import java.io.Serializable;

public class StandardEventService extends StandardManager implements EventService, Serializable {

    private static final long serialVersionUID = 1L;

    public static StandardEventService newStandardEventService() throws WTException {
        StandardEventService instance = new StandardEventService();
        instance.initialize();
        return instance;
    }

    @Override
    public synchronized void performStartupProcess() throws ManagerException {
        EventListenerAdapter listener = new EventListenerAdapter(StandardEventService.class.getName());
        getManagerService().addEventListener(listener, PersistenceManagerEvent.generateEventKey(PersistenceManagerEvent.POST_STORE));
    }
}
