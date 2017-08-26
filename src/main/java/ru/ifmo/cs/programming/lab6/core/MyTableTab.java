package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab6.utils.MyColor;
import ru.ifmo.cs.programming.lab6.utils.MyTableModel;
import ru.ifmo.cs.programming.lab6.utils.StandardButton;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

import static ru.ifmo.cs.programming.lab6.core.MainFrame.fontName;

class MyTableTab extends JPanel {
    private static MyTable table;
    private JTextField searchField;
    private boolean isSearchFieldEmpty = true;
    private StandardButton clearButton;
    private InteractiveModeFunctions imf;

    MyTableTab(InteractiveModeFunctions imf) {
        super();

        this.imf = imf;

        this.setFont(new Font(fontName, Font.PLAIN, 16));
        //tableTab.setBackground(AppGUI.backgroundEighthAlphaColor);
        this.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        //Add components
        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        makeTree(constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        makeSearchField(constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        makeSearchButton(constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 2;
        constraints.gridheight = 3;
        constraints.gridx = 1;
        constraints.gridy = 1;
        makeScrollTable(constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        makeClearButton(constraints);

        /*constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        makeSaveButton(constraints);*/
    }

    private void makeTree(GridBagConstraints constraints) {
        JTree tree = new MyCheckBoxTree(initRootNode());

        //tree.putClientProperty();
        //UIManager.put("Tree.textForeground", Color.WHITE);
        //UIManager.put("Tree.textBackground", Color.WHITE);

        tree.setOpaque(false);
        tree.setBackground(MyColor.backgroundEighthAlphaColor);

        this.add(tree, constraints);
    }

    private DefaultMutableTreeNode initRootNode() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Employee");

        DefaultMutableTreeNode secondNode;

        secondNode = new DefaultMutableTreeNode("Factory Worker");
        rootNode.add(secondNode);

        secondNode = new DefaultMutableTreeNode("Shop Assistant");
        rootNode.add(secondNode);

        return rootNode;
    }

    private void makeScrollTable(GridBagConstraints constraints) {
        table = new MyTable(new MyTableModel(imf), imf);

        table.getModel().addTableModelListener(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        //scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.getViewport().setOpaque(false);

        this.add(scrollPane, constraints);
    }

    private void makeSearchField(GridBagConstraints constraints) {
        searchField = new JTextField("Type to search...", 1);

        searchField.setForeground(MyColor.whiteTextColor);
        searchField.setBackground(MyColor.backgroundEighthAlphaColor);

        //if ENTER typed
        searchField.addActionListener(e -> {
            if (Objects.equals(searchField.getText(), "")) {
                searchField.setText("Type to search...");
                isSearchFieldEmpty = true;
            } else isSearchFieldEmpty = false;
            doSearch();
        });

        //автоматически заполняет строчку дефолтной строкой
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isSearchFieldEmpty) searchField.setText(null); // Empty the text field when it receives focus
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Objects.equals(searchField.getText(), "")) {
                    searchField.setText("Type something...");
                    isSearchFieldEmpty = true;
                } else isSearchFieldEmpty = false;
            }
        });

        this.add(searchField, constraints);
    }

    private void makeSearchButton(GridBagConstraints constraints) {
        JButton searchButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "/src/resources/images/loop.png"));
        //searchButton.setBackground(new Color(0, 0, 0, 0));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setBorder(null);

        searchButton.addActionListener(e -> doSearch());

        this.add(searchButton, constraints);
    }

    private void doSearch() {
        if (isSearchFieldEmpty) {
            table.getSorter().setRowFilter(null);
        } else {
            //поиск по таблице без учета регистра
            table.getSorter().setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText().trim()));
        }
    }

    private void makeClearButton(GridBagConstraints constraints) {
        clearButton = new StandardButton("Clear table");

        //clearButton.setBorderPainted(false);
        //clearButton.setBackground(AppGUI.backgroundEighthAlphaColor);

        clearButton.addActionListener(e -> {
            // Потверждение очищения таблицы
            int n = JOptionPane.showConfirmDialog(clearButton, "Очистить таблицу работников?",
                    "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (n == 0) imf.clear();
        });

        this.add(clearButton, constraints);
    }

    public static MyTable getTable() {
        return table;
    }
}
