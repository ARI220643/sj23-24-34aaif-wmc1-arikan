package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int[][] adjacency;
    private final int[][] distanceMatrix;
    private final int nodeCount;
    public static final int x = Integer.MAX_VALUE;

    public Graph(int knoten) {
        this.adjacency = new int[knoten][knoten];
        this.distanceMatrix = new int[knoten][knoten];
        this.nodeCount = knoten;
    }

    public Graph(int[][] adjacency) {
        this.adjacency = adjacency;
        this.nodeCount = adjacency.length;
        this.distanceMatrix = new int[nodeCount][nodeCount];
    }

    public static Graph fromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int nodeCount = lines.size();
        int[][] adjacency = new int[nodeCount][nodeCount];
        int row = 0;
        for (String line : lines) {
            String[] cells = line.split(",");
            for (int col = 0; col < nodeCount; col++) {
                adjacency[row][col] = Integer.parseInt(cells[col]);
            }
            row++;
        }
        return new Graph(adjacency);
    }

    public void calcDistanceMatrix() {
        for (int r = 0; r < nodeCount; r++) {
            for (int c = 0; c < nodeCount; c++) {
                if (r == c) {
                    distanceMatrix[r][c] = 0;
                    continue;
                }
                int value = adjacency[r][c];
                distanceMatrix[r][c] = (value > 0) ? value : x;
            }
        }

        for (int k = 0; k < nodeCount; k++) {
            for (int i = 0; i < nodeCount; i++) {
                for (int j = 0; j < nodeCount; j++) {
                    if (distanceMatrix[i][k] != x && distanceMatrix[k][j] != x) {
                        int dist = distanceMatrix[i][k] + distanceMatrix[k][j];
                        if (distanceMatrix[i][j] > dist) {
                            distanceMatrix[i][j] = dist;
                        }
                    }
                }
            }
        }
    }

    public List<Integer> getReachableNodes(int startNode) {
        List<Integer> result = new ArrayList<>();
        for (int node = 0; node < nodeCount; ++node) {
            if (distanceMatrix[startNode][node] != x) {
                result.add(node);
            }
        }
        return result;
    }

    public List<List<Integer>> getComponents() {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nodeCount];

        for (int node = 0; node < nodeCount; node++) {
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfs(node, visited, component);
                result.add(component);
            }
        }

        return result;
    }

    private void dfs(int node, boolean[] visited, List<Integer> component) {
        visited[node] = true;
        component.add(node);
        for (int neighbor = 0; neighbor < nodeCount; neighbor++) {
            if (adjacency[node][neighbor] != 0 && !visited[neighbor]) {
                dfs(neighbor, visited, component);
            }
        }
    }

    public int componentsCount() {
        return getComponents().size();
    }

    public boolean isConnected() {
        for (int node = 0; node < nodeCount; node++) {
            if (distanceMatrix[0][node] == x) {
                return false;
            }
        }
        return true;
    }

    public int[] getEccentricity() {
        int[] eccentricities = new int[nodeCount];

        for (int startNode = 0; startNode < nodeCount; startNode++) {
            int maxDist = 0;
            for (int node = 0; node < nodeCount; node++) {
                if (distanceMatrix[startNode][node] != x) {
                    maxDist = Math.max(maxDist, distanceMatrix[startNode][node]);
                }
            }
            eccentricities[startNode] = maxDist;
        }

        return eccentricities;
    }

    public int durchmesser() {
        int[] exzentrizitaeten = this.getEccentricity();
        int durchmesser = exzentrizitaeten[0];

        for (int i = 0; i < exzentrizitaeten.length; i++) {
            durchmesser = Math.max(exzentrizitaeten[i], durchmesser);
        }

        return durchmesser;
    }

    public int radius() {
        int[] exzentrizitaeten = this.getEccentricity();
        int radius = exzentrizitaeten[0];

        for (int i = 0; i < exzentrizitaeten.length; ++i) {
            radius = Math.min(exzentrizitaeten[i], radius);
        }

        return radius;
    }

    public List<Integer> zentrum() {
        int[] exzentrizitaet = this.getEccentricity();
        int radius = this.radius();
        ArrayList<Integer> zentrum = new ArrayList<>();

        for (int i = 0; i < exzentrizitaet.length; ++i) {
            if (exzentrizitaet[i] == radius) {
                zentrum.add(i + 1);
            }
        }

        return zentrum;
    }

    public Graph removeNode(int node) {
        int[][] newAdjacency = new int[nodeCount - 1][nodeCount - 1];
        for (int i = 0, ni = 0; i < nodeCount; i++) {
            if (i == node) continue;
            for (int j = 0, nj = 0; j < nodeCount; j++) {
                if (j == node) continue;
                newAdjacency[ni][nj++] = adjacency[i][j];
            }
            ni++;
        }
        return new Graph(newAdjacency);
    }

    public List<Integer> getArticulations() {
        List<Integer> result = new ArrayList<>();
        int initialComponentCount = componentsCount();

        for (int articulationNode = 0; articulationNode < nodeCount; articulationNode++) {
            Graph testGraph = removeNode(articulationNode);
            testGraph.calcDistanceMatrix(); // Add this line
            if (testGraph.componentsCount() > initialComponentCount) {
                result.add(articulationNode);
            }
        }

        return result;
    }

    public Graph removeEdge(int startNode, int endNode) {
        int[][] newAdjacency = new int[nodeCount][nodeCount];
        for (int row = 0; row < nodeCount; row++) {
            System.arraycopy(adjacency[row], 0, newAdjacency[row], 0, nodeCount);
        }
        newAdjacency[startNode][endNode] = 0;
        newAdjacency[endNode][startNode] = 0;
        return new Graph(newAdjacency);
    }

    public List<int[]> getBridges() {
        List<int[]> result = new ArrayList<>();
        int initialComponentCount = componentsCount();

        for (int startNode = 0; startNode < nodeCount; startNode++) {
            for (int endNode = startNode + 1; endNode < nodeCount; endNode++) {
                if (adjacency[startNode][endNode] > 0) {
                    Graph testGraph = removeEdge(startNode, endNode);
                    testGraph.calcDistanceMatrix(); // Add this line
                    if (testGraph.componentsCount() > initialComponentCount) {
                        result.add(new int[]{startNode, endNode});
                    }
                }
            }
        }

        return result;
    }

    public void print() {
        for (int row = 0; row < this.adjacency.length; ++row) {
            for (int col = 0; col < this.adjacency[row].length; ++col) {
                if (this.adjacency[row][col] == -99) {
                    System.out.print("x ");
                } else {
                    System.out.print(this.adjacency[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    public void printDistanceMatrix() {
        for (int row = 0; row < this.distanceMatrix.length; ++row) {
            for (int col = 0; col < this.distanceMatrix[row].length; ++col) {
                if (this.distanceMatrix[row][col] == x) {
                    System.out.print("x ");
                } else {
                    System.out.print(this.distanceMatrix[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    public int getNodeCount() {
        return this.nodeCount;
    }

    public int[][] getAdjacencyMatrix() {
        return this.adjacency;
    }
}
