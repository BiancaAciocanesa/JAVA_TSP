package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PointsPlotter extends JPanel {

    private List<Point.Double> points;
    private List<Integer> optimalOrder = new ArrayList<>();
    private List<Integer> currentOrder = new ArrayList<>();
    private double optimalLength;
    private double currentLength;


    private int margin = 50; // margin around the plot

    public PointsPlotter(List<Point.Double> points) {
        this.points = points;
        setPreferredSize(new Dimension(2400, 1200)); // Set preferred size of the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        var scaledPoints = rotatePoints(points, 270);

        double minX = scaledPoints.stream().mapToDouble(p -> p.x).min().orElse(0);
        double maxX = scaledPoints.stream().mapToDouble(p -> p.x).max().orElse(0);
        double minY = scaledPoints.stream().mapToDouble(p -> p.y).min().orElse(0);
        double maxY = scaledPoints.stream().mapToDouble(p -> p.y).max().orElse(0);

        double scaleX = (getWidth() - 2 * margin) / (maxX - minX);
        double scaleY = (getHeight() - 2 * margin) / (maxY - minY);
        double scale = Math.min(scaleX, scaleY); // to keep the aspect ratio

        for (Point.Double point : scaledPoints) {
            int x = (int) ((point.x - minX) * scale + margin);
            int y = (int) ((point.y - minY) * scale + margin);

            // Draw the point
            g.fillOval(x - 2, y - 2, 5, 5); // Draw each point as a small circle

        }
        g.setColor(Color.BLUE);
        optimalLength = 0;
        for(int i = 1; i < optimalOrder.size(); i++) {
            drawLineBetweenPoints(scaledPoints.get(optimalOrder.get(i - 1) - 1), scaledPoints.get(optimalOrder.get(i) - 1), g);
            optimalLength += getDistance(scaledPoints.get(optimalOrder.get(i - 1) - 1).getX(),
                                        scaledPoints.get(optimalOrder.get(i - 1) - 1).getY(),
                                        scaledPoints.get(optimalOrder.get(i) - 1).getX(),
                                        scaledPoints.get(optimalOrder.get(i) - 1).getY());
        }
        //System.out.println(optimalLength);
        g.setColor(Color.BLACK);
        if(!currentOrder.isEmpty()){

            currentLength = 0;
            Graphics2D g2 = (Graphics2D) g;
            for(int i = 1; i < currentOrder.size(); i++){

                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{10, 10}, 0));
                drawLineBetweenPoints(scaledPoints.get(currentOrder.get(i - 1) - 1), scaledPoints.get(currentOrder.get(i) - 1), g2);
                currentLength += getDistance(scaledPoints.get(currentOrder.get(i - 1) - 1).getX(),
                        scaledPoints.get(currentOrder.get(i - 1) - 1).getY(),
                        scaledPoints.get(currentOrder.get(i) - 1).getX(),
                        scaledPoints.get(currentOrder.get(i) - 1).getY());
            }
        }
    }
    public void drawLineBetweenPoints(Point2D.Double point1, Point2D.Double point2, Graphics g) {

        var scaledPoints = rotatePoints(points, 270);

        double minX = scaledPoints.stream().mapToDouble(p -> p.x).min().orElse(0);
        double maxX = scaledPoints.stream().mapToDouble(p -> p.x).max().orElse(0);
        double minY = scaledPoints.stream().mapToDouble(p -> p.y).min().orElse(0);
        double maxY = scaledPoints.stream().mapToDouble(p -> p.y).max().orElse(0);
        double scaleX = (getWidth() - 2 * margin) / (maxX - minX);
        double scaleY = (getHeight() - 2 * margin) / (maxY - minY);
        double scale = Math.min(scaleX, scaleY); // to keep the aspect ratio

        int x1 = (int) ((point1.x - minX) * scale + margin);
        int y1 = (int) ((point1.y - minY) * scale + margin);
        int x2 = (int) ((point2.x - minX) * scale + margin);
        int y2 = (int) ((point2.y - minY) * scale + margin);

        g.drawLine(x1, y1, x2, y2);
    }

    public void setPoints(List<Point.Double> points) {
        this.points = points;
    }

    public List<Point.Double> getPoints(){

        return this.points;
    }

    public void setOptimalOrder(List<Integer> order){
        this.optimalOrder = order;
    }

    public void setcurrentOrder(List<Integer> order){
        this.currentOrder = order;
    }

    public List<Point2D.Double> rotatePoints(List<Point2D.Double> points, double thetaDegrees) {
        List<Point2D.Double> rotatedPoints = new ArrayList<>();

        // Convert the angle from degrees to radians
        double thetaRadians = Math.toRadians(thetaDegrees);

        // Calculate cosine and sine of the angle
        double cosTheta = Math.cos(thetaRadians);
        double sinTheta = Math.sin(thetaRadians);

        // Apply the rotation matrix to each point
        for (var p : points) {
            double xNew = p.x * cosTheta - p.y * sinTheta;
            double yNew = p.x * sinTheta + p.y * cosTheta;
            rotatedPoints.add(new Point2D.Double(xNew, yNew));
        }

        return rotatedPoints;
    }

    public double getLength() {
        return currentLength;
    }

    public double getOptimalLength() {
        return optimalLength;
    }

    private double getDistance(double x1, double y1, double x2, double y2){

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
