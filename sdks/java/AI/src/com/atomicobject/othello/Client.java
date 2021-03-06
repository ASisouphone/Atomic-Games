package com.atomicobject.othello;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;

public class Client {

	BufferedReader input;
	OutputStreamWriter out;
	Gson gson = new Gson();
	AI ai;

	public Client(Socket socket, int[][] moves) {
		try {
			ai = new AI(moves);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new OutputStreamWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println("Starting client processing ...");
		GameState state;
		try {
			while ((state = readStateFromServer()) != null) {
				int[] move = ai.computeMove(state);
				respondWithMove(move);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeStreams();
	}
	Scanner reader = new Scanner(System.in);  // Reading from System.in

	// Start client with user input
	public void startWithInput() {
		System.out.println("Starting client with input ...");
		GameState state;
		try {
			while ((state = readStateFromServer()) != null) {
				int[] inputMove = new int[2];
				System.out.println("Board:\n" + state.toString());
				System.out.println("Enter a move: ");
				inputMove[0] = reader.nextInt(); // Scans the next token of the input as an int.
				inputMove[1] = reader.nextInt();
				respondWithMove(inputMove);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reader.close();
		}
		reader.close();
		closeStreams();
	}
	
	public void startWithRandValidInput() {
		System.out.println("Starting client with input ...");
		GameState state;
		try {
			while ((state = readStateFromServer()) != null) {
				ai.stateStack.push(state.getBoard());
				int[] inputMove = new int[2];
				ArrayList<Node> tempList = ai.getChildrenNodes(true);
				if (tempList.size() > 0) {
					int rnd = new Random().nextInt(tempList.size());
					inputMove[0]=tempList.get(rnd).c;
					inputMove[1]=tempList.get(rnd).r;
				}
				System.out.println("Board:\n" + state.toString());
				respondWithMove(inputMove);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reader.close();
		}
		reader.close();
		closeStreams();
	}
	
	private GameState readStateFromServer() throws IOException {
		System.out.println("Reading from server ...");
		String nextLine = input.readLine();
		System.out.println("Read data: " + nextLine);
		if (nextLine == null) return null;
		return gson.fromJson(nextLine.trim(), GameState.class);
	}

	private void respondWithMove(int[] move) throws IOException {
		String encoded = gson.toJson(move);
		System.out.println("Sending response: " + encoded);
		out.write(encoded);
		out.write("\n");
		out.flush();
	}

	private void closeStreams() {
		closeQuietly(input);
		closeQuietly(out);
	}

	private void closeQuietly(Closeable stream) {
		try {
			stream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
