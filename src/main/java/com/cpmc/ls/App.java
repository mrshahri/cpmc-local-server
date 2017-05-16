package com.cpmc.ls;

import nu.xom.*;

import java.io.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            generateDeviceOperationForJobs();
            generateComponentOperationForActions();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate XML for deviceoperation for jobs
     */
    private static void generateDeviceOperationForJobs() throws ParsingException, IOException {
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
        Element devOpElement = new Element("DeviceOperation");
        devOpElement.addAttribute(new Attribute("id", "Ultimaker2"));
        devOpElement.addAttribute(new Attribute("name", "Ultimaker2 3D Printer"));

        // construct Jobs element
        Element jobsElement = new Element("Jobs");

        // create a Printing Job element
        Element printJobElement = new Element("Printing");
        printJobElement.addAttribute(new Attribute("operationId", "startJob"));
        printJobElement.addAttribute(new Attribute("name", "Start new job"));
        printJobElement.addAttribute(new Attribute("sequence", "9"));
        printJobElement.addAttribute(new Attribute("timestamp", "2017-01-18T05:45:40"));

        // construct parameters element
        Element parametersElement = new Element("Parameters");

        // create parameter elements
        Element p1Element = new Element("Material");
        p1Element.addAttribute(new Attribute("id", "material"));
        p1Element.addAttribute(new Attribute("name", "Material Type"));
        p1Element.addAttribute(new Attribute("timestamp", "2017-01-18T05:45:40"));
        p1Element.appendChild("PLA");

        Element p2Element = new Element("Quantity");
        p2Element.addAttribute(new Attribute("id", "quantity"));
        p2Element.addAttribute(new Attribute("name", "Number of objects"));
        p2Element.addAttribute(new Attribute("timestamp", "2017-01-18T05:45:40"));
        p2Element.appendChild("1");

        Element p3Element = new Element("Object");
        p3Element.addAttribute(new Attribute("id", "objName"));
        p3Element.addAttribute(new Attribute("name", "Object Name"));
        p3Element.addAttribute(new Attribute("timestamp", "2017-01-18T05:45:40"));
        p3Element.appendChild("Clip");

        // append child and grand child to operations
        parametersElement.appendChild(p1Element);
        parametersElement.appendChild(p2Element);
        parametersElement.appendChild(p3Element);
        printJobElement.appendChild(parametersElement);
        jobsElement.appendChild(printJobElement);
        devOpElement.appendChild(jobsElement);
        deviceElement.appendChild(devOpElement);
        opElement.appendChild(deviceElement);
        // add child elements to root
        doc.getRootElement().appendChild(opElement);

        prettyPrint(doc);
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
}
