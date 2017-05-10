package ru.ifmo.cs.programming.lab6.core;

import static ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions.*;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab6.App;
import ru.ifmo.cs.programming.lab6.MyTableModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.Objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel tableTab;
    private JPanel tab2;
    private JTree tree;
    private JTable table;
    private JTextField searchField;
    private JButton searchButton;
    private JButton saveButton;
    private JButton removeAllButton;
    private JTextArea textArea;
    private JTextField nameTextField;
    private JComboBox professionComboBox;
    private Background background;
    private Dimension size;
    private ArrayDeque<Employee> deque;

    private static String fontName = "Gill Sans MT Bold Condensed";
    private static String currentDir = System.getProperty("user.dir") + "\\src\\java\\ru\\ifmo\\cs\\programming\\lab6";

    public MainFrame(ArrayDeque<Employee> deque) {
        super("MainFrame");

        this.deque = deque;

        UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
        UIManager.put("TabbedPane.tabsOpaque", Boolean.TRUE);
        UIManager.put("TabbedPane.selected", new Color(152, 156, 153, 96));

        setMainPanel();
        setContentPane(mainPanel);

        setBackground();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //TODO: починить диалоговое окно закрытия
        /*addWindowListener (new WindowAdapter() {
                                 @Override
                                 public void windowClosing(WindowEvent e) {
                                     // Потверждение закрытия окна JFrame
                                     Object[] options = { "Да", "Нет!" };
                                     int n = JOptionPane.showOptionDialog(e.getWindow(), "Закрыть окно?",
                                             "Подтверждение", JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                     if (n == 0) {
                                         e.getWindow().setVisible(false);
                                         System.exit(0);
                                     }
                                 }
                             });*/

        setMenu();
    }

    private void setMainPanel() {
        mainPanel = new JPanel();

        size = new Dimension(1100, 700);
        setPreferredSize(size);

        mainPanel.setBackground(new Color(-9408400)); //ToDo нужен ли? Стоит же картинка

        LayoutManager overlay = new OverlayLayout(mainPanel);
        mainPanel.setLayout(overlay);

        setTabbedPane();
    }

    private void setTabbedPane() {
        tabbedPane = new JTabbedPane();
        /*tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
            protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
        });*/

        setTableTab();
        setTab1();

        //Прозрачность фона вкладок
        tabbedPane.setOpaque(false);
        //tabbedPane.putClientProperty("TabbedPane.contentOpaque", Boolean.FALSE);
        //tabbedPane.putClientProperty("TabbedPane.tabsOpaque", Boolean.FALSE);

        //Шрифт
        tabbedPane.setFont(new Font(fontName, Font.PLAIN, 18));

        //Цвет
        tabbedPane.setForeground(new Color(152, 156, 153));
        tabbedPane.setBackground(new Color(152, 156, 153, 32));

        mainPanel.add(
                tabbedPane,
                new com.intellij.uiDesigner.core.GridConstraints(
                        0, 0, 1, 1,
                        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                        com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                        null,
                        new Dimension(200, 200),
                        null,
                        0,
                        false
                )

        );

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);//при маленьком окне табы в линию, а не друг под другом
    }

    private void setTableTab() {
        tableTab = new JPanel();
        tableTab.setBackground(new Color(152, 156, 153, 32));

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        tableTab.setFont(new Font(fontName, Font.PLAIN, 16));
        tableTab.setLayout(gridBagLayout);

        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        makeTree(gridBagLayout, constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        makeSearchField(gridBagLayout, constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        makeSearchButton(gridBagLayout, constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 2;
        constraints.gridheight = 3;
        constraints.gridx = 1;
        constraints.gridy = 1;
        makeScrollTable(gridBagLayout, constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        makeRemoveAllButton(gridBagLayout, constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        makeSaveButton(gridBagLayout, constraints);

        tabbedPane.addTab("Table", tableTab);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tableTab.setOpaque(false);
    }

    private void makeTree(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        tree = new JTree();

        tree.setOpaque(false);

        //

        tableTab.add(tree, constraints);
    }

    private void makeScrollTable(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        table = new JTable(new MyTableModel(deque));
        //table.setBackground(new Color(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setOpaque(false);

        tableTab.add(scrollPane, constraints);
    }

    private void makeSearchField(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        searchField = new JTextField("Type something...");
        searchField.setDisabledTextColor(Color.WHITE);
        searchField.setSelectedTextColor(Color.WHITE);
        searchField.setBackground(new Color(152, 156, 153, 32));
        searchField.addFocusListener(new FocusListener() {
            boolean empty = true;

            @Override
            public void focusGained(FocusEvent e) {
                if (empty) searchField.setText(null); // Empty the text field when it receives focus
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Objects.equals(searchField.getText(), "")) {
                    searchField.setText("Type something...");
                    empty = true;
                } else empty = false;
            }
        });
        searchField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((!searchField.getText().isEmpty()) && (searchField.getText().charAt(searchField.getText().length() - 1) == '\n')) {
                    doSearch(searchField.getText().trim());
                }
            }
        });

        //

        tableTab.add(searchField, constraints);
    }

    private void makeSearchButton(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        searchButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\src\\resources\\images\\loop.png"));
        //searchButton.setBackground(new Color(0, 0, 0, 0));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setBorder(null);

        searchButton.addActionListener(e -> doSearch(searchField.getText().trim()));

        tableTab.add(searchButton, constraints);
    }

    private void makeRemoveAllButton(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        removeAllButton = new JButton("Remove all");

        //removeAllButton.setBorderPainted(false);
        removeAllButton.setBackground(new Color(152, 156, 153, 32));

        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete_all(App.getDeque());
            }
        });

        tableTab.add(removeAllButton, constraints);
    }

    private void makeSaveButton(GridBagLayout gridBagLayout, GridBagConstraints constraints) {
        saveButton = new JButton("Save");

        //saveButton.setForeground(Color.BLACK);
        saveButton.setBackground(new Color(152, 156, 153, 32));
        //saveButton.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 15, 5, 15)));
        //saveButton.setBorderPainted(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(App.getDeque());
            }
        });

        tableTab.add(saveButton, constraints);
    }

    //TODO: слишком много кода в одном месте. Разбить на односложные составляющие
    //TODO: придумать другое название вкладки
    private void setTab1() {
        //Цвета
        Color foregroundColor = new Color(152, 156, 153);
        Color opaqueColor = new Color(0,0,0,0);

        //Шрифты
        Font font = new Font(fontName, Font.ITALIC, 12);

        //Граница
        LineBorder lineBorder = new LineBorder(Color.BLACK,2);

        final JPanel tab1 = new JPanel();
        tab1.setOpaque(false);

        GridLayout gridLayout = new GridLayout(2, 0);

        //Кнопка добавления аватара
        String avatarPath = System.getProperty("user.dir") + "\\src\\resources\\images\\standartAvatar.jpg";
        JButton avatar = new JButton(new ImageIcon(avatarPath));
        avatar.setBorder(null);
//            tab1.add(avatar);

        //Многострочная область ввода
        JTextArea notes = new JTextArea("Здесь можно вводить заметки", 15,50);
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        //шрифт
        notes.setForeground(foregroundColor);
        notes.setFont(font);
        //прозрачность
        notes.setBackground(opaqueColor);
        //границы
        notes.setBorder(new LineBorder(foregroundColor));

        //Прокрутка многострочной области ввода
        JScrollPane scrollNotes = new JScrollPane(notes);
        //Прозрачность
        scrollNotes.setOpaque(false);
        scrollNotes.getViewport().setOpaque(false);
        //Добавление элемента
//            tab1.add(scrollNotes);

        //Кнопка Remove
        //StandartButton remove = new StandartButton("Remove");
        Button remove = new Button("Remove");
//            tab1.add(remove);

        //Кнопка ok
        JButton ok = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\src\\resources\\images\\button_ok.png"));
        ok.setBorder(null);
        //Нажатая кнопка
        ok.setPressedIcon(new ImageIcon(System.getProperty("user.dir") + "\\src\\resources\\images\\button_ok_2.png"));
        //Кнопка при наведении
        ok.setSelectedIcon(new ImageIcon(System.getProperty("user.dir") + "\\src\\resources\\images\\button_ok_1.png"));
        //Прозрачность фона
        ok.setBackground(opaqueColor);
        //Добавление элемента
//            tab1.add(ok);

        //Поле ввода имени
        JTextField nameField = new JTextField("Name");
        //Фон
        nameField.setBackground(opaqueColor);
        //Размер
        nameField.setPreferredSize(new Dimension(500, 28));
        //Границы
        nameField.setBorder(lineBorder);
        //Шрифт
        nameField.setFont(font);
        nameField.setForeground(foregroundColor);
        //Добавление элемента
//            tab1.add(nameField);

        //Выбор класса
        String[] classType = {"Employee", "FactoryWorker", "ShopAssistant"};
        JList<String> classList = new JList<String>(classType);
        classList.setSelectionMode(0);
        classList.setPreferredSize(new Dimension(500, 90));
        //Шрифт
        classList.setFont(new Font(fontName, Font.PLAIN, 19));
        classList.setForeground(foregroundColor);
        //Цвета
        classList.setBackground(Color.BLACK);
        classList.setSelectionBackground(new Color(54, 151, 175));
        //Границы
        classList.setBorder(lineBorder);
        //Добавление элемента
//            tab1.add(classList);

        //Выбор профессии
        String[] prof = {"ShopAssistant", "Economist", "Worker"};
        JComboBox<String> profession = new JComboBox<String>(prof);
//            profession.setBackground(opaqueColor);
        profession.setForeground(foregroundColor);
        profession.setFont(font);
//            tab1.add(profession);

        //Зарплата
        JSlider salary = new JSlider(JSlider.HORIZONTAL, 10000, 50000, 20000);
        salary.setPreferredSize(new Dimension(500, 50));
        salary.setOpaque(false);
        //Прорисовка значений
        salary.setPaintLabels(true);
        salary.setMinorTickSpacing(2500);
        salary.setMajorTickSpacing(5000);
        salary.setPaintTicks(true);
        //Ползунок перемещается только по значениям
        salary.setSnapToTicks(true);
        //Смена курсора
        salary.setCursor(new Cursor(12));
        //Шрифт
        salary.setFont(font);
        salary.setForeground(foregroundColor);
        //Добавление элемента
//            tab1.add(salary);

        //Отношение к боссу
        JPanel panelRadio = new JPanel(new GridLayout(0, 5, 0, 0));
        panelRadio.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK),"Attitude to Boss",1,1,new Font(fontName, Font.PLAIN, 12),foregroundColor));
        panelRadio.setPreferredSize(new Dimension(400,50));
        String[] names1 = { "HATE", "LOW", "DEFAULT", "NORMAL", "HIGH"};
        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < names1.length; i++) {
            JRadioButton radio = new JRadioButton(names1[i]);
            if (i == 2) radio.setSelected(true);
            //Шрифт
            radio.setForeground(foregroundColor);
            radio.setFont(font);
            //
            radio.setFocusPainted(false);
            radio.setOpaque(false);
            panelRadio.add(radio);
            bg.add(radio);
        }
        panelRadio.setOpaque(false);
//            tab1.add(panelRadio);

        //Работоспособность
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0,-127,128,5);
        JSpinner workQuality = new JSpinner(numberModel);
        workQuality.setPreferredSize(new Dimension(300,30));
//            tab1.add(workQuality);

        JPanel panel1 = new JPanel();
        panel1.add(avatar);
        panel1.setOpaque(false);
        panel1.setSize(new Dimension(500,5));
        tab1.add(panel1);


        JPanel panel2 =  new JPanel();
        panel2.add(scrollNotes);
        panel2.setOpaque(false);
        tab1.add(panel2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(8,0,10,10));
        panel3.setOpaque(false);
        panel3.add(nameField);
        panel3.add(classList);
        panel3.add(profession);
        panel3.add(salary);
        panel3.add(workQuality);
        panel3.add(panelRadio);
        tab1.add(panel3);

        JPanel panel4 = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel4.setLayout(gridBagLayout);
        panel4.setOpaque(false);
        remove.setPreferredSize(new Dimension(200,60));
        ok.setPreferredSize(new Dimension(90,80));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 2;
        panel4.add(remove, gridBagConstraints);
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridx = 2;
        panel4.add(ok, gridBagConstraints);
        tab1.add(panel4);

//        tab1.setLayout(gridLayout);
        tabbedPane.addTab("Commands", tab1);

        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        //tab2.setOpaque(false);
    }

    private void setBackground() {
        background = new Background(new ImageIcon(
                System.getProperty("user.dir") + "\\src\\resources\\images\\background.png").getImage());
        getContentPane().add(background);
    }

    private void setMenu() {
        JMenu menu = new JMenu("File");

        Font font = new Font(fontName, Font.PLAIN, 14);

        menu.setFont(font);

        JMenu settings = new JMenu("Settings");
        settings.setFont(font);
        menu.add(settings);

        JMenuItem hotKeysItem = new JMenuItem("Hot keys");
        hotKeysItem.setFont(font);
        hotKeysItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HotKeys dialog = new HotKeys();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int locationX = (screenSize.width - dialog.getSize().width) / 2;
                int locationY = (screenSize.height - dialog.getSize().height) / 2;
                dialog.setBounds(locationX, locationY, dialog.getSize().width, dialog.getSize().height);//по центру экрана

                dialog.pack();
                dialog.setVisible(true);
            }
        });
        menu.add(hotKeysItem);

        JMenuItem buttonColorItem = new JMenuItem("Button color");
        buttonColorItem.setFont(font);
        settings.add(buttonColorItem);

        JMenuItem fontColorItem = new JMenuItem("Font color");
        fontColorItem.setFont(font);
        settings.add(fontColorItem);

        menu.addSeparator();

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        menu.add(saveItem);

//        saveItem.addActionListener ToDO: сделать сохранение по кнопке save и для остальных

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void doSearch(String inp) {
        //TODO: make search
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    /**
     * THEN WILL BE A PART FROM AUTOGENERATED CODE
     * IT SHOULDN"T BE USED, ONLY TO FIND SOMETHING USEFUL
     *
     *
     */

    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(15, 15, 15, 15), -1, -1));
        mainPanel.setBackground(new Color(-9408400));
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Show", panel1);
        searchField = new JTextField();
        panel1.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        panel1.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(594, 41), null, 0, false));
        removeAllButton = new JButton();
        removeAllButton.setText("Remove All");
        panel1.add(removeAllButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(594, 41), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Label");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        table = new JTable();
        panel1.add(table, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 3, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(50, 50), null, 0, false));
        tree = new JTree();
        panel1.add(tree, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(50, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Commands", panel2);
        textArea = new JTextArea();
        panel2.add(textArea, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Photo");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        nameTextField = new JTextField();
        panel2.add(nameTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Name");
        panel2.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Type");
        panel2.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Profession");
        panel2.add(label5, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        professionComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Employee");
        defaultComboBoxModel1.addElement("ShopAssistant");
        defaultComboBoxModel1.addElement("FactoryWorker");
        professionComboBox.setModel(defaultComboBoxModel1);
        panel2.add(professionComboBox, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public static String getFontName() {
        return fontName;
    }


}
