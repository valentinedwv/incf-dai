package org.incf.atlas.server.hubs.aba.server;

import org.incf.atlas.server.hubs.aba.resource.CoordinateTransformationChainResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class ServerApplication extends Application {

	private static final Logger logger = LoggerFactory.getLogger(
			ServerApplication.class);

	@Override
	public Restlet createRoot() {

		System.out.println("***CreateRoot***");
		Router router = new Router(getContext());

		router.attach("?Request=Execute&Identifier=TransformationChain"
				+ "&DataInputs={dataInputs}",
				CoordinateTransformationChainResource.class);

		return router;

	}

	public static void main(String[] args) {

		// redirect restlet logging to slf4j
		// see http://wiki.restlet.org/docs_1.1/13-restlet/48-restlet/101-restlet.html
		// "Replacing default JDK logging with log4j"
		SLF4JBridgeHandler.install();

		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("       *******************************************\n");
		buf.append("       *          Starting INCF Server           *\n");
		buf.append("       *******************************************\n");
		buf.append("\n  Server version   : ");
		buf.append("\nWaiting for connections ...");
		logger.info(buf.toString());
		
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, 8182);
			component.getDefaultHost().attach(new ServerApplication());
			component.start();
		} catch (Exception e) {
			logger.error("Exception in main().", e);
		}
	}

}