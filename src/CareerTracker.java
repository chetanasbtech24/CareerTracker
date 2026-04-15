// Cleaned file header to remove hidden BOM
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class CareerTracker extends JFrame {

    private static final Color APP_BG = new Color(0x0F172A);
    private static final Color SURFACE = new Color(0x1E293B);
    private static final Color SURFACE_2 = new Color(0x1E293B);
    private static final Color TEXT_PRIMARY = new Color(0xE2E8F0);
    private static final Color TEXT_SECONDARY = new Color(0x94A3B8);
    private static final Color BORDER = new Color(0x475569);

    private static final Color FIELD_BG = new Color(0x1E293B);
    private static final Color ACCENT_PURPLE = new Color(0x7C3AED);
    private static final Color ACCENT_PURPLE_2 = new Color(0x7C3AED);
    private static final Color ACCENT_BLUE = new Color(0x3B82F6);

    private static final Color SUCCESS = new Color(0x10B981);
    private static final Color ERROR = new Color(0xEF4444);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);

    private static final int PAD = 24;
    private static final int GAP = 18;
    private static final Dimension FIELD_SIZE = new Dimension(320, 50);

    private static final String[] SKILLS = new String[]{
            "Java", "Python", "SQL", "MySQL", "Data Structures", "AI", "ML", "Web Dev"
    };
    private static final String[] LEVELS = new String[]{
            "Beginner", "Intermediate", "Advanced"
    };
    private static final String[] ENGLISH = new String[]{
            "Basic", "Professional", "Fluent"
    };

    private final PortalManager portal = new PortalManager();
    private final Map<String, JComponent> focusByCard = new HashMap<>();

    private CardLayout contentCards;
    private JPanel contentPanel;
    private JLabel statusLabel;
    private NavButton activeNav;

    private GridBagConstraints baseFormGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, GAP, 0);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        return g;
    }

    private void formRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
    int startRow = 1;
    g.gridy = startRow + row;

    // LABEL
    g.gridx = 0;
    g.gridwidth = 1;
    g.weightx = 0;
    g.insets = new Insets(10, 10, 10, 20);

    JLabel l = new JLabel(label);
    l.setFont(FONT_LABEL);
    l.setForeground(TEXT_SECONDARY);
    l.setPreferredSize(new Dimension(180, 30));
    form.add(l, g);

    // FIELD
    g.gridx = 1;
    g.weightx = 1;
    g.insets = new Insets(10, 0, 10, 10);

    form.add(field, g);
}
    public CareerTracker() {
        setTitle("Career Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainContent(), BorderLayout.CENTER);
        root.add(buildInfoPanel(), BorderLayout.EAST);

        setVisible(true);
        SwingUtilities.invokeLater(() -> requestFocusForCard("HOME"));
    }

    private JPanel buildSidebar() {

    JPanel side = new JPanel();
    side.setBackground(new Color(15, 23, 42)); // deeper dark
    side.setPreferredSize(new Dimension(260, 0));
    side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
    side.setBorder(new EmptyBorder(PAD, 18, PAD, 18));

    JLabel logo = img("images/logo.png", 160, 90);
    logo.setAlignmentX(Component.CENTER_ALIGNMENT);

    side.add(Box.createVerticalStrut(15));
    side.add(logo);
    side.add(Box.createVerticalStrut(10));

    JLabel title = new JLabel("Career Tracker");
    title.setFont(new Font("Segoe UI", Font.BOLD, 16));
    title.setForeground(Color.WHITE);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    side.add(title);

    side.add(Box.createVerticalStrut(30));

    side.add(navButton("Home", "HOME", navIcon("images/dashboard.png"), true));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("Add Student", "ADD_STUDENT", navIcon("images/profile.png"), false));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("Add Skill", "ADD_SKILL", navIcon("images/data.png"), false));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("Add Experience", "ADD_EXP", navIcon("images/experience.png"), false));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("View Profile", "VIEW", navIcon("images/profile.png"), false));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("Search", "SEARCH", navIcon("images/empty.png"), false));
    side.add(Box.createVerticalStrut(10));

    side.add(navButton("All Profiles", "LIST", navIcon("images/data.png"), false));

    side.add(Box.createVerticalGlue());

    JLabel footer = new JLabel("Modern Dashboard");
    footer.setFont(FONT_SMALL);
    footer.setForeground(new Color(255, 255, 255, 140));
    footer.setAlignmentX(Component.CENTER_ALIGNMENT);
    side.add(footer);

    return side;
}
    
    private Icon navIcon(String path) {
    try {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    } catch (Exception e) {
        return null;
    }
}

    // ✅ CORRECTED buildMainContent() - NO SCROLL
    private JPanel buildMainContent() {
        JPanel center = new JPanel(new BorderLayout(0, GAP));
        center.setOpaque(true);
        center.setBackground(APP_BG);
        center.setBorder(new EmptyBorder(20, 40, 20, 40));

        center.add(buildHeader(), BorderLayout.NORTH);

        contentCards = new CardLayout();
        contentPanel = new JPanel(contentCards);
        contentPanel.setOpaque(false);

        contentPanel.add(homePage(), "HOME");
        contentPanel.add(addStudentPage(), "ADD_STUDENT");
        contentPanel.add(addSkillPage(), "ADD_SKILL");
        contentPanel.add(addExperiencePage(), "ADD_EXP");
        contentPanel.add(viewProfilePage(), "VIEW");
        contentPanel.add(searchPage(), "SEARCH");
        contentPanel.add(listPage(), "LIST");

        center.add(contentPanel, BorderLayout.CENTER);

        return center;
    }

    private JPanel buildHeader() {
        JPanel header = createCardPanel();
        header.setLayout(new BorderLayout(GAP, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Student profiles, skills, and experience in one clean workspace.");
        subtitle.setFont(FONT_BODY);
        subtitle.setForeground(TEXT_SECONDARY);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(FONT_LABEL);
        statusLabel.setForeground(SUCCESS);

        header.add(left, BorderLayout.CENTER);
        header.add(statusLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildInfoPanel() {
        JPanel east = new JPanel(new BorderLayout(0, GAP));
        east.setPreferredSize(new Dimension(285, 0));
        east.setBackground(APP_BG);
        east.setBorder(new EmptyBorder(PAD, 0, PAD, PAD));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        stack.add(infoWidget("Pro Tip", "Ensure student IDs are unique for accurate tracking.", new Badge("TIP")));
        stack.add(Box.createVerticalStrut(GAP));
        stack.add(suggestionBar("Suggested Skills", new String[] {"Java", "SQL", "Communication", "Project Management", "UI/UX Design"}));
        stack.add(Box.createVerticalStrut(GAP));
        stack.add(infoWidget("Best Practice", "Maintain updated skillsets and internship records for better insights.", new Badge("BP")));
        stack.add(Box.createVerticalStrut(GAP));
        stack.add(infoWidget("System Status", "All records are securely stored using SQLite database integration.", new Badge("DB")));

        east.add(stack, BorderLayout.NORTH);
        return east;
    }

    private JPanel infoWidget(String title, String body, JComponent icon) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 10));

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        top.add(icon, BorderLayout.WEST);

        JLabel t = new JLabel(title);
        t.setFont(FONT_LABEL);
        t.setForeground(TEXT_PRIMARY);
        top.add(t, BorderLayout.CENTER);

        JLabel b = new JLabel("<html><body style='width:190px'>" + body + "</body></html>");
        b.setFont(FONT_BODY);
        b.setForeground(TEXT_SECONDARY);

        card.add(top, BorderLayout.NORTH);
        card.add(b, BorderLayout.CENTER);
        return card;
    }

    private JPanel suggestionBar(String title, String[] skills) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 10));

        JLabel t = new JLabel(title);
        t.setFont(FONT_LABEL);
        t.setForeground(TEXT_PRIMARY);
        card.add(t, BorderLayout.NORTH);

        JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        chipRow.setOpaque(false);
        for (String skill : skills) {
            JLabel chip = new JLabel(skill);
            chip.setOpaque(true);
            chip.setBackground(new Color(237, 242, 255));
            chip.setForeground(new Color(38, 54, 105));
            chip.setBorder(new EmptyBorder(8, 14, 8, 14));
            chip.setFont(FONT_BODY);
            chipRow.add(chip);
        }

        card.add(chipRow, BorderLayout.CENTER);
        return card;
    }

    private JPanel homePage() {

    JPanel page = new JPanel(new BorderLayout(0, GAP));
    page.setOpaque(true);
    page.setBackground(APP_BG);

    JPanel stats = new JPanel(new GridLayout(1, 3, GAP, 0));
    stats.setOpaque(false);

    JLabel studentsCount = new JLabel("0");
    JLabel skillsCount = new JLabel("0");
    JLabel expCount = new JLabel("0");

    stats.add(statCard("Students", studentsCount));
    stats.add(statCard("Skills", skillsCount));
    stats.add(statCard("Experience", expCount));

    JPanel hero = createCardPanel();
    hero.setLayout(new BorderLayout(GAP, GAP));

    JLabel dashboardImg = img("images/InternshipDashboard.png", 760, 300);
    dashboardImg.setHorizontalAlignment(SwingConstants.CENTER);
    hero.add(dashboardImg, BorderLayout.NORTH);

    JPanel left = new JPanel();
    left.setOpaque(false);
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Dashboard");
    title.setFont(FONT_SECTION);
    title.setForeground(TEXT_PRIMARY);

    JLabel subtitle = new JLabel("Manage students, skills, and experience efficiently.");
    subtitle.setFont(FONT_BODY);
    subtitle.setForeground(TEXT_SECONDARY);

    ActionButton refresh = createStyledButton("Refresh Metrics");

    refresh.addActionListener(e ->
        refreshMetrics(studentsCount, skillsCount, expCount)
    );

    left.add(title);
    left.add(Box.createVerticalStrut(8));
    left.add(subtitle);
    left.add(Box.createVerticalStrut(20));
    left.add(refresh);

    hero.add(left, BorderLayout.CENTER);
    hero.setBorder(new EmptyBorder(20, 24, 20, 24));

    page.add(stats, BorderLayout.NORTH);
    page.add(hero, BorderLayout.CENTER);

    focusByCard.put("HOME", refresh);
    refresh.doClick();

    return page;
}
    private void refreshMetrics(JLabel studentsCount, JLabel skillsCount, JLabel expCount) {
        List<Student> all = portal.getAllStudents();
        int skills = 0;
        int exp = 0;
        for (Student s : all) {
            skills += s.getSkills().size();
            if (s.getInternship() != null) {
                exp++;
            }
        }
        studentsCount.setText(String.valueOf(all.size()));
        skillsCount.setText(String.valueOf(skills));
        expCount.setText(String.valueOf(exp));
        setStatus("Metrics refreshed", SUCCESS);
    }

    private JPanel statCard(String label, JLabel value) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel l = new JLabel(label);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_SECONDARY);

        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(TEXT_PRIMARY);

        card.add(l);
        card.add(Box.createVerticalStrut(10));
        card.add(value);
        return card;
    }

    private JPanel addStudentPage() {

    JPanel form = createFormCard("Add Student");

    StyledTextField tfName = createTextField();
    StyledTextField tfEmail = createTextField();
    StyledTextField tfUsn = createTextField();
    JLabel msg = msgLabel();

    GridBagConstraints g = baseFormGbc();

    formRow(form, g, 0, "Full Name", tfName);
    formRow(form, g, 1, "Email", tfEmail);
    formRow(form, g, 2, "Student ID (USN)", tfUsn);

    ActionButton add = createStyledButton("Add Student");

    add.addActionListener(e -> {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String usn = tfUsn.getText().trim().toUpperCase();

        if (name.isEmpty() || email.isEmpty() || usn.isEmpty()) {
            showMsg(msg, "All fields are required.", ERROR);
            return;
        }

        if (portal.usnExists(usn)) {
            showMsg(msg, "Student ID already exists.", ERROR);
            return;
        }

        portal.addStudent(new Student(name, email, usn));

        showMsg(msg, "Student added successfully.", SUCCESS);
        setStatus("Student added: " + name, SUCCESS);

        tfName.setText("");
        tfEmail.setText("");
        tfUsn.setText("");

        SwingUtilities.invokeLater(tfName::requestFocusInWindow);
    });

    addFormFooter(form, g, 3, add, msg);

    JLabel headerImage = img("images/profile.png", 420, 180);
    headerImage.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel page = new JPanel(new BorderLayout());
    page.setBackground(APP_BG);
    form.setBorder(new EmptyBorder(12, 24, 20, 24));
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
    wrapper.setOpaque(false);

    headerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
    wrapper.add(headerImage);

    wrapper.add(Box.createVerticalStrut(8));

    form.setAlignmentX(Component.CENTER_ALIGNMENT);
    wrapper.add(form);

    page.add(wrapper, BorderLayout.CENTER);

    focusByCard.put("ADD_STUDENT", tfName);

    return page;
}

private JPanel addSkillPage() {

    JPanel form = createFormCard("Add Skill");
    StyledTextField tfUsn = createTextField();

    JPanel skillPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 8, 8));
    skillPanel.setOpaque(false);

    JScrollPane scroll = new JScrollPane(skillPanel);
    scroll.setPreferredSize(new Dimension(0, 120));
    scroll.setMinimumSize(new Dimension(0, 120));
    scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
    scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    JComboBox<String> skillDropdown = new JComboBox<>(SKILLS);
    skillDropdown.setPreferredSize(new Dimension(220, 36));

    StyledTextField customSkillField = createTextField();
    customSkillField.setPreferredSize(new Dimension(170, 36));

    java.util.Map<String, JComboBox<String>> skillLevels = new java.util.LinkedHashMap<>();

    java.util.function.Consumer<String> addSkillChip = skill -> {
        if (skill == null || skill.trim().isEmpty() || skillLevels.containsKey(skill)) return;

        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        chip.setBackground(new Color(124, 58, 237));
        chip.setBorder(new EmptyBorder(4, 10, 4, 8));

        JLabel text = new JLabel(skill);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JComboBox<String> levelBox = new JComboBox<>(LEVELS);
        levelBox.setPreferredSize(new Dimension(100, 24));
        skillLevels.put(skill, levelBox);

        JButton remove = new JButton("✕");
        remove.setForeground(Color.WHITE);
        remove.setBackground(new Color(200, 50, 50));
        remove.setBorder(null);
        remove.setContentAreaFilled(false);
        remove.setFocusPainted(false);
        remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        remove.setFont(new Font("Segoe UI", Font.BOLD, 14));

        remove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                remove.setOpaque(true);
                remove.setBackground(new Color(200, 50, 50));
                remove.setContentAreaFilled(true);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                remove.setContentAreaFilled(false);
            }
        });

        remove.addActionListener(ev -> {
            skillPanel.remove(chip);
            skillLevels.remove(skill);
            skillPanel.revalidate();
            skillPanel.repaint();
        });

        chip.add(text);
        chip.add(levelBox);
        chip.add(remove);

        skillPanel.add(chip);
        skillPanel.revalidate();
        skillPanel.repaint();
    };

    skillDropdown.addActionListener(e -> {
        String selected = (String) skillDropdown.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            addSkillChip.accept(selected);
            skillDropdown.setSelectedIndex(-1);
        }
    });

    JButton addCustomBtn = new JButton("+");
    addCustomBtn.setPreferredSize(new Dimension(44, 36));
    addCustomBtn.setFocusPainted(false);
    addCustomBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    addCustomBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    addCustomBtn.setBackground(new Color(124, 58, 237));
    addCustomBtn.setForeground(Color.WHITE);
    addCustomBtn.setContentAreaFilled(true);
    addCustomBtn.setBorder(BorderFactory.createEmptyBorder());

    addCustomBtn.addActionListener(e -> {
        String text = customSkillField.getText().trim();
        if (!text.isEmpty()) {
            addSkillChip.accept(text);
            customSkillField.setText("");
            customSkillField.requestFocus();
        }
    });

    customSkillField.addActionListener(e -> addCustomBtn.doClick());

    JPanel customPanel = new JPanel(new BorderLayout(6, 0));
    customPanel.setOpaque(false);
    customPanel.add(customSkillField, BorderLayout.CENTER);
    customPanel.add(addCustomBtn, BorderLayout.EAST);

    JComboBox<String> english = createCombo(ENGLISH);
    english.setSelectedIndex(-1);

    JLabel msg = msgLabel();

    GridBagConstraints g = baseFormGbc();

    formRow(form, g, 0, "Student ID (USN)", tfUsn);

    g.gridy = 2;
    g.gridx = 0;
    g.weightx = 0.25;
    g.weighty = 0.2;
    g.fill = GridBagConstraints.NONE;
    g.anchor = GridBagConstraints.FIRST_LINE_START;
    g.insets = new Insets(10, 10, 10, 20);
    
    JLabel skillsLabel = new JLabel("Added Skills");
    skillsLabel.setFont(FONT_LABEL);
    skillsLabel.setForeground(TEXT_SECONDARY);
    skillsLabel.setPreferredSize(new Dimension(180, 30));
    form.add(skillsLabel, g);

    g.gridx = 1;
    g.weightx = 0.75;
    g.weighty = 0;
    g.fill = GridBagConstraints.BOTH;
    g.anchor = GridBagConstraints.FIRST_LINE_START;
    g.insets = new Insets(10, 0, 10, 10);
    form.add(scroll, g);

    g.gridy = 3;
    g.gridx = 0;
    g.weightx = 0.25;
    g.weighty = 0;
    g.fill = GridBagConstraints.NONE;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 10, 10, 20);
    
    JLabel selectLabel = new JLabel("Select from List");
    selectLabel.setFont(FONT_LABEL);
    selectLabel.setForeground(TEXT_SECONDARY);
    selectLabel.setPreferredSize(new Dimension(180, 30));
    form.add(selectLabel, g);

    g.gridx = 1;
    g.weightx = 0.75;
    g.weighty = 0;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 0, 10, 10);
    form.add(skillDropdown, g);

    g.gridy = 4;
    g.gridx = 0;
    g.weightx = 0.25;
    g.weighty = 0;
    g.fill = GridBagConstraints.NONE;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 10, 10, 20);
    
    JLabel customLabel = new JLabel("Add Custom Skill");
    customLabel.setFont(FONT_LABEL);
    customLabel.setForeground(TEXT_SECONDARY);
    customLabel.setPreferredSize(new Dimension(180, 30));
    form.add(customLabel, g);

    g.gridx = 1;
    g.weightx = 0.75;
    g.weighty = 0;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 0, 10, 10);
    form.add(customPanel, g);

    g.gridy = 5;
    g.gridx = 0;
    g.weightx = 0.25;
    g.weighty = 0;
    g.fill = GridBagConstraints.NONE;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 10, 10, 20);
    
    JLabel englishLabel = new JLabel("English (Optional)");
    englishLabel.setFont(FONT_LABEL);
    englishLabel.setForeground(TEXT_SECONDARY);
    englishLabel.setPreferredSize(new Dimension(180, 30));
    form.add(englishLabel, g);

    g.gridx = 1;
    g.weightx = 0.75;
    g.weighty = 0;
    g.fill = GridBagConstraints.HORIZONTAL;
    g.anchor = GridBagConstraints.WEST;
    g.insets = new Insets(10, 0, 10, 10);
    form.add(english, g);

    ActionButton add = createStyledButton("Add Skill");

    add.addActionListener(e -> {

        String usn = tfUsn.getText().trim().toUpperCase();

        if (usn.isEmpty()) {
            showMsg(msg, "Student ID is required.", ERROR);
            return;
        }

        Student student = portal.findByUsn(usn);
        if (student == null) {
            showMsg(msg, "Student not found.", ERROR);
            return;
        }

        if (skillLevels.isEmpty()) {
            showMsg(msg, "Add at least one skill.", ERROR);
            return;
        }

        for (java.util.Map.Entry<String, JComboBox<String>> entry : skillLevels.entrySet()) {
            String skill = entry.getKey();
            String levelValue = (String) entry.getValue().getSelectedItem();
            if (levelValue != null) {
                portal.addSkill(usn, skill, ratingFromLevel(levelValue));
            }
        }

        String englishValue = (String) english.getSelectedItem();
        if (englishValue != null) {
            portal.addSkill(usn, "English", ratingFromEnglish(englishValue));
        }

        showMsg(msg, "Skills added for " + student.getName(), SUCCESS);
        setStatus("Skills added: " + student.getName(), SUCCESS);

        skillLevels.clear();
        skillPanel.removeAll();
        skillPanel.revalidate();
        skillPanel.repaint();
        english.setSelectedIndex(-1);
    });

    g.gridy = 6;
    g.gridx = 0;
    g.gridwidth = 2;
    g.weightx = 1;
    g.weighty = 0;
    g.insets = new Insets(GAP, 0, 6, 0);
    g.fill = GridBagConstraints.NONE;
    g.anchor = GridBagConstraints.CENTER;
    form.add(add, g);

    g.gridy = 7;
    g.gridx = 0;
    g.gridwidth = 2;
    g.insets = new Insets(8, 0, 0, 0);
    g.fill = GridBagConstraints.HORIZONTAL;
    g.anchor = GridBagConstraints.WEST;
    form.add(msg, g);

    JLabel headerImage = img("images/skills.png", 520, 180);
    headerImage.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel page = new JPanel(new BorderLayout(GAP, 8));
    page.setBackground(APP_BG);
    page.setBorder(new EmptyBorder(PAD, PAD, PAD, PAD));
    page.add(headerImage, BorderLayout.NORTH);
    page.add(form, BorderLayout.CENTER);

    focusByCard.put("ADD_SKILL", tfUsn);

    return page;
}

    private int ratingFromLevel(String level) {
        if ("Beginner".equalsIgnoreCase(level)) {
            return 2;
        }
        if ("Intermediate".equalsIgnoreCase(level)) {
            return 3;
        }
        return 5;
    }

    private int ratingFromEnglish(String level) {
        if ("Basic".equalsIgnoreCase(level)) {
            return 2;
        }
        if ("Professional".equalsIgnoreCase(level)) {
            return 4;
        }
        return 5;
    }

    private JPanel addExperiencePage() {

    JPanel form = createFormCard("Add Experience");

    StyledTextField tfUsn = createTextField();
    StyledTextField tfCompany = createTextField();
    StyledTextField tfRole = createTextField();

    JComboBox<String> duration = createCombo(
            new String[]{"1 month", "2 months", "3 months", "4 months", "6 months", "12 months"}
    );

    JLabel msg = msgLabel();

    GridBagConstraints g = baseFormGbc();

    formRow(form, g, 0, "Student ID (USN)", tfUsn);
    formRow(form, g, 1, "Company", tfCompany);
    formRow(form, g, 2, "Role", tfRole);
    formRow(form, g, 3, "Duration", duration);

    ActionButton add = createStyledButton("Save Experience");

    add.addActionListener(e -> {

        String usn = tfUsn.getText().trim().toUpperCase();
        String company = tfCompany.getText().trim();
        String role = tfRole.getText().trim();

        if (usn.isEmpty() || company.isEmpty() || role.isEmpty()) {
            showMsg(msg, "All fields are required.", ERROR);
            return;
        }

        Student student = portal.findByUsn(usn);

        if (student == null) {
            showMsg(msg, "Student not found.", ERROR);
            return;
        }

        int months = Integer.parseInt(
                ((String) duration.getSelectedItem()).split(" ")[0]
        );

        portal.assignInternship(usn, company, months, role);

        showMsg(msg, "Experience saved for " + student.getName() + ".", SUCCESS);
        setStatus("Experience saved: " + student.getName(), SUCCESS);

        tfCompany.setText("");
        tfRole.setText("");
    });

    addFormFooter(form, g, 4, add, msg);

    JLabel headerImage = img("images/experience.png", 520, 150);
    headerImage.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel page = new JPanel();
    page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
    page.setOpaque(true);
    page.setBackground(APP_BG);
    page.setBorder(new EmptyBorder(PAD, PAD, PAD, PAD));

    form.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
    form.setBorder(new EmptyBorder(10, 24, 20, 24));

    page.add(headerImage);
    page.add(Box.createVerticalStrut(4));
    page.add(form);

    focusByCard.put("ADD_EXP", tfUsn);

    return page;
}

    private JPanel viewProfilePage() {
        JPanel page = new JPanel(new BorderLayout(0, GAP));
        page.setOpaque(true);
        page.setBackground(APP_BG);

        JPanel top = createCardPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 0, GAP);
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;

        JLabel l = new JLabel("Student ID (USN)");
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_SECONDARY);
        top.add(l, g);

        StyledTextField tfUsn = createTextField();
        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        top.add(tfUsn, g);

        ActionButton view = createStyledButton("View");
        g.gridx = 2;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        g.insets = new Insets(0, 0, 0, 0);
        top.add(view, g);

        JLabel msg = msgLabel();
        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 3;
        g.insets = new Insets(GAP, 0, 0, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        top.add(msg, g);

        JPanel profile = createCardPanel();
        profile.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(FONT_BODY);
        area.setForeground(TEXT_PRIMARY);
        area.setBackground(SURFACE);
        area.setCaretColor(TEXT_PRIMARY);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane sc = new JScrollPane(area);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(SURFACE);
        profile.add(sc, BorderLayout.CENTER);

        view.addActionListener(e -> {
            String usn = tfUsn.getText().trim().toUpperCase();
            if (usn.isEmpty()) {
                showMsg(msg, "Student ID is required.", ERROR);
                return;
            }
            Student s = portal.findByUsn(usn);
            if (s == null) {
                showMsg(msg, "Student not found.", ERROR);
                area.setText("");
                return;
            }
            area.setText(formatProfile(s));
            showMsg(msg, "Profile loaded for " + s.getName() + ".", SUCCESS);
            setStatus("Viewing profile: " + s.getName(), SUCCESS);
        });

        page.add(top, BorderLayout.NORTH);
        page.add(profile, BorderLayout.CENTER);

        focusByCard.put("VIEW", tfUsn);
        return page;
    }

    private JPanel searchPage() {

    JPanel page = new JPanel(new BorderLayout(GAP, GAP));
    page.setBackground(APP_BG);
    page.setBorder(new EmptyBorder(PAD, PAD, PAD, PAD));

    JPanel form = createFormCard("");

    StyledTextField tfSearch = createTextField();
    JLabel msg = msgLabel();

    JPanel resultsPanel = new JPanel();
    resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
    resultsPanel.setOpaque(false);

    JScrollPane scroll = new JScrollPane(resultsPanel);
    scroll.setBorder(null);
    scroll.getViewport().setOpaque(false);
    scroll.setOpaque(false);
    scroll.setPreferredSize(new Dimension(0, 100));
    scroll.setMinimumSize(new Dimension(200, 80));

    GridBagConstraints g = baseFormGbc();

    g.gridy = 0;
    g.gridx = 0;
    g.gridwidth = 2;
    g.fill = GridBagConstraints.NONE;

    JLabel label = new JLabel("Search (Name / Skill / USN)");
    label.setFont(FONT_LABEL);
    label.setForeground(TEXT_SECONDARY);
    form.add(label, g);

    g.gridy = 1;
    g.gridx = 0;
    g.gridwidth = 2;
    g.fill = GridBagConstraints.HORIZONTAL;

    form.add(tfSearch, g);

    ActionButton searchBtn = createStyledButton("Search");

    g.gridy = 2;
    g.gridx = 0;
    g.gridwidth = 2;
    g.anchor = GridBagConstraints.CENTER;
    g.fill = GridBagConstraints.NONE;

    form.add(searchBtn, g);

    g.gridy = 3;
    form.add(msg, g);

    searchBtn.addActionListener(e -> {

        String query = tfSearch.getText().trim().toLowerCase();
        resultsPanel.removeAll();

        if (query.isEmpty()) {
            showMsg(msg, "Enter something to search.", ERROR);
            return;
        }

        java.util.List<Student> students = portal.getAllStudents();
        boolean found = false;

        for (Student s : students) {

            boolean match = false;

            if (s.getName().toLowerCase().contains(query)) match = true;
            if (s.getUsn().toLowerCase().contains(query)) match = true;

            java.util.List<String> skills = s.getSkillLabels();

            for (String sk : skills) {
                if (sk.toLowerCase().contains(query)) {
                    match = true;
                    break;
                }
            }

            if (match) {

                found = true;

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(SURFACE);
                card.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(12, 14, 12, 14)
                ));

                JLabel name = new JLabel(s.getName() + " (" + s.getUsn() + ")");
                name.setFont(FONT_LABEL);
                name.setForeground(TEXT_PRIMARY);

                StringBuilder skillText = new StringBuilder("Skills: ");

                for (String sk : skills) {
                    skillText.append(sk).append(", ");
                }

                if (!skills.isEmpty()) {
                    skillText.setLength(skillText.length() - 2);
                }

                JLabel skillsLabel = new JLabel(skillText.toString());
                skillsLabel.setFont(FONT_SMALL);
                skillsLabel.setForeground(TEXT_SECONDARY);

                card.add(name);
                card.add(Box.createVerticalStrut(6));
                card.add(skillsLabel);

                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                resultsPanel.add(card);
                resultsPanel.add(Box.createVerticalStrut(12));
            }
        }

        if (!found) {
            showMsg(msg, "No results found.", ERROR);
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    });

    JLabel headerImage = img("images/search.png", 520, 220);
    headerImage.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
    wrapper.setOpaque(false);

    headerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
    form.setAlignmentX(Component.CENTER_ALIGNMENT);
    scroll.setAlignmentX(Component.CENTER_ALIGNMENT);

    wrapper.add(headerImage);
    wrapper.add(Box.createVerticalStrut(20));
    wrapper.add(form);
    wrapper.add(Box.createVerticalStrut(20));
    wrapper.add(scroll);

    page.add(wrapper, BorderLayout.CENTER);

    focusByCard.put("SEARCH", tfSearch);

    return page;
}

    private JPanel listPage() {

    JPanel page = new JPanel(new BorderLayout(0, GAP));
    page.setOpaque(true);
    page.setBackground(APP_BG);

    JPanel top = createCardPanel();
    top.setLayout(new BorderLayout(GAP, 0));

    ActionButton refresh = createStyledButton("Refresh");
    JLabel count = new JLabel("0 records");
    count.setFont(FONT_BODY);
    count.setForeground(TEXT_SECONDARY);

    JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP, 0));
    left.setOpaque(false);
    left.add(refresh);
    left.add(count);

    top.add(left, BorderLayout.WEST);
    top.add(img("images/data.png", 120, 80), BorderLayout.EAST);

    String[] cols = {"#", "Name", "Email", "USN", "Skills", "Experience"};
    DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable table = new JTable(model) {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);

            if (!isRowSelected(row)) {
                c.setBackground(row % 2 == 0 ? SURFACE_2 : SURFACE);
                c.setForeground(TEXT_PRIMARY);
            }

            return c;
        }
    };

    table.setRowHeight(36);
    table.setFont(FONT_BODY);
    table.setBackground(SURFACE);
    table.setForeground(TEXT_PRIMARY);
    table.setSelectionBackground(new Color(0x334155));
    table.setSelectionForeground(TEXT_PRIMARY);

    table.setShowGrid(false);
    table.setIntercellSpacing(new Dimension(0, 6));

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    table.getColumnModel().getColumn(0).setPreferredWidth(40);
    table.getColumnModel().getColumn(1).setPreferredWidth(160);
    table.getColumnModel().getColumn(2).setPreferredWidth(220);
    table.getColumnModel().getColumn(3).setPreferredWidth(140);
    table.getColumnModel().getColumn(4).setPreferredWidth(260);
    table.getColumnModel().getColumn(5).setPreferredWidth(220);

    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(JLabel.CENTER);

    table.getColumnModel().getColumn(0).setCellRenderer(center);
    table.getColumnModel().getColumn(3).setCellRenderer(center);

    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setBackground(new Color(0x273449));
    header.setForeground(TEXT_PRIMARY);
    header.setReorderingAllowed(false);

    JScrollPane sc = new JScrollPane(table);
    sc.setBorder(BorderFactory.createEmptyBorder());
    sc.getViewport().setBackground(SURFACE);
    sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    JPanel tableCard = createCardPanel();
    tableCard.setLayout(new BorderLayout());
    tableCard.add(sc, BorderLayout.CENTER);

    JPanel emptyCard = new JPanel(new BorderLayout());
    emptyCard.setOpaque(false);
    emptyCard.add(img("images/empty.png", 300, 200), BorderLayout.CENTER);

    CardLayout dataCards = new CardLayout();
    JPanel dataPanel = new JPanel(dataCards);
    dataPanel.setOpaque(false);
    dataPanel.add(tableCard, "TABLE");
    dataPanel.add(emptyCard, "EMPTY");

    refresh.addActionListener(e -> {

        model.setRowCount(0);

        List<Student> all = portal.getAllStudents();
        int idx = 1;

        for (Student s : all) {

            String skills = s.getSkillLabels().isEmpty()
                    ? "-"
                    : String.join(", ", s.getSkillLabels());

            String exp = s.getInternship() == null
                    ? "-"
                    : s.getInternship().getCompanyName() + " / " + s.getInternship().getRole();

            model.addRow(new Object[]{
                    idx++,
                    s.getName(),
                    s.getEmail(),
                    s.getUsn(),
                    skills,
                    exp
            });
        }

        count.setText(all.size() + " records");

        if (all.isEmpty()) {
            dataCards.show(dataPanel, "EMPTY");
        } else {
            dataCards.show(dataPanel, "TABLE");
        }

        setStatus("List refreshed", SUCCESS);
    });

    refresh.doClick();

    page.add(top, BorderLayout.NORTH);
    page.add(dataPanel, BorderLayout.CENTER);

    focusByCard.put("LIST", refresh);

    return page;
}

    private JPanel createFormCard(String title) {
        JPanel card = createCardPanel();
        card.setLayout(new GridBagLayout());

        JLabel t = new JLabel(title);
        t.setFont(FONT_SECTION);
        t.setForeground(TEXT_PRIMARY);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        g.weightx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(0, 0, GAP, 0);
        card.add(t, g);
        return card;
    }

    private JPanel createCardPanel() {
        JPanel card = new SurfaceCard(20, SURFACE);
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(PAD, PAD, PAD, PAD));
        return card;
    }

    private StyledTextField createTextField() {
        StyledTextField tf = new StyledTextField();
        tf.setPreferredSize(FIELD_SIZE);
        tf.setMinimumSize(FIELD_SIZE);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_SIZE.height));
        return tf;
    }

    private ActionButton createStyledButton(String text) {
        ActionButton b = new ActionButton(text, ACCENT_PURPLE, ACCENT_BLUE, ACCENT_BLUE.darker());
        b.setFont(FONT_BUTTON);
        b.setPreferredSize(new Dimension(200, 50));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return b;
    }

    private JLabel img(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image));
        label.setOpaque(false);
        return label;
    }

    private JPanel formWithFloatingImage(JPanel form, Component rightComponent) {
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        GridBagConstraints r = new GridBagConstraints();
        r.gridx = 0;
        r.gridy = 0;
        r.weightx = 1;
        r.weighty = 1;
        r.fill = GridBagConstraints.BOTH;
        r.insets = new Insets(0, 0, 0, 30);
        row.add(form, r);

        r.gridx = 1;
        r.weightx = 0;
        r.fill = GridBagConstraints.NONE;
        r.anchor = GridBagConstraints.EAST;
        row.add(rightComponent, r);

        GridBagConstraints p = new GridBagConstraints();
        p.gridx = 0;
        p.gridy = 0;
        p.weightx = 1;
        p.weighty = 1;
        p.fill = GridBagConstraints.BOTH;
        page.add(row, p);
        return page;
    }

    private Icon navIcon(String path, String fallbackText) {
        return loadAndScaleIcon(path, 18, 18, fallbackText);
    }

    private Icon loadAndScaleIcon(String path, int width, int height, String fallbackText) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ignored) {
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, ACCENT_PURPLE, width, height, ACCENT_BLUE);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, 10, 10));
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, width / 2)));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (width - fm.stringWidth(fallbackText)) / 2;
        int ty = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(fallbackText, tx, ty);
        g2.dispose();
        return new ImageIcon(img);
    }

    private JComboBox<String> createCombo(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(FONT_BODY);
        box.setPreferredSize(FIELD_SIZE);
        box.setMaximumSize(new Dimension(Short.MAX_VALUE, FIELD_SIZE.height));
        box.setBackground(FIELD_BG);
        box.setForeground(TEXT_PRIMARY);
        box.setFocusable(true);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.setBorder(new CompoundBorder(new GlowRoundedBorder(), new EmptyBorder(6, 10, 6, 10)));
        return box;
    }

    private void addFormFooter(JPanel form, GridBagConstraints g, int row, JComponent button, JLabel msg) {
        int startRow = 1;
        g.gridy = startRow + row;
        g.gridx = 0;
        g.gridwidth = 2;
        g.weightx = 1;
        g.insets = new Insets(GAP, 0, 6, 0);
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        form.add(button, g);

        g.gridy = startRow + row + 1;
        g.insets = new Insets(8, 0, 0, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        form.add(msg, g);
    }

    private JLabel msgLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    private void showMsg(JLabel label, String text, Color color) {
        label.setText(text);
        label.setForeground(color);
    }

    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    private NavButton navButton(String text, String cardName, Icon icon, boolean active) {
        NavButton b = new NavButton(icon, text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e -> {
            if (activeNav != null) {
                activeNav.setActive(false);
            }
            activeNav = b;
            activeNav.setActive(true);
            contentCards.show(contentPanel, cardName);
            setStatus(text + " opened", SUCCESS);
            requestFocusForCard(cardName);
        });
        if (active) {
            activeNav = b;
            b.setActive(true);
        }
        return b;
    }

    private void requestFocusForCard(String cardName) {
        JComponent c = focusByCard.get(cardName);
        if (c == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            c.setFocusable(true);
            c.requestFocusInWindow();
            if (c instanceof JTextField) {
                ((JTextField) c).selectAll();
            }
        });
    }

    private String formatProfile(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(s.getName()).append("\n");
        sb.append("Email: ").append(s.getEmail()).append("\n");
        sb.append("USN: ").append(s.getUsn()).append("\n");
        sb.append("Skills: ").append(s.getSkills().isEmpty() ? "None" : String.join(", ", s.getSkillLabels())).append("\n");
        if (s.getInternship() == null) {
            sb.append("Experience: Not added");
        } else {
            sb.append("Experience: ").append(s.getInternship().getCompanyName())
                    .append(" / ").append(s.getInternship().getRole())
                    .append(" / ").append(s.getInternship().getDurationMonths()).append(" month(s)");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CareerTracker::new);
    }
}

class GradientPanel extends JPanel {
    private final Color start;
    private final Color end;

    GradientPanel(Color start, Color end) {
        this.start = start;
        this.end = end;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, start, 0, getHeight(), end));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}

class SurfaceCard extends JPanel {
    private final int radius;
    private final Color bg;

    SurfaceCard(int radius, Color bg) {
        this.radius = radius;
        this.bg = bg;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            super.paintComponent(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(4, 8, width - 8, height - 12, radius, radius);

        g2.setColor(new Color(30, 41, 59, 220));
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        g2.setColor(new Color(255, 255, 255, 18));
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}

class NavButton extends JButton {
    private boolean active;
    private boolean hovered;

    NavButton(Icon icon, String text) {
        super(text, icon);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(new Color(255, 255, 255, 215));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setIconTextGap(8);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    void setActive(boolean active) {
        this.active = active;
        setFont(active ? new Font("Segoe UI", Font.BOLD, 15) : new Font("Segoe UI", Font.PLAIN, 15));
        setForeground(active ? Color.WHITE : new Color(255, 255, 255, 215));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (active) {
            g2.setColor(new Color(255, 255, 255, 52));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(167, 139, 250, 90));
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
            g2.setColor(new Color(255, 255, 255, 130));
            g2.fillRoundRect(0, 10, 4, getHeight() - 20, 4, 4);
        } else if (hovered) {
            g2.setColor(new Color(255, 255, 255, 24));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}

class ActionButton extends JButton {
    private final Color base;
    private final Color accent;
    private final Color press;
    private boolean hovering;
    private boolean pressing;

    ActionButton(String text, Color base, Color accent, Color press) {
        this(text, null, base, accent, press);
    }

    ActionButton(String text, Icon icon, Color base, Color accent, Color press) {
        super(text, icon);
        this.base = base;
        this.accent = accent;
        this.press = press;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalTextPosition(SwingConstants.CENTER);
        setIconTextGap(10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                pressing = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressing = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressing = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int radius = 15;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(4, 5, width - 8, height - 8, radius, radius);

        Color top = pressing ? blend(press, Color.BLACK, 0.15f) : (hovering ? blend(base, Color.WHITE, 0.18f) : base);
        Color bottom = pressing ? blend(press, Color.BLACK, 0.30f) : (hovering ? blend(accent, Color.WHITE, 0.14f) : accent);
        GradientPaint paint = new GradientPaint(0, 0, top, 0, height, bottom);
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, width - 1, height - 2, radius, radius);

        if (hovering && !pressing) {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(6, 4, width - 12, height / 2, radius, radius);
        }

        g2.setColor(new Color(255, 255, 255, 45));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, width - 1, height - 2, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    private static Color blend(Color a, Color b, float ratio) {
        float inv = 1f - ratio;
        return new Color(
                Math.min(255, Math.round(a.getRed() * inv + b.getRed() * ratio)),
                Math.min(255, Math.round(a.getGreen() * inv + b.getGreen() * ratio)),
                Math.min(255, Math.round(a.getBlue() * inv + b.getBlue() * ratio)),
                Math.min(255, Math.round(a.getAlpha() * inv + b.getAlpha() * ratio)));
    }
}

class StyledTextField extends JTextField {
    StyledTextField() {
        super();
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setForeground(new Color(0xE2E8F0));
        setBackground(new Color(30, 41, 59));
        setCaretColor(new Color(0xE2E8F0));
        setOpaque(true);
        setEditable(true);
        setEnabled(true);
        setFocusable(true);
        setRequestFocusEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 116, 139), 1),
            new EmptyBorder(8, 14, 8, 14)
        ));
    }
}

class GlowRoundedBorder implements Border {
    private static final int ARC = 14;

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 4, 4, 4);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean focused = c.isEnabled() && c.isFocusable() && c.hasFocus();
        Color border = focused ? new Color(167, 139, 250, 210) : new Color(71, 85, 105, 200);

        if (focused) {
            g2.setColor(new Color(167, 139, 250, 50));
            g2.setStroke(new BasicStroke(6f));
            g2.drawRoundRect(x + 2, y + 2, width - 6, height - 6, ARC + 6, ARC + 6);
        }

        g2.setColor(border);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, ARC, ARC);
        g2.dispose();
    }
}

class Badge extends JComponent {
    private final String text;

    Badge(String text) {
        this.text = text;
        setPreferredSize(new Dimension(34, 22));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(167, 139, 250, 40));
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        g2.setColor(new Color(255, 255, 255, 210));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(text)) / 2;
        int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, tx, ty);
        g2.dispose();
        super.paintComponent(g);
    }
}

class WrapLayout extends FlowLayout {

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {

            int width = target.getParent().getWidth();
            if (width == 0) width = 600;

            int hgap = getHgap();
            int vgap = getVgap();

            int x = 0;
            int y = vgap;
            int rowHeight = 0;

            for (Component comp : target.getComponents()) {

                Dimension d = comp.getPreferredSize();

                if (x + d.width > width - hgap) {
                    x = 0;
                    y += rowHeight + vgap;
                    rowHeight = 0;
                }

                x += d.width + hgap;
                rowHeight = Math.max(rowHeight, d.height);
            }

            y += rowHeight + vgap;

            return new Dimension(width, y);
        }
    }
}
