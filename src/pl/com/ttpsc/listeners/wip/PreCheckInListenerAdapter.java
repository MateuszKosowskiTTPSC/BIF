package pl.com.ttpsc.listeners.wip;

import pl.com.ttpsc.service.StandardService;
import wt.doc.WTDocument;
import wt.events.KeyedEvent;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManagerEvent;
import wt.fc.QueryResult;
import wt.folder.Foldered;

import wt.iba.definition.AbstractAttributeDefinition;
import wt.iba.value.BooleanValue;
import wt.iba.value.IBAHolder;
import wt.iba.value.service.IBAValueHelper;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceEventListenerAdapter;

public class PreCheckInListenerAdapter extends ServiceEventListenerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(PreCheckInListenerAdapter.class);
    public PreCheckInListenerAdapter(String className) {
        super(className);
    }

    @Override
    public void notifyVetoableEvent(Object event) throws Exception {

        KeyedEvent myEvent = (KeyedEvent) event;

        if(myEvent.getEventTarget() instanceof WTDocument){
            LOGGER.debug("LOG DOC !");
//            WTDocument myDocument = (WTDocument) myEvent.getEventTarget();
//
//            QuerySpec qs = new QuerySpec();
//            Class ibaClass = BooleanValue.class;
//            int documentIndex = qs.appendClassList(WTDocument.class, true);
//            int ibaIndex = qs.appendClassList(ibaClass, false);
//            int ibaDefinitionIndex = qs.appendClassList(AbstractAttributeDefinition.class, false);
//
//            qs.appendWhere(getIBADefDefaultViews("oneAndOnly"), new int[]{ibaIndex});
//            qs.appendAnd();
//            qs.appendWhere(getIBAsWithValue(ibaClass, Boolean.TRUE), new int[]{ibaIndex});
//            qs.appendAnd();
//            qs.appendWhere(getContainedIn(myDocument), new int[]{documentIndex});;
//            qs.appendAnd();
//            qs.appendWhere(getFolderedIn(myDocument), new int[]{documentIndex});
//            qs.appendAnd();
//            qs.appendWhere(connectIBAWithAttributeDefinition(ibaClass), new int[]{ibaIndex, ibaDefinitionIndex});
//            qs.appendAnd();
//            qs.appendWhere(connectIBAWithObject(ibaClass), new int[]{ibaIndex, documentIndex});
//            qs.appendAnd();
//            qs.appendWhere(getWithName(myDocument, myDocument.getName(), true), new int[]{documentIndex});
//
//            QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) qs);
//
//            if(queryResult.size() > 0){
//                LOGGER.error("DUPLICAT FOUND !");
//                throw new Exception("Document with the same name and 'oneAndOnly' IBA set to TRUE already exists in this folder and container.");
//            } else {
//                LOGGER.error("THERE IS NO DUPLICAT !.");
//            }

        } else{
            LOGGER.debug("NOT A DOC !");
        }
    }


    public SearchCondition connectIBAWithAttributeDefinition(Class ibaClass) throws QueryException {
        return new SearchCondition(ibaClass, "definitionReference.key.id", AbstractAttributeDefinition.class, "thePersistInfo.theObjectIdentifier.id");
    }

    public SearchCondition connectIBAWithObject(Class ibaClass) throws QueryException {
        return new SearchCondition(ibaClass, "theIBAHolderReference.key.id", Persistable.class, "thePersistInfo.theObjectIdentifier.id");
    }

    public SearchCondition getIBADefDefaultViews(String attributeName) throws QueryException {
        return new SearchCondition(AbstractAttributeDefinition.class, "name", SearchCondition.EQUAL, attributeName);
    }

    public SearchCondition getIBAsWithValue(Class ibaClass, Object attributeValue) throws QueryException {
        SearchCondition result = null;

        if (attributeValue instanceof String) {
            result = new SearchCondition(ibaClass, "value", SearchCondition.EQUAL, (String) attributeValue);
        } else if (attributeValue instanceof Boolean) {
            String condition = (Boolean) attributeValue ? SearchCondition.IS_TRUE : SearchCondition.IS_FALSE;
            result = new SearchCondition(ibaClass, "value", condition);
        } else throw new QueryException("Cannot build a Search Condition");

        return result;
    }

    public SearchCondition getContainedIn(WTContained contained) throws QueryException {
        return new SearchCondition(WTContained.class, WTContained.CONTAINER_ID, SearchCondition.EQUAL, contained.getContainer().getPersistInfo().getObjectIdentifier().getId());
    }

    public SearchCondition getFolderedIn(Foldered foldered) throws QueryException {
        return new SearchCondition(Foldered.class, Foldered.PARENT_FOLDER + "key.id", SearchCondition.EQUAL, foldered.getParentFolder().getObject().getPersistInfo().getObjectIdentifier().getId());
    }

    public SearchCondition getWithName(Persistable persistable, String name, boolean negated) throws QueryException {
        String condition = negated ? SearchCondition.NOT_EQUAL : SearchCondition.EQUAL;
        SearchCondition result = null;

        if (persistable instanceof WTDocument) {
            result = new SearchCondition(WTDocument.class, WTDocument.NAME, condition, name);
        } else if (persistable instanceof WTPart) {
            result = new SearchCondition(WTPart.class, WTPart.NAME, condition, name);
        } else throw new QueryException("WTDocument and WTPart support only!");
        return result;
    }
}
