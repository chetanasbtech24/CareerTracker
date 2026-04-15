import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class CareerTracker extends JFrame {

    private static final Color APP_BG = new Color(0x0F172A);
    private static final Color SURFACE = new Color(0x1E293B);
    private static final Color TEXT_PRIMARY = new Color(0xE2E8F0);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 16);

    private final PortalManager portal = new PortalManager();
    private final Map<String, JComponent> focusByCard = new HashMap<>();

    private CardLayout contentCards;
    private JPanel contentPanel;
    private JLabel statusLabel;
    private NavButton activeNav;

    public CareerTracker() {
        setTitle("Career Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainContent(), BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        side.add(navButton("Home", "HOME"));
        side.add(navButton("Add Student", "ADD_STUDENT"));
        side.add(navButton("Add Skill", "ADD_SKILL"));

        return side;
    }

    private JPanel buildMainContent() {
        contentCards = new CardLayout();
        contentPanel = new JPanel(contentCards);

        contentPanel.add(homePage(), "HOME");
        contentPanel.add(addStudentPage(), "ADD_STUDENT");
        contentPanel.add(addSkillPage(), "ADD_SKILL");

        return contentPanel;
    }

    private JPanel homePage() {
        return new JPanel(new BorderLayout());
    }

    private JPanel addStudentPage() {
        JPanel form = new JPanel(new GridBagLayout());

        JTextField tfName = new JTextField();
        JButton add = new JButton("Add");

        add.addActionListener(e -> {
            String name = tfName.getText();
            if (!name.isEmpty()) {
                portal.addStudent(new Student(name, "", ""));
            }
        });

        form.add(tfName);
        form.add(add);
        return form;
    }

    private JPanel addSkillPage() {
        JPanel panel = new JPanel();
        JComboBox<String> skills = new JComboBox<>(new String[]{"Java", "Python"});
        panel.add(skills);
        return panel;
    }

    private NavButton navButton(String text, String card) {
        NavButton b = new NavButton(null, text);
        b.addActionListener(e -> contentCards.show(contentPanel, card));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CareerTracker::new);
    }
}
