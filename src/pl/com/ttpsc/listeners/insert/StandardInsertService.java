package pl.com.ttpsc.listeners.insert;

import wt.fc.PersistenceManagerEvent;
import wt.services.ManagerException;
import wt.services.StandardManager;
import wt.util.WTException;

import java.io.Serializable;

public class StandardInsertService extends StandardManager implements InsertService, Serializable {

    private static final long serialVersionUID = 1234567890123456787L;

    @Override
    public synchronized void performStartupProcess() throws ManagerException {
        InsertListenerAdapter listener = new InsertListenerAdapter(StandardInsertService.class.getName());
        getManagerService().addEventListener(listener, PersistenceManagerEvent.generateEventKey(PersistenceManagerEvent.POST_STORE));
    }

    public static StandardInsertService newStandardInsertService
            () throws WTException {
        StandardInsertService instance = new StandardInsertService();
        instance.initialize();
        return instance;
    }
}
