package dk.sdu.mmmi.enemy;

import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import java.util.ArrayList;
import java.util.HashMap;

public class AStar {

    private static final int weight = 5;

    public static PositionPart search(int sightLimit, PositionPart initialState, PositionPart goalState) {
        // A* search
        HashMap<String, Node> fringe = new HashMap<String, Node>();
        HashMap<String, Node> visited = new HashMap<String, Node>();

        Node initialNode = new Node(initialState);
        fringe.put(initialNode.key(), initialNode);

        while (!fringe.isEmpty()) {

            Node node = getCheapestNode(fringe);
            fringe.remove(node.key());
            visited.put(node.key(), node);

            if (node.isState(goalState)) {
                return node.nextMove();
            }

            if (node.depth == sightLimit) {
                break;
            }

            HashMap<String, Node> children = expandNode(node, goalState, fringe, visited);
            fringe.putAll(children);
        }

        return null;
    }

    private static HashMap<String, Node> expandNode(
            Node node,
            PositionPart goalState,
            HashMap<String, Node> fringe,
            HashMap<String, Node> visited) {
        HashMap<String, Node> successors = new HashMap<String, Node>();
        ArrayList<PositionPart> children = getChildren(node.state);

        for (PositionPart child : children) {

            Node s = new Node(child);
            // node already visited
            if (visited.containsKey(s.key())) {
                continue;
            }

            s.parent = node;
            s.depth = node.depth + 1;
            s.cost = node.cost + 1;
            s.f = f(s, goalState);

            // node already on fringe, and the node is worse than the fringe version
            if (fringe.containsKey(s.key()) && s.cost > fringe.get(s.key()).cost) {
                continue;
            }

            successors.put(s.key(), s);
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

        float deadZoneStartX = 100;
        float deadZoneStopX = 200;
        float deadZoneStartY = 100;
        float deadZoneStopY = 200;

        if (deadZoneStartX <= x && x <= deadZoneStopX && deadZoneStartY <= y && y <= deadZoneStopY) {
            return;
        }

        children.add(new PositionPart(x, y));
    }

    private static Node getCheapestNode(HashMap<String, Node> fringe) {
        Node cheapest = null;
        for (Node n : fringe.values()) {
            if (cheapest == null) {
                cheapest = n;
            } else if (cheapest.f > n.f) {
                cheapest = n;
            }
        }
        return cheapest;
    }

    private static double f(Node n, PositionPart targetPos) {
        return g(n) + weight * h(n, targetPos);
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
