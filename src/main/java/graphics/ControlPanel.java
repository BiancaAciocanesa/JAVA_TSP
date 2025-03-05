package graphics;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final JButton startAlgorithm;
    private final JButton generateReport;
    private final JComboBox<String> dropdownMenuAlgorithms;
    private final JComboBox<String> dropdownMenuInputs;

    private final JLabel labelAction;
    private final JLabel labelAlgorithm;
    private final JLabel labelInput;


    public ControlPanel() {

        setLayout(new FlowLayout());
        Font bigFont = new Font("Arial", Font.PLAIN, 16);

        labelAction = new JLabel("Actiune:");
        labelAction.setFont(bigFont);
        add(labelAction);

        startAlgorithm = new JButton("Ruleaza");
        startAlgorithm.setFont(bigFont);
        add(startAlgorithm);

        labelAlgorithm = new JLabel("Algoritm:");
        labelAlgorithm.setFont(bigFont);
        add(labelAlgorithm);

        dropdownMenuAlgorithms = new JComboBox<>(new String[]{"--", "Algoritm Genetic", "Christofides", "Christofides JGraphT"});
        dropdownMenuAlgorithms.setFont(bigFont);
        add(dropdownMenuAlgorithms);

        labelInput = new JLabel("Input:");
        labelInput.setFont(bigFont);
        add(labelInput);

        dropdownMenuInputs = new JComboBox<>(new String[]{"--", "berlin52", "ro2950_geom", "att48", "eil101", "ch150"});
        dropdownMenuInputs.setFont(bigFont);
        add(dropdownMenuInputs);

        generateReport = new JButton("Genereaza statistici");
        generateReport.setFont(bigFont);
        add(generateReport);

    }

    public JButton getStartAlgorithm() {
        return startAlgorithm;
    }

    public JButton getGenerateReport() {
        return generateReport;
    }

    public JComboBox<String> getDropdownMenuAlgorithms() {
        return dropdownMenuAlgorithms;
    }

    public JComboBox<String> getDropdownMenuInputs() {
        return dropdownMenuInputs;
    }
}