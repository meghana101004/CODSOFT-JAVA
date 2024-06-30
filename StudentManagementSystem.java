import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;
    private int age;
    private String address;

    public Student(String name, int rollNumber, String grade, int age, String address) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade + 
               ", Age: " + age + ", Address: " + address;
    }
}

public class StudentManagementSystem extends JFrame {
    private ArrayList<Student> students;
    private static final String FILE_NAME = "students.dat";

    private JTextField nameField, rollNumberField, gradeField, ageField, addressField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        createGUI();
        loadStudents();
    }

    private void createGUI() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(173, 216, 230)); // Light blue background

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        inputPanel.add(rollNumberField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        JButton addButton = new JButton("Add Student");
        addButton.setBackground(new Color(144, 238, 144)); // Light green background
        addButton.addActionListener(e -> {
            addStudent();
            hideTable();
        });
        inputPanel.add(addButton);

        JButton removeButton = new JButton("Remove Student");
        removeButton.setBackground(new Color(255, 182, 193)); // Light pink background
        removeButton.addActionListener(e -> {
            removeStudent();
            hideTable();
        });
        inputPanel.add(removeButton);

        JButton searchButton = new JButton("Search Student");
        searchButton.setBackground(new Color(255, 255, 102)); // Light yellow background
        searchButton.addActionListener(e -> {
            searchStudentAction();
            hideTable();
        });
        inputPanel.add(searchButton);

        JButton displayButton = new JButton("Display All Students");
        displayButton.setBackground(new Color(173, 216, 230)); // Light blue background
        displayButton.addActionListener(e -> displayAllStudents());
        inputPanel.add(displayButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Roll Number", "Grade", "Age", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true);

        scrollPane = new JScrollPane(studentTable);
        scrollPane.setVisible(false); 
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            String grade = gradeField.getText();
            int age = Integer.parseInt(ageField.getText());
            String address = addressField.getText();

            Student student = new Student(name, rollNumber, grade, age, address);
            if (isValidStudent(student)) {
                students.add(student);
                saveStudents();
                clearFields();
                JOptionPane.showMessageDialog(this, "Student added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid student details.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for roll number and age.");
        }
    }

    private void removeStudent() {
        try {
            int rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter roll number to remove:"));
            students.removeIf(student -> student.getRollNumber() == rollNumber);
            saveStudents();
            JOptionPane.showMessageDialog(this, "Student removed successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid roll number.");
        }
    }

    private void searchStudentAction() {
        try {
            int rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter roll number to search:"));
            Student foundStudent = searchStudent(rollNumber);
            if (foundStudent != null) {
                JOptionPane.showMessageDialog(this, "Student found: " + foundStudent);
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid roll number.");
        }
    }

    private Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    private void displayAllStudents() {
        tableModel.setRowCount(0); 
        for (Student student : students) {
            Object[] row = {student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getAddress()};
            tableModel.addRow(row);
        }
        scrollPane.setVisible(true); 
        revalidate();
        repaint();
    }

    private void hideTable() {
        scrollPane.setVisible(false); 
        revalidate();
        repaint();
    }

    private boolean isValidStudent(Student student) {
        return student.getName() != null && !student.getName().isEmpty()
            && student.getRollNumber() > 0
            && student.getGrade() != null && !student.getGrade().isEmpty()
            && student.getAge() > 0
            && student.getAddress() != null && !student.getAddress().isEmpty();
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving student data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading student data: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
        ageField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}
