package pl.com.ttpsc.service;

import wt.services.ServiceFactory;

public class ServiceHelper {
    public static final ExampleService service = ServiceFactory.getService(ExampleService.class);
}
