package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {

    public static PositionPart search(int sightLimit, PositionPart initialState, PositionPart goalState) {
        // A* search
        PriorityQueue<Node> fringe = new PriorityQueue<Node>();
        Node initialNode = new Node(initialState);
        fringe.add(initialNode);

        while (!fringe.isEmpty()) {

            Node node = removeCheapestNode(fringe);
            if (node.isState(goalState)) {
                return node.nextMove();
            }

            if (node.depth == sightLimit) {
                break;
            }

            //startTime = System.nanoTime();
            ArrayList<Node> children = expandNode(node, goalState);

            fringe.addAll(children);
        }

        return initialState;
    }

    private static ArrayList<Node> expandNode(Node node, PositionPart goalState) {
        ArrayList<Node> successors = new ArrayList<>();
        ArrayList<PositionPart> children = getChildren(node.state);

        for (PositionPart child : children) {
            Node s = new Node(child);
            s.parent = node;
            s.depth = node.depth + 1;
            s.cost = node.cost + 1;
            s.f = f(s, goalState);
            successors.add(s);
        }

        return successors;
    }

    private static ArrayList<PositionPart> getChildren(PositionPart state) {
        float x = state.getX();
        float y = state.getY();
        ArrayList<PositionPart> children = new ArrayList<>();

        // go north
        addIfValid(children, x, y + 1);
        // go south
        addIfValid(children, x, y - 1);
        // go east
        addIfValid(children, x + 1, y);
        // go west
        addIfValid(children, x - 1, y);
        // go north east
        addIfValid(children, x + 1, y + 1);
        // go north west
        addIfValid(children, x - 1, y + 1);
        // go south east
        addIfValid(children, x + 1, y - 1);
        // go south west
        addIfValid(children, x - 1, y - 1);

        return children;
    }

    private static void addIfValid(ArrayList<PositionPart> children, float x, float y) {

        children.add(new PositionPart(x, y));
    }

    private static Node removeCheapestNode(PriorityQueue<Node> fringe) {
        return fringe.poll();
    }

    private static double f(Node n, PositionPart targetPos) {
        return g(n) + 5 * h(n, targetPos);
    }

    private static double g(Node n) {
        // travel cost (from initialState to this state)
        return n.cost;
    }

    private static double h(Node n, PositionPart targetPos) {
        // heuristic cost (from this state to goalState)

        // x distance to target
        float dx = n.state.getX() - targetPos.getX();
        // y distance to target
        float dy = n.state.getY() - targetPos.getY();

        // a^2 + b^2 = c^2
        // c = sqrt(a^2 + b^2)
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
}
