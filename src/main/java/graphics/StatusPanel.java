package graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final JLabel optimumLengthLabel;
    private final JTextField optimumLengthField;
    private final JLabel currentLengthLabel;
    private final JLabel currentTimeLabel;
    private final JTextField currentLengthField;
    private final JTextField currentTimeField;

    public StatusPanel() {
        setLayout(new FlowLayout());

        Font bigFont = new Font("Arial", Font.PLAIN, 16);

        optimumLengthLabel = new JLabel("Lungime optima:");
        optimumLengthLabel.setFont(bigFont);
        add(optimumLengthLabel);

        optimumLengthField = new JTextField(10);
        optimumLengthField.setFont(bigFont);
        add(optimumLengthField);

        currentLengthLabel = new JLabel("        Lungime algoritm:");
        currentLengthLabel.setFont(bigFont);
        add(currentLengthLabel);

        currentLengthField = new JTextField(10);
        currentLengthField.setFont(bigFont);
        add(currentLengthField);

        currentTimeLabel = new JLabel("        Timp scurs:");
        currentTimeLabel.setFont(bigFont);
        add(currentTimeLabel);

        currentTimeField = new JTextField(6);
        currentTimeField.setFont(bigFont);
        add(currentTimeField);

        setBorder(new EmptyBorder(0, 0, 20, 0)); // 20 pixels bottom margin
    }

    public JLabel getCurrentLengthLabel() {
        return currentLengthLabel;
    }

    public JLabel getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public JTextField getCurrentLengthField() {
        return currentLengthField;
    }

    public JTextField getCurrentTimeField() {
        return currentTimeField;
    }

    public JLabel getOptimumLengthLabel(){ return optimumLengthLabel;}

    public JTextField getOptimumLengthField(){return optimumLengthField;}
}
