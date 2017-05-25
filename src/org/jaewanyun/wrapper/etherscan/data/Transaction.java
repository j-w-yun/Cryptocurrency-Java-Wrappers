package org.jaewanyun.wrapper.etherscan.data;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Transaction data
 */
public class Transaction implements AutoDataAdder {

	public String blockNumber;
	public String timeStamp;
	public String hash;
	public String nonce;
	public String blockHash;
	public String transactionIndex;
	public String from;
	public String to;
	public String value;
	public String gas;
	public String gasPrice;
	public boolean isError;
	public String input;
	public String contractAddress;
	public String cumulativeGasUsed;
	public String gasUsed;
	public String confirmations;

	// Used in Block from module geth
	public String condition;
	public String creates;
	public String networkId;
	public String publicKey;
	public String r;
	public String raw;
	public String s;
	public String standardV;
	public String v;

	public Transaction() {}

	public Transaction(String blockNumber,
			String timeStamp,
			String hash,
			String nonce,
			String blockHash,
			String transactionIndex,
			String from,
			String to,
			String value,
			String gas,
			String gasPrice,
			boolean isError,
			String input,
			String contractAddress,
			String cumulativeGasUsed,
			String gasUsed,
			String confirmations,
			String condition,
			String creates,
			String networkId,
			String publicKey,
			String r,
			String raw,
			String s,
			String standardV,
			String v) {
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.from = from;
		this.to = to;
		this.value = value;
		this.gas = gas;
		this.gasPrice = gasPrice;
		this.isError = isError;
		this.input = input;
		this.contractAddress = contractAddress;
		this.cumulativeGasUsed = cumulativeGasUsed;
		this.gasUsed = gasUsed;
		this.confirmations = confirmations;

		// Used in Block from module geth
		this.condition = condition;
		this.creates = creates;
		this.networkId = networkId;
		this.publicKey = publicKey;
		this.r = r;
		this.raw = raw;
		this.s = s;
		this.standardV = standardV;
		this.v = v;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("blockNumber"))
			this.blockNumber = data.getValue().toString();
		else if(data.getName().toString().equals("timeStamp"))
			this.timeStamp = data.getValue().toString();
		else if(data.getName().toString().equals("hash"))
			this.hash = data.getValue().toString();
		else if(data.getName().toString().equals("nonce"))
			this.nonce = data.getValue().toString();
		else if(data.getName().toString().equals("blockHash"))
			this.blockHash = data.getValue().toString();
		else if(data.getName().toString().equals("transactionIndex"))
			this.transactionIndex = data.getValue().toString();
		else if(data.getName().toString().equals("from"))
			this.from = data.getValue().toString();
		else if(data.getName().toString().equals("to"))
			this.to = data.getValue().toString();
		else if(data.getName().toString().equals("value"))
			this.value = data.getValue().toString();
		else if(data.getName().toString().equals("gas"))
			this.gas = data.getValue().toString();
		else if(data.getName().toString().equals("gasPrice"))
			this.gasPrice = data.getValue().toString();
		else if(data.getName().toString().equals("isError"))
			this.isError = data.getValue().toString().equals("1") ? true : false;
		else if(data.getName().toString().equals("input"))
			this.input = data.getValue().toString();
		else if(data.getName().toString().equals("contractAddress"))
			this.contractAddress = data.getValue().toString();
		else if(data.getName().toString().equals("cumulativeGasUsed"))
			this.cumulativeGasUsed = data.getValue().toString();
		else if(data.getName().toString().equals("gasUsed"))
			this.gasUsed = data.getValue().toString();
		else if(data.getName().toString().equals("confirmations"))
			this.confirmations = data.getValue().toString();

		// Used in Block from module geth
		else if(data.getName().toString().equals("condition"))
			this.condition = data.getValue().toString();
		else if(data.getName().toString().equals("creates"))
			this.creates = data.getValue().toString();
		else if(data.getName().toString().equals("networkId"))
			this.networkId = data.getValue().toString();
		else if(data.getName().toString().equals("publicKey"))
			this.publicKey = data.getValue().toString();
		else if(data.getName().toString().equals("r"))
			this.r = data.getValue().toString();
		else if(data.getName().toString().equals("raw"))
			this.raw = data.getValue().toString();
		else if(data.getName().toString().equals("s"))
			this.s = data.getValue().toString();
		else if(data.getName().toString().equals("standardV"))
			this.standardV = data.getValue().toString();
		else if(data.getName().toString().equals("v"))
			this.v = data.getValue().toString();

	}

	@Override
	public String toString() {

		return "\"blockHash\":" + blockHash + System.lineSeparator() +
				"\"blockNumber\":" + blockNumber + System.lineSeparator() +
				"\"condition\":" + condition + System.lineSeparator() +
				"\"creates\":" + creates + System.lineSeparator() +
				"\"from\":" + from + System.lineSeparator() +
				"\"gas\":" + gas + System.lineSeparator() +
				"\"gasPrice\":" + gasPrice + System.lineSeparator() +
				"\"hash\":" + hash + System.lineSeparator() +
				"\"input\":" + input + System.lineSeparator() +
				"\"networkId\":" + networkId + System.lineSeparator() +
				"\"nonce\":" + nonce + System.lineSeparator() +
				"\"publicKey\":" + publicKey + System.lineSeparator() +
				"\"r\":" + r + System.lineSeparator() +
				"\"raw\":" + raw + System.lineSeparator() +
				"\"s\":" + s + System.lineSeparator() +
				"\"standardV\":" + standardV + System.lineSeparator() +
				"\"to\":" + to + System.lineSeparator() +
				"\"transactionIndex\":" + transactionIndex + System.lineSeparator() +
				"\"v\":" + v + System.lineSeparator() +
				"\"value\":" + value;

		//		return "{\"blockNumber\":" + blockNumber + "," +
		//				"\"timeStamp\":" + timeStamp + "," +
		//				"\"hash\":" + hash + "," +
		//				"\"nonce\":" + nonce + "," +
		//				"\"blockHash\":" + blockHash + "," +
		//				"\"transactionIndex\":" + transactionIndex + "," +
		//				"\"from\":" + from + "," +
		//				"\"to\":" + to + "," +
		//				"\"gas\":" + gas + "," +
		//				"\"gasPrice\":" + gasPrice + "," +
		//				"\"isError\":" + isError + "," +
		//				"\"input\":" + input + "," +
		//				"\"contractAddress\":" + contractAddress + "," +
		//				"\"cumulativeGasUsed\":" + cumulativeGasUsed + "," +
		//				"\"gasUsed\":" + gasUsed + "," +
		//				"\"confirmations\":" + confirmations + "," +
		//
		//				"\"condition\":" + condition + "," +
		//				"\"creates\":" + creates + "," +
		//				"\"networkId\":" + networkId + "," +
		//				"\"publicKey\":" + publicKey + "," +
		//				"\"r\":" + r + "," +
		//				"\"raw\":" + raw + "," +
		//				"\"s\":" + s + "," +
		//				"\"standardV\":" + standardV + "," +
		//				"\"v\":" + v + "," +
		//				"\"transactionsRoot\":" + transactionsRoot + "}";
	}

}
