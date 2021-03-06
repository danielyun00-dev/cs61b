import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;
    private int startVertex;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        adjLists[v1].add(new Edge(v1, v2, weight));
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        adjLists[v1].add(new Edge(v1, v2, weight));
        adjLists[v2].add(new Edge(v2, v1, weight));
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        Iterator<Edge> edges = adjLists[from].iterator();
        while (edges.hasNext()) {
            Edge e = edges.next();
            if (e.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> toReturn = new LinkedList<>();
        for (Edge e : adjLists[v]) {
            toReturn.add(e.to);
        }
        return toReturn;
    }

    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int total = 0;
        for (int i = 0; i < vertexCount; i++) {
            if (isAdjacent(v, i)) {
                total += 1;
            }
        }
        return total;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            return !fringe.empty();
        }

        public Integer next() {
            int v = fringe.pop();
            if (!visited.contains(v)) {
                for (Edge e : adjLists[v]) {
                    if (!visited.contains(e.to) && !fringe.contains(e.to)) {
                        fringe.push(e.to);
                    }
                }
                visited.add(v);
            }
            return v;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        if (start == stop) {
            return true;
        }
        List<Integer> temp = dfs(start);
        for (Integer i : temp) {
            if (i == stop) {
                return true;
            }
        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        List toReturn = new LinkedList<>();
        if (!pathExists(start, stop)) {
            return toReturn;
        } else if (start == stop) {
            toReturn.add(start);
            return toReturn;
        } else {
            Iterator<Integer> iter = new DFSIterator(start);
            List<Integer> temp = new LinkedList();
            while (iter.hasNext()) {
                int v = iter.next();
                if (v != stop) {
                    temp.add(v);
                } else {
                    break;
                }
            }
            int end = stop;
            toReturn.add(end);
            for (int i = temp.size() - 1; i >= 0; i--) {
                if (temp.get(i) == start && isAdjacent(start, end)) {
                    toReturn.add(temp.get(i));
                    break;
                } else if (isAdjacent(temp.get(i), end)) {
                    toReturn.add(temp.get(i));
                    end = temp.get(i);
                }
            }
        }
        Collections.reverse(toReturn);
        return toReturn;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;
        private Integer[] inDegrees;
        //  Instance variables here!

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            //  YOUR CODE HERE
            visited = new HashSet<>();
            inDegrees = new Integer[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                inDegrees[i] = inDegree(i);
                if (inDegree(i) == 0) {
                    fringe.push(i);
                }
            }
        }

        public boolean hasNext() {
            //  YOUR CODE HERE
            return !fringe.isEmpty();
        }

        public Integer next() {
            //  YOUR CODE HERE
            Integer v = fringe.pop();
            for (Edge e : adjLists[v]) {
                inDegrees[v] = inDegrees[v] - 1;
            }
            visited.add(v);
            for (int i = 0; i < vertexCount; i++) {
                if (!visited.contains(v) && !fringe.contains(v)
                        && inDegrees[i] == 0) {
                    fringe.push(i);
                }
            }
            return v;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public List<Integer> shortestPath(int start, int stop) {
        LinkedList<Integer> toReturn = new LinkedList<>();
        if (start == stop || !pathExists(start, stop)) {
            toReturn.add(start);
            return toReturn;
        } else if (!pathExists(start, stop)) {
            toReturn.add(stop);
            return toReturn;
        }
        // initialize distance
        HashMap<Integer, Integer> distance = new HashMap<>();
        HashMap<Integer, Integer> preVertex = new HashMap<>();
        List<Integer> allNode = dfs(start);
        //queue
        PriorityQueue<Integer> queue = new
                PriorityQueue<>((o1, o2) -> (distance.get(o1) - distance.get(o2)));
        for (int i = 0; i < vertexCount; i++) {
            if (i == start) {
                distance.put(i, 0);
            } else {
                distance.put(i, 10000000);
            }
        }
        queue.add(start);
        //visited
        HashSet<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            Integer currVertex = queue.poll();
            if (!visited.contains(currVertex)) {
                visited.add(currVertex);
                for (Edge e : adjLists[currVertex]) {
                    queue.add(e.to);
                    if (distance.get(e.to) > distance.get(currVertex) + e.weight) {
                        distance.put(e.to, distance.get(currVertex) + e.weight);
                        preVertex.put(e.to, currVertex);
                    }
                }
            }
        }
        int pre = preVertex.get(stop);
        toReturn.add(stop);
        while (pre != start) {
            toReturn.add(pre);
            pre = preVertex.get(pre);
        }
        toReturn.add(start);
        Collections.reverse(toReturn);
        return toReturn;
    }

    public Edge getEdge(int u, int v) {
        Iterator<Edge> edges = adjLists[u].iterator();
        while (edges.hasNext()) {
            Edge e = edges.next();
            if (e.from == u && e.to == v) {
                return e;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        /*Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();*/
        Graph g = new Graph(5);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 3, 30);
        g.addEdge(0, 4, 100);
        g.addEdge(1, 2, 50);
        g.addEdge(2, 4, 10);
        g.addEdge(3, 4, 60);
        g.addEdge(3, 2, 20);
        List<Integer> a = g.shortestPath(0, 2);
        System.out.println(a);
    }
}
