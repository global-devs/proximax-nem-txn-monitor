package io.nem.monitor.handler;

import org.springframework.messaging.simp.stomp.StompFrameHandler;


/**
 * The Class AbstractTransactionMonitorHandler.
 */
public abstract class AbstractTransactionMonitorHandler implements StompFrameHandler {
	
	/** The address. */
	protected String address;
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
