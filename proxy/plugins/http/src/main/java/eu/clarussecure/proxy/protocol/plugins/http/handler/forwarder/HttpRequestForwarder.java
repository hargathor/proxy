/*
 * 
 */
package eu.clarussecure.proxy.protocol.plugins.http.handler.forwarder;

import eu.clarussecure.proxy.protocol.plugins.http.HttpServerPipelineInitializer;
import eu.clarussecure.proxy.protocol.plugins.http.HttpSession;
import eu.clarussecure.proxy.protocol.plugins.tcp.handler.forwarder.ClientMessageForwarder;
import io.netty.handler.codec.http.HttpObject;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpRequestForwarder.
 */
public class HttpRequestForwarder
		extends ClientMessageForwarder<HttpObject, HttpServerPipelineInitializer, HttpSession> {

	/**
	 * Instantiates a new http request forwarder.
	 */
	public HttpRequestForwarder() {
		super(HttpServerPipelineInitializer.class, HttpSession.class);
	}

}
