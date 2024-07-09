package model;

import java.io.IOException;
import java.util.List;

public class Import {

    public static void main(String[] args) {
        try {
            Graph g = Graph.fromFile("12KnotenTest.csv");
            g.calcDistanceMatrix();
            System.out.println("Graph:");
            g.print();
            System.out.println();
            System.out.println("Distanzmatrix:");
            g.printDistanceMatrix();
            System.out.println();
            System.out.println("Anzahl der Knoten: " + g.getNodeCount());
            int kanten = 0;
            int[][] graphKanten = g.getAdjacencyMatrix();
            for (int i = 0; i < graphKanten.length; i++) {
                for (int j = i; j < graphKanten[i].length; j++) {
                    if (graphKanten[i][j] != 0 && graphKanten[i][j] != Graph.x) {
                        kanten++;
                    }
                }
            }
            System.out.println("Anzahl der Kanten: " + kanten);

            System.out.println("Komponenten:");
            List<List<Integer>> komponenten = g.getComponents();
            for (List<Integer> komponente : komponenten) {
                System.out.println(komponente);
            }

            g = new Graph(g.getAdjacencyMatrix());
            g.calcDistanceMatrix(); // Stellen Sie sicher, dass die Distanzmatrix berechnet wurde

            int[] eccentricities = g.getEccentricity();
            System.out.println("Exzentrizität:");
            for (int i = 0; i < g.getNodeCount(); i++) {
                System.out.println("Knoten " + i + ": " + eccentricities[i]);
            }
            System.out.println();

            System.out.println("Durchmesser: " + g.durchmesser());

            System.out.println("Radius: " + g.radius());

            System.out.println("Zentrum:");
            System.out.println(g.zentrum());

            System.out.println("Artikulationen:");
            System.out.println(g.getArticulations());

            System.out.println("Brücken:");
            List<int[]> bruecken = g.getBridges();
            for (int[] bruecke : bruecken) {
                System.out.println("Brücke zwischen: " + bruecke[0] + " und " + bruecke[1]);
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }
}
