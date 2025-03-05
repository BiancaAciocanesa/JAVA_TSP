package utils;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Report {

    public void generateHtmlReport(List<AlgMetrics> algMetricsList) {

        StringBuilder htmlContent = new StringBuilder("<html>\n" +
                "<head>\n" +
                "    <title>TSP Algorithm Report</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>TSP Algorithm Report</h1>\n");

        for(var alg : algMetricsList){

            String info =
                    "    <p><strong>Optimal Length:</strong> " + String.format("%.2f", alg.optimalLength()) + "</p>\n" +
                    "    <p><strong>Algorithm Length:</strong> " + String.format("%.2f", alg.algorithmLength()) + "</p>\n" +
                    "    <p><strong>Algorithm Name:</strong> " + alg.algorithmName() + "</p>\n" +
                    "    <p><strong>Execution Time:</strong> " + String.format("%.2f", alg.executionTime()) + " milliseconds</p>\n"+
                    "<br>";
            htmlContent.append(info);
        }
        String end = "</body>\n" +
                "</html>";

        htmlContent.append(end);

        try (FileWriter fileWriter = new FileWriter("tsp_report.html")) {
            fileWriter.write(htmlContent.toString());
            System.out.println("Report generated successfully.");

            Desktop d = Desktop.getDesktop();
            try{
                Path folderPath = Paths.get(".");
                d.open(folderPath.toFile());

            }catch (IOException e){

                System.out.println("Eroare la deschidere -> path invalid");
            }
        } catch (IOException e) {
            System.out.println("Error writing HTML report: " + e.getMessage());
        }
    }
}
