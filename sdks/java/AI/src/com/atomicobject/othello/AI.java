package com.atomicobject.othello;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Random;


import java.util.Stack;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class AI {
	
	ListIterator<int[]> moveList;
	
	private int depth;

	private int color;

	private int enemyColor;

	public Stack<int[][]> stateStack;

	private int[] bestMove;
	
	private long startTime;
	
	private long TIME_OUT;
	
	private int MAX_DEPTH = 30;

	public AI(int[][] moves) {
		color = 1;
		enemyColor = 2;
		stateStack = new Stack<int[][]>();
		depth = 3; // CHANGE DEPTH 
		// /moveList = Arrays.asList(moves).listIterator();
	}

//	public int[] computeMove(GameState state) {
//		bestMove = null;
//		stateStack.clear();
//		System.out.println(state.toString());
//		color = state.getPlayer();
//		if (color == 1) enemyColor = 2;
//		else enemyColor = 1;
//		stateStack.push(state.getBoard());
//		Node root = new Node();
//		
//		TIME_OUT = (long)state.getMaxTurnTime() - 2000;
//		
//		
//		int eval = -8000;
//		startTime = System.currentTimeMillis();
//		//start timer
//		try {
//			while(depth < MAX_DEPTH) {
//				eval = Integer.max(eval, negamax(root, depth, -8000, 8000));
//				System.out.println("" + bestMove[0] + " " + bestMove[1]);
//				depth++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.print("Failed at depth" + depth +"\n");
//			//random move failsafe
//	        if (bestMove == null) {
//	        		System.out.print("Random!!!" +"\n");
//	        		stateStack.clear();
//	        		stateStack.push(state.getBoard());
//	        		bestMove = new int[2];
//	            ArrayList<Node> list =  getChildrenNodes(true);
//	            int rand = new Random().nextInt(list.size());
//	            bestMove[0] = list.get(rand).r;
//	            bestMove[1] = list.get(rand).c;
//	        }
//			return bestMove;
//		}
//		return bestMove;
//        
//	}
	
	public int[] computeMove(GameState state) {
		stateStack.clear();
		System.out.println(state.toString());
		color = state.getPlayer();
		if (color == 1) enemyColor = 2;
		else enemyColor = 1;
		stateStack.push(state.getBoard());
		Node root = new Node();
		negamax(root, depth, -8000, 8000);
		//negamax(root, depth, -8000, 8000);
         //random move failsafe
//        bestMove = new int[2];
//        ArrayList<Node> list =  getChildrenNodes(true);
//        int rand = new Random().nextInt(list.size());
//        bestMove[0] = list.get(rand).r;
//        bestMove[1] = list.get(rand).c;
		return bestMove;
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
		ArrayList<Node> maxNodes = getChildrenNodes(true);
		boolean maxNoMoves = false, minNoMoves = false;
		ArrayList<Node> minNodes = getChildrenNodes(false);
		if (maxNodes.size() == 1) {
			if (maxNodes.get(0).c == -1)
				maxNoMoves = true;
		}
		if (minNodes.size() == 1) {
			if (minNodes.get(0).c == -1)
				minNoMoves = true;
		}
		
		return (minNoMoves && maxNoMoves);
	}

	private boolean isSquareExists(int r, int c) {
		return (c > -1 && c < 8 && r > -1 && r < 8);
	}

	public ArrayList<Node> getChildrenNodes(boolean isMax) {
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
		ArrayList<Node> children = new ArrayList<Node>();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == enemyColor) {
					// Check square right
					if (isSquareExists(i, j + 1))
						if (board[i][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i, j + 1});
					
					// Check square left
					if (isSquareExists(i, j - 1))
						if (board[i][j - 1] == EMPTY)
							potentialMoves.add(new int[] {i, j - 1});

					// Check square above
					if (isSquareExists(i - 1, j))
						if (board[i - 1][j] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j});

					// Check square down
					if (isSquareExists(i + 1, j))
						if (board[i + 1][j] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j});

					// Check diagnonals

					// Check square down and left
					if (isSquareExists(i + 1, j - 1))
						if (board[i + 1][j - 1] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j - 1});

					// Check square down and right
					if (isSquareExists(i + 1, j + 1))
						if (board[i + 1][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i + 1, j + 1});

					// Check square up and left
					if (isSquareExists(i - 1, j - 1))
						if (board[i - 1][j - 1] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j - 1});

					// Check square up and right
					if (isSquareExists(i - 1, j + 1))
						if (board[i - 1][j + 1] == EMPTY)
							potentialMoves.add(new int[] {i - 1, j + 1});
				}
			}

			for (int i = 0; i < potentialMoves.size(); i++) {
				ArrayList<Integer> directions = new ArrayList<Integer>();
				int[] m = potentialMoves.get(i);
				// Check Direction 0: up, left of potential
				for (int j = 1; j < 8; j++) {
					int r = m[0] - j;
					int c = m[1] - j;
					
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(0);
							break;
						} else break;
					} else break;
				}

					// Check Direction 1: up
				for (int j = 1; j < 8; j++) {
					int r = m[0] - j;
					int c = m[1];
					
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(1);
							break;
						} else break;
					} else break;
				}

				// Check Direction 2: up, right
				for (int j = 1; j < 8; j++) {
					int c = m[1] + j;
					int r = m[0] - j;
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(2);
							break;
						} else break;
					} else break;
				}

				// Check Direction 3: left
				for (int j = 1; j < 8; j++) {
					int c = m[1] - j;
					int r = m[0];
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(3);
							break;
						} else break;
					} else break;
				}

				// Check Direction 4: right
				for (int j = 1; j < 8; j++) {
					int c = m[1] + j;
					int r = m[0];
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(4);
							break;
						} else break;
					} else break;
				}

				// Check Direction 5: down, left
				for (int j = 1; j < 8; j++) {
					int c = m[1] - j;
					int r = m[0] + j;
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(5);
							break;
						} else break;
					} else break;
				}

				// Check Direction 6: down
				for (int j = 1; j < 8; j++) {
					int c = m[1];
					int r = m[0] + j;
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(6);
							break;
						} else break;
					} else break;
				}

				// Check Direction 7: down, right
				for (int j = 1; j < 8; j++) {
					int c = m[1] + j;
					int r = m[0] + j;
					if (isSquareExists(r, c)) {
						if (board[r][c] == EMPTY) break;

						if (board[r][c] == enemyColor) continue;

						if (board[r][c] == color && j > 1) {
							directions.add(7);
							break;
						} else break;
					} else break;
				}

				if (directions.size() > 0) {
					int[] d = new int[directions.size()];
					for (int j = 0; j < directions.size(); j++) {
						d[j] = directions.get(j);
					}

					children.add(new Node(m[0], m[1], d));
				}
			}

		if (children.size() == 0) {
			children.add(new Node(-1, -1, null));
		}

		return children;
	}

	public void doMove(int r, int c, int[] d) {
		int[][] board = cloneArray(stateStack.peek());

		// If there is no move
		if (r == -1 && c == -1) {
			stateStack.push(board);
			return;
		}


		int color, enemyColor;
		if (stateStack.size() % 2 == 1) {
			color = this.color;
			enemyColor = this.enemyColor;
		} else {
			color = this.enemyColor;
			enemyColor = this.color;
		}
		
		board[r][c]=color;

		for (int i = 0; i < d.length; i++) {
			if (d[i] == 0) {
				for (int j = 1; j < 8; j++) {
					int r2 = r - j;
					int c2 = c - j;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 1) {
				for (int j = 1; j < 8; j++) {
					int r2 = r - j;
					int c2 = c;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 2) {
				for (int j = 1; j < 8; j++) {
					int r2 = r - j;
					int c2 = c + j;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 3) {
				for (int j = 1; j < 8; j++) {
					int r2 = r;
					int c2 = c - j;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 4) {
				for (int j = 1; j < 8; j++) {
					int r2 = r;
					int c2 = c + j;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 5) {
				for (int j = 1; j < 8; j++) {
					int r2 = r + 1;
					int c2 = c - 1;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 6) {
				for (int j = 1; j < 8; j++) {
					int r2 = r + 1;
					int c2 = c;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			} else if(d[i] == 7) {
				for (int j = 1; j < 8; j++) {
					int r2 = r + 1;
					int c2 = c + 1;
					if (!isSquareExists(r2, c2)) break;
					if (board[r2][c2] == enemyColor)
						board[r2][c2] = color;
					if (board[r2][c2] == color)
						break;
				}
			}
			
		}

		stateStack.push(board);
	}

	private void undoMove() {
		stateStack.pop();
	}
	
	private int evaluateState() {
		if (winnerEval() != 0)
			return winnerEval();
		
		return (int)(cornersCaputured() + mobilityEval() + greedyEval() + stabilityEval());
	}
	
	private int winnerEval() {
		if (isGameOver() && greedyEval2() > 0)
			return 100000;
		else if (isGameOver() && greedyEval2() < 0)
			return -100000;
		else
			return 0;
	}
	
	private float cornersCaputured() {
		
		
		int goodCount = 0, badCount = 0;
		int[][] board = stateStack.peek();
		if (board[0][0] == color) goodCount++;
		if (board[0][7] == color) goodCount++;
		if (board[7][0] == color) goodCount++;
		if (board[7][7] == color) goodCount++;
		
		if (board[0][0] == enemyColor) badCount++;
		if (board[0][7] == enemyColor) badCount++;
		if (board[7][0] == enemyColor) badCount++;
		if (board[7][7] == enemyColor) badCount++;
		if (goodCount == 0 && badCount == 0) return 0;
		return ((goodCount - badCount) / (goodCount + badCount))*(20);
	}
	
	private float mobilityEval() {
		int goodCount = getChildrenNodes(true).size();
		int badCount = getChildrenNodes(false).size();
		
		return ((goodCount - badCount)/(goodCount + badCount))*(20);
	}
	
	private float stabilityEval() {
		
		int goodCount = 0, badCount = 0;
		int[][] values = {{10,   -3,  2, 2, 2, 2, -3, 10},
						  {-3,  -15, -1, -1, -1, -1, -15, -3},
					      {2,   -1, 1, 0, 0, 1, -1, 2},
					      {2,   -1, 0, 1, 1, 0, -1, 2},
					      {2,   -1, 0, 1, 1, 0, -1, 2},
					      {2,   -1, 1, 0, 0, 1, -1, 2},
					      {-3,  -15, -1, -1, -1, -1, -15, -3},
					      {10,   -3, 2, 2, 2, 2, -3,10}};
		int[][] board = stateStack.peek();
		for(int i = 0; i < 8; i++)
			for(int j=0; j < 8; j++) {
				if (board[i][j] == color)
					goodCount += values[i][j];
				if (board[i][j] == enemyColor) {
					badCount += values[i][j];
				}
			}
		if ((goodCount == 0 && badCount == 0) || (goodCount + badCount == 0)) return 0;
		return ((goodCount - badCount ) / (goodCount + badCount))*40;
	}
	
	private int greedyEval2() {
		int[][] board = stateStack.peek();
		int goodCount = 0, badCount = 0;
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if (board[i][j] == color) goodCount++;
				if (board[i][j] == enemyColor) badCount++;
			}
		}
		return goodCount - badCount;
	}
	
	private float greedyEval() {
		
		int[][] board = stateStack.peek();
		int goodCount = 0, badCount = 0;
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if (board[i][j] == color) goodCount++;
				if (board[i][j] == enemyColor) badCount++;
			}
		}
		
		return ((goodCount - badCount) / (goodCount + badCount)) * 20;
	}

	private int negamax(Node node, int depth, int alpha, int beta) {
//		if (System.currentTimeMillis() - startTime>= TIME_OUT) {
////			throw new TimeoutException("Timeout....");
//		}
		
		boolean isMax = (stateStack.size() % 2 == 1);
		if (depth == 0 || isGameOver()) {
			if (isMax) return evaluateState();
			else return -evaluateState();
			
		}
		
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
					bestMove = new int[2];
					bestMove[0] = child.r;
					bestMove[1] = child.c;
				}
			}
		}
		return eval;
	}
}
