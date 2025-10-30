package pl.com.ttpsc.listeners.template;

import wt.services.ServiceEventListenerAdapter;

public class EventListenerAdapter extends ServiceEventListenerAdapter {
    public EventListenerAdapter(String className) {
        super(className);
    }

    @Override
    public void notifyVetoableEvent(Object event) throws Exception {
        // TODO
    }
}
