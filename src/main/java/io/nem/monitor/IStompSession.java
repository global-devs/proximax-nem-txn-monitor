/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.nem.monitor;

import io.nem.model.ChannelHandleModel;
import java.util.List;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 *
 * @author thcao
 */
public interface IStompSession extends StompSessionHandler {
    public String getAddress();
    public void setAddress(String address);
    public List<ChannelHandleModel> getChannelHandleModels();
    public void setChannelHandleModels(List<ChannelHandleModel> channelHandleModels);    
    public void destroy();
}
