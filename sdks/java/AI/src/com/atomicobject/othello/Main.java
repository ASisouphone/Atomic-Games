package com.atomicobject.othello;
import java.net.Socket;

import com.google.gson.Gson;


public class Main {

	public static void main(String[] args) {
		String ip = args.length > 0 ? args[0] : "127.0.0.1";
		int port = args.length > 1 ? parsePort(args[1]) : 1338;
		int[][] moves = args.length > 2 ? new Gson().fromJson(args[2], int[][].class) : new int[][] {};
		try {
			System.out.println("Connecting to " + ip + " at " + port);
			Socket socket = new Socket(ip, port);
			// Use startWithInput for input
			// Start normnal
			new Client(socket, moves).startWithRandValidInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int parsePort(String port) {
		return Integer.parseInt(port);
	}
}
