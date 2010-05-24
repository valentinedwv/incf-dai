package org.incf.atlas.server.central.resource;

//import generated.Capabilities;
//import generated.ObjectFactory;
//import generated.ServiceIdentification;
//import generated.ServiceProvider;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.opengis.wps._1_0.ObjectFactory;
import net.opengis.wps._1_0.ProcessBriefType;
import net.opengis.wps._1_0.WPSCapabilitiesType;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class CapabilitiesResource extends Resource {
	
	private final Logger logger = LoggerFactory.getLogger(CapabilitiesResource.class);
	
	/*
	 * /incf-services/service/ABAServiceController?request=GetCapabilities&output=xml
	 */
	
	private String req;
	private String output;

	public CapabilitiesResource(Context context, Request request, Response response) {
		super(context, request, response);
		
		String query = (String) getRequest().getAttributes().get("query");
		Form queryString = new Form(query);
//		Form queryString = request.getEntityAsForm();
		req = queryString.getValues("request");
		output = queryString.getValues("output");
		
		logger.debug("*****query: {}", query);
		Set<String> names = queryString.getNames();
		Map<String, String> map = queryString.getValuesMap();
		for (String name : names) {
			logger.debug("name: {}, value: {}", name, map.get(name));
		}
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
		
		
//		logger.debug("req: {}, output: {}", req, output);
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		ObjectFactory of = new ObjectFactory();
		
		WPSCapabilitiesType capeType = of.createWPSCapabilitiesType();
		capeType.setService("test service");
		
		// block 1
//		ProcessBriefType pbt1 = of.createProcessBriefType();
//		pbt1.getProfile().add("http://abc.dev.ghi/jkl/profile-1a");
//		pbt1.getProfile().add("http://abc.dev.ghi/jkl/profile-1b");
//		pbt1.setProcessVersion("v1");
//		
//		ProcessBriefType pbt2 = of.createProcessBriefType();
//		pbt2.getProfile().add("http://abc.dev.ghi/jkl/profile-2a");
//		pbt2.getProfile().add("http://abc.dev.ghi/jkl/profile-2b");
//		pbt2.setProcessVersion("v2");
//		
//		capeType.getProcessOfferings().getProcess().add(pbt1);
//		capeType.getProcessOfferings().getProcess().add(pbt2);
		
		// block 2
//		ServiceIdentification serviceIdentification = 
//			of.createServiceIdentification();
//		serviceIdentification.setTitle("ABA Services");
//		serviceIdentification.setAbstract("ABA Services are created to access "
//				+ "various aba atlas space features that are offered by UCSD "
//				+ "to its clients.");
//		serviceIdentification.setServiceType("WPS");
//		serviceIdentification.setServiceVersion("0.2.4");
//		serviceIdentification.setFees("NONE");
//		serviceIdentification.setAccessConstraints("NONE");
//		
//		ServiceProvider serviceProvider = of.createServiceProvider();
//		serviceProvider.setProviderName("Asif Memon");
//		serviceProvider.setServiceContact("amemon@ncmir.ucsd.edu");
//		
//		Capabilities capabilities = of.createCapabilities();
//		capabilities.setServiceIdentification(serviceIdentification);
//		capabilities.setServiceProvider(serviceProvider);
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
//			return new JaxbRepresentation<Capabilities>(capabilities);
//			return new JaxbRepresentation<WPSCapabilitiesType>(capeType);
			JAXBElement<WPSCapabilitiesType> je = of.createCapabilities(capeType);
//			return getDomRepresentation(capeType);
			return getDomRepresentation(je);
		}
		
		return null;
	}
	
	// --------------------------------
	
	/**
	 * For the server. Get a Restlet DomRepresentation of the given object,
	 * which should be a generated class type.
	 * 
	 * @param object
	 *            - generated class type.
	 * @return - the DomRepresentation that is constructed from that generated
	 *         class type
	 * @see DomRepresentation
	 */
	@SuppressWarnings("unchecked")
	public static synchronized DomRepresentation getDomRepresentation(
			JAXBElement<WPSCapabilitiesType> je) {

		// Class typeClass = object.getClass();
		// return getDomRepresentation(object, typeClass);
		DomRepresentation representation = null;
		try {
			// create representation and get its empty dom
			representation = new DomRepresentation(MediaType.TEXT_XML);
			Document d = representation.getDocument();

			final Marshaller marshaller = getWBCJAXBContext()
					.createMarshaller();

			// marshal object into representation's dom
//			Class clazz = object.getClass();
//			QName qName = new QName(
////					"http://wholebrainproject.org/wbc/generated", 
//					"net.opengis.wps._1_0",
//					clazz.getSimpleName());
//			JAXBElement jaxbElement = new JAXBElement(qName, clazz, object);
//			marshaller.marshal(jaxbElement, d);
			marshaller.marshal(je, d);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return representation;
	}

//	/**
//	 * For the server. Get a Restlet DomRepresentation of the given object,
//	 * which should be a generated class type.
//	 * 
//	 * @param object
//	 *            - generated class type.
//	 * @return - the DomRepresentation that is constructed from that generated
//	 *         class type
//	 * @see DomRepresentation
//	 */
//	@SuppressWarnings("unchecked")
//	public static synchronized DomRepresentation getDomRepresentation(
//			Object object) {
//
//		// Class typeClass = object.getClass();
//		// return getDomRepresentation(object, typeClass);
//		DomRepresentation representation = null;
//		try {
//			// create representation and get its empty dom
//			representation = new DomRepresentation(MediaType.TEXT_XML);
//			Document d = representation.getDocument();
//
//			final Marshaller marshaller = getWBCJAXBContext()
//					.createMarshaller();
//
//			// marshal object into representation's dom
//			Class clazz = object.getClass();
//			QName qName = new QName(
////					"http://wholebrainproject.org/wbc/generated", 
//					"net.opengis.wps._1_0",
//					clazz.getSimpleName());
//			JAXBElement jaxbElement = new JAXBElement(qName, clazz, object);
//			marshaller.marshal(jaxbElement, d);
//
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//
//		return representation;
//	}

	protected static JAXBContext jaxbContext = null;

	/**
	 * Lazily instantiates a single version of the JAXBContext since it is slow
	 * to create and can be reused throughout the lifetime of the app.
	 * 
	 * @return
	 */
	public static JAXBContext getWBCJAXBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(
//				"org.wholebrainproject.wbc.generated");
				"net.opengis.wps._1_0");
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		return jaxbContext;
	}
    
}
