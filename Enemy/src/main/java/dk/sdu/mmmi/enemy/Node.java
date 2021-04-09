package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import java.util.ArrayList;

public class Node implements Comparable<Node> {

    public PositionPart state;
    public Node parent;
    public int depth;
    public int cost;
    public double f;

    public Node(PositionPart state) {
        this.state = state;
    }

    public boolean isState(PositionPart goalState) {
        return this.state.getX() == goalState.getX() && this.state.getY() == goalState.getY();
    }

    public PositionPart nextMove() {
        ArrayList<Node> path = path();
        if (path.size() > 1) {
            // remove start position
            path.remove(path.size() - 1);
        }
        // return next move
        return path.get(path.size() - 1).state;
    }

    private ArrayList<Node> path() {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(this);

        Node currentNode = this;
        // while current node has parent
        while (currentNode.parent != null) {
            // make parent the current node path
            currentNode = currentNode.parent;
            // add current node to path
            path.add(currentNode);
        }

        return path;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }
}
