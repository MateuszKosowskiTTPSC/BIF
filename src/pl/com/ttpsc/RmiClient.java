package pl.com.ttpsc;

import wt.method.RemoteMethodServer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public class RmiClient {

    private static final String userName = "wcadmin";
    private static final String password = "wcadmin";

    public static void main(String[] args) throws RemoteException, InvocationTargetException {
        RemoteMethodServer rms = RemoteMethodServer.getDefault();
        rms.setUserName(userName);
        rms.setPassword(password);

        Class[] rmiArgTypes = new Class[1];
        rmiArgTypes[0] = java.lang.String.class;
        Object[] rmiArgs = new Object[1];
        rmiArgs[0] = "MATEUSZ";
        Object result = rms.invoke(
                "sayHelloTo",
                "pl.com.ttpsc.RmiTarget",
                null,
                rmiArgTypes,
                rmiArgs);

        Object propertyResult1 = rms.invoke(
                "getProperty",
                "pl.com.ttpsc.RmiTarget",
                null,
                rmiArgTypes,
                new Object[]{"product"});

        Object propertyResult2 = rms.invoke(
                "getProperty",
                "pl.com.ttpsc.RmiTarget",
                null,
                rmiArgTypes,
                new Object[]{"organization"});

        Object messageResult = rms.invoke(
                "displayMessage",
                "pl.com.ttpsc.RmiTarget",
                null,
                rmiArgTypes,
                new Object[]{"MATEUSZ"});

        Object messageWithParamResult = rms.invoke(
                "displayMessageWithParam",
                "pl.com.ttpsc.RmiTarget",
                null,
                rmiArgTypes,
                new Object[]{"MATEUSZ"});
    }
}