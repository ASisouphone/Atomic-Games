package com.atomicobject.othello;

import java.util.Arrays;
import java.util.ListIterator;

import java.util.Stack;
import java.util.ArrayList;

public class AI {
	class Node() {
		public int c;
		public int r;
		public int[] d;

		public ArrayList<Node> children;

		public Node(int c, int r, int[] d) {
			this.c = c;
			this.r = r;
			this.d = d;
		}
	}
	ListIterator<int[]> moveList;
	
	private int depth;

	private int color;

	private int enemyColor;

	private Stack<int[][]> stateStack;

	public AI(int[][] moves) {
		stateStack = new Stack<int[][]>();
		depth = 3; // CHANGE DEPTH 
		// /moveList = Arrays.asList(moves).listIterator();
	}

	public int[] computeMove(GameState state) {
		System.out.println("AI returning canned move for game state - " + state);
		return moveList.next();
	}

	private static int[][] cloneArray(int[][] src) {
	    int length = src.length;
	    int[][] target = new int[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}

	private boolean isGameOver() {
		return false;
	}

	private boolean isSquareExists(int c, int r) {
		return (c > -1 && c < 8 && r > -1 && r < 8);
	}

	private ArrayList<Node> getChildrenNodes(boolean isMax) {
		int color, enemyColor;
		int EMPTY = 0;
		if (isMax) {
			color = this.color;
			enemyColor = this.enemyColor;
		} else {
			color = this.enemyColor;
			enemyColor = this.color;
		}
		int[][] board = stateStack.peek();
		ArrayList<int[]> potentialMoves = new ArrayList<int[]>();

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == enemyColor) {
					// Check square below
					if (isSquareExists(i, j + 1))
						if (board[i][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i, j + 1});
					
					// Check square above
					if (isSquareExists(i, j - 1))
						if (board[i][j - 1] == EMPTY)
							potentialMoves.add(new int[] {i, j - 1});

					// Check square left
					if (isSquareExists(i - 1, j))
						if (board[i - 1][j] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j});

					// Check square right
					if (isSquareExists(i + 1, j))
						if (board[i + 1][j] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j});

					// Check diagnonals

					// Check square down and left
					if (isSquareExists(i - 1, j + 1))
						if (board[i - 1][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j + 1});

					// Check square down and right
					if (isSquareExists(i + 1, j + 1))
						if (board[i + 1][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j + 1});

					// Check square up and left
					if (isSquareExists(i - 1, j - 1))
						if (board[i - 1][j - 1] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j - 1});

					// Check square up and right
					if (isSquareExists(i + 1, j + 1))
						if (board[i + 1][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j + 1});
				}
			}

			

	}

	private void doMove(int c, int r, int[] d) {

	}

	private void undoMove() {
		stateStack.pop();
	}

	private int negamax(Node node, int depth, int alpha, int beta) {
		if (depth == 0 || isGameOver()) {
			// evaluateState() relative to current player
		}
		boolean isMax = (stateStack.size() % 2 == 1);
		node.children = getChildrenNodes(isMax);
		int eval = -8000;
		for (int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i);
			doMove(child.c, child.r, child.d);
			eval = negamax(child, depth - 1, -beta, -alpha);
			undoMove();
			alpha = Integer.max(alpha, eval);
			if (depth == this.depth) {
				if (alpha == eval) {
					// create bestMove here!
				}
			}
		}


	}
}
