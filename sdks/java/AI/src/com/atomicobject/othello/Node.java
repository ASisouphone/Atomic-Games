package com.atomicobject.othello;

import java.util.ArrayList;

public class Node {
    public int c;
    public int r;
    public int[] d;

    public ArrayList<Node> children;

    public Node(){
        
    }

    public Node(int c, int r, int[] d) {
        this.c = c;
        this.r = r;
        this.d = d;
    }
}