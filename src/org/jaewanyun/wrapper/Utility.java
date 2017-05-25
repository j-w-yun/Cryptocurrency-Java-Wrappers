package org.jaewanyun.wrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A store of various utility methods applicable to more than one wrapper
 */
public class Utility {

	/**
	 * Write to disk
	 * @param records List of Strings to write to disk
	 * @param bufferSize Buffer size
	 * @param directory Directory to write to
	 * @param prefix File name
	 * @param postfix File extension
	 * @throws IOException
	 */
	public static void write(List<String> records, int bufferSize, File directory, String prefix, String postfix) throws IOException {

		File file = new File(directory, prefix+postfix);

		if(!file.createNewFile())
			System.err.println("File already exists! Overwriting...");

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file), bufferSize);

		System.out.print("Writing to : " + directory.getPath() + "\\" + prefix+postfix + " ... ");

		write(records, bufferedWriter);

	}

	/**
	 * Write to disk
	 * @param records List of Strings to write to disk
	 * @param writer File writer
	 * @throws IOException See Writer
	 */
	private static void write(List<String> records, Writer writer) throws IOException {

		long start = System.nanoTime();

		for(String each : records)
			writer.write(each);
		writer.flush();
		writer.close();

		long end = System.nanoTime();

		System.out.println((end - start)/1000000f + " ms");

	}

	/**
	 * Write to disk
	 * @param record String to write to disk
	 * @param bufferSize Buffer size
	 * @param directory Directory to write to
	 * @param prefix File name
	 * @param postfix File extension
	 * @throws IOException See File
	 */
	public static void write(String record, int bufferSize, File directory, String prefix, String postfix) {

		long start = System.nanoTime();

		File file = new File(directory, prefix+postfix);

		if(file.exists())
			System.out.println("File already exists! Overwriting...");

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file), bufferSize)) {
			System.out.print(prefix+postfix);
			writer.write(record);
			writer.flush();
			writer.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

		long end = System.nanoTime();

		System.out.println(" " + (end - start)/1000000f + " ms");

	}

	/**
	 * Converts Wei to Ether
	 * @param wei Wei amount
	 * @return Ether amount
	 */
	public static BigDecimal toEther(BigInteger wei) {
		return new BigDecimal(wei).divide(new BigDecimal("1e18"));
	}

	/**
	 * Converts Wei to Ether
	 * @param wei Wei amount
	 * @return Ether amount
	 */
	public static BigDecimal toEther(BigDecimal wei) {
		return wei.divide(new BigDecimal("1e18"));
	}

	/**
	 * Converts Ether to Wei
	 * @param ether Ether amount
	 * @return Wei amount
	 */
	public static BigInteger toWei(BigDecimal ether) {
		return new BigInteger(ether.multiply(new BigDecimal("1e18")).toString());
	}

	/**
	 * Converts Ether to Wei
	 * @param ether Ether amount
	 * @return Wei amount
	 */
	public static BigInteger toWei(BigInteger ether) {
		return ether.multiply(new BigInteger("1e18"));
	}



	/**
	 * Parses API response in JSON format
	 * @param parse API response in JSON format
	 * @return Parsed API response
	 */
	public static ArrayList<CustomNameValuePair<String, CustomNameValuePair>> evaluateExpression(String parse) {

		//		/*
		//		 * TODO: Debug output
		//		 */
		//
		//		System.out.println("Original JSON :" + parse);
		//		System.out.println();

		/*
		 * Cut into individual pieces
		 */

		String[] splitStrings = parse.split(""); // Split everything into length of 1

		StringBuilder sb = new StringBuilder();
		Stack<String> inFixList = new Stack<>();

		StringBuilder quoteBuilder = new StringBuilder();

		for(String each : splitStrings) {

			// Number or word has finished building
			if(each.equals("{") || each.equals("[") || each.equals("}") || each.equals("]") || each.equals(",") || each.equals("\"") || each.equals(":")) {

				if(!sb.toString().equals("")) // Do not include whitespace
					inFixList.push(sb.toString());

				if(!each.equals("\"")) { // Do not include quotes
					inFixList.push(each);
					quoteBuilder.setLength(0);
				} else {
					quoteBuilder.append("\"");
					if(quoteBuilder.length() > 1) {
						inFixList.push("null");
					}
				}

				sb.setLength(0); // Clear the StringBuilder

			}

			// Number or word not yet completly parsed
			else {
				sb.append(each); // Keep building until sentinel characters are met
				quoteBuilder.setLength(0);
			}

		}

		/*
		 * Convert in-fix expression to post-fix expression
		 */

		Stack<String> operandStack = new Stack<>();
		Stack<String> postFixList = new Stack<>();

		for(String each : inFixList) {

			if(each.equals("{") || each.equals("[") || each.equals("}") || each.equals("]") || each.equals(":") || each.equals(",")) {

				// { or [
				if(each.equals("{") || each.equals("["))
					operandStack.push(each);

				// } or ]
				else if(each.equals("}") || each.equals("]")){
					String popped = operandStack.pop();
					while(!popped.equals("{") && !popped.equals("[")) {
						postFixList.push(popped);
						popped = operandStack.pop();
					}
				}

				// :
				else {
					while(!operandStack.isEmpty() && rankOperand(operandStack.peek()) < rankOperand(each)) {
						postFixList.push(operandStack.pop());
					}
					operandStack.push(each);
				}

			} else
				postFixList.push(each);

		}

		while(!operandStack.isEmpty()) {
			postFixList.push(operandStack.pop());
		}

		//		/*
		//		 * TODO: Debug post-fix expression output
		//		 */
		//
		//		for(String each : postFixList) {
		//			System.out.println(each);
		//		}

		/*
		 * Convert post-fix expression into name-value pair format
		 */

		Stack<CustomNameValuePair<String, CustomNameValuePair>> stack = new Stack<>();
		for(String each : postFixList) {

			if(each.equals(",")) {
				CustomNameValuePair b = stack.pop();
				CustomNameValuePair a = stack.pop();
				if(a.getValue() == null) {
					a.setValue(b);
					stack.push(a);
				} else {
					stack.push(a);
					stack.push(b);
				}
			}

			else if(each.equals(":")) {
				CustomNameValuePair b = stack.pop();
				CustomNameValuePair a = stack.pop();
				if(a.getValue() == null) {
					a.setValue(b);
					stack.push(a);
				} else {
					stack.push(a);
					stack.push(b);
				}
			}

			else {
				stack.push(new CustomNameValuePair<String, CustomNameValuePair>(each, null));
			}

		}


		//		/*
		//		 * TODO: Debug parsed output
		//		 */
		//
		//		int counter = 0;
		//		for(CustomNameValuePair<String, CustomNameValuePair> each : stack) {
		//			System.out.println(padRight("#" + String.valueOf(counter), 10) + each);
		//			counter++;
		//		}
		//		System.out.println();


		/*
		 * Move stack to ArrayList and return
		 */

		ArrayList<CustomNameValuePair<String, CustomNameValuePair>> toReturn = new ArrayList<>();
		for(CustomNameValuePair each : stack) {
			toReturn.add(each);
		}

		return toReturn;
	}

	/**
	 * Used for converting infix expression to post-fix expression
	 * @param operand Operand
	 * @return Rank of operand
	 */
	private static int rankOperand(String operand) {

		if(operand.equals(":"))
			return 0;
		else if(operand.equals(","))
			return 1;
		else if(operand.equals("{") || operand.equals("["))
			return 2;
		else
			return 3;

	}



	/**
	 * Clean console output
	 * @param string String to pad right
	 * @param numPads Number of pads appended to string
	 * @return Padded string
	 */
	public static String padRight(String string, int numPads) {
		return padRight(string, numPads, ' ');
	}

	/**
	 * Clean console output
	 * @param string String to pad right
	 * @param numPads Number of pads appended to string
	 * @param padWith Character with which to pad
	 * @return Padded string
	 */
	public static String padRight(String string, int numPads, char padWith) {
		if(padWith == ' ')
			return String.format("%1$-" + numPads + "s", string);
		else
			return String.format("%1$-" + numPads + "s", string).replace(' ', padWith);
	}

	/**
	 * Clean console output
	 * @param string String to pad left
	 * @param numPads Number of pads appended to string
	 * @return Padded string
	 */
	public static String padLeft(String string, int numPads) {
		return padLeft(string, numPads, ' ');
	}

	/**
	 * Clean console output
	 * @param string String to pad left
	 * @param numPads Number of pads appended to string
	 * @param padWith Character with which to pad
	 * @return Padded string
	 */
	public static String padLeft(String string, int numPads, char padWith) {
		if(padWith == ' ')
			return String.format("%1$" + numPads + "s", string);
		else
			return String.format("%1$" + numPads + "s", string).replace(' ', padWith);
	}

}
