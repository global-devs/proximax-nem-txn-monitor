package io.nem.monitor.handler;

import org.springframework.messaging.simp.stomp.StompFrameHandler;

public abstract class AbstractTransactionMonitorHandler implements StompFrameHandler {
	
	protected String address;
	public void setAddress(String address) {
		this.address = address;
	}

}
