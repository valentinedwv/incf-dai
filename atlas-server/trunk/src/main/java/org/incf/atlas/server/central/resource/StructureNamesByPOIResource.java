package org.incf.atlas.server.central.resource;


import generated.ObjectFactory;
import generated.StructureNames;

import org.incf.atlas.server.central.util.DataInputs;
import org.restlet.Context;
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

public class StructureNamesByPOIResource extends Resource {

	private final Logger logger = LoggerFactory.getLogger(
			StructureNamesByPOIResource.class);
	
	private String dataInputString; 
	private DataInputs dataInputs;
	
	public StructureNamesByPOIResource(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		System.out.println("You are in");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );
		
		dataInputs = new DataInputs(dataInputString);
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {

		// text return for debugging
//		StringBuilder buf = new StringBuilder();
//		buf.append("dataInputs: ").append(dataInputString);
//		Set<String> dataInputKeys = dataInputs.getKeys();
//		for (String key : dataInputKeys) {
//			buf.append("\n  ").append(key).append(" = ");
//			buf.append(dataInputs.getValue(key));
//		}

		ObjectFactory of = new ObjectFactory();
		
		StructureNames structureNames = 
			of.createStructureNames(); 
		
		structureNames.setSrsCode(dataInputs.getValue("srsCode"));
		structureNames.setX(dataInputs.getValue("x"));
		structureNames.setY(dataInputs.getValue("y"));
		structureNames.setZ(dataInputs.getValue("z"));
		structureNames.setVocabulary(dataInputs.getValue("vocabulary"));
		structureNames.setFilter(dataInputs.getValue("filter"));

		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<StructureNames>(structureNames);
		}
		
		return null;
	}

}
