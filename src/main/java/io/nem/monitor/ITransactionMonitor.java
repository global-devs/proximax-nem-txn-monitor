package io.nem.monitor;

import org.springframework.messaging.simp.stomp.StompFrameHandler;

/**
 *
 * @author thcao
 */
public interface ITransactionMonitor extends StompFrameHandler {

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String address);

    /**
     * Get the address.
     *
     * @return address the address
     */
    public String getAddress();
}
