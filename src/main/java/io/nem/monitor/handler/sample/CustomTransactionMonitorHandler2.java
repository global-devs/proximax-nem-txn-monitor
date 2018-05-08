package io.nem.monitor.handler.sample;

import java.lang.reflect.Type;

import org.pmw.tinylog.Logger;
import org.springframework.messaging.simp.stomp.StompHeaders;

import io.nem.monitor.handler.AbstractTransactionMonitorHandler;


/**
 * The Class CustomTransactionMonitorHandler1.
 */
public class CustomTransactionMonitorHandler2 extends AbstractTransactionMonitorHandler {


	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompFrameHandler#getPayloadType(org.springframework.messaging.simp.stomp.StompHeaders)
	 */
	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompFrameHandler#handleFrame(org.springframework.messaging.simp.stomp.StompHeaders, java.lang.Object)
	 */
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		Logger.info("Handle your payload here");
	}

}
