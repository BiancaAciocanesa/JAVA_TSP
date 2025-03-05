package algorithms;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ChristofidesJGraphT {

    public List<Integer> run(List<Point2D.Double> newPoints){

        Graph<Integer,DefaultEdge> graph = new SimpleWeightedGraph<>(DefaultEdge.class);
        for(int i=0;i<newPoints.size();i++)
            graph.addVertex(i);

        for(int i=0;i<newPoints.size();i++)
            for(int j=i+1;j<newPoints.size();j++)
                graph.setEdgeWeight(graph.addEdge(i,j), getDistance(newPoints.get(i).getX(), newPoints.get(i).getY(), newPoints.get(j).getX(), newPoints.get(j).getY()));

        ChristofidesThreeHalvesApproxMetricTSP<Integer, DefaultEdge> christofides = new ChristofidesThreeHalvesApproxMetricTSP<>();
        GraphPath<Integer, DefaultEdge> tour = christofides.getTour(graph);

        // Print the tour
        System.out.println("Approximate TSP tour: " + tour.getVertexList());
        System.out.println("Tour weight: " + tour.getWeight());

        return tour.getVertexList().stream().map( p -> p+1).toList();
    }

    private double getDistance(double x1, double y1, double x2, double y2){

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
