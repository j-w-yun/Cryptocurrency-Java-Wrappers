package org.jaewanyun.wrapper.examples;

import org.jaewanyun.wrapper.etherscan.Etherscan;

/**
 * Example methods that can be called from Etherscan Class
 */
public class EtherscanExample {

	public static void main(String[] args) {

		/*
		 * Initialize with API key, retrieved from etherscan_api_key in res/api_keys.txt
		 * Can be called multiple times without re-initialization
		 */
		Etherscan.Account myAccount = new Etherscan.Account();

		//		Balance balance = myAccount.etherBalance("0x1e55352143e3fb171e7fefec03282220bf39b463");
		//		System.out.println(balance);

		//		String[] addresses = {"0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a", "0x63a9975ba31b0b9626b34300f7f627147df1f526", "0x198ef1ec325a96cc354c7266a038be8b5c558f67"};
		//		Balance[] balances = myAccount.etherBalance(addresses);
		//		for(Balance each : balances)
		//			System.out.println(each);

		//		Transaction[] transactions = myAccount.txnsByAddress("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae", 0, 99999999);
		//		for(Transaction each : transactions)
		//			System.out.println(each);

		//		InternalTransaction[] transactions = myAccount.txnsByAddressInternal("0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae", 0, 99999999);
		//		for(InternalTransaction each : transactions)
		//			System.out.println(each);

		//		InternalTransaction[] transactions = myAccount.txnsByHashInternal("0x40eb908387324f2b575b4879cd9d7188f69c8fc9d87c901b9e2daaea4b442170");
		//		for(InternalTransaction each : transactions)
		//			System.out.println(each);

		//		LatestBlock[] blocks = myAccount.blocksMined("0x9dd134d14d1e65f84b706d6f205cd5b1cd03a46b");
		//		for(LatestBlock each : blocks)
		//			System.out.println(each);



		/*
		 * Initialize with API key, retrieved from etherscan_api_key in res/api_keys.txt
		 * Can be called multiple times without re-initialization
		 */
		Etherscan.Transactions myTransactions = new Etherscan.Transactions();

		//		ContractExecutionStatus status = myTransactions.checkContractExecutionStatus("0x15f8e5ea1079d9a0bb04a4c58ae5fe7654b5b2b4463375ff7ffb490aa0032f3a");
		//		System.out.println(status);



		/*
		 * Initialize with API key, retrieved from etherscan_api_key in res/api_keys.txt
		 * Can be called multiple times without re-initialization
		 */
		Etherscan.Geth myGeth = new Etherscan.Geth();

		//		int blockNumber = myGeth.blockNumber();
		//		System.out.println(blockNumber);

		//		Block block = myGeth.blockByNumber(blockNumber-1);
		//		System.out.println(block);

		//		Block block = myGeth.uncleByBlockNumberAndIndex(new BigInteger("2165403"), new BigInteger("0"));
		//		System.out.println(block);

		//		BigInteger transactionCount = myGeth.blockTransactionCountByNumber(new BigInteger("1112952"));
		//		System.out.println(transactionCount);

		//		Transaction transaction = myGeth.transactionByHash("0x1e2910a262b1008d0616a0beb24c1a491d78771baa54a33e66065e03b1f46bc1");
		//		System.out.println(transaction);

		//		Transaction transaction = myGeth.transactionByBlockNumberAndIndex(new BigInteger("68943"), new BigInteger("0"));
		//		System.out.println(transaction);

		//		BigInteger transactionCount = myGeth.transactionCount("0x2910543af39aba0cd09dbb2d50200b3e800a63d2");
		//		System.out.println(transactionCount);

		//		String result = myGeth.sendRawTransaction("0xf904808000831cfde080");
		//		System.out.println(result);

		//		TransactionReceipt transactionReceipt = myGeth.transactionReceipt("0x1e2910a262b1008d0616a0beb24c1a491d78771baa54a33e66065e03b1f46bc1");
		//		System.out.println(transactionReceipt);

		//		String result = myGeth.call("0xAEEF46DB4855E25702F8237E8f403FddcaF931C0", "0x70a08231000000000000000000000000e16359506c028e51f16be38986ec5746251e9724");
		//		System.out.println(result);

		//		String result = myGeth.getCode("0xf75e354c5edc8efed9b59ee9f67a80845ade7d0c");
		//		System.out.println(result);

		//		String result = myGeth.getStorageAt("0x6e03d9cce9d60f3e9f2597e13cd4c54c55330cfd", new BigInteger("0"));
		//		System.out.println(result);

		//		BigInteger gasPrice = myGeth.gasPrice();
		//		System.out.println(gasPrice);

		//		String result = myGeth.estimateGas("0x57d90b64a1a57749b0f932f1a3395792e12e7055", new BigInteger("55000"), new BigInteger("50000000000"), new BigInteger("21000000"));
		//		System.out.println(result);



		/*
		 * Initialize with API key, retrieved from etherscan_api_key in res/api_keys.txt
		 * Can be called multiple times without re-initialization
		 */
		Etherscan.Tokens myTokens = new Etherscan.Tokens();

		//		BigInteger tokenTotalSupply = myTokens.getTokenTotalSupply("DGD");
		//		System.out.println(tokenTotalSupply);

		//		BigInteger tokenTotalSupply = myTokens.getErc20TokenTotalSupply("0x57d90b64a1a57749b0f932f1a3395792e12e7055");
		//		System.out.println(tokenTotalSupply);

		//		BigInteger tokenBalance = myTokens.getTokenAccountBalance("DGD", "0x4366ddc115d8cf213c564da36e64c8ebaa30cdbd");
		//		System.out.println(tokenBalance);

		//		BigInteger tokenBalance = myTokens.getErc20TokenAccountBalance("0x57d90b64a1a57749b0f932f1a3395792e12e7055", "0xe04f27eb70e025b78871a2ad7eabe85e61212761");
		//		System.out.println(tokenBalance);



		/*
		 * Initialize with API key, retrieved from etherscan_api_key in res/api_keys.txt
		 * Can be called multiple times without re-initialization
		 */
		Etherscan.Stats myStats = new Etherscan.Stats();

		//		BigInteger totalEtherSupply = myStats.totalEtherSupply();
		//		System.out.println(totalEtherSupply);

		//		EtherPrice etherPrice = myStats.lastEtherPrice();
		//		System.out.println(etherPrice);

	}

}
