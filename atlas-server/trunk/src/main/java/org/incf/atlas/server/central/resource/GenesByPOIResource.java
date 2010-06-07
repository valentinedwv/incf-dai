package org.incf.atlas.server.central.resource;


import java.util.Set;

import org.incf.atlas.server.central.util.DataInputs;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenesByPOIResource extends Resource {
	
	private final Logger logger = LoggerFactory.getLogger(
			GenesByPOIResource.class);
	
	private String dataInputString;
	private String responseFormat;
	private DataInputs dataInputs;
	
	public GenesByPOIResource(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		String formatA = 
				(String) request.getAttributes().get("responseFormatA");
		if (formatA == null) {
			responseFormat = null;
		} else {
			responseFormat = formatA + "/" 
					+ (String) request.getAttributes().get("responseFormatB"); 
		}
		
		dataInputs = new DataInputs(dataInputString);
		
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
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
		StringBuilder buf = new StringBuilder();
		buf.append("dataInputs: ").append(dataInputString);
		Set<String> dataInputKeys = dataInputs.getKeys();
		for (String key : dataInputKeys) {
			buf.append("\n  ").append(key).append(" = ");
			buf.append(dataInputs.getValue(key));
		}
		buf.append("\nresponseFormat: ").append(responseFormat);
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
			return new StringRepresentation(buf.toString());
		}
		
		return null;
	}

}
