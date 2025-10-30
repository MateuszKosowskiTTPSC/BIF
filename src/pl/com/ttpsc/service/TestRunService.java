package pl.com.ttpsc.service;

import wt.inf.container.WTContainer;
import wt.log4j.LogManager;
import wt.log4j.Logger;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;
import wt.util.WTException;

import java.util.List;

public class TestRunService {

    private static final Logger LOGGER = LogManager.getLogger(TestRunService.class);

    public static void main(String[] gs) throws WTException {

//        try{
//            ExampleService someService = ServiceHelper.service;
//
//            String helloResult = someService.sayHelloTo("Mateusz from service");
//            LOGGER.error("Hello Result: " + helloResult);
//
//            String propertyResult = someService.getProperty("product");
//            LOGGER.error("Property Result: " + propertyResult);
//
//            String messageResult = someService.displayMessage("Mateusz from service");
//            LOGGER.error("Message Result: " + messageResult);
//
//            String messageWithParamResult = someService.displayMessageWithParam("Mateusz from service");
//            LOGGER.error("Message With Param Result: " + messageWithParamResult);
//
//        } catch (Exception e) {
//            LOGGER.error("An error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
// -----------------------------------------------------
//        try {
//            WTPart part = ServiceHelper.service.createPart("SomePartName");
//            try {
//                WTPart checkout = ServiceHelper.service.checkout(part);
//                WTPart red = ServiceHelper.service.setAttributes(checkout, Map.of("pl.ttpsc.Color", "pl.ttpsc.Red"));
//                WTPart checkin = ServiceHelper.service.checkin(red);
//                LOGGER.error("Updated part: " + checkin);
//                Map<String, Object> attributes = ServiceHelper.service.getAttributes(checkin, Set.of("pl.ttpsc.Color", "state.state"));
//                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
//                    LOGGER.error(entry.getKey() + " : " + entry.getValue());
//                }
//                // ServiceHelper.service.updateAttribute(part, "pl.ttpsc.eyeglassesLens", Map.of("pl.ttpsc.eyeglassesLens", "Red"), true, true);
//            } catch (WTException e) {
//                e.printStackTrace();
//            } catch (WTPropertyVetoException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (WTException | IOException e) {
//            e.printStackTrace();
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// --------------------------------------------------
//        try {
//            WTProperties properties = WTProperties.getLocalProperties();
//            String orgName = properties.getProperty("organization");
//            String prodName = properties.getProperty("product");
//            WTContainerRef orgRef = WTContainerHelper.service.getByPath(ServiceHelper.service.getPath(prodName, orgName));
//            //WTDocument document = ServiceHelper.service.createDocumentWithAttachment("Attachment", orgRef, "/home/oracle/test.txt");
//            WTDocument objectByReference = (WTDocument) ServiceHelper.service.getObjectByReference("VR:wt.doc.WTDocument:257350");
//            LOGGER.error("Document name: " + objectByReference.getNumber());
//        } catch (WTException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

// ---------------------------------
//        try{
//            WTPart parentPart = ServiceHelper.service.createPart("ParentPart");
//            WTPart childPart = ServiceHelper.service.createPart("ChildPart");
//            // Checkot parent part
//            WTPart checkedOutParent = ServiceHelper.service.checkout(parentPart);
//            WTPartUsageLink usageLink = ServiceHelper.service.createUsageLink(checkedOutParent, childPart);
//            // Checkin parent part
//            WTPart checkedInParent = ServiceHelper.service.checkin(checkedOutParent);
//            LOGGER.error("Usage Link created between parent part and child part. Link: " + usageLink);
//
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// ---------------------------------------------------
//        try{
//            WTPart parentPart = ServiceHelper.service.createPart("PartDoc");
//
//            WTProperties properties = WTProperties.getLocalProperties();
//            String orgName = properties.getProperty("organization");
//            String prodName = properties.getProperty("product");
//            WTContainerRef orgRef = WTContainerHelper.service.getByPath(ServiceHelper.service.getPath(prodName, orgName));
//            WTDocument document = ServiceHelper.service.createDocumentWithAttachment("Attachment", orgRef, "/home/oracle/test.txt");
//
//            // Checkot parent part
//            WTPart checkedOutParent = ServiceHelper.service.checkout(parentPart);
//            // Link document to part
//            WTPartDescribeLink describeLink = ServiceHelper.service.createDescribeLink(checkedOutParent, document);
//            // Checkin parent part
//            WTPart checkedInParent = ServiceHelper.service.checkin(checkedOutParent);
//            LOGGER.error("Describe Link created between part and document. Link: " + describeLink);
//
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// ---------------------------------
//        try {
//            // Get part by object reference
//            Persistable part = ServiceHelper.service.getObjectByReference("VR:wt.part.WTPart:260750");
//            if (part instanceof WTPart) {
//                LOGGER.error("Part found: " + ((WTPart) part).getNumber());
//            } else {
//                LOGGER.error("The object is not a WTPart.");
//            }
//
//            // Chkeout part
//            WTPart checkedOutPart = ServiceHelper.service.checkout((WTPart) part);
//
//            // Get existing usage link from part
//            wt.fc.QueryResult qr = WTPartHelper.service.getUsesWTPartMasters(checkedOutPart);
//            while(qr.hasMoreElements()){
//                WTPartUsageLink usageLink = (WTPartUsageLink) qr.nextElement();
//                LOGGER.error("Usage Link found: " + usageLink.getPersistInfo().getObjectIdentifier().getStringValue());
//                // Delete usage link
//                ServiceHelper.service.deleteUsageLink(usageLink);
//                LOGGER.error("Usage Link deleted: " + usageLink.getPersistInfo().getObjectIdentifier().getStringValue());
//            }
//            // Checkin part
//            WTPart checkedInPart = ServiceHelper.service.checkin(checkedOutPart);
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// ----------------------------------
//        try{
//            WTChangeOrder2 changeNotice = ServiceHelper.service.createChangeNotice("Test Change Notice from Service");
//            WTChangeActivity2 changeTask = ServiceHelper.service.createChangeTask("Test Change Task from Service", changeNotice);
//            LOGGER.error("Change Task created: " + changeTask.getNumber());
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// -------------------------------------------
//        try{
//            WTChangeOrder2 changeNotice = ServiceHelper.service.createChangeNotice("Test Change Notice for change request");
//            LOGGER.error("Change Notice created: " + changeNotice.getNumber());
//            WTChangeRequest2 changeRequest = ServiceHelper.service.createChangeRequest("Test Change Request from Service");
//            LOGGER.error("Change Request created: " + changeRequest.getNumber());
//            ChangeProcessLink cpl = ServiceHelper.service.createChangeProcessLink(changeNotice, changeRequest);
//            LOGGER.error("Change Request addressed to Change Notice. Link: " + cpl);
//
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        } catch (WTPropertyVetoException e) {
//            throw new RuntimeException(e);
//        }
// -------------------------------------------
//        try{
//            WTPart part = ServiceHelper.service.createPart("MyNewPartFromTestRunService");
//            LOGGER.error("Part created: " + part.getNumber());
//        } catch (WTException | WTPropertyVetoException | IOException e) {
//            throw new RuntimeException(e);
//        }
// -------------------------------------------
//        try{
//            WTPart wtPartByNumber = ServiceHelper.service.findWTPartByNumber("0000000006");
//            LOGGER.error("Part found by number: " + wtPartByNumber.getNumber());
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        }
// -------------------------------------------
//        try{
//            WTContainer container = ServiceHelper.service.findContainerRefByName("GOLF_CART");
//            LOGGER.error("Container found by name: " + container.getName());
//        } catch (WTException e) {
//            throw new RuntimeException(e);
//        }
// -------------------------------------------
        try{
            ServiceHelper.service.initDemoStructure();
            List<Object[]> objects = ServiceHelper.service.findLinkByParentNameChildName("A", "Child");
            LOGGER.error("Links found: " + objects.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}