package tsp.helper;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPFileReader {

    private String name;
    private String type;
    private int dimension;
    private String edgeWeightType;
    private List<Point2D.Double> coordinates;

    public TSPFileReader() {
        coordinates = new ArrayList<>();
    }

    public void readFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isNodeSection = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("NAME")) {
                    name = line.split(":")[1].trim();
                } else if (line.startsWith("TYPE")) {
                    type = line.split(":")[1].trim();
                } else if (line.startsWith("DIMENSION")) {
                    dimension = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                    edgeWeightType = line.split(":")[1].trim();
                } else if (line.startsWith("NODE_COORD_SECTION")) {
                    isNodeSection = true;
                } else if (line.startsWith("EOF")) {
                    break;
                } else if (isNodeSection) {
                    String[] parts = line.split("\\s+");
                    int id = Integer.parseInt(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    coordinates.add(new Point2D.Double(x, y));
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDimension() {
        return dimension;
    }

    public String getEdgeWeightType() {
        return edgeWeightType;
    }

    public List<Point2D.Double> getCoordinates() {
        return coordinates;
    }

    public static void main(String[] args) {
        TSPFileReader reader = new TSPFileReader();
        try {
            reader.readFile("path/to/your/file.tsp");
            System.out.println("Name: " + reader.getName());
            System.out.println("Type: " + reader.getType());
            System.out.println("Dimension: " + reader.getDimension());
            System.out.println("Edge Weight Type: " + reader.getEdgeWeightType());
            System.out.println("Coordinates:");
            for (Point2D.Double point : reader.getCoordinates()) {
                System.out.println(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
