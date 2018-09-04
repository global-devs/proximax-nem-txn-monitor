package io.nem.monitor.handler;

import io.nem.monitor.ITransactionMonitor;
import org.pmw.tinylog.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * The Class AbstractTransactionMonitorHandler.
 */
public abstract class AbstractTransactionMonitorHandler implements ITransactionMonitor {

    /**
     * The address.
     */
    protected String address;

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }    
    
    @Override
    public void afterConnected(StompSession ss, StompHeaders sh) {
        Logger.error("Implement afterConnected");
        
    }

    @Override
    public void handleException(StompSession ss, StompCommand sc, StompHeaders sh, byte[] bytes, Throwable thrwbl) {
        Logger.error("Implement handleException");
    }

    @Override
    public void handleTransportError(StompSession ss, Throwable thrwbl) {
        Logger.error("Implement handleTransportError");
    }

}
