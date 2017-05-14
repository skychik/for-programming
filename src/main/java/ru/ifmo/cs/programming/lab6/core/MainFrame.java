package ru.ifmo.cs.programming.lab6.core;

import static ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions.*;

import javafx.stage.FileChooser;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab6.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel tableTab;
    private JPanel commandTab;
    private JTree tree;
    private MyTable table;
    private JTextField searchField;
    private boolean isSearchFieldEmpty = true;
    private JButton searchButton;
    private JButton saveButton;
    private JButton removeAllButton;
    private JTextArea textArea;
    private JTextField nameTextField;
    private JComboBox professionComboBox;
    private Background background;
    private Dimension size;
    private ArrayDeque<Employee> deque;
    private JButton avatar = null;
    private FileFilterExt eff;
    private JFileChooser fileChooser = null;
    private final String[][] FILTERS = {{"png", "Изображения (*.png)"},
            {"jpg" , "Изображения(*.jpg)"}};

    private static String fontName = "Gill Sans MT Bold Condensed";
    private static String imageDir = System.getProperty("user.dir") + "\\src\\resources\\images\\";

    private Color foregroundColor = new Color(152, 156, 153);
    private Color opaqueColor = new Color(0,0,0,0);
    private Font font = new Font(fontName, Font.ITALIC, 12);
    private LineBorder lineBorder = new LineBorder(Color.BLACK,2);


    public MainFrame(ArrayDeque<Employee> deque) {
        super("MainFrame");

        setIconImage(new ImageIcon(
                System.getProperty("user.dir") + "\\src\\resources\\images\\icon.png").getImage());

        this.deque = deque;

        UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
        UIManager.put("TabbedPane.tabsOpaque", Boolean.TRUE);
        UIManager.put("TabbedPane.selected", new Color(152, 156, 153, 96));

        setMainPanel();
        setContentPane(mainPanel);

        setBackground();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Диалоговое окно для подтвеждения закрытия программы
        addWindowListener (new WindowAdapter() {
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
                             });

        setMenu();
    }

    private void setMainPanel() {
        mainPanel = new JPanel();

        size = new Dimension(1100, 710);
        setPreferredSize(size);

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
        setCommandTab();

        //Прозрачность фона вкладок
        //tabbedPane.setOpaque(false);
        //tabbedPane.putClientProperty("TabbedPane.contentOpaque", Boolean.FALSE);
        //tabbedPane.putClientProperty("TabbedPane.tabsOpaque", Boolean.FALSE);

        //Шрифт
        tabbedPane.setFont(new Font(fontName, Font.PLAIN, 18));

        //Цвет
        tabbedPane.setForeground(new Color(152, 156, 153));
        tabbedPane.setBackground(App.defEighthAlphaColor);

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

        tableTab.setFont(new Font(fontName, Font.PLAIN, 16));
        //tableTab.setBackground(App.defEighthAlphaColor);
        tableTab.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        tableTab.setLayout(gridBagLayout);

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
        makeRemoveAllButton(constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        makeSaveButton(constraints);

        tabbedPane.addTab("Table", tableTab);

        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    }

    private void makeTree(GridBagConstraints constraints) {
//        tree = new MyCheckBoxTree(makeRootNode());

        //tree.putClientProperty();
        //UIManager.put("Tree.textForeground", Color.WHITE);
        //UIManager.put("Tree.textBackground", Color.WHITE);

        tree.setOpaque(false);
        //tree.setBackground(App.defEighthAlphaColor);

        tableTab.add(tree, constraints);
    }

    private DefaultMutableTreeNode makeRootNode() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Employee");

        DefaultMutableTreeNode secondNode;

        secondNode = new DefaultMutableTreeNode("Factory Worker");
        rootNode.add(secondNode);

        secondNode = new DefaultMutableTreeNode("Shop Assistant");
        rootNode.add(secondNode);

        return rootNode;
    }

    private void makeScrollTable(GridBagConstraints constraints) {
        table = new MyTable(new MyTableModel(deque));
        //table.setBackground(defEighthAlphaColor);

        JScrollPane scrollTable = new JScrollPane(table);
        //scrollTable.setOpaque(false);

        table.getModel().addTableModelListener(table);

        tableTab.add(scrollTable, constraints);
    }

    private void makeSearchField(GridBagConstraints constraints) {
        searchField = new JTextField("Type something...", 1);

        searchField.setForeground(Color.WHITE);
        searchField.setBackground(App.defEighthAlphaColor);

        //if ENTER typed
        searchField.addActionListener(e -> {
            if (Objects.equals(searchField.getText(), "")) {
                searchField.setText("Type something...");
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

        tableTab.add(searchField, constraints);
    }

    private void makeSearchButton(GridBagConstraints constraints) {
        searchButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\src\\resources\\images\\loop.png"));
        //searchButton.setBackground(new Color(0, 0, 0, 0));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setBorder(null);

        searchButton.addActionListener(e -> doSearch());

        tableTab.add(searchButton, constraints);
    }

    private void doSearch() {
        if (isSearchFieldEmpty) {
            table.getSorter().setRowFilter(null);
        } else {
            //поиск по таблице без учета регистра
            table.getSorter().setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText().trim()));
        }
    }

    private void makeRemoveAllButton(GridBagConstraints constraints) {
        removeAllButton = new StandartButton("Remove all");

        //removeAllButton.setBorderPainted(false);
        removeAllButton.setBackground(new Color(152, 156, 153, 32));

        removeAllButton.addActionListener(e -> deque.clear());

        tableTab.add(removeAllButton, constraints);
    }

    private void makeSaveButton(GridBagConstraints constraints) {
        saveButton = new StandartButton("Save");

        //saveButton.setForeground(Color.BLACK);
        saveButton.setBackground(new Color(152, 156, 153, 32));
        //saveButton.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 15, 5, 15)));
        //saveButton.setBorderPainted(false);

        saveButton.addActionListener(e -> save(App.getDeque()));

        tableTab.add(saveButton, constraints);
    }

    private void setCommandTab() {

        commandTab = new JPanel();
        commandTab.setOpaque(false);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        commandTab.setLayout(gridBagLayout);

        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0, 0,20,0);
        //Кнопка добавления аватара
        makeAvatarButton(constraints);

        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridy = 0;
        constraints.weightx = 1;
        //Прокрутка многострочной области ввода
        makeScrollTextArea(constraints);

        constraints.insets = new Insets(0, 0,0,0);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        //Поле ввода имени
        makeNameField(constraints);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        //Выбор класса
        makeClassTypeChooser(constraints);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        //Выбор профессии
        makeProfessionComboBox(constraints);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        //Зарплата
        makeSalarySlider(constraints);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        //Отношение к боссу
        makeBossAttitudePanel(constraints);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;
        //Работоспособность
        makeQualityStepper(constraints);

        constraints.weighty = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 2;
        //Кнопка Remove
        makeRemoveButton(constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        //Кнопка ok
        makeOkButton(constraints);

        tabbedPane.addTab("Commands", commandTab);

        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        //tab2.setOpaque(false);
    }

    private void makeAvatarButton(GridBagConstraints constraints){
        String avatarPath = imageDir + "standartAvatar.jpg";
        avatar = new JButton();
        try {
            Image avatarImage = ImageIO.read(new File(avatarPath));
            avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            avatar.setBackground(new Color(0,0,0,0));
        }catch (IOException e){}
        avatar.setBorder(new LineBorder(new Color(60, 60, 60), 2));

        avatar.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                avatar.setBorder(new LineBorder(Color.WHITE, 2));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                avatar.setBorder(new LineBorder(Color.GRAY, 2));

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                avatar.setBorder(new LineBorder(Color.GRAY, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                avatar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
            }

        });
        addFileChooserListeners();
        commandTab.add(avatar, constraints);
    }

    private void addFileChooserListeners()
    {
        avatar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setDialogTitle("Выберите файл");
                // Определяем фильтры типов файлов
                for (int i = 0; i < FILTERS[0].length; i++) {
                    eff = new FileFilterExt(FILTERS[i][0],
                            FILTERS[i][1]);
                    fileChooser.addChoosableFileFilter(eff);
                }
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Word & Excel", "docx", "xlsx");
                fileChooser.setFileFilter(filter);

                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(MainFrame.this);
                // Если файл выбран, покажем его в сообщении
                if (result == JFileChooser.APPROVE_OPTION )
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Выбран файл ( " +
                                    fileChooser.getSelectedFile() + " )");
            }
        });
    }

    // Фильтр выбора файлов определенного типа
    class FileFilterExt extends javax.swing.filechooser.FileFilter
    {
        String extension  ;  // расширение файла
        String description;  // описание типа файлов

        FileFilterExt(String extension, String descr)
        {
            this.extension = extension;
            this.description = descr;
        }
        @Override
        public boolean accept(java.io.File file)
        {
            if(file != null) {
                if (file.isDirectory())
                    return true;
                if( extension == null )
                    return (extension.length() == 0);
                return file.getName().endsWith(extension);
            }
            return false;
        }
        // Функция описания типов файлов
        @Override
        public String getDescription() {
            return description;
        }
    }

    private void makeScrollTextArea(GridBagConstraints constraints){
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

        JScrollPane scrollNotes = new JScrollPane(notes);
        //Прозрачность
        scrollNotes.setOpaque(false);
        scrollNotes.getViewport().setOpaque(false);

        commandTab.add(scrollNotes, constraints);
    }

    private void makeRemoveButton(GridBagConstraints constraints){
        StandartButton remove = new StandartButton("Remove");

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(500,50));
        panel.add(remove, new BorderLayout().CENTER);

        commandTab.add(panel, constraints);
    }

    private void makeOkButton(GridBagConstraints constraints){
        JButton ok = new JButton(new ImageIcon(imageDir + "button_ok.png"));
        ok.setBorder(null);
        //Нажатая кнопка
        ok.setPressedIcon(new ImageIcon(imageDir + "button_ok_2.png"));
        //Кнопка при наведении
        ok.setSelectedIcon(new ImageIcon(imageDir + "button_ok_1.png"));
        //Прозрачность фона
        ok.setBackground(opaqueColor);
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        panel.setPreferredSize(new Dimension(500,80));
        panel.add(ok, new BorderLayout().CENTER);

        commandTab.add(panel, constraints);
    }

    private void makeNameField(GridBagConstraints constraints){
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

        commandTab.add(nameField, constraints);
    }

    private void makeClassTypeChooser(GridBagConstraints constraints){
        String[] classType = {"Employee", "FactoryWorker", "ShopAssistant"};
        JList<String> classList = new JList<String>(classType);
        classList.setSelectionMode(0);
        classList.setPreferredSize(new Dimension(500, 90));
        //Шрифт
        classList.setFont(new Font(fontName, Font.PLAIN, 19));
        classList.setForeground(foregroundColor);
        //Цвета
        classList.setBackground(Color.WHITE);
        classList.setSelectionBackground(new Color(54, 151, 175));
        //Границы
        classList.setBorder(lineBorder);

        commandTab.add(classList, constraints);
    }

    private void makeProfessionComboBox(GridBagConstraints constraints){
        String[] prof = {"ShopAssistant", "Economist", "Worker"};
        JComboBox<String> profession = new JComboBox<String>(prof);
        profession.setForeground(foregroundColor);
        profession.setFont(font);
        profession.setPreferredSize(new Dimension(500, 30));

        commandTab.add(profession, constraints);
    }

    private void makeSalarySlider(GridBagConstraints constraints){
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

        commandTab.add(salary, constraints);
    }

    private void makeBossAttitudePanel(GridBagConstraints constraints){
        JPanel panelRadio = new JPanel(new GridLayout(0, 5, 0, 0));
        //panelRadio.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK),"Attitude to Boss",1,1,new Font(fontName, Font.PLAIN, 12),foregroundColor));
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
        panelRadio.setPreferredSize(new Dimension(500, 50));

        commandTab.add(panelRadio, constraints);
    }

    private void makeQualityStepper(GridBagConstraints constraints){
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0,-127,128,5);
        JSpinner workQuality = new JSpinner(numberModel);
        workQuality.setPreferredSize(new Dimension(500,30));

        commandTab.add(workQuality, constraints);
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

    @Override
    public Dimension getSize() {
        return size;
    }

    static String getFontName() {
        return fontName;
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
        table = new MyTable(null);
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
}
