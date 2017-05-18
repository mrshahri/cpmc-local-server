package com.cpmc.ls;

import com.cpmc.ls.models.DeviceOperation;
import nu.xom.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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

            // generate XML for component operation
//            generateComponentOperationForActions();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        String tagName = getParameterName(deviceOperationType);
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
            String parameterName = getParameterName(probeJobParameter.getAttributeValue("type"));
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
    private static void generateComponentOperationForActions() throws IOException, ParsingException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        // get the empty operation XML as root
        Builder parser = new Builder();
        InputStream is = classloader.getResourceAsStream("Operations.xml");
        Document doc = parser.build(is);
        is.close();

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
        doc.getRootElement().appendChild(headerElement);

        // construct Device element
        Element deviceElement = new Element("Device");
        deviceElement.addAttribute(new Attribute("name", "Ultimaker2"));
        deviceElement.addAttribute(new Attribute("uuid", "P2673"));

        // construct DeviceOperation element
        //component="Linear" componentId="x" name="X"
        Element compOpElement = new Element("ComponentOperation");
        compOpElement.addAttribute(new Attribute("component", "Linear"));
        compOpElement.addAttribute(new Attribute("componentId", "x"));
        compOpElement.addAttribute(new Attribute("name", "X"));
        // construct Jobs element
        Element actionsElement = new Element("Actions");
        Element a1Element = new Element("Position");
        a1Element.addAttribute(new Attribute("operationId", "moveX"));
        a1Element.addAttribute(new Attribute("name", "Move X Axis"));
        a1Element.addAttribute(new Attribute("sequence", "9"));
        a1Element.addAttribute(new Attribute("timestamp", "2017-01-18T06:16:39"));
        a1Element.appendChild("100.0");
        actionsElement.appendChild(a1Element);
        compOpElement.appendChild(actionsElement);

        // append child and grand child to operations
        deviceElement.appendChild(compOpElement);
        opElement.appendChild(deviceElement);
        // add child elements to root
        doc.getRootElement().appendChild(opElement);

        prettyPrint(doc);
    }

    private static void prettyPrint(Document document) throws IOException {
        System.out.println();
        Serializer serializer = new Serializer(System.out, "UTF-8");
        serializer.setIndent(4);
        serializer.write(document);
        System.out.println();
    }

    private static String getParameterName(String type) {
        return Character.toUpperCase(type.charAt(0)) + type.substring(1);
    }
}
