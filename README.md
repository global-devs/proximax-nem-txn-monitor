# NEM Transaction Monitor

<h2>Usage</h2>

Simply run your Java code with the following.

```java

WsNemTransactionMonitor.networkName("mijinnet").host("a1.nem.foundation").port("7895").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y")
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitor())
	.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new TransactionMonitor())
	.monitor();
			
```

Output 

```java
{"meta":{"innerHash":{},"id":0,"hash":{"data":"f142a61007c02325c1be36b1bd31bb104a08c137c65f3d7c0d3c733cddd96d08"},"height":382168},"transaction":{"timeStamp":76125237,"amount":1000000,"signature":"aec219d957f8a401b7892e12c42427c151f0b8ec86438b3fea669ffc1b23c18e263c3ae952108548fae39d5bd7206cbb229f0e2de4fbc81f2c07210aa4d1830b","fee":1000000,"recipient":"MDVJCH6F5FXVUOFCC3PZTSXPQNPCULYQMWEGAOOW","type":257,"deadline":76211637,"message":{},"version":1610612737,"signer":"8043f36622be5c91e00d9977c870935c887ff9050ba0a62207db76dba1a87385"}}
```

<h3>Custom Transaction Monitor</h3>
You can create your own handler to handle the incoming payload properly.

```java
public class CustomTransactionMonitor implements StompFrameHandler {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println(payload.toString()); // handle the payload.
		
	}

}
```

```java
WsNemTransactionMonitor.networkName("mijinnet").host("a1.nem.foundation").port("7895").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y")
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new CustomTransactionMonitor())
	.monitor();
```
<sub>Copyright (c) 2017</sub>
