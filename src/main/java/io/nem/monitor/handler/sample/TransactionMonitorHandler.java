package io.nem.monitor.handler.sample;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.nem.core.model.mosaic.MosaicFeeInformation;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.primitive.Amount;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import io.nem.monitor.handler.AbstractTransactionMonitorHandler;
import io.nem.utils.Constants;
import io.nem.utils.HexStringUtils;
import io.nem.utils.KeyConvertor;
import io.nem.utils.NISQuery;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * The Class TransactionMonitorHandler.
 */
public class TransactionMonitorHandler extends AbstractTransactionMonitorHandler {
	

	/** The incoming file. */
	private static File incomingFile = new File("incoming_trans.txt");
	
	/** The outgoing file. */
	private static File outgoingFile = new File("outgoing_trans.txt");
	
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
		parse(address, payload.toString());
		
	}
	
	/**
	 * Parses the.
	 *
	 * @param address the address
	 * @param result the result
	 */
	private void parse(String address, String result) {
		
		System.out.println(">>>>");
		System.out.println(result);
		System.out.println(">>>>");
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(result);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String hash =
		String hash = json.getJSONObject("meta").getJSONObject("hash").getString("data");
		JSONObject transaction = json.getJSONObject("transaction");
		JSONObject outJSON = new JSONObject();
		if (transaction.containsKey("signatures")) { // multisig transaction
			JSONObject otherTrans = transaction.getJSONObject("otherTrans");
			String recipient = otherTrans.getString("recipient");
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(otherTrans.getString("signer")));
			outJSON.put("recipient", recipient);
			
			outJSON.put("amount", Amount.fromMicroNem(otherTrans.getLong("amount")).getNumNem());
			outJSON.put("date",
					dateFormat.format(new Date((otherTrans.getLong("timeStamp") + Constants.NEMSISTIME) * 1000)));
			// outJSON.put("hash", hash);
			// message
			if (otherTrans.containsKey("message") && otherTrans.getJSONObject("message").containsKey("type")) {
				JSONObject message = otherTrans.getJSONObject("message");
				// if message type is 1, convert to String
				if (message.getInt("type") == 1 && HexStringUtils.hex2String(message.getString("payload")) != null) {
					outJSON.put("message", HexStringUtils.hex2String(message.getString("payload")));
				}
			}
			// mosaic
//			if (otherTrans.containsKey("mosaics")) {
//				JSONArray outMosaicArray = new JSONArray();
//				JSONArray mosaics = otherTrans.getJSONArray("mosaics");
//				for (int i = 0; i < mosaics.size(); i++) {
//					JSONObject outMosaic = new JSONObject();
//					JSONObject mosaic = mosaics.getJSONObject(i);
//					long quantity = mosaic.getLong("quantity");
//					String namespace = mosaic.getJSONObject("mosaicId").getString("namespaceId");
//					String mosaicName = mosaic.getJSONObject("mosaicId").getString("name");
//					MosaicId mosaicId = new MosaicId(new NamespaceId(namespace), mosaicName);
//					MosaicFeeInformation m = NISQuery.findMosaicFeeInformationByNIS(mosaicId);
//					outMosaic.put("name", mosaicId.toString());
//					outMosaic.put("quantity", quantity / Math.pow(10, m.getDivisibility()));
//					outMosaicArray.add(outMosaic);
//				}
//				if (outMosaicArray.size() != 0) {
//					outJSON.put("mosaics", outMosaicArray);
//				}
//			}
			outJSON.put("hash",hash);
			outJSON.put("isMultisig", "1");
			try {
				if(!address.equals(recipient)){ // if not incoming transaction, return
					FileUtils.writeStringToFile(outgoingFile, outJSON.toString() + "\n",true);
				}else {
					FileUtils.writeStringToFile(incomingFile, outJSON.toString() + "\n",true);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(outJSON.toString());

		} else { // normal transaction
			String recipient = transaction.getString("recipient");
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(transaction.getString("signer")));
			outJSON.put("recipient", recipient);
			outJSON.put("amount", Amount.fromMicroNem(transaction.getLong("amount")).getNumNem());
			outJSON.put("date",
					dateFormat.format(new Date((transaction.getLong("timeStamp") + Constants.NEMSISTIME) * 1000)));
			// outJSON.put("hash", hash);
			// message
			if (transaction.containsKey("message") && transaction.getJSONObject("message").containsKey("type")) {
				JSONObject message = transaction.getJSONObject("message");
				// if message type is 1, convert to String
				if (message.getInt("type") == 1 && HexStringUtils.hex2String(message.getString("payload")) != null) {
					outJSON.put("message", HexStringUtils.hex2String(message.getString("payload")));
				}
			}
			
			// mosaic
//			if (transaction.containsKey("mosaics")) {
//				JSONArray outMosaicArray = new JSONArray();
//				JSONArray mosaics = transaction.getJSONArray("mosaics");
//				for (int i = 0; i < mosaics.size(); i++) {
//					JSONObject outMosaic = new JSONObject();
//					JSONObject mosaic = mosaics.getJSONObject(i);
//					long quantity = mosaic.getLong("quantity");
//					String namespace = mosaic.getJSONObject("mosaicId").getString("namespaceId");
//					String mosaicName = mosaic.getJSONObject("mosaicId").getString("name");
//					MosaicId mosaicId = new MosaicId(new NamespaceId(namespace), mosaicName);
//					MosaicFeeInformation m = NISQuery.findMosaicFeeInformationByNIS(mosaicId);
//					outMosaic.put("name", mosaicId.toString());
//					outMosaic.put("quantity", quantity / Math.pow(10, m.getDivisibility()));
//					outMosaicArray.add(outMosaic);
//				}
//				if (outMosaicArray.size() != 0) {
//					outJSON.put("mosaics", outMosaicArray);
//				}
//			}
			outJSON.put("hash",hash);
			outJSON.put("isMultisig", "0");
			System.out.println(outJSON.toString());
			try {
				if(!address.equals(recipient)){ // if not incoming transaction, return
					FileUtils.writeStringToFile(outgoingFile, outJSON.toString() + "\n",true);
				}else {
					FileUtils.writeStringToFile(incomingFile, outJSON.toString() + "\n",true);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	
	}
	
	/**
	 * monitor incoming transactions and output the transactions.
	 *
	 * @param address the address
	 * @param result the result
	 */
	private void monitor(String address, String result){
		
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(result);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String hash = json.getJSONObject("meta").getJSONObject("hash").getString("data");
		JSONObject transaction = json.getJSONObject("transaction");
		JSONObject outJSON = new JSONObject();
		if(transaction.containsKey("signatures")){ //multisig transaction
			JSONObject otherTrans = transaction.getJSONObject("otherTrans");
			String recipient = otherTrans.getString("recipient");
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(otherTrans.getString("signer")));
			outJSON.put("recipient", recipient);
			outJSON.put("amount", Amount.fromMicroNem(otherTrans.getLong("amount")).getNumNem());
			outJSON.put("date", dateFormat.format(new Date((otherTrans.getLong("timeStamp") + Constants.NEMSISTIME)*1000)));
			//outJSON.put("hash", hash);
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
			try {
				if(!address.equals(recipient)){ // if not incoming transaction, return
					FileUtils.writeStringToFile(outgoingFile, outJSON.toString() + "\n",true);
				}else {
					FileUtils.writeStringToFile(incomingFile, outJSON.toString() + "\n",true);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { //normal transaction
			String recipient = transaction.getString("recipient");
			outJSON.put("sender", KeyConvertor.getAddressFromPublicKey(transaction.getString("signer")));
			outJSON.put("recipient", recipient);
			outJSON.put("amount", Amount.fromMicroNem(transaction.getLong("amount")).getNumNem());
			outJSON.put("date", dateFormat.format(new Date((transaction.getLong("timeStamp") + Constants.NEMSISTIME)*1000)));
			//outJSON.put("hash", hash);
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
			try {
				if(!address.equals(recipient)){ // if not incoming transaction, return
					FileUtils.writeStringToFile(outgoingFile, outJSON.toString() + "\n",true);
				}else {
					FileUtils.writeStringToFile(incomingFile, outJSON.toString() + "\n",true);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
