import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Custom JPanel for Background Image
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

// Bed Class
class Bed {
    private int bedId;
    private boolean isOccupied;
    private Patient patient;

    public Bed(int bedId) {
        this.bedId = bedId;
        this.isOccupied = false;
        this.patient = null;
    }

    public int getBedId() {
        return bedId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void assignPatient(Patient patient) {
        this.patient = patient;
        this.isOccupied = true;
    }

    public void freeBed() {
        this.patient = null;
        this.isOccupied = false;
    }

    public Patient getPatient() {
        return patient;
    }
}

// Patient Class
class Patient {
    private int id;
    private String name;
    private String address;

    public Patient(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getInfo() {
        return "Patient ID: " + id + ", Name: " + name + ", Address: " + address;
    }
}

// Hospital Class
class Hospital {
    private ArrayList<Bed> beds;

    public Hospital(int totalBeds) {
        beds = new ArrayList<>();
        for (int i = 1; i <= totalBeds; i++) {
            beds.add(new Bed(i));
        }
    }

    public ArrayList<Bed> getBeds() {
        return beds;
    }
}

// GUI Class
public class HospitalBedManagementGUI {
    private Hospital generalWard, icuWard;
    private JFrame frame;
    private JPanel bedPanel;
    private int currentWard = 1; // 1 - General Ward, 2 - ICU

    public HospitalBedManagementGUI() {
        generalWard = new Hospital(40); // General Ward with 40 beds
        icuWard = new Hospital(10); // ICU with 10 beds

        frame = new JFrame("Hospital Bed Management System");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Background Image Panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("D:\\CPP\\back.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        // Load the logo image
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\adesh\\Downloads\\logo.jpeg.png"); // Update the path to your logo image
        JLabel logoLabel = new JLabel(logoIcon);
        Image scaledLogo = logoIcon.getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel titleLabel = new JLabel("KJ's Hospital", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(logoLabel);
        titlePanel.add(titleLabel);
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu wardMenu = new JMenu("Select Ward");
        JMenuItem generalWardMenuItem = new JMenuItem("General Ward");
        JMenuItem icuWardMenuItem = new JMenuItem("ICU Ward");

        wardMenu.add(generalWardMenuItem);
        wardMenu.add(icuWardMenuItem);
        menuBar.add(wardMenu);
        frame.setJMenuBar(menuBar);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton admitButton = new JButton("Add Patient");
        JButton dischargeButton = new JButton("Discharge Patient");
        JButton viewStatusButton = new JButton("View Bed Status");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(admitButton);
        buttonPanel.add(dischargeButton);
        buttonPanel.add(viewStatusButton);
        buttonPanel.add(exitButton);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Bed Panel (For Visualization)
        bedPanel = new JPanel();
        bedPanel.setOpaque(false);
        backgroundPanel.add(bedPanel, BorderLayout.SOUTH);

        // Button Actions
        admitButton.addActionListener(e -> admitPatient());
        dischargeButton.addActionListener(e -> dischargePatient());
        viewStatusButton.addActionListener(e -> viewBedStatus());
        exitButton.addActionListener(e -> System.exit(0));

        // Menu Actions
        generalWardMenuItem.addActionListener(e -> {
            currentWard = 1;
            updateBedLayout();
        });

        icuWardMenuItem.addActionListener(e -> {
            currentWard = 2;
            updateBedLayout();
        });

        frame.setVisible(true);
        updateBedLayout();
    }

    private void updateBedLayout() {
        bedPanel.removeAll();
        Hospital ward = (currentWard == 1) ? generalWard : icuWard;
        int totalBeds = ward.getBeds().size();

        bedPanel.setLayout(new GridLayout(5, totalBeds / 5, 5, 5));

        for (Bed bed : ward.getBeds()) {
            JButton bedButton = new JButton("Bed " + bed.getBedId());
            bedButton.setBackground(bed.isOccupied() ? Color.GREEN : Color.WHITE);
            bedButton.setOpaque(true);
            bedButton.setBorderPainted(false);
            bedPanel.add(bedButton);
        }

        bedPanel.revalidate();
        bedPanel.repaint();
    }

    private void admitPatient() {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField bedNoField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Patient Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Bed No:"));
        panel.add(bedNoField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Admit Patient", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String idText = idField.getText().trim();
            String address = addressField.getText().trim();
            String bedNoText = bedNoField.getText().trim();

            if (name.isEmpty() || idText.isEmpty() || address.isEmpty() || bedNoText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bedNo;
            try {
                bedNo = Integer.parseInt(bedNoText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Bed Number must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Hospital ward = (currentWard == 1) ? generalWard : icuWard;
            if (bedNo < 1 || bedNo > ward.getBeds().size()) {
                JOptionPane.showMessageDialog(frame, "Invalid Bed Number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Bed selectedBed = ward.getBeds().get(bedNo - 1);
            if (selectedBed.isOccupied()) {
                JOptionPane.showMessageDialog(frame, "Bed is already occupied!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient newPatient = new Patient(Integer.parseInt(idText), name, address);
            selectedBed.assignPatient(newPatient);
            JOptionPane.showMessageDialog(frame, "Patient admitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateBedLayout();
        }
    }

    private void dischargePatient() {
        String patientIdStr = JOptionPane.showInputDialog(frame, "Enter Patient ID to Discharge:");
        if (patientIdStr == null || patientIdStr.trim().isEmpty()) return;

        int patientId = Integer.parseInt(patientIdStr.trim());
        Hospital ward = (currentWard == 1) ? generalWard : icuWard;
        for (Bed bed : ward.getBeds()) {
            if (bed.isOccupied() && bed.getPatient().getInfo().contains("Patient ID: " + patientId)) {
                bed.freeBed();
                JOptionPane.showMessageDialog(frame, "Patient Discharged Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateBedLayout();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Patient Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void viewBedStatus() {
        Hospital ward = (currentWard == 1) ? generalWard : icuWard;
        StringBuilder status = new StringBuilder();

        status.append(" Ward Status (" + (currentWard == 1 ? "General Ward" : "ICU") + "):\n");
        for (Bed bed : ward.getBeds()) {
            status.append("Bed " + bed.getBedId() + ": ");
            if (bed.isOccupied()) {
                status.append("Occupied by " + bed.getPatient().getInfo() + "\n");
            } else {
                status.append("Available\n");
            }
        }
        JOptionPane.showMessageDialog(frame, status.toString(), "Bed Status", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalBedManagementGUI::new);
    }
}
