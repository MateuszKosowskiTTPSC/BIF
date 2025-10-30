package pl.com.ttpsc.listeners.insert;

import wt.method.RemoteInterface;
import wt.services.ManagerException;

@RemoteInterface
public interface InsertService {
    void performStartupProcess() throws ManagerException;
}
