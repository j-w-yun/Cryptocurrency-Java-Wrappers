package org.jaewanyun.wrapper.etherscan.data;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Transaction receipt data
 */
public class TransactionReceipt implements AutoDataAdder {

	public String blockHash;
	public String blockNumber;
	public String contractAddress;
	public String cumulativeGasUsed;
	public String gasUsed;

	// Logs
	public String address;
	public String data;
	public String logIndex;
	public String topics;
	public String transactionHash;
	public String transactionIndex;
	public String transactionLogIndex;
	public String type;
	public String logsBloom;
	public String root;

	public TransactionReceipt() {}

	public TransactionReceipt(String blockHash,
			String blockNumber,
			String contractAddress,
			String cumulativeGasUsed,
			String gasUsed,
			String address,
			String data,
			String logIndex,
			String topics,
			String transactionHash,
			String transactionIndex,
			String transactionLogIndex,
			String type,
			String logsBloom,
			String root) {
		this.blockHash = blockHash;
		this.blockNumber = blockNumber;
		this.contractAddress = contractAddress;
		this.cumulativeGasUsed = cumulativeGasUsed;
		this.gasUsed = gasUsed;
		this.address = address;
		this.data = data;
		this.logIndex = logIndex;
		this.topics = topics;
		this.transactionHash = transactionHash;
		this.transactionIndex = transactionIndex;
		this.transactionLogIndex = transactionLogIndex;
		this.type = type;
		this.logsBloom = logsBloom;
		this.root = root;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("blockHash"))
			this.blockHash = data.getValue().toString();
		else if(data.getName().toString().equals("blockNumber"))
			this.blockNumber = data.getValue().toString();
		else if(data.getName().toString().equals("contractAddress"))
			this.contractAddress = data.getValue().toString();
		else if(data.getName().toString().equals("cumulativeGasUsed"))
			this.cumulativeGasUsed = data.getValue().toString();
		else if(data.getName().toString().equals("gasUsed"))
			this.gasUsed = data.getValue().toString();
		else if(data.getName().toString().equals("address"))
			this.address = data.getValue().toString();
		else if(data.getName().toString().equals("data"))
			this.data = data.getValue().toString();
		else if(data.getName().toString().equals("logIndex"))
			this.logIndex = data.getValue().toString();
		else if(data.getName().toString().equals("topics"))
			this.topics = data.getValue().toString();
		else if(data.getName().toString().equals("transactionHash"))
			this.transactionHash = data.getValue().toString();
		else if(data.getName().toString().equals("transactionIndex"))
			this.transactionIndex = data.getValue().toString();
		else if(data.getName().toString().equals("transactionLogIndex"))
			this.transactionLogIndex = data.getValue().toString();
		else if(data.getName().toString().equals("type"))
			this.type = data.getValue().toString();
		else if(data.getName().toString().equals("logsBloom"))
			this.logsBloom = data.getValue().toString();
		else if(data.getName().toString().equals("root"))
			this.root = data.getValue().toString();

	}

	@Override
	public String toString() {
		return "{\"blockHash\":" + blockHash + "," +
				"\"blockNumber\":" + blockNumber + "," +
				"\"contractAddress\":" + contractAddress + "," +
				"\"cumulativeGasUsed\":" + cumulativeGasUsed + "," +
				"\"gasUsed\":" + gasUsed + "," +
				"\"address\":" + address + "," +
				"\"data\":" + data + "," +
				"\"logIndex\":" + logIndex + "," +
				"\"topics\":" + topics + "," +
				"\"transactionHash\":" + transactionHash + "," +
				"\"transactionIndex\":" + transactionIndex + "," +
				"\"transactionLogIndex\":" + transactionLogIndex + "," +
				"\"type\":" + type + "," +
				"\"logsBloom\":" + logsBloom + "," +
				"\"root\":" + root + "}";
	}

}
