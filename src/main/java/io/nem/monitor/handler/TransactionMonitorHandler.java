package io.nem.monitor.handler;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nem.core.model.mosaic.MosaicFeeInformation;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.primitive.Amount;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import io.nem.model.ChannelHandleModel;
import io.nem.utils.Constants;
import io.nem.utils.HexStringUtils;
import io.nem.utils.KeyConvertor;
import io.nem.utils.NISQuery;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransactionMonitorHandler implements StompFrameHandler {
	
	private String address;
	
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		monitor(address, payload.toString());
		
	}
	
	/**
	 * monitor incoming transactions and output the transactions
	 * @param address
	 * @param result
	 * @return
	 */
	private void monitor(String address, String result){
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(result);
		} catch (Exception ex) {
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject transaction = json.getJSONObject("transaction");
		JSONObject outJSON = new JSONObject();
		if(transaction.containsKey("signatures")){ //multisig transaction
			JSONObject otherTrans = transaction.getJSONObject("otherTrans");
			String recipient = otherTrans.getString("recipient");
			if(!address.equals(recipient)){ // if not incoming transaction, return
				return;
			}
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(otherTrans.getString("signer")));
			outJSON.put("amount", Amount.fromMicroNem(otherTrans.getLong("amount")).getNumNem());
			outJSON.put("date", dateFormat.format(new Date((otherTrans.getLong("timeStamp") + Constants.NEMSISTIME)*1000)));
			// message 
			if(otherTrans.containsKey("message") && otherTrans.getJSONObject("message").containsKey("type")){
				JSONObject message = otherTrans.getJSONObject("message");
				// if message type is 1, convert to String
				if(message.getInt("type")==1 && HexStringUtils.hex2String(message.getString("payload"))!=null){
					outJSON.put("message", HexStringUtils.hex2String(message.getString("payload")));
				}
			}
			// mosaic
			if(otherTrans.containsKey("mosaics")){
				JSONArray outMosaicArray = new JSONArray();
				JSONArray mosaics = otherTrans.getJSONArray("mosaics");
				for(int i=0;i<mosaics.size();i++){
					JSONObject outMosaic = new JSONObject();
					JSONObject mosaic = mosaics.getJSONObject(i);
					long quantity = mosaic.getLong("quantity");
					String namespace = mosaic.getJSONObject("mosaicId").getString("namespaceId");
					String mosaicName = mosaic.getJSONObject("mosaicId").getString("name");
					MosaicId mosaicId = new MosaicId(new NamespaceId(namespace), mosaicName);
					MosaicFeeInformation m = NISQuery.findMosaicFeeInformationByNIS(mosaicId);
					outMosaic.put("name", mosaicId.toString());
					outMosaic.put("quantity", quantity / Math.pow(10, m.getDivisibility()));
					outMosaicArray.add(outMosaic);
				}
				if(outMosaicArray.size()!=0){
					outJSON.put("mosaics", outMosaicArray);
				}
			}
			outJSON.put("isMultisig", "1");
			System.out.println(outJSON.toString());
		} else { //normal transaction
			String recipient = transaction.getString("recipient");
			if(!address.equals(recipient)){ // if not incoming transaction, return
				return;
			}
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(transaction.getString("signer")));
			outJSON.put("amount", Amount.fromMicroNem(transaction.getLong("amount")).getNumNem());
			outJSON.put("date", dateFormat.format(new Date((transaction.getLong("timeStamp") + Constants.NEMSISTIME)*1000)));
			// message 
			if(transaction.containsKey("message") && transaction.getJSONObject("message").containsKey("type")){
				JSONObject message = transaction.getJSONObject("message");
				// if message type is 1, convert to String
				if(message.getInt("type")==1 && HexStringUtils.hex2String(message.getString("payload"))!=null){
					outJSON.put("message", HexStringUtils.hex2String(message.getString("payload")));
				}
			}
			// mosaic
			if(transaction.containsKey("mosaics")){
				JSONArray outMosaicArray = new JSONArray();
				JSONArray mosaics = transaction.getJSONArray("mosaics");
				for(int i=0;i<mosaics.size();i++){
					JSONObject outMosaic = new JSONObject();
					JSONObject mosaic = mosaics.getJSONObject(i);
					long quantity = mosaic.getLong("quantity");
					String namespace = mosaic.getJSONObject("mosaicId").getString("namespaceId");
					String mosaicName = mosaic.getJSONObject("mosaicId").getString("name");
					MosaicId mosaicId = new MosaicId(new NamespaceId(namespace), mosaicName);
					MosaicFeeInformation m = NISQuery.findMosaicFeeInformationByNIS(mosaicId);
					outMosaic.put("name", mosaicId.toString());
					outMosaic.put("quantity", quantity / Math.pow(10, m.getDivisibility()));
					outMosaicArray.add(outMosaic);
				}
				if(outMosaicArray.size()!=0){
					outJSON.put("mosaics", outMosaicArray);
				}
			}
			outJSON.put("isMultisig", "0");
			System.out.println(outJSON.toString());
		}
	}
	

}
