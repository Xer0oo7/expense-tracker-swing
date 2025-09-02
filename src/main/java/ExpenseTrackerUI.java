import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerUI extends JFrame {
    DefaultTableModel tableModel;
    JTable table;
    JLabel totalLabel;
    JComboBox<String> categoryCombo;
    JTextField dateField;

    public ExpenseTrackerUI() {
        setTitle("ExpenseMate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0xF6F8FA));
        setLayout(new BorderLayout(10,10));

        // Header
        add(createHeader(), BorderLayout.NORTH);

    // Main split
    JPanel main = new JPanel(new GridBagLayout());
    main.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.3;
    gbc.weighty = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    JPanel rightPanel = createRightPanel();
    main.add(rightPanel, gbc);
    gbc.gridx = 0;
    gbc.weightx = 1;
    JPanel leftPanel = createLeftPanel();
    main.add(leftPanel, gbc);
    add(main, BorderLayout.CENTER);

        // Footer / status
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader(){
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));
        p.setOpaque(false);

        JLabel title = new JLabel("ExpenseMate");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setBorder(new EmptyBorder(0,6,0,0));
        title.setForeground(new Color(0x111827));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        left.setOpaque(false);
        JLabel logo = new JLabel("\uD83D\uDCB8"); // emoji money-bag as a quick logo
        logo.setFont(logo.getFont().deriveFont(26f));
        left.add(logo);
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
        right.setOpaque(false);
        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(240,28));
        search.setToolTipText("Search description or category");
        JButton searchBtn = new JButton("Search");
        searchBtn.setPreferredSize(new Dimension(80,28));
        searchBtn.setFocusPainted(false);

        right.add(search);
        right.add(searchBtn);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);

        return p;
    }

    private JPanel createLeftPanel(){
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BorderLayout(10,10));
        p.add(createAddExpenseCard(), BorderLayout.NORTH);
        p.add(createTableCard(), BorderLayout.CENTER);
        return p;
    }

    private JPanel createAddExpenseCard(){
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createTitledBorder("Add Expense"));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel amountLabel = new JLabel("Amount");
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(amountLabel, gbc);
        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(100, 24));
        gbc.gridx = 1; gbc.gridy = 0;
        card.add(amountField, gbc);
        JLabel rupee = new JLabel("\u20B9");
        gbc.gridx = 2; gbc.gridy = 0;
        card.add(rupee, gbc);

        JLabel catLabel = new JLabel("Category");
        gbc.gridx = 0; gbc.gridy = 1;
        card.add(catLabel, gbc);
        categoryCombo = new JComboBox<>(new String[] {"Food","Transport","Shopping","Bills","Entertainment","Other"});
        categoryCombo.setPreferredSize(new Dimension(120, 24));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        card.add(categoryCombo, gbc);
        gbc.gridwidth = 1;

        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd)");
        gbc.gridx = 0; gbc.gridy = 2;
        card.add(dateLabel, gbc);
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(120, 24));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        card.add(dateField, gbc);
        gbc.gridwidth = 1;

        JLabel descLabel = new JLabel("Description");
        gbc.gridx = 0; gbc.gridy = 3;
        card.add(descLabel, gbc);
        JTextField desc = new JTextField();
        desc.setPreferredSize(new Dimension(200, 24));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        card.add(desc, gbc);
        gbc.gridwidth = 1;

        JButton addBtn = new JButton("Add");
        gbc.gridx = 2; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        card.add(addBtn, gbc);

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amt = amountField.getText().trim();
                String cat = (String)categoryCombo.getSelectedItem();
                String date = dateField.getText().trim();
                String d = desc.getText().trim();
                if(amt.isEmpty()){
                    JOptionPane.showMessageDialog(card, "Please enter an amount.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                tableModel.addRow(new Object[]{amt, cat, date, d});
                recalcTotal();
                amountField.setText(""); desc.setText(""); dateField.setText("");
            }
        });

        return card;
    }

    private JScrollPane createTableCard(){
        tableModel = new DefaultTableModel(new Object[]{"Amount","Category","Date","Description"}, 0){
            @Override public boolean isCellEditable(int row, int col){ return false; }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1,1));
        table.setBorder(null);

        // header style
        table.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        // right align Amount column
        DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer();
        amountRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(0).setCellRenderer(amountRenderer);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.setPreferredSize(new Dimension(600,300));

        // sample data
        tableModel.addRow(new Object[]{"500","Food","2025-09-02","Lunch"});
        tableModel.addRow(new Object[]{"1200","Transport","2025-09-01","Taxi"});
        recalcTotal();

        return sp;
    }

    private JPanel createRightPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder("Summary"));
        p.setPreferredSize(new Dimension(220,0));

        JPanel card1 = new JPanel();
        card1.setBorder(BorderFactory.createTitledBorder("Total"));
        totalLabel = new JLabel("\u20B9 0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 18f));
        card1.add(totalLabel);
        p.add(card1);

        JPanel card2 = new JPanel();
        card2.setBorder(BorderFactory.createTitledBorder("Top Category"));
        JLabel topCat = new JLabel("—");
        card2.add(topCat);
        p.add(card2);

        return p;
    }

    private JPanel createFooter(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder(new EmptyBorder(8,12,8,12));
        p.setOpaque(false);
        p.add(new JLabel("Tip: Press Add to add a new expense. Use the search box to filter.")); 
        return p;
    }

    private void recalcTotal(){
        double total = 0;
        for(int i=0;i<tableModel.getRowCount();i++){
            try{
                total += Double.parseDouble(tableModel.getValueAt(i,0).toString());
            }catch(Exception ex){}
        }
        totalLabel.setText("\u20B9 " + String.format("%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerUI().setVisible(true);
        });
    }
}
