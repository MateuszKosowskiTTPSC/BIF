package pl.com.ttpsc.service;

import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.DisplayOperationIdentifier;
import pl.com.ttpsc.MessageProvider;
import pl.com.ttpsc.testResource;
import wt.admin.AdministrativeDomainHelper;
import wt.change2.*;
import wt.content.ApplicationData;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.doc.WTDocument;
import wt.fc.*;
import wt.inf.container.WTContainer;
import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.session.SessionContext;
import wt.util.WTException;
import wt.util.WTProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.ptc.core.businessfield.common.BusinessField;
import com.ptc.core.businessfield.common.BusinessFieldIdFactoryHelper;
import com.ptc.core.businessfield.common.BusinessFieldServiceHelper;
import com.ptc.core.businessfield.server.BusinessFieldIdentifier;
import com.ptc.core.businessfield.server.businessObject.BusinessObject;
import com.ptc.core.businessfield.server.businessObject.BusinessObjectHelper;
import com.ptc.core.businessfield.server.businessObject.BusinessObjectHelperFactory;
import com.ptc.core.meta.common.UpdateOperationIdentifier;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.folder.Folder;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.part.WTPart;
import wt.preference.PreferenceClient;
import wt.preference.PreferenceHelper;
import wt.session.SessionHelper;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.util.WTPropertyVetoException;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

import java.util.*;

public class StandardService extends StandardManager implements ExampleService {

    private static final BusinessObjectHelper businessObjectHelper = BusinessObjectHelperFactory
            .getBusinessObjectHelper();

    private static final Logger LOGGER = LogManager.getLogger(StandardService.class);

    public static StandardService newStandardService() throws WTException {
        final StandardService instance = new StandardService();
        instance.initialize();
        return instance;
    }

    @Override
    public String sayHelloTo(String name) throws WTException {
        String message = "Windchill says hello, " + name;
        LOGGER.error("Message + " + message);
        return message;
    }

    @Override
    public String getProperty(String key) throws WTException, IOException {
        WTProperties properties = WTProperties.getLocalProperties();
        String value = properties.getProperty(key);
        LOGGER.error("Property for key '" + key + "' is: " + value);
        return value;
    }

    @Override
    public String displayMessage(String name) throws WTException {
        String message = MessageProvider.getMessage(testResource.class, "MESSAGE_NAME");
        LOGGER.error("MESSAGE_NAME: " + message);
        return message;
    }

    @Override
    public String displayMessageWithParam(String name) throws WTException {
        String message = MessageProvider.getMessage(testResource.class, "HELLO_NUMBER_WORLD", new String[]{name});
        LOGGER.error("HELLO_NUMBER_WORLD: " + message);
        return message;
    }

    @Override
    public WTPart createPart(String name) throws WTException, WTPropertyVetoException, IOException {
        WTPart wtpart = WTPart.newWTPart();
        TypeDefinitionReference typeDefinitionReference = TypedUtility.getTypeDefinitionReference("pl.ttpsc.eyeglassesLens");
        wtpart.setTypeDefinitionReference(typeDefinitionReference);

        wtpart.setName(name);

        WTProperties properties = WTProperties.getLocalProperties();
        String orgName = properties.getProperty("organization");
        String prodName = properties.getProperty("product");
        WTContainerRef orgRef = WTContainerHelper.service.getByPath(getPath(prodName, orgName));
        wtpart.setContainerReference(orgRef);

        return (WTPart) PersistenceHelper.manager.save(wtpart);
    }

    @Override
    public WTPart createPartInSpecifiedProduct()
            throws WTException, WTPropertyVetoException, IOException {
        String preferenceValuePartName = (String) PreferenceHelper.service.getValue("/com/ptc/training/CUSTOM_PART_NAME", PreferenceClient.WINDCHILL_CLIENT_NAME);
        if (preferenceValuePartName == null || preferenceValuePartName.isBlank()) {
            throw new WTException("Preference CUSTOM_PART_NAME is not set or is empty.");
        }

        WTProperties properties = WTProperties.getLocalProperties();
        String productName = properties.getProperty("product");
        String orgName = properties.getProperty("organization");

        if (productName == null || orgName == null) {
            LOGGER.error("Product or Organization property is missing in WTProperties");
            return null;
        }

        WTPart wtpart = WTPart.newWTPart();
        wtpart.setName(preferenceValuePartName);
        WTContainerRef orgRef = WTContainerHelper.service.getByPath(getPath(productName, orgName));
        wtpart.setContainerReference(orgRef);
        String type = "wt.part.WTEyeglassLens";
        TypeDefinitionReference typeDefinitionReference = TypedUtility.getTypeDefinitionReference(type);
        wtpart.setTypeDefinitionReference(typeDefinitionReference);
        LOGGER.error("Part created in product: " + productName + ", organization: " + orgName + ", with name from preference: " + preferenceValuePartName);
        return (WTPart) PersistenceHelper.manager.save(wtpart);
    }

    @Override
    public String getPath(String nameProduct, String nameOrg) {
        String path = "/wt.inf.container.OrgContainer=" + nameOrg + "/wt.pdmlink.PDMLinkProduct=" + nameProduct;
        return path;

    }

    @Override
    public Persistable getObjectByReference(String oid) throws WTException {
        ReferenceFactory referenceFactory = new ReferenceFactory();
        WTReference reference = referenceFactory.getReference(oid);
        return reference.getObject();
    }

    @Override
    public WTCollection updateAttribute(Persistable object, String type, Map<String, String> attributes,
                                        boolean withCheckOut, boolean withCheckIn) throws WTException, WTPropertyVetoException {
        if (withCheckOut) {
            object = (Persistable) checkout((Workable) object);
        }

        try {
            UpdateOperationIdentifier updateOI = new UpdateOperationIdentifier();
            List<BusinessObject> businessObjects = businessObjectHelper.newBusinessObjects(SessionHelper.getLocale(),
                    updateOI, false, object);
            Map<BusinessField, String> businessFields = collectBusinessFields(type, attributes);
            businessObjectHelper.load(businessObjects, businessFields.keySet());

            for (BusinessObject businessObject : businessObjects) {
                for (Map.Entry<BusinessField, String> entry : businessFields.entrySet()) {
                    businessObject.set(entry.getKey(), entry.getValue());
                }
            }

            List<Persistable> appliedPersistables = businessObjectHelper.apply(businessObjects);
            WTCollection updatedObjects;
            if (withCheckOut) {
                updatedObjects = PersistenceHelper.manager.modify(new WTArrayList(appliedPersistables));
            } else {
                updatedObjects = updateWithoutCheckOut(new WTArrayList(appliedPersistables));
            }

            if (withCheckIn) {
                updatedObjects = WorkInProgressHelper.service.checkin(updatedObjects, "test");
            }
            return updatedObjects;
        } catch (WTException | WTPropertyVetoException e) {
            LOGGER.error("Error during attribute update: " + e.getMessage());
            e.printStackTrace();
            throw new WTException(e, "Cannot update attribute");
        }
    }

    private WTCollection updateWithoutCheckOut(WTArrayList wtArrayList) throws WTException {
        return PersistenceHelper.manager.save(wtArrayList);
    }

    private Map<BusinessField, String> collectBusinessFields(String type, Map<String, String> attributes) {
        LOGGER.error("Collecting attributes...");
        Map<BusinessField, String> businessFields = new HashMap<>();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            String newValue = entry.getValue();
            try {
                BusinessField businessField = getBusinessField(attributeName, type);
                if (businessField != null) {
                    businessFields.put(businessField, newValue);
                }
            } catch (WTException e) {
                LOGGER.error("Error getting business field: " + e.getMessage());
            }
        }
        LOGGER.error("Attributes collected");
        return businessFields;
    }

    private BusinessField getBusinessField(String attributeName, String typeName) throws WTException {
        try {
            BusinessFieldIdentifier businessFieldIdentifier = BusinessFieldIdFactoryHelper.FACTORY
                    .getTypeBusinessFieldIdentifier(attributeName, typeName);
            return BusinessFieldServiceHelper.SERVICE.getBusinessField(businessFieldIdentifier);
        } catch (WTException e) {
            throw new WTException(e, "There is no attribute '" + attributeName);
        }
    }

    @Override
    public <T extends Workable> T checkout(T workable) throws WTException, WTPropertyVetoException {
        T wrk = workable;
        if (!WorkInProgressHelper.isCheckedOut(workable)) {
            Folder checkoutFolder = WorkInProgressHelper.service.getCheckoutFolder();
            wrk = (T) WorkInProgressHelper.service.checkout(workable, checkoutFolder, "test").getWorkingCopy();
        }
        return wrk;

    }

    @Override
    public <T extends Workable> T checkin(T workable)
            throws WTException, WTPropertyVetoException {
        return (T) WorkInProgressHelper.service.checkin(workable, "test");

    }


    @Override
    public <T extends Persistable> T setAttributes(T object, Map<String, Object> attributes) throws WTException {
        PersistableAdapter persistableAdapter = new PersistableAdapter(object, null, SessionHelper.getLocale(), new UpdateOperationIdentifier());
        persistableAdapter.load(attributes.keySet());
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            Object newValue = entry.getValue();
            persistableAdapter.set(attributeName, newValue);
        }
        Persistable apply = persistableAdapter.apply();
        return (T) PersistenceHelper.manager.save(apply);
    }

    @Override
    public Map<String, Object> getAttributes(Persistable object, Set<String> attributeNames) throws WTException {
        PersistableAdapter persistableAdapter = new PersistableAdapter(object, null, SessionHelper.getLocale(), new DisplayOperationIdentifier());
        persistableAdapter.load(attributeNames);
        Map<String, Object> attributes = new HashMap<>();
        for (String attributeName : attributeNames) {
            Object value = persistableAdapter.get(attributeName);
            attributes.put(attributeName, value);
        }
        return attributes;
    }

    @Override
    public WTDocument createDocument(String name, WTContainerRef containerRef)
            throws WTException, WTPropertyVetoException {

        WTDocument wtDocument = WTDocument.newWTDocument();
        wtDocument.setName(name);
        wtDocument.setContainerReference(containerRef);

        return (WTDocument) PersistenceHelper.manager.save(wtDocument);
    }

    @Override
    public WTDocument createDocumentWithAttachment(String name, WTContainerRef containerRef, String path) throws WTException, WTPropertyVetoException {
        Transaction tx = new Transaction();
        WTDocument wtDocument = WTDocument.newWTDocument();
        wtDocument.setName(name);
        wtDocument.setContainerReference(containerRef);

        wtDocument = (WTDocument) PersistenceHelper.manager.store(wtDocument);
        File theFile = new File(path);

        try (FileInputStream fis = new FileInputStream(theFile)) {
            ApplicationData theContent = ApplicationData.newApplicationData(wtDocument);
            String filename = "test.txt";

            theContent.setFileName(filename);
            theContent.setRole(ContentRoleType.toContentRoleType("PRIMARY"));
            theContent.setFileSize(theFile.length());


            tx.start();
            theContent = ContentServerHelper.service.updateContent(wtDocument, theContent, fis);
            ContentServerHelper.service.updateHolderFormat(wtDocument);
            tx.commit();
            wtDocument = (WTDocument) PersistenceHelper.manager.refresh((Persistable) wtDocument, true, true);
            tx = null;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
        return wtDocument;
    }

    @Override
    public WTPartUsageLink createUsageLink(WTPart parent, WTPart child) throws WTException {
        WTPartUsageLink usageLink = WTPartUsageLink.newWTPartUsageLink(parent, child.getMaster());
        return (WTPartUsageLink) PersistenceHelper.manager.save(usageLink);
    }

    @Override
    public WTPartDescribeLink createDescribeLink(WTPart part, WTDocument document) throws WTException {
        WTPartDescribeLink describeLink = WTPartDescribeLink.newWTPartDescribeLink(part, document);
        return (WTPartDescribeLink) PersistenceHelper.manager.save(describeLink);
    }

    @Override
    public void deleteUsageLink(WTPartUsageLink usageLink) throws WTException {
        PersistenceHelper.manager.delete(usageLink);
    }

    @Override
    public WTChangeOrder2 createChangeNotice(String cnName) throws WTException, WTPropertyVetoException {
        WTChangeOrder2 changeNotice = WTChangeOrder2.newWTChangeOrder2(cnName);
        changeNotice.setChangeNoticeComplexity(ChangeNoticeComplexity.BASIC);
        return (WTChangeOrder2) PersistenceHelper.manager.save(changeNotice);
    }

    @Override
    public WTChangeActivity2 createChangeTask(String changeActivityName, WTChangeOrder2 changeNotice) throws WTException, WTPropertyVetoException {
        WTChangeActivity2 changeTask = WTChangeActivity2.newWTChangeActivity2(changeActivityName);
        changeTask.setContainer(changeNotice.getContainer());
        return (WTChangeActivity2) ChangeHelper2.service.saveChangeActivity(changeNotice, changeTask);
    }

    @Override
    public WTChangeRequest2 createChangeRequest(String crName) throws WTException {
        WTChangeRequest2 changeRequest = WTChangeRequest2.newWTChangeRequest2(crName);
        return (WTChangeRequest2) PersistenceHelper.manager.save(changeRequest);
    }

    @Override
    public AddressedBy2 addressChangeRequestToChangeNotice(WTChangeRequest2 changeRequest, WTChangeOrder2 changeNotice) throws WTException {
        AddressedBy2 addressedBy = AddressedBy2.newAddressedBy2(changeRequest, changeNotice);
        return (AddressedBy2) PersistenceHelper.manager.save(addressedBy);
    }

    @Override
    public ChangeProcessLink createChangeProcessLink(WTChangeOrder2 changeOrder, WTChangeRequest2 changeRequest) throws WTException {
        ChangeProcessLink link = ChangeProcessLink.newChangeProcessLink(changeOrder, changeRequest);
        return (ChangeProcessLink) PersistenceHelper.manager.save(link);
    }

    @Override
    public WTPart findWTPartByNumber(String partNumber) throws WTException {
        QuerySpec querySpec = new QuerySpec(WTPart.class);
        querySpec.appendWhere(
                new wt.query.SearchCondition(
                        WTPart.class,
                        WTPart.NUMBER,
                        wt.query.SearchCondition.EQUAL,
                        partNumber
                ),
                new int[]{0}
        );
        querySpec.appendAnd();
        // Newest version only
        querySpec.appendWhere(
                new wt.query.SearchCondition(
                        WTPart.class,
                        WTPart.LATEST_ITERATION,
                        SearchCondition.IS_TRUE
                ),
                new int[]{0}
        );
        wt.fc.QueryResult queryResult = PersistenceHelper.manager.find(querySpec);
        if (queryResult.hasMoreElements()) {
            return (WTPart) queryResult.nextElement();
        } else {
            return null;
        }


    }

    @Override
    public WTContainer findContainerRefByName(String name) throws WTException{
        QuerySpec querySpec = new QuerySpec(WTContainer.class);
        querySpec.appendWhere(
                new wt.query.SearchCondition(
                        WTContainer.class,
                        WTContainer.NAME,
                        wt.query.SearchCondition.EQUAL,
                        name
                ),
                new int[]{0}
        );
        wt.fc.QueryResult queryResult = PersistenceHelper.manager.find(querySpec);
        if (queryResult.hasMoreElements()) {
            return (WTContainer) queryResult.nextElement();
        } else {
            return null;
        }


    }

    @Override
    public WTCollection findPartUsageLink(WTPart parent, WTPart child) throws WTException {
        QuerySpec querySpec = new QuerySpec(WTPartUsageLink.class);

        querySpec.appendWhere(new SearchCondition(WTPartUsageLink.class,
                "roleAObjectRef.key.id",
                SearchCondition.EQUAL,
                parent.getPersistInfo().getObjectIdentifier().getId()), new int[]{0});
        querySpec.appendAnd();
        querySpec.appendWhere(new SearchCondition(WTPartUsageLink.class,
                "roleBObjectRef.key.id",
                SearchCondition.EQUAL,
                child.getMaster().getPersistInfo().getObjectIdentifier().getId()), new int[]{0});
        QueryResult queryResult = PersistenceHelper.manager.find(querySpec);
        WTArrayList wtArrayList = new WTArrayList(queryResult);
        return wtArrayList;
    }

// -------- Piotrek START--------

    @Override
    public WTPart createWTPart(String name) throws WTException {
        try {
            WTPart part = WTPart.newWTPart();
            part.setName(name);
            return part;
        } catch (WTPropertyVetoException e) {
            throw new WTException(e);
        }

    }

    @Override
    public void initDemoStructure() throws Exception {
        //create two parents with the same name
        WTCollection toSave = new WTArrayList();
        List<String> partNames = List.of("A","A");
        for(String name: partNames) {
            toSave.add(createWTPart(name));
        }
        toSave = PersistenceHelper.manager.save(toSave);

        //create single child
        WTPart childPart =  (WTPart) PersistenceHelper.manager.save(createWTPart("Child"));

        //checkOut parents (A)
        WTArrayList linkToSave = new WTArrayList();
        WTCollection checkout = WorkInProgressHelper.service.checkout(toSave, WorkInProgressHelper.service.getCheckoutFolder(), "");

        //create links and save
        for(Object obj: checkout.persistableCollection()){
            CheckoutLink link = (CheckoutLink) obj;
            linkToSave.add(WTPartUsageLink.newWTPartUsageLink(((WTPart) link.getWorkingCopy()),childPart.getMaster()));
        }
        PersistenceHelper.manager.save(linkToSave);
    }

    @Override
    public List<Object[]> findLinkByParentNameChildName(String parentName, String childName) throws WTException {
        QuerySpec querySpec = new QuerySpec();
        //we need 3 tables WTPartUsageLink, WTPart, WTPartMaster
        int parentIndex = querySpec.appendClassList(WTPart.class, true);
        int linkIndex = querySpec.appendClassList(WTPartUsageLink.class, true);
        int childIndex = querySpec.appendClassList(WTPartMaster.class, true);

        SearchCondition whereParentName = new SearchCondition(WTPart.class, WTPart.NAME, SearchCondition.EQUAL, parentName);
        SearchCondition whereChildName = new SearchCondition(WTPartMaster.class, WTPartMaster.NAME, SearchCondition.EQUAL, childName);
        //roleA link to WTPart id
        SearchCondition whereParentToLink = new SearchCondition(WTPartUsageLink.class, "roleAObjectRef.key.id", WTPart.class, "thePersistInfo.theObjectIdentifier.id");
        //roleB link to WTPartMaster id
        SearchCondition whereChildMasteToLink = new SearchCondition(WTPartUsageLink.class, "roleBObjectRef.key.id", WTPartMaster.class, "thePersistInfo.theObjectIdentifier.id");

        //build query
        querySpec.appendWhere(whereParentName, new int[]{parentIndex});
        querySpec.appendAnd();
        querySpec.appendWhere(whereChildName,new int[]{childIndex});
        querySpec.appendAnd();
        querySpec.appendWhere(whereParentToLink,new int[]{linkIndex,parentIndex});
        querySpec.appendAnd();
        querySpec.appendWhere(whereChildMasteToLink,new int[]{linkIndex,childIndex});

        QueryResult queryResult = PersistenceHelper.manager.find(querySpec);
        List<Object[]> result = new ArrayList<>();
        while (queryResult.hasMoreElements()){
            //because we used 'appendClassList' the result will be Array with requested objects. In this example: [WTPart,WTPartUsageLink,WTPartMaster]
            Object[] objs =(Object[]) queryResult.nextElement();
            result.add(objs);
        }
        return result;
    }
// -------- Piotrek END--------

    @Override
    public void modifyPart(WTPart part) throws WTException {
        Transaction tx = new Transaction();
        WTPart modifiedPart;
        SessionContext previous = SessionContext.getContext();
        try {
            tx.start();
            SessionHelper.manager.setPrincipal(AdministrativeDomainHelper.ADMINISTRATOR_NAME);
            WTPart checkout = ServiceHelper.service.checkout(part);
            modifiedPart = ServiceHelper.service.setAttributes(checkout, Map.of("boolean", false));
            ServiceHelper.service.checkin(modifiedPart);
            tx.commit();
            tx = null;
        } catch (WTPropertyVetoException e) {
            throw new RuntimeException(e);
        } finally {
            if (tx != null) {
                tx.rollback();
            }
            SessionContext.setContext(previous);
        }
    }

}

