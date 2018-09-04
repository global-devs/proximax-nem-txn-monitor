/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.nem.monitor;

import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 *
 * @author thcao
 */
public interface ITransactionMonitor extends StompSessionHandler {

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
