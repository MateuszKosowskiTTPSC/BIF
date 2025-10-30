package pl.com.ttpsc.listeners.postStore;

import wt.fc.PersistenceManagerEvent;
import wt.services.ManagerException;
import wt.services.StandardManager;
import wt.util.WTException;

import java.io.Serializable;

public class StandardPostStoreService extends StandardManager implements PostStoreService, Serializable {

    private static final long serialVersionUID = 111L;

    public static StandardPostStoreService newStandardPostStoreService() throws WTException {
        StandardPostStoreService instance = new StandardPostStoreService();
        instance.initialize();
        return instance;
    }

    @Override
    public synchronized void performStartupProcess() throws ManagerException {
        PostStoreListenerAdapter listener = new PostStoreListenerAdapter(StandardPostStoreService.class.getName());
        getManagerService().addEventListener(listener, PersistenceManagerEvent.generateEventKey(PersistenceManagerEvent.POST_STORE));
    }
}
