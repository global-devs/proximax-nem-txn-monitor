# NEM Transaction Monitor

Java library for monitoring transactions in NEM Blockchain platform

<h2>Usage</h2>

Simply run your Java code with the following.

```java

WsNemTransactionMonitor.networkName("mijinnet").host("a1.nem.foundation").port("7895").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y")
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitor())
	.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new TransactionMonitor())
	.monitor();
			
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

<h2>Monitor and Handle Transactions</h2>
TBD
<h2>Monitor and Handle MultisigTransaction</h2>
TBD
<sub>Copyright (c) 2017</sub>
