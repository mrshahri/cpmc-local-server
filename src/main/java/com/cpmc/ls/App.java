package com.cpmc.ls;

import com.cpmc.ls.models.ComponentOperation;
import com.cpmc.ls.models.DeviceOperation;
import com.cpmc.ls.models.Operation;
import nu.xom.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {

            // generate XML for device operation
/*
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("material", "PLA");
            parameters.put("targetExtTemp", "200");
            parameters.put("targetBedTemp", "30");
            parameters.put("quantity", "1");
            parameters.put("OBJnAME", "Clip");
            DeviceOperation deviceOperation = new DeviceOperation();
            deviceOperation.setDeviceId("Ultimaker01");
            deviceOperation.setOperationId("startJob");
            deviceOperation.setParameters(parameters);
            generateDeviceOperationForJobs(deviceOperation);
*/

            // generate XML for component operation
            Operation operation = new Operation();
            operation.setDeviceId("Ultimaker01");
            operation.setUuid("P2673");
            List<ComponentOperation> componentOperations = new ArrayList<>();
            ComponentOperation co1 = new ComponentOperation();
            co1.setName("X");
            co1.setComponentId("x");
            co1.setOperationId("moveX");
            co1.setComponent("Linear");
            co1.setComponentValue("100.0");
            co1.setParameters(new HashMap<String, String>());
            componentOperations.add(co1);
            ComponentOperation co2 = new ComponentOperation();
            co2.setName("Y");
            co2.setComponentId("y");
            co2.setOperationId("moveY");
            co2.setComponent("Linear");
            co2.setComponentValue("-50.0");
            co2.setParameters(new HashMap<String, String>());
            componentOperations.add(co2);
            ComponentOperation co3 = new ComponentOperation();
            co3.setName("Z");
            co3.setComponentId("extruder");
            co3.setOperationId("changeExtruderTemp");
            co3.setComponent("Sensor");
            co3.setComponentValue("210.0");
            co3.setParameters(new HashMap<String, String>());
            componentOperations.add(co3);
            operation.setComponentOperations(componentOperations);

            generateComponentOperationForActions(operation);

        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate XML for deviceoperation for jobs
     */
    private static void generateDeviceOperationForJobs(DeviceOperation deviceOperation) throws ParsingException, IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        // get the empty operation XML as root
        Builder parser = new Builder();

        InputStream operationsXmlStream = classloader.getResourceAsStream("Operations.xml");
        Document operationDoc = parser.build(operationsXmlStream);
        operationsXmlStream.close();

        InputStream probeXmlStream = classloader.getResourceAsStream("printer_Config.xml");
        Document probeDoc = parser.build(probeXmlStream);
        probeXmlStream.close();

        // construct Operations element
        Element opElement = new Element("Operations");

        // construct header element
        Element headerElement = new Element("Header");
        headerElement.addAttribute(new Attribute("bufferSize", "10"));
        headerElement.addAttribute(new Attribute("instanceId", "1"));
        headerElement.addAttribute(new Attribute("creationTime", "2017-01-18T12:00:00"));
        headerElement.addAttribute(new Attribute("sender", "Ultimaker2"));
        headerElement.addAttribute(new Attribute("version", "0.1"));
        headerElement.addAttribute(new Attribute("firstSequence", "9"));
        headerElement.addAttribute(new Attribute("lastSequence", "9"));
        headerElement.addAttribute(new Attribute("nextSequence", "10"));
        operationDoc.getRootElement().appendChild(headerElement);

        // construct Device element
        Element deviceElement = new Element("Device");
        deviceElement.addAttribute(new Attribute("name", "Ultimaker2"));
        deviceElement.addAttribute(new Attribute("uuid", "P2673"));

        // construct DeviceOperation element
        Element devOpElement = new Element("DeviceOperation");
        devOpElement.addAttribute(new Attribute("id", "Ultimaker2"));
        devOpElement.addAttribute(new Attribute("name", "Ultimaker2 3D Printer"));

        // construct Jobs element
        Element jobsElement = new Element("Jobs");

        // get operation by XPATH
        XPathContext context = new XPathContext("xmlns", "urn:mtconnect.org:MTConnectDevices:1.2");
        Nodes deviceOperations = probeDoc.query("/xmlns:MTConnectDevices/xmlns:Devices/xmlns:Device[@id=\""
                        + deviceOperation.getDeviceId() + "\"]/xmlns:Operations//xmlns:Operation[@id=\""
                        + deviceOperation.getOperationId() + "\"]",
                context);
        Element deviceOperationElement = (Element) deviceOperations.get(0);

        // Find the type of operation to create XML element
        String deviceOperationType = deviceOperationElement.getAttributeValue("type");
        String tagName = properCase(deviceOperationType);
        Element jobElement = new Element(tagName);
        jobElement.addAttribute(new Attribute("operationId", deviceOperation.getOperationId()));
        jobElement.addAttribute(new Attribute("name", deviceOperationElement.getAttributeValue("name")));
        jobElement.addAttribute(new Attribute("sequence", "1"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        jobElement.addAttribute(new Attribute("timestamp", sdf.format(new Date())));

        // create all parameters
        Nodes probeJobParamters = deviceOperationElement.query("/xmlns:MTConnectDevices" +
                "/xmlns:Devices/xmlns:Device[@id=\""
                + deviceOperation.getDeviceId() + "\"]/xmlns:Operations/xmlns:Operation[@id=\""
                + deviceOperation.getOperationId()
                + "\"]/xmlns:Parameters//xmlns:Parameter", context);
        Element parameters = new Element("Parameters");
        for (int i=0; i<probeJobParamters.size(); i++) {
            Element probeJobParameter = (Element) probeJobParamters.get(i);
            String parameterName = properCase(probeJobParameter.getAttributeValue("type"));
            Element parameter = new Element(parameterName);
            parameter.addAttribute(new Attribute("id", probeJobParameter.getAttributeValue("id")));
            parameter.addAttribute(new Attribute("name", probeJobParameter.getAttributeValue("name")));
            parameter.addAttribute(new Attribute("timestamp", sdf.format(new Date())));
            parameter.appendChild(deviceOperation.getParameters().get(probeJobParameter.getAttributeValue("id")));
            parameters.appendChild(parameter);
        }
        jobElement.appendChild(parameters);
        jobsElement.appendChild(jobElement);
        devOpElement.appendChild(jobsElement);
        deviceElement.appendChild(devOpElement);
        opElement.appendChild(deviceElement);

        // add child elements to root
        operationDoc.getRootElement().appendChild(opElement);

        prettyPrint(operationDoc);
    }

    /**
     * generate XML for deviceoperation for actions
     */
    private static void generateDeviceOperationForActions() {

    }

    /**
     * generate XML for component operation for jobs
     */
    private static void generateComponentOperationForJobs() {

    }

    /**
     * generate XML for component operation for actions
     */
    private static void generateComponentOperationForActions(Operation operation)
            throws IOException, ParsingException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        // get the empty operation XML as root
        Builder parser = new Builder();
        InputStream is = classloader.getResourceAsStream("Operations.xml");
        Document operationDoc = parser.build(is);
        is.close();

        InputStream probeXmlStream = classloader.getResourceAsStream("printer_Config.xml");
        Document probeDoc = parser.build(probeXmlStream);
        probeXmlStream.close();

        // construct Operations element
        Element opElement = new Element("Operations");

        // construct header element
        Element headerElement = new Element("Header");
        headerElement.addAttribute(new Attribute("bufferSize", "10"));
        headerElement.addAttribute(new Attribute("instanceId", "1"));
        headerElement.addAttribute(new Attribute("creationTime", "2017-01-18T12:00:00"));
        headerElement.addAttribute(new Attribute("sender", operation.getDeviceId()));
        headerElement.addAttribute(new Attribute("version", "0.1"));
        headerElement.addAttribute(new Attribute("firstSequence", "9"));
        headerElement.addAttribute(new Attribute("lastSequence", "9"));
        headerElement.addAttribute(new Attribute("nextSequence", "10"));
        operationDoc.getRootElement().appendChild(headerElement);

        // construct Device element
        Element deviceElement = new Element("Device");
        deviceElement.addAttribute(new Attribute("name", operation.getDeviceId()));
        deviceElement.addAttribute(new Attribute("uuid", operation.getUuid()));

        XPathContext context = new XPathContext("xmlns", "urn:mtconnect.org:MTConnectDevices:1.2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        int sequence = 0;

        for (ComponentOperation componentOperation : operation.getComponentOperations()) {
            // get operation by XPATH
            Nodes probeCompOpNodes = probeDoc.query("//xmlns:Operation[@id=\""
                            + componentOperation.getOperationId() + "\"]", context);
            Element probeCompOpElement = (Element) probeCompOpNodes.get(0);
            System.out.println(probeCompOpElement.toXML());

            // construct DeviceOperation element
            //component="Linear" componentId="x" name="X"
            Element compOpElement = new Element("ComponentOperation");
            compOpElement.addAttribute(new Attribute("component", componentOperation.getComponent()));
            compOpElement.addAttribute(new Attribute("componentId", componentOperation.getComponentId()));
            compOpElement.addAttribute(new Attribute("name", componentOperation.getName()));

            // construct Actions element
            Element actionsElement = new Element("Actions");

            String elementName = properCase(probeCompOpElement.getAttributeValue("type"));
            Element actionElement = new Element(elementName);
            actionElement.addAttribute(new Attribute("operationId", componentOperation.getOperationId()));
            actionElement.addAttribute(new Attribute("name", probeCompOpElement.getAttributeValue("name")));
            actionElement.addAttribute(new Attribute("sequence", Integer.toString(++sequence)));
            actionElement.addAttribute(new Attribute("timestamp", sdf.format(new Date())));
            actionElement.appendChild(componentOperation.getComponentValue());

            actionsElement.appendChild(actionElement);
            compOpElement.appendChild(actionsElement);
            deviceElement.appendChild(compOpElement);
        }

        opElement.appendChild(deviceElement);
        operationDoc.getRootElement().appendChild(opElement);

        prettyPrint(operationDoc);
    }

    private static void prettyPrint(Document document) throws IOException {
        System.out.println();
        Serializer serializer = new Serializer(System.out, "UTF-8");
        serializer.setIndent(4);
        serializer.write(document);
        System.out.println();
    }

    private static String properCase(String type) {
        // Empty strings should be returned as-is.
        if (type.length() == 0) return "";
        // Strings with only one character uppercased.
        if (type.length() == 1) return type.toUpperCase();
        // Otherwise uppercase first letter, lowercase the rest.
        return type.substring(0,1).toUpperCase()
                + type.substring(1).toLowerCase();
    }
}
