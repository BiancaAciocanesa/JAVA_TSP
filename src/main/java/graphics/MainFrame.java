package graphics;

import algorithms.ChristofidesJGraphT;
import algorithms.ChristofidesTSP;
import algorithms.GeneticAlgorithm;
import tsp.helper.TSPFileReader;
import tsp.helper.TSPSolutionFileReader;
import utils.AlgMetrics;
import utils.Report;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainFrame extends JFrame {
    private final ControlPanel controlPanel;
    private final PointsPlotter plotter;
    private final StatusPanel statusPanel;


    public MainFrame() {

        super("Traveling Salesman");

        controlPanel = new ControlPanel();
        statusPanel = new StatusPanel();

        List<AlgMetrics> algMetrics = new ArrayList<>();

        List<Point2D.Double> points = new ArrayList<>();
        plotter = new PointsPlotter(points);
        controlPanel.getStartAlgorithm().addActionListener(e -> {

            var selectedAlg = controlPanel.getDropdownMenuAlgorithms().getSelectedItem();
            var selectedInput = controlPanel.getDropdownMenuInputs().getSelectedItem();

            switch (selectedAlg.toString()) {

                case "Christofides": {

                    var timeStart = System.currentTimeMillis();
                    ChristofidesTSP tsp = new ChristofidesTSP();
                    var pointsOrder = tsp.run(plotter.getPoints());
                    var timeFinish = System.currentTimeMillis();
                    var totalTime = (timeFinish - timeStart);
                    plotter.setcurrentOrder(pointsOrder);
                    plotter.repaint();

                    SwingUtilities.invokeLater(() -> {
                        String formattedValue = String.format("%.2f", plotter.getLength());
                        statusPanel.getCurrentLengthField().setText(formattedValue);
                        formattedValue = String.format("%.2f", plotter.getOptimalLength());
                        statusPanel.getOptimumLengthField().setText(formattedValue);
                        statusPanel.getCurrentTimeField().setText(totalTime + "ms");
                        statusPanel.repaint();
                        algMetrics.add(new AlgMetrics(plotter.getOptimalLength(), plotter.getLength(), controlPanel.getDropdownMenuAlgorithms().getSelectedItem().toString(), Double.parseDouble(statusPanel.getCurrentTimeField().getText().replace("ms",""))));

                    });
                    break;
                }
                case "Algoritm Genetic": {

                    var timeStart = System.currentTimeMillis();
                    GeneticAlgorithm ga = new GeneticAlgorithm();
                    var pointsOrder =  List.copyOf(ga.run(plotter.getPoints()));
                    var timeFinish = System.currentTimeMillis();
                    var totalTime = (timeFinish - timeStart);
                    plotter.setcurrentOrder(pointsOrder);
                    plotter.repaint();

                    SwingUtilities.invokeLater(() -> {
                        String formattedValue = String.format("%.2f", plotter.getLength());
                        statusPanel.getCurrentLengthField().setText(formattedValue);
                        formattedValue = String.format("%.2f", plotter.getOptimalLength());
                        statusPanel.getOptimumLengthField().setText(formattedValue);
                        statusPanel.getCurrentTimeField().setText(totalTime + "ms");
                        statusPanel.repaint();
                        algMetrics.add(new AlgMetrics(plotter.getOptimalLength(), plotter.getLength(), controlPanel.getDropdownMenuAlgorithms().getSelectedItem().toString(), Double.parseDouble(statusPanel.getCurrentTimeField().getText().replace("ms",""))));

                    });
                    break;
                }
                case "Christofides JGraphT": {

                    var timeStart = System.currentTimeMillis();
                    ChristofidesJGraphT chr = new ChristofidesJGraphT();
                    var pointsOrder =  List.copyOf(chr.run(plotter.getPoints()));
                    var timeFinish = System.currentTimeMillis();
                    var totalTime = (timeFinish - timeStart);
                    plotter.setcurrentOrder(pointsOrder);
                    plotter.repaint();

                    SwingUtilities.invokeLater(() -> {
                        String formattedValue = String.format("%.2f", plotter.getLength());
                        statusPanel.getCurrentLengthField().setText(formattedValue);
                        formattedValue = String.format("%.2f", plotter.getOptimalLength());
                        statusPanel.getOptimumLengthField().setText(formattedValue);
                        statusPanel.getCurrentTimeField().setText(totalTime + "ms");
                        statusPanel.repaint();
                        algMetrics.add(new AlgMetrics(plotter.getOptimalLength(), plotter.getLength(), controlPanel.getDropdownMenuAlgorithms().getSelectedItem().toString(), Double.parseDouble(statusPanel.getCurrentTimeField().getText().replace("ms",""))));

                    });
                    break;
                }

                default: {
                    System.out.println("none");
                    break;
                }
            }
        });

        controlPanel.getDropdownMenuAlgorithms().addActionListener(e -> {
            String selectedOption = (String) controlPanel.getDropdownMenuAlgorithms().getSelectedItem();
            System.out.println("Selected alg: " + selectedOption);
        });

        controlPanel.getDropdownMenuInputs().addActionListener(e -> {
            String selectedOption = (String) controlPanel.getDropdownMenuInputs().getSelectedItem();

            TSPFileReader reader = new TSPFileReader();
            try {
                reader.readFile("src/main/resources/" + selectedOption + ".tsp");
                List<Point2D.Double> newPoints = reader.getCoordinates();

                plotter.setPoints(newPoints);
                plotter.setOptimalOrder(new ArrayList<>());
                plotter.setcurrentOrder(new ArrayList<>());

                plotter.repaint();
            } catch (IOException exception) {
                System.out.println("File not found!");
                JOptionPane.showMessageDialog(this, "Error: File not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TSPSolutionFileReader solReader = new TSPSolutionFileReader();
            List<Integer> pointsOrder = solReader.readNumbersFromFile("src/main/resources/solution_" + selectedOption + ".txt");

            plotter.setOptimalOrder(pointsOrder);
            plotter.setcurrentOrder(new ArrayList<>());

            SwingUtilities.invokeLater(() -> {
                String formattedValue = String.format("%.2f", plotter.getOptimalLength());
                statusPanel.getOptimumLengthField().setText(formattedValue);
                statusPanel.getCurrentLengthField().setText("0.00");
                statusPanel.repaint();
            });
        });

        controlPanel.getGenerateReport().addActionListener(e -> {
            Report algStatistics = new Report();
            algStatistics.generateHtmlReport(algMetrics);
        });

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(plotter, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);


        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}