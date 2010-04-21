package edu.crbs.incfdemo.server.resource;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucsd.crbs.incfdemo.Capabilities;
import edu.ucsd.crbs.incfdemo.ObjectFactory;
import edu.ucsd.crbs.incfdemo.ServiceIdentification;
import edu.ucsd.crbs.incfdemo.ServiceProvider;

public class TestResource extends Resource {
	
	private final Logger logger = LoggerFactory.getLogger(TestResource.class);
	
	/*
	 * /incf-services/service/ABAServiceController?request=GetCapabilities&output=xml
	 */
	
	private String req;
	private String output;

	public TestResource(Context context, Request request, Response response) {
		super(context, request, response);
		
		Form queryString = request.getEntityAsForm();
		req = queryString.getValues("request");
		output = queryString.getValues("output");
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
		
		logger.debug("req: {}, output: {}", req, output);
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
		
		ServiceIdentification serviceIdentification = 
			of.createServiceIdentification();
		serviceIdentification.setTitle("ABA Services");
		serviceIdentification.setAbstract("ABA Services are created to access "
				+ "various aba atlas space features that are offered by UCSD "
				+ "to its clients.");
		serviceIdentification.setServiceType("WPS");
		serviceIdentification.setServiceVersion("0.2.4");
		serviceIdentification.setFees("NONE");
		serviceIdentification.setAccessConstraints("NONE");
		
		ServiceProvider serviceProvider = of.createServiceProvider();
		serviceProvider.setProviderName("Asif Memon");
		serviceProvider.setServiceContact("amemon@ncmir.ucsd.edu");
		
		Capabilities capabilities = of.createCapabilities();
		capabilities.setServiceIdentification(serviceIdentification);
		capabilities.setServiceProvider(serviceProvider);
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<Capabilities>(capabilities);
		}
		
		return null;
	}

}
