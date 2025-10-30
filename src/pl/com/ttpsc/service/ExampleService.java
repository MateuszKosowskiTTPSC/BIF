package pl.com.ttpsc.service;

import wt.change2.*;
import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.collections.WTCollection;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteInterface;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartUsageLink;
import wt.pom.PersistenceException;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.wip.WorkInProgressException;
import wt.vc.wip.Workable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RemoteInterface
public interface ExampleService {

    String getProperty(String key) throws WTException, IOException;

    String sayHelloTo(String name) throws WTException;

    String displayMessage(String name) throws WTException;

    String displayMessageWithParam(String name) throws WTException;

    WTPart createPartInSpecifiedProduct() throws WTException, WTPropertyVetoException, IOException;

    WTDocument createDocument(String name, WTContainerRef containerRef) throws WTException, WTPropertyVetoException;

    WTDocument createDocumentWithAttachment(String name, WTContainerRef containerRef, String path) throws WTException, WTPropertyVetoException;

    Persistable getObjectByReference(String oid) throws WTException;

    String getPath(String nameProduct, String nameOrg);

    WTPart createPart(String name) throws WTException, WTPropertyVetoException, IOException;

    WTCollection updateAttribute(Persistable object, String type, Map<String, String> attributes, boolean withCheckOut, boolean withCheckIn) throws WTException, WTPropertyVetoException;

    <T extends Persistable> T setAttributes(T object, Map<String, Object> attributes) throws WTException;

    <T extends Workable> T checkout(T workable) throws WTException, WTPropertyVetoException;

    <T extends Workable> T checkin(T workable) throws WorkInProgressException, WTPropertyVetoException, PersistenceException, WTException;

    Map<String, Object> getAttributes(Persistable object, Set<String> attributeNames) throws WTException;

    WTPartUsageLink createUsageLink(WTPart parent, WTPart child) throws WTException;

    WTPartDescribeLink createDescribeLink(WTPart part, WTDocument document) throws WTException;

    void deleteUsageLink(WTPartUsageLink usageLink) throws WTException;

    WTChangeOrder2 createChangeNotice(String cnName) throws WTException, WTPropertyVetoException;

    WTChangeActivity2 createChangeTask(String changeActivityName, WTChangeOrder2 changeNotice) throws WTException, WTPropertyVetoException;

    WTChangeRequest2 createChangeRequest(String crName) throws WTException, WTPropertyVetoException;

    AddressedBy2 addressChangeRequestToChangeNotice(WTChangeRequest2 changeRequest, WTChangeOrder2 changeNotice) throws WTException;

    ChangeProcessLink createChangeProcessLink(WTChangeOrder2 changeOrder, WTChangeRequest2 changeRequest) throws WTException;

    WTPart findWTPartByNumber(String partNumber) throws WTException;

    WTContainer findContainerRefByName(String name) throws WTException;

    WTCollection findPartUsageLink(WTPart parent, WTPart child) throws WTException;

    WTPart createWTPart(String name) throws WTException;

    void initDemoStructure() throws Exception;

    List<Object[]> findLinkByParentNameChildName(String parentName, String childName) throws WTException;

    void modifyPart(WTPart part) throws WTException;
}
