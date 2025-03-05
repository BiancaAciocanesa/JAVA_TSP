package algorithms;

import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.Multigraph;
import org.graph4j.alg.eulerian.HierholzerEulerianCircuit;
import org.graph4j.alg.eulerian.HierholzerEulerianTrail;
import org.graph4j.alg.matching.GreedyWeightedMatching;
import org.graph4j.alg.mst.PrimMinimumSpanningTreeDefault;
import org.graph4j.util.Circuit;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChristofidesTSP {

    private static Graph<?,?> getMst(Graph<?,?> g){

        var mst = new PrimMinimumSpanningTreeDefault(g);
        return  mst.getTree();
    }
    private static Graph<?,?> getOddDegreeVerticesSubGraph(Graph<?,?> mst, Graph<?,?> original){

        int[] oddDegreeVertices = new int[mst.vertices().length];
        int num = 0;

        for(var vertex : mst.vertices()){
            if(mst.degree(vertex) % 2 == 1){
                oddDegreeVertices[num++] = vertex;
            }
        }
        return original.subgraph(Arrays.stream(oddDegreeVertices).limit(num).toArray());
    }

    private static int[][] getPerfectMatching(Graph<?,?> g){

        org.jgrapht.Graph<Integer, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for(var vertex: g.vertices()) {
            graph.addVertex(vertex);
        }
        for(var edge: g.edges()) {
            graph.setEdgeWeight(graph.addEdge(edge.source(), edge.target()), edge.weight());
        }

        KolmogorovWeightedPerfectMatching<Integer, DefaultWeightedEdge> matchingAlgorithm = new KolmogorovWeightedPerfectMatching<>(graph);
        MatchingAlgorithm.Matching<Integer, DefaultWeightedEdge> matching = matchingAlgorithm.getMatching();

        int[][] edges = new int[matching.getEdges().size()][2];
        int cnt = 0;
        for(var edge: matching.getEdges()) {
            edges[cnt][0] = graph.getEdgeSource(edge);
            edges[cnt][1] = graph.getEdgeTarget(edge);
            cnt++;
        }
        return edges;
    }

    private static Multigraph<?,?> getCombinedGraph(Graph<?,?> mst, int[][] matching){

        Multigraph<?,?> m = GraphBuilder.empty().buildMultigraph();

        for(var vertex : mst.vertices())
            m.addVertex(vertex);
        for(var edge : mst.edges())
            m.addEdge(edge.source(), edge.target(), edge.weight());

        for(var edge : matching){
            m.addEdge(edge[0], edge[1]);
        }
        return  m;
    }

    private static Circuit getEulerianCircuit(Multigraph<?,?> g){

        var alg = new HierholzerEulerianCircuit(g);
        return alg.findCircuit();
    }

    private double getDistance(double x1, double y1, double x2, double y2){

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public List<Integer> run(List<Point2D.Double> cities){

        var g = GraphBuilder.empty().buildGraph();
        for(int i = 0 ; i < cities.size(); i++){
            g.addVertex(i);
        }

        for (int i = 0 ; i < cities.size() - 1; i++)
            for (int j = i + 1; j < cities.size(); j++)
                g.addEdge(i, j, getDistance(cities.get(i).getX(), cities.get(i).getY(), cities.get(j).getX(), cities.get(j).getY()));

        var mst = getMst(g);
        var oddDegreeGraph = getOddDegreeVerticesSubGraph(mst, g);
        var matching = getPerfectMatching(oddDegreeGraph);
        var multigraph = getCombinedGraph(mst, matching);
        var circuit  = getEulerianCircuit(multigraph);
        List<Integer> citiesOrder = new ArrayList<>();
        if(circuit != null){
            for(int i = 0 ;i < circuit.length(); i++)
                if(!citiesOrder.contains(circuit.get(i) + 1))
                    citiesOrder.add(circuit.get(i) + 1);

            citiesOrder.add(citiesOrder.getFirst());
        }
        System.out.println(citiesOrder);
        return citiesOrder;
    }
}

