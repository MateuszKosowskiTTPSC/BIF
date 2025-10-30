package pl.com.ttpsc.listeners.wip;

import wt.fc.PersistenceManagerEvent;
import wt.services.ManagerException;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.vc.wip.WorkInProgressServiceEvent;

import java.io.Serializable;

public class StandardPreCheckInService extends StandardManager implements PreCheckInService, Serializable {

    private static final long serialVersionUID = 1L;

    public static StandardPreCheckInService newStandardPreCheckInService() throws WTException {
        StandardPreCheckInService instance = new StandardPreCheckInService();
        instance.initialize();
        return instance;
    }

    @Override
    public synchronized void performStartupProcess() throws ManagerException {
        PreCheckInListenerAdapter listener = new PreCheckInListenerAdapter(StandardPreCheckInService.class.getName());
        getManagerService().addEventListener(listener, WorkInProgressServiceEvent.generateEventKey(WorkInProgressServiceEvent.PRE_CHECKIN));
    }
}
