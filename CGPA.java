package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cgpachat extends JFrame {
    private JTextField[] creditFields;
    private JTextField[] gradeFields;
    private JButton calculateButton;
    private JLabel resultLabel;

    public cgpachat() {
        setTitle("CGPA Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        createLayout();

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCGPA();
            }
        });
    }

    private void initializeComponents() {
        int numSubjects = 5; // You can change this value based on the number of subjects you have

        creditFields = new JTextField[numSubjects];
        gradeFields = new JTextField[numSubjects];

        for (int i = 0; i < numSubjects; i++) {
            creditFields[i] = new JTextField(5);
            gradeFields[i] = new JTextField(5);
        }

        calculateButton = new JButton("Calculate CGPA");
        resultLabel = new JLabel("CGPA: ");
    }

    private void createLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new SpringLayout());

        // Add components to the panel
        for (int i = 0; i < creditFields.length; i++) {
            panel.add(new JLabel("Subject " + (i + 1) + " Credits:"));
            panel.add(creditFields[i]);
            panel.add(new JLabel("Subject " + (i + 1) + " Grade:"));
            panel.add(gradeFields[i]);
        }

        panel.add(calculateButton);
        panel.add(resultLabel);

        SpringUtilities.makeCompactGrid(panel,
                numSubjects * 2 + 2, 2,
                5, 5,
                5, 5);

        add(panel);
    }

    private void calculateCGPA() {
        int totalCredits = 0;
        double totalGradePoints = 0;

        for (int i = 0; i < creditFields.length; i++) {
            try {
                int credits = Integer.parseInt(creditFields[i].getText());
                double grade = Double.parseDouble(gradeFields[i].getText());

                totalCredits += credits;
                totalGradePoints += credits * grade;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numerical values for credits and grades.");
                return;
            }
        }

        if (totalCredits == 0) {
            JOptionPane.showMessageDialog(this, "Total credits cannot be zero.");
            return;
        }

        double cgpa = totalGradePoints / totalCredits;
        resultLabel.setText("CGPA: " + cgpa);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CGPACalculator().setVisible(true);
            }
        });
    }
}

class SpringUtilities {
    public static void makeCompactGrid(JPanel grid,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout = new SpringLayout();
        grid.setLayout(layout);

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = layout.getConstraint(SpringLayout.WEST, grid);
        Spring initialYSpring = layout.getConstraint(SpringLayout.NORTH, grid);

        Spring previousRow = initialYSpring;
        for (int row = 0; row < rows; row++) {
            Spring rowPad;
            if (row == 0) {
                rowPad = initialYSpring;
            } else {
                rowPad = yPadSpring;
            }

            Spring rowSpring = Spring.sum(previousRow, rowPad);
            layout.putConstraint(SpringLayout.NORTH, grid.getComponent(row * 2), rowSpring);

            previousRow = rowSpring;
        }

        Spring previousCol = initialXSpring;
        for (int col = 0; col < cols; col++) {
            Spring colPad;
            if (col == 0) {
                colPad = initialXSpring;
            } else {
                colPad = xPadSpring;
            }

            Spring colSpring = Spring.sum(previousCol, colPad);
            layout.putConstraint(SpringLayout.WEST, grid.getComponent(col * 2 + 1), colSpring);

            previousCol = colSpring;
        }

        layout.putConstraint(SpringLayout.EAST, grid, 0, SpringLayout.EAST, grid.getComponent(cols * 2 - 1));
        layout.putConstraint(SpringLayout.SOUTH, grid, 0, SpringLayout.SOUTH, grid.getComponent(rows * 2 - 1));
    }
}
