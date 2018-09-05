package io.nem.monitor.handler;

import io.nem.monitor.ITransactionMonitor;

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
}
