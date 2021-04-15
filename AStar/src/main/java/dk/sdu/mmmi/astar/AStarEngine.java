package dk.sdu.mmmi.astar;

import dk.sdu.mmmi.common.data.Entity;
import dk.sdu.mmmi.common.data.World;
import dk.sdu.mmmi.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.common.services.ICollisionChecker;
import java.util.ArrayList;
import java.util.HashMap;

public class AStarEngine {

    private ICollisionChecker collisionChecker;
    private final int weight = 5;

    public AStarEngine(ICollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public PositionPart search(
            World world,
            Entity me,
            int sightLimit,
            PositionPart goalState) {

        // A* search
        HashMap<String, Node> fringe = new HashMap<String, Node>();
        HashMap<String, Node> visited = new HashMap<String, Node>();

        PositionPart initialState = me.getPart(PositionPart.class);

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

            HashMap<String, Node> children = expandNode(world, me, node, goalState, fringe, visited);
            fringe.putAll(children);
        }

        return null;
    }

    private HashMap<String, Node> expandNode(
            World world,
            Entity me,
            Node node,
            PositionPart goalState,
            HashMap<String, Node> fringe,
            HashMap<String, Node> visited) {
        HashMap<String, Node> successors = new HashMap<String, Node>();
        ArrayList<PositionPart> children = getChildren(world, me, node.state);

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

    private ArrayList<PositionPart> getChildren(
            World world,
            Entity me,
            PositionPart state) {
        float x = state.getX();
        float y = state.getY();
        ArrayList<PositionPart> children = new ArrayList<>();

        // go north
        addIfValid(world, me, children, x, y + 1);
        // go south
        addIfValid(world, me, children, x, y - 1);
        // go east
        addIfValid(world, me, children, x + 1, y);
        // go west
        addIfValid(world, me, children, x - 1, y);
        // go north east
        addIfValid(world, me, children, x + 1, y + 1);
        // go north west
        addIfValid(world, me, children, x - 1, y + 1);
        // go south east
        addIfValid(world, me, children, x + 1, y - 1);
        // go south west
        addIfValid(world, me, children, x - 1, y - 1);

        return children;
    }

    private void addIfValid(
            World world,
            Entity me,
            ArrayList<PositionPart> children,
            float x,
            float y) {
        if (this.collisionChecker.isPositionFree(world, me, x, y)) {
            children.add(new PositionPart(x, y));
        }
    }

    private Node getCheapestNode(HashMap<String, Node> fringe) {
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

    private double f(Node n, PositionPart targetPos) {
        return g(n) + weight * h(n, targetPos);
    }

    private double g(Node n) {
        // travel cost (from initialState to this state)
        return n.cost;
    }

    private double h(Node n, PositionPart targetPos) {
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
