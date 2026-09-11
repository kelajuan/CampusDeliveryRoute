import java.util.*;

public class CampusDeliveryRoute {

    // Edge class for neighbor connections
    static class Edge {
        String target;
        int distance;

        Edge(String target, int distance) {
            this.target = target;
            this.distance = distance;
        }
    }

    // Node class for priority queue
    static class Node implements Comparable<Node> {
        String name;
        int dist;

        Node(String name, int dist) {
            this.name = name;
            this.dist = dist;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    public static void main(String[] args) {
        // Init graph
        Map<String, List<Edge>> graph = new HashMap<>();

        String[] locations = {
            "Main Gate", "Administration", "Library", 
            "Cafeteria", "Computer Lab", "Sports Complex", "Hostel"
        };

        for (String loc : locations) {
            graph.put(loc, new ArrayList<>());
        }

        // Add roads from assignment table
        addRoad(graph, "Main Gate", "Administration", 4);
        addRoad(graph, "Main Gate", "Library", 6);
        addRoad(graph, "Administration", "Cafeteria", 3);
        addRoad(graph, "Administration", "Computer Lab", 5);
        addRoad(graph, "Library", "Computer Lab", 2);
        addRoad(graph, "Library", "Hostel", 7);
        addRoad(graph, "Cafeteria", "Sports Complex", 6);
        addRoad(graph, "Computer Lab", "Sports Complex", 3);
        addRoad(graph, "Computer Lab", "Hostel", 4);
        addRoad(graph, "Sports Complex", "Hostel", 2);

        // Run dijkstra
        findShortestRoute(graph, "Main Gate", "Hostel");
    }

    // Helper to add 2-way roads
    private static void addRoad(Map<String, List<Edge>> graph, String from, String to, int dist) {
        graph.get(from).add(new Edge(to, dist));
        graph.get(to).add(new Edge(from, dist));
    }

    public static void findShortestRoute(Map<String, List<Edge>> graph, String source, String destination) {
        Map<String, Integer> distMap = new HashMap<>();
        Map<String, String> prevMap = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Set dist to infinity first
        for (String node : graph.keySet()) {
            distMap.put(node, Integer.MAX_VALUE);
            prevMap.put(node, null);
        }

        // Start node dist is 0
        distMap.put(source, 0);
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String u = current.name;

            // Stop if at target
            if (u.equals(destination)) {
                break;
            }

            if (current.dist > distMap.get(u)) {
                continue;
            }

            // Check neighbors
            for (Edge edge : graph.get(u)) {
                String v = edge.target;
                int weight = edge.distance;

                // Update if shorter path found
                if (distMap.get(u) + weight < distMap.get(v)) {
                    distMap.put(v, distMap.get(u) + weight);
                    prevMap.put(v, u);
                    pq.add(new Node(v, distMap.get(v)));
                }
            }
        }

        // Track path backwards
        List<String> path = new ArrayList<>();
        String current = destination;
        while (current != null) {
            path.add(current);
            current = prevMap.get(current);
        }
        Collections.reverse(path);

        // Print output
        System.out.println("==================================================");
        System.out.println("   SMART CAMPUS DELIVERY SYSTEM - ROUTE RESULT   ");
        System.out.println("==================================================");
        System.out.println("Start Point: " + source);
        System.out.println("Destination: " + destination);
        System.out.print("Shortest Route: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("Total Distance: " + distMap.get(destination) + " km");
        System.out.println("==================================================");
    }
}