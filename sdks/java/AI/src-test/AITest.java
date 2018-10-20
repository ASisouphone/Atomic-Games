import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.atomicobject.othello.AI;
import com.atomicobject.othello.GameState;


public class AITest {

	class Node {
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
	
	private static int parsePort(String port) {
		return Integer.parseInt(port);
	}

	
	public void test() {
		// This is just an example test to show JUnit working. It won't be useful
		// for your real implementation.
		AI ai = new AI(new int[][] {{2, 4}, {3, 5}});
		GameState state = new GameState();
		state.setPlayer(1);
		state.setBoard(new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 1, 2, 0, 0, 0},
				                   {0, 0, 0, 2, 1, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0}});
		
		// Our first canned move is [2,4]
		assertArrayEquals(new int[]{2, 4}, ai.computeMove(state));

		// Our second canned move is [3, 5]
		assertArrayEquals(new int[]{3, 5}, ai.computeMove(state));
	}

	@Test
	public void testValidMove () {
		AI ai = new AI(new int[][] {});
		GameState state = new GameState();
		state.setPlayer(1);
		state.setBoard(new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 1, 2, 0, 0, 0},
				                   {0, 0, 0, 2, 1, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0},
				                   {0, 0, 0, 0, 0, 0, 0, 0}});
		ai.stateStack.push(state.getBoard());
		ArrayList<com.atomicobject.othello.Node> tempList = ai.getChildrenNodes(true);
		System.out.println(tempList.size());
		assertEquals(tempList.size(), 4);
		
		
	}
}
