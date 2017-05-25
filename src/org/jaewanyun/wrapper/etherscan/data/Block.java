package org.jaewanyun.wrapper.etherscan.data;

import java.util.ArrayList;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Block data
 */
public class Block implements AutoDataAdder {

	public String author;
	public String difficulty;
	public String extraData;
	public String gasLimit;
	public String gasUsed;
	public String hash;
	public String logsBloom;
	public String miner;
	public String mixHash;
	public String nonce;
	public String number;
	public String parentHash;
	public String receiptsRoot;
	public String sealFields;
	public String sha3Uncles;
	public String size;
	public String stateRoot;
	public String timeStamp;
	public String totalDifficulty;
	public String transactionsRoot;
	public ArrayList<Transaction> transactions;

	public Block() {
		this.transactions = new ArrayList<>();
	}

	public Block(
			String blockNumber,
			String author,
			String difficulty,
			String extraData,
			String gasLimit,
			String gasUsed,
			String hash,
			String logsBloom,
			String miner,
			String mixHash,
			String nonce,
			String number,
			String parentHash,
			String receiptsRoot,
			String sealFields,
			String sha3Uncles,
			String size,
			String stateRoot,
			String timeStamp,
			String totalDifficulty,
			String transactionsRoot,
			Transaction transaction) {
		this();
		this.author = author;
		this.difficulty = difficulty;
		this.extraData = extraData;
		this.gasLimit = gasLimit;
		this.gasUsed = gasUsed;
		this.hash = hash;
		this.logsBloom = logsBloom;
		this.miner = miner;
		this.mixHash = mixHash;
		this.nonce = nonce;
		this.number = number;
		this.parentHash = parentHash;
		this.receiptsRoot = receiptsRoot;
		this.sealFields = sealFields;
		this.sha3Uncles = sha3Uncles;
		this.size = size;
		this.stateRoot = stateRoot;
		this.timeStamp = timeStamp;
		this.totalDifficulty = totalDifficulty;
		this.transactionsRoot = transactionsRoot;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("author"))
			this.author = data.getValue().toString();
		else if(data.getName().toString().equals("difficulty"))
			this.difficulty = data.getValue().toString();
		else if(data.getName().toString().equals("extraData"))
			this.extraData = data.getValue().toString();
		else if(data.getName().toString().equals("gasLimit"))
			this.gasLimit = data.getValue().toString();
		else if(data.getName().toString().equals("gasUsed"))
			this.gasUsed = data.getValue().toString();
		else if(data.getName().toString().equals("hash") && this.hash == null)
			this.hash = data.getValue().toString();
		else if(data.getName().toString().equals("logsBloom"))
			this.logsBloom = data.getValue().toString();
		else if(data.getName().toString().equals("miner"))
			this.miner = data.getValue().toString();
		else if(data.getName().toString().equals("mixHash"))
			this.mixHash = data.getValue().toString();
		else if(data.getName().toString().equals("nonce") && this.nonce == null)
			this.nonce = data.getValue().toString();
		else if(data.getName().toString().equals("number"))
			this.number = data.getValue().toString();
		else if(data.getName().toString().equals("parentHash"))
			this.parentHash = data.getValue().toString();
		else if(data.getName().toString().equals("receiptsRoot"))
			this.receiptsRoot = data.getValue().toString();
		else if(data.getName().toString().equals("sealFields"))
			this.sealFields = data.getValue().toString();
		else if(data.getName().toString().equals("sha3Uncles"))
			this.sha3Uncles = data.getValue().toString();
		else if(data.getName().toString().equals("size"))
			this.size = data.getValue().toString();
		else if(data.getName().toString().equals("stateRoot"))
			this.stateRoot = data.getValue().toString();
		else if(data.getName().toString().equals("timestamp"))
			this.timeStamp = data.getValue().toString();
		else if(data.getName().toString().equals("totalDifficulty"))
			this.totalDifficulty = data.getValue().toString();
		else if(data.getName().toString().equals("transactionsRoot"))
			this.transactionsRoot = data.getValue().toString();
		else {

			if(data.getName().toString().equals("blockHash")) {
				transactions.add(new Transaction());
				transactions.get(transactions.size()-1).addData(data);
				transactions.get(transactions.size()-1).addData(new CustomNameValuePair<String, String>("timeStamp", this.timeStamp));
			} else if(!transactions.isEmpty()) { // Skip header jargon
				transactions.get(transactions.size()-1).addData(data);
			} else if(transactions.isEmpty() && data.getName().toString().equals("transactionsRoot")) {
				transactions.add(new Transaction());
				transactions.get(transactions.size()-1).addData(data);
				transactions.get(transactions.size()-1).addData(new CustomNameValuePair<String, String>("blockNumber", this.number));
				transactions.get(transactions.size()-1).addData(new CustomNameValuePair<String, String>("timeStamp", this.timeStamp));
				transactions.get(transactions.size()-1).addData(new CustomNameValuePair<String, String>("blockHash", this.hash));
			}

		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\"author\":" + author + System.lineSeparator() +
				"\"difficulty\":" + difficulty + System.lineSeparator() +
				"\"extraData\":" + extraData + System.lineSeparator() +
				"\"gasLimit\":" + gasLimit + System.lineSeparator() +
				"\"gasUsed\":" + gasUsed + System.lineSeparator() +
				"\"hash\":" + hash + System.lineSeparator() +
				"\"logsBloom\":" + logsBloom + System.lineSeparator() +
				"\"miner\":" + miner + System.lineSeparator() +
				"\"mixHash\":" + mixHash + System.lineSeparator() +
				"\"nonce\":" + nonce + System.lineSeparator() +
				"\"number\":" + number + System.lineSeparator() +
				"\"parentHash\":" + parentHash + System.lineSeparator() +
				"\"receiptsRoot\":" + receiptsRoot + System.lineSeparator() +
				"\"sealFields\":" + sealFields + System.lineSeparator() +
				"\"sha3Uncles\":" + sha3Uncles + System.lineSeparator() +
				"\"size\":" + size + System.lineSeparator() +
				"\"stateRoot\":" + stateRoot + System.lineSeparator() +
				"\"timeStamp\":" + timeStamp + System.lineSeparator() +
				"\"totalDifficulty\":" + totalDifficulty + System.lineSeparator() +
				"\"transactionsRoot\":" + transactionsRoot + System.lineSeparator() + System.lineSeparator());

		for(Transaction each : transactions)
			sb.append(each + System.lineSeparator() + System.lineSeparator());

		return sb.toString();

	}

}
