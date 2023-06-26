import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class TemperatureConverter extends JFrame {
    private JTextField inputField;
    private JComboBox<String> fromUnitComboBox;
    private JComboBox<String> toUnitComboBox;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private static final String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public TemperatureConverter() {
        setTitle("Temperature Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Enter temperature:");
        inputField = new JTextField(10);
        fromUnitComboBox = new JComboBox<>(temperatureUnits);
        toUnitComboBox = new JComboBox<>(temperatureUnits);
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertTemperature();
            }
        });

        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(fromUnitComboBox);
        inputPanel.add(new JLabel("to"));
        inputPanel.add(toUnitComboBox);
        inputPanel.add(convertButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Unit", "Temperature"}, 0);
        resultTable = new JTable(tableModel);
        tableScrollPane = new JScrollPane(resultTable);
        resultPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void convertTemperature() {
        try {
            double temperature = Double.parseDouble(inputField.getText());
            String fromUnit = (String) fromUnitComboBox.getSelectedItem();
            String toUnit = (String) toUnitComboBox.getSelectedItem();

            Map<String, Double> conversions = convertAllTemperatures(temperature, fromUnit);
            displayConversions(conversions, toUnit);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<String, Double> convertAllTemperatures(double temperature, String fromUnit) {
        Map<String, Double> conversions = new HashMap<>();

        for (String unit : temperatureUnits) {
            double result = convertTemperature(temperature, fromUnit, unit);
            conversions.put(unit, result);
        }

        return conversions;
    }

    private double convertTemperature(double temperature, String fromUnit, String toUnit) {
        double result;

        switch (fromUnit) {
            case "Celsius":
                switch (toUnit) {
                    case "Fahrenheit":
                        result = (temperature * 9 / 5) + 32;
                        break;
                    case "Kelvin":
                        result = temperature + 273.15;
                        break;
                    default:
                        result = temperature;
                        break;
                }
                break;
            case "Fahrenheit":
                switch (toUnit) {
                    case "Celsius":
                        result = (temperature - 32) * 5 / 9;
                        break;
                    case "Kelvin":
                        result = ((temperature - 32) * 5 / 9) + 273.15;
                        break;
                    default:
                        result = temperature;
                        break;
                }
                break;
            case "Kelvin":
                switch (toUnit) {
                    case "Celsius":
                        result = temperature - 273.15;
                        break;
                    case "Fahrenheit":
                        result = ((temperature - 273.15) * 9 / 5) + 32;
                        break;
                    default:
                        result = temperature;
                        break;
                }
                break;
            default:
                result = temperature;
                break;
        }

        return result;
    }

    private void displayConversions(Map<String, Double> conversions, String toUnit) {
        tableModel.setRowCount(0);

        for (Map.Entry<String, Double> entry : conversions.entrySet()) {
            String unit = entry.getKey();
            double temperature = entry.getValue();
            String formattedTemperature = decimalFormat.format(temperature);
            tableModel.addRow(new String[]{unit, formattedTemperature});
        }

        // Highlight the row for the target unit
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String unit = (String) tableModel.getValueAt(i, 0);
            if (unit.equals(toUnit)) {
                resultTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TemperatureConverter().setVisible(true);
            }
        });
    }
}
