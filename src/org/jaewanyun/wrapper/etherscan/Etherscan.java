package org.jaewanyun.wrapper.etherscan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jaewanyun.wrapper.CustomNameValuePair;
import org.jaewanyun.wrapper.Utility;
import org.jaewanyun.wrapper.etherscan.data.Balance;
import org.jaewanyun.wrapper.etherscan.data.Block;
import org.jaewanyun.wrapper.etherscan.data.ContractExecutionStatus;
import org.jaewanyun.wrapper.etherscan.data.EtherPrice;
import org.jaewanyun.wrapper.etherscan.data.InternalTransaction;
import org.jaewanyun.wrapper.etherscan.data.LatestBlock;
import org.jaewanyun.wrapper.etherscan.data.Transaction;
import org.jaewanyun.wrapper.etherscan.data.TransactionReceipt;
import org.jaewanyun.wrapper.poloniex.Poloniex;

/**
 * Wrapper for Etherscan API. https://etherscan.io/apis
 * Powered by Etherscan.io APIs
 *
 * Per documentation:
 * 		The Ethereum Developer APIs are provided as a community service and without warranty, so please just use what you need and no more
 * 		They support both GET/POST requests and a rate limit of 5 requests/sec (exceed and receive a http 403)
 * 		If you plan to push a lot more transactions, please create a FREE Api-Key Token from within the ClientPortal->MyApiKey area and use that with all your api requests
 * 		Either a link back or mention that your app is "Powered by Etherscan.io APIs" would be very much appreciated.
 */
public class Etherscan {

	private static final String PUBLIC_URL = "https://api.etherscan.io/api";
	private static CloseableHttpClient httpClient;

	static {httpClient = HttpClients.createDefault();}


	public static class Account {

		private final String API_KEY;

		/**
		 * Reads API key from res/api_keys.txt
		 */
		public Account() {

			String API_KEY = null;

			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {

				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {

					if(currentLine.startsWith("etherscan_api_key:")) {
						int startIndex = currentLine.indexOf(":");
						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.API_KEY = API_KEY;

		}

		public Account(String api_key) {
			this.API_KEY = api_key;
		}

		/**
		 * Get Ether balance for a single address in Wei
		 * @param address Address
		 * @return Balance
		 */
		public Balance etherBalance(String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=balance&address=" + address + "&tag=latest&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++) {
				if(a.get(j).getName().equals("result")) { // Equals compares names
					return new Balance(address, new BigInteger(a.get(j).getValue().toString()));
				}
			}

			return null;

		}

		/**
		 * Get Ether balance for multiple addresses in a single call in Wei. Up to a maximum of 20 accounts in a single batch
		 * @param addresses Addresses
		 * @return Balances
		 */
		public Balance[] etherBalance(String[] addresses) {

			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < addresses.length; j++) {
				sb.append(addresses[j]);
				if(j+1 != addresses.length)
					sb.append(",");
			}
			String address = sb.toString();

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=balancemulti&address=" + address + "&tag=latest&apikey=" + API_KEY);
			System.out.println(get.toString());
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ArrayList<Balance> b = new ArrayList<>();

			for(int j = 0; j < a.size(); j++) {
				if(a.get(j).getName().equals("account")) { // Equals compares names
					b.add(new Balance(a.get(j).getValue().toString(), new BigInteger(a.get(++j).getValue().toString())));
				}
			}

			return b.toArray(new Balance[b.size()]);

		}

		/**
		 * Get a list of 'Normal' transactions by address. Returns up to a maximum of the last 10000 transactions only
		 * @param address Addresses
		 * @param startBlock Starting block number
		 * @param endBlock Ending block number
		 * @return Normal transactions
		 */
		public Transaction[] txnsByAddress(String address, long startBlock, long endBlock) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=txlist&address=" + address
					+ "&startblock=" + startBlock + "&endblock=" + endBlock + "&sort=asc" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ArrayList<Transaction> transactions = new ArrayList<>();

			Transaction current = new Transaction();

			for(int j = 0; j < a.size(); j++) {

				current.addData(a.get(j));

				if(a.get(j).getName().toString().equals("confirmations")) {
					transactions.add(current);
					current = new Transaction();
				}

			}

			return transactions.toArray(new Transaction[transactions.size()]);

		}

		/**
		 * [BETA] Get a list of 'Internal' transactions by address. Returns up to a maximum of the last 10000 transactions
		 * @param address Addresses
		 * @param startBlock Starting block number
		 * @param endBlock Ending block number
		 * @return Internal transactions
		 */
		public InternalTransaction[] txnsByAddressInternal(String address, long startBlock, long endBlock) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=txlistinternal&address=" + address
					+ "&startblock=" + startBlock + "&endblock=" + endBlock + "&sort=asc" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ArrayList<InternalTransaction> transactions = new ArrayList<>();

			InternalTransaction current = new InternalTransaction();

			for(int j = 0; j < a.size(); j++) {

				current.addData(a.get(j));

				if(a.get(j).getName().toString().equals("errCode")) {
					transactions.add(current);
					current = new InternalTransaction();
				}

			}

			return transactions.toArray(new InternalTransaction[transactions.size()]);

		}

		/**
		 * Get "Internal Transactions" by transaction hash. Returns up to a maximum of the last 10000 transactions
		 * @param txHash Transaction hash
		 * @return Internal transactions
		 */
		public InternalTransaction[] txnsByHashInternal(String txHash) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=txlistinternal&txhash=" + txHash + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ArrayList<InternalTransaction> transactions = new ArrayList<>();

			InternalTransaction current = new InternalTransaction();

			for(int j = 0; j < a.size(); j++) {

				current.addData(a.get(j));

				if(a.get(j).getName().toString().equals("errCode")) {
					transactions.add(current);
					current = new InternalTransaction();
				}

			}

			return transactions.toArray(new InternalTransaction[transactions.size()]);

		}

		/**
		 * Get list of blocks mined by address
		 * @param address Addresses
		 * @return Mined blocks
		 */
		public LatestBlock[] blocksMined(String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=getminedblocks&address=" + address + "&blocktype=blocks" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ArrayList<LatestBlock> blocks = new ArrayList<>();

			LatestBlock current = new LatestBlock();

			for(int j = 0; j < a.size(); j++) {

				current.addData(a.get(j));

				if(a.get(j).getName().toString().equals("blockReward")) {
					blocks.add(current);
					current = new LatestBlock();
				}

			}

			return blocks.toArray(new LatestBlock[blocks.size()]);

		}

	}

	//	public static class Contracts {
	//
	//		private final String API_KEY;
	//
	//		/**
	//		 * Reads API key from res/api_keys.txt
	//		 */
	//		public Contracts() {
	//
	//			String API_KEY = null;
	//
	//			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {
	//
	//				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {
	//
	//					if(currentLine.startsWith("etherscan_api_key:")) {
	//						int startIndex = currentLine.indexOf(":");
	//						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
	//					}
	//
	//				}
	//
	//			} catch (FileNotFoundException e) {
	//				e.printStackTrace();
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//
	//			this.API_KEY = API_KEY;
	//
	//		}
	//
	//		public Contracts(String api_key) {
	//			this.API_KEY = api_key;
	//		}
	//
	//		public void contractAbi(String address) {
	//
	//			HttpGet get = new HttpGet(PUBLIC_URL + "?module=contract&action=getabi&address=" + address + "&apikey=" + API_KEY);
	//			String response = null;
	//
	//			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
	//				response = EntityUtils.toString(httpResponse.getEntity());
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//
	//			@SuppressWarnings("rawtypes")
	//			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);
	//
	//		}
	//
	//	}

	public static class Transactions {

		private final String API_KEY;

		/**
		 * Reads API key from res/api_keys.txt
		 */
		public Transactions() {

			String API_KEY = null;

			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {

				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {

					if(currentLine.startsWith("etherscan_api_key:")) {
						int startIndex = currentLine.indexOf(":");
						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.API_KEY = API_KEY;

		}

		public Transactions(String api_key) {
			this.API_KEY = api_key;
		}

		/**
		 * [BETA] Check Contract Execution Status (if there was an error during contract execution)
		 * @param txHash Transaction hash
		 * @return Contract execution status. isError:0 = pass , isError:1 = error during contract execution
		 */
		public ContractExecutionStatus checkContractExecutionStatus(String txHash) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=transaction&action=getstatus&txhash=" + txHash + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			ContractExecutionStatus current = new ContractExecutionStatus();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

	}

	//	public static class Blocks {
	//
	//	}

	public static class EventLogs {

	}

	public static class Geth {

		private final String API_KEY;

		/**
		 * Reads API key from res/api_keys.txt
		 */
		public Geth() {

			String API_KEY = null;

			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {

				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {

					if(currentLine.startsWith("etherscan_api_key:")) {
						int startIndex = currentLine.indexOf(":");
						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.API_KEY = API_KEY;

		}

		public Geth(String api_key) {
			this.API_KEY = api_key;
		}

		/**
		 * @return The number of most recent block. Null if none found--in which case the API may be unresponsive
		 */
		public int blockNumber() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_blockNumber" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return Integer.parseInt(a.get(j).getValue().toString().substring(2), 16); // Hex to dec. "0x" in hex must be clipped off

			return -1; // -1 should not be expected when API is functional

		}

		/**
		 * Returns information about a block by block number
		 * @param blockNumber Block number
		 * @return Block information
		 */
		public Block blockByNumber(int blockNumber) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getBlockByNumber&tag=0x" + Integer.toHexString(blockNumber) + "&boolean=true&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			Block current = new Block();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

		/**
		 * Returns information about a uncle by block number
		 * @param blockNumber Block number
		 * @param index Index
		 * @return Block information
		 */
		public Block uncleByBlockNumberAndIndex(BigInteger blockNumber, BigInteger index) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getUncleByBlockNumberAndIndex&tag=" + "0x" + blockNumber.toString(16) + "&index=" + "0x" + index.toString(16) + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			Block current = new Block();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

		/**
		 * Returns the number of transactions in a block from a block matching the given block number
		 * @param blockNumber Block number
		 * @return Number of transactions
		 */
		public BigInteger blockTransactionCountByNumber(BigInteger blockNumber) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getBlockTransactionCountByNumber&tag=" + "0x" + blockNumber.toString(16) + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString().substring(2), 16); // Hex to dec

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Returns the information about a transaction requested by transaction hash
		 * @param txHash Transaction hash
		 * @return Transaction information
		 */
		public Transaction transactionByHash(String txHash) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getTransactionByHash&txhash=" + txHash + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			Transaction current = new Transaction();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

		/**
		 * Returns information about a transaction by block number and transaction index position
		 * @param blockNumber Block number
		 * @param index Index
		 * @return Transaction information
		 */
		public Transaction transactionByBlockNumberAndIndex(BigInteger blockNumber, BigInteger index) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getTransactionByBlockNumberAndIndex&tag=" + "0x" + blockNumber.toString(16) + "&index=" + "0x" + index.toString(16) + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			Transaction current = new Transaction();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

		/**
		 * Returns the number of transactions sent from an address
		 * @param address Address
		 * @return Number of transactions sent
		 */
		public BigInteger transactionCount(String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getTransactionCount&address=" + address + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString().substring(2), 16); // Hex to dec

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Creates new message call transaction or a contract creation for signed transactions
		 * @param txHex Raw hex encoded transaction to send
		 * @return Error message or call result
		 */
		public String sendRawTransaction(String txHex) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_sendRawTransaction&hex=" + txHex + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++) {
				if(a.get(j).getName().toString().equals("message"))
					return a.get(j).getValue().toString();
				else if(a.get(j).getName().toString().equals("result"))
					return a.get(j).getValue().toString();
			}

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Returns the receipt of a transaction by transaction hash
		 * @param txHash Transaction hash
		 * @return Transaction receipt
		 */
		public TransactionReceipt transactionReceipt(String txHash) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getTransactionReceipt&txhash=" + txHash + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			TransactionReceipt current = new TransactionReceipt();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

		/**
		 * Executes a new message call immediately without creating a transaction on the block chain
		 * @param to Target of message
		 * @param data Message data
		 * @return Message call result
		 */
		public String call(String to, String data) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_call&to=" + to + "&data=" + data + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return a.get(j).getValue().toString();

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Returns code at a given address
		 * @param address Address
		 * @return Code at given address
		 */
		public String getCode(String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getCode&address=" + address + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return a.get(j).getValue().toString();

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Experimental. Returns the value from a storage position at a given address
		 * @param address Address
		 * @param position Storage position
		 * @return Value from storage position
		 */
		public String getStorageAt(String address, BigInteger position) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_getStorageAt&address=" + address + "&position=" + "0x" + position.toString(16) + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return a.get(j).getValue().toString();

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Returns the current price per gas in Wei
		 * @return Current gas price in Wei
		 */
		public BigInteger gasPrice() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_gasPrice" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString().substring(2), 16); // Hex to dec

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used for estimating the used gas
		 * @param to Target of call or transaction
		 * @param gasPrice Gas price in Wei
		 * @param gas Gas
		 * @param txValue Transaction value in Wei
		 * @return Error message or gas to pay in hex
		 */
		public String estimateGas(String to, BigInteger gas, BigInteger gasPrice, BigInteger txValue) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=proxy&action=eth_estimateGas&to=" + to + "&value=" + "0x" + txValue.toString(16) + "&gasPrice=" + "0x" + gasPrice.toString(16) + "&gas=" + "0x" + gas.toString(16) + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++) {
				if(a.get(j).getName().toString().equals("message"))
					return a.get(j).getValue().toString();
				else if(a.get(j).getName().toString().equals("result"))
					return a.get(j).getValue().toString();
			}

			return null;  // Null should not be expected when API is functional

		}

	}

	public static class Tokens {

		private final String API_KEY;

		/**
		 * Reads API key from res/api_keys.txt
		 */
		public Tokens() {

			String API_KEY = null;

			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {

				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {

					if(currentLine.startsWith("etherscan_api_key:")) {
						int startIndex = currentLine.indexOf(":");
						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.API_KEY = API_KEY;

		}

		public Tokens(String api_key) {
			this.API_KEY = api_key;
		}

		/**
		 * Get Token total supply by Token name (Supported Token names: DGD, MKR, FirstBlood, HackerGold, ICONOMI, Pluton, REP, SNGLS)
		 * @param tokenName Token name
		 * @return Token total supply
		 */
		public BigInteger getTokenTotalSupply(String tokenName) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=stats&action=tokensupply&tokenname=" + tokenName + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString());

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Get ERC20-Token total supply by contract address
		 * @param contractAddress Contract address
		 * @return ERC20-Token total supply
		 */
		public BigInteger getErc20TokenTotalSupply(String contractAddress) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=stats&action=tokensupply&contractaddress=" + contractAddress + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString());

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Get Token account balance by known Token name (Supported Token names: DGD, MKR, FirstBlood, HackerGold, ICONOMI, Pluton, REP, SNGLS)
		 * @param tokenName Token name
		 * @param address Address
		 * @return Token account balance
		 */
		public BigInteger getTokenAccountBalance(String tokenName, String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=tokenbalance&tokenname=" + tokenName + "&address=" + address + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString());

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Get ERC20-Token account balance by contract address
		 * @param contractAddress Contract address
		 * @param address Address
		 * @return ERC29-Token account balance
		 */
		public BigInteger getErc20TokenAccountBalance(String contractAddress, String address) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=account&action=tokenbalance&contractaddress=" + contractAddress + "&address=" + address + "&tag=latest" + "&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString());

			return null;  // Null should not be expected when API is functional

		}

	}

	public static class Stats {

		private final String API_KEY;

		/**
		 * Reads API key from res/api_keys.txt
		 */
		public Stats() {

			String API_KEY = null;

			try(BufferedReader br = new BufferedReader(new InputStreamReader(Poloniex.class.getResourceAsStream("/api_keys.txt")))) {

				for(String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {

					if(currentLine.startsWith("etherscan_api_key:")) {
						int startIndex = currentLine.indexOf(":");
						API_KEY = currentLine.substring(startIndex + 1, currentLine.length());
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.API_KEY = API_KEY;

		}

		public Stats(String api_key) {
			this.API_KEY = api_key;
		}

		/**
		 * Return the total Ether supply
		 * @return Total Ether supply
		 */
		public BigInteger totalEtherSupply() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=stats&action=ethsupply&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			for(int j = 0; j < a.size(); j++)
				if(a.get(j).getName().toString().equals("result"))
					return new BigInteger(a.get(j).getValue().toString());

			return null;  // Null should not be expected when API is functional

		}

		/**
		 * Returns the latest Ether price in Wei
		 * @return Latest Ether price in Wei
		 */
		public EtherPrice lastEtherPrice() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?module=stats&action=ethprice&apikey=" + API_KEY);
			String response = null;

			try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}

			@SuppressWarnings("rawtypes")
			ArrayList<CustomNameValuePair<String, CustomNameValuePair>> a = Utility.evaluateExpression(response);

			EtherPrice current = new EtherPrice();

			for(int j = 0; j < a.size(); j++)
				current.addData(a.get(j));

			return current;

		}

	}



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

		int blockNumber = myGeth.blockNumber();
		System.out.println(blockNumber);

		Block block = myGeth.blockByNumber(blockNumber-1);
		System.out.println(block);

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
