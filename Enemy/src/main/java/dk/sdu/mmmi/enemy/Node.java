package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import java.util.ArrayList;

public class Node {

    public PositionPart state;
    public Node parent;
    public int depth;
    public int cost;
    public double f;

    public Node(PositionPart state) {
        this.state = state;
    }

    public String key() {
        return state.getX() + ":" + state.getY();
    }

    public boolean isState(PositionPart otherPos) {
        PositionPart myPos = this.state;
        return myPos.getX() == otherPos.getX()
                && myPos.getY() == otherPos.getY();
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
}
