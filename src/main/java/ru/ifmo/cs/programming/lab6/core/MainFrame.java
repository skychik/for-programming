//TODO: make commandTab as new class
package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab6.utils.Background;
import ru.ifmo.cs.programming.lab6.utils.MyColor;
import ru.ifmo.cs.programming.lab6.utils.StandardButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import static ru.ifmo.cs.programming.lab6.core.MyTableTab.getTable;
import static ru.ifmo.cs.programming.lab6.utils.MyColor.foregroundColor;
import static ru.ifmo.cs.programming.lab6.utils.MyColor.opaqueColor;

public class MainFrame extends JFrame {
    private InteractiveModeFunctions imf;

//    private boolean usingBD = false;

    private JMenu menu;

    private JPanel mainPanel;
    private static JTabbedPane tabbedPane;
    private JPanel commandTab;
    private StandardButton standardValuesButton;
    private static JTextArea notes = null;
    private static JTextField nameField;
    private static JComboBox professionComboBox;
    private static JSlider salarySlider;
    private static ButtonGroup bg;
    private JRadioButton[] radio;
    private String selectedRadio;
    private static JSpinner workQualityStepper;
    private Dimension size;
//    private ArrayDeque<Employee> deque;
    static JButton avatar;
    private String avatarPath;
    private FileFilterExt eff;
    private JFileChooser fileChooser;
    private JColorChooser colorChooser;
    private static JList<String> classList;
    private JButton okButton;
    private JMenu settings;
    private JMenuItem hotKeysItem;
    private JMenuItem buttonColorItem;
    private JMenuItem standardAvatarItem;
    private JMenuItem saveItem;
    private JMenu language;
    private String[] prof;
    protected Locale locale = new Locale("eng", "UK");
    private Properties prop = new Properties();
    private String pathToProperties;
    private FileInputStream fileInputStream;
    private MyTableTab tableTab;

    static String fontName = "Gill Sans MT Bold Condensed";
    private Font font = new Font(fontName, Font.ITALIC, 13);
    private LineBorder lineBorder = new LineBorder(Color.BLACK,2);

    // if -1 then nothing was deleted
	private long deletedId = -1;
	private long nextId;

    public MainFrame(InteractiveModeFunctions imf) {
        super("CRUD application");

        this.imf = imf;

        size = new Dimension(1200, 700);
        setPreferredSize(size);

        setIconImage(new ImageIcon(
                System.getProperty("user.dir") + "/src/resources/images/icon.png").getImage());

//        this.deque = deque;

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
                                         imf.exit();
                                     }
                                 }
                             });

        setMenu();
    }

    private void setMainPanel() {
        mainPanel = new JPanel();

        LayoutManager overlay = new OverlayLayout(mainPanel);
        mainPanel.setLayout(overlay);

        setTabbedPane();
    }

    private void setTabbedPane() {
        tabbedPane = new JTabbedPane();
        /*tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
            protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
        });*/

        tableTab = new MyTableTab(imf);
        tabbedPane.addTab("Table", tableTab);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        setCommandTab();

        //Прозрачность фона вкладок
        //tabbedPane.setOpaque(false);
        //tabbedPane.putClientProperty("TabbedPane.contentOpaque", Boolean.FALSE);
        //tabbedPane.putClientProperty("TabbedPane.tabsOpaque", Boolean.FALSE);

        //Шрифт
        tabbedPane.setFont(new Font(fontName, Font.PLAIN, 18));

        //Цвет
        tabbedPane.setForeground(new Color(152, 156, 153));
        tabbedPane.setBackground(MyColor.backgroundEighthAlphaColor);

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

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);// TODO: при маленьком окне табы в линию, а не друг под другом
    }

    /*private void makeSaveButton(GridBagConstraints constraints) {
        StandardButton saveButton = new StandardButton("Save");

        //saveButton.setForeground(Color.BLACK);
//        saveButton.setBackground(new Color(152, 156, 153, 32));
        //saveButton.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 15, 5, 15)));
        //saveButton.setBorderPainted(false);
        saveButton.setBackground(MyColor.backgroundEighthAlphaColor);

        saveButton.addActionListener(e -> save(AppGUI.getDeque()));

        //tableTab.add(saveButton, constraints);
    }*/

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
        makeStandardValuesFieldsButton(constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        //Кнопка okButton
        makeOkButton(constraints);

        tabbedPane.addTab("Commands", commandTab);

        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        //tab2.setOpaque(false);
    }

    private void makeAvatarButton(GridBagConstraints constraints) {
        avatarPath = System.getProperty("user.dir") + "/src/resources/images/standardAvatar.jpg";
        avatar = new JButton();
        try {
            Image avatarImage = ImageIO.read(new File(avatarPath));
            avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            avatar.setBackground(new Color(0,0,0,0));
        } catch (IOException e) {
            //TODO: and what?
        }
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
        fileChooser = new JFileChooser();
        addFileChooserListeners();
        commandTab.add(avatar, constraints);
    }

    private void addFileChooserListeners() {
        avatar.addActionListener(new ActionListener()
        {
            private final String[][] FILTERS = {{"png", "Изображения (*.png)"},
                    {"jpg" , "Изображения(*.jpg)"}};

            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setDialogTitle("Выберите файл");
                // Определяем фильтры типов файлов
                for (int i = 0; i < FILTERS[0].length; i++) {
                    eff = new FileFilterExt(FILTERS[i][0],
                            FILTERS[i][1]);
                    fileChooser.addChoosableFileFilter(eff);
                }

                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(MainFrame.this);
                // Если файл выбран, покажем его в сообщении
                if (result == JFileChooser.APPROVE_OPTION ){
                setAvatarIcon(fileChooser.getSelectedFile());
                avatarPath = fileChooser.getSelectedFile().toString();
                }
            }
        });
    }

    class FileFilterExt extends javax.swing.filechooser.FileFilter {
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
                    return (extension.length() == 0);//TODO: might produce NULLPOINTEREXCEPTION
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

    private void setAvatarIcon(File avatarFile){
        try {
            Image avatarImage = ImageIO.read(avatarFile);
            avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            avatar.setBackground(new Color(0,0,0,0));
        } catch (IOException e) {
            //TODO: and what?
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Выбранный файл ( " +
                            fileChooser.getSelectedFile() + " ) не может быть выбран в качестве аватара");
        }
    }

    private void makeScrollTextArea(GridBagConstraints constraints){
        notes = new JTextArea("Здесь можно вводить заметки", 15,50);
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

    private void makeStandardValuesFieldsButton(GridBagConstraints constraints){
        standardValuesButton = new StandardButton("Standart Values");

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(500,80));
        panel.add(standardValuesButton, BorderLayout.CENTER);

        standardValuesButton.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setDefaultCommandTab();
            }
        });

        commandTab.add(panel, constraints);
    }

    private void makeOkButton(GridBagConstraints constraints){
        okButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "/src/resources/images/button_ok.png"));
        okButton.setBorder(null);
        //Нажатая кнопка
        okButton.setPressedIcon(new ImageIcon(System.getProperty("user.dir") + "/src/resources/images/button_ok_2.png"));
        //Кнопка при наведении
        okButton.setSelectedIcon(new ImageIcon(System.getProperty("user.dir") + "/src/resources/images/button_ok_1.png"));
        //Прозрачность фона
        okButton.setBackground(opaqueColor);
        okButton.addActionListener(e -> {
            addEmployee();
            getTable().updateUI();
            setDefaultCommandTab();
        });
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        panel.setPreferredSize(new Dimension(500,80));
        panel.add(okButton, BorderLayout.CENTER);

        commandTab.add(panel, constraints);
    }

    private void makeNameField(GridBagConstraints constraints){
        nameField = new JTextField("Name");
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
        classList = new JList<>(classType);
        classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classList.setPreferredSize(new Dimension(500, 69));
        //Шрифт
        classList.setFont(font);
        classList.setForeground(foregroundColor);
        //Цвета
        classList.setBackground(Color.WHITE);
        classList.setSelectionBackground(new Color(54, 151, 175));
        //Границы
        classList.setBorder(lineBorder);
        classList.setSelectedIndex(0);
        commandTab.add(classList, constraints);
    }

    private void makeProfessionComboBox(GridBagConstraints constraints){
        prof = new String[]{"Programmer", "Economist", "Manager"};
        professionComboBox = new JComboBox<>(prof);
        professionComboBox.setForeground(foregroundColor);
        professionComboBox.setFont(font);
        professionComboBox.setPreferredSize(new Dimension(500, 30));

        commandTab.add(professionComboBox, constraints);
    }

    private void makeSalarySlider(GridBagConstraints constraints){
        salarySlider = new JSlider(JSlider.HORIZONTAL, 10000, 50000, 20000);
        salarySlider.setPreferredSize(new Dimension(500, 50));
        salarySlider.setOpaque(false);
        //Прорисовка значений
        salarySlider.setPaintLabels(true);
        salarySlider.setMinorTickSpacing(2500);
        salarySlider.setMajorTickSpacing(5000);
        salarySlider.setPaintTicks(true);
        //Ползунок перемещается только по значениям
        salarySlider.setSnapToTicks(true);
        //Смена курсора
        salarySlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Шрифт
        salarySlider.setFont(font);
        salarySlider.setForeground(foregroundColor);

        commandTab.add(salarySlider, constraints);
    }

    private void makeBossAttitudePanel(GridBagConstraints constraints){
        JPanel bossAttitudeRadioPanel = new JPanel(new GridLayout(0, 5, 0, 0));
        bossAttitudeRadioPanel.setPreferredSize(new Dimension(400,50));
        String[] name = { "HATE", "LOW", "DEFAULT", "NORMAL", "HIGH"};
        bg = new ButtonGroup();
        selectedRadio = "DEFAULT";
        radio = new JRadioButton[5];

        for (int i = 0; i < 5; i++) {
            radio[i] = new JRadioButton(name[i]);
            //Шрифт
            radio[i].setForeground(foregroundColor);
            radio[i].setFont(font);
            radio[i].setFocusPainted(false);
            radio[i].setOpaque(false);
            radio[i].addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedRadio = e.toString().substring(e.toString().indexOf('=') + 1,
                            e.toString().indexOf(',', e.toString().indexOf('=')));

                }
            });
            bg.add(radio[i]);
            bossAttitudeRadioPanel.add(radio[i]);

        }

        radio[2].setSelected(true);
        bossAttitudeRadioPanel.setOpaque(false);
        bossAttitudeRadioPanel.setPreferredSize(new Dimension(500, 50));

        commandTab.add(bossAttitudeRadioPanel, constraints);
    }

    private void makeQualityStepper(GridBagConstraints constraints){
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0,-127,128,5);
        workQualityStepper = new JSpinner(numberModel);
        workQualityStepper.setPreferredSize(new Dimension(500,30));

        commandTab.add(workQualityStepper, constraints);
    }

    private void addEmployee(){
        Employee employee = new Employee();
        String name = nameField.getText();
	    String profession = professionComboBox.getSelectedItem().toString();
	    int salary = salarySlider.getValue();
        AttitudeToBoss attitudeToBoss = AttitudeToBoss.DEFAULT;
        pathToProperties = "src/resources/resourceBundles/Language_" + locale + ".xml";
        try{
            fileInputStream = new FileInputStream(pathToProperties);
            prop.loadFromXML(fileInputStream);
            String high = prop.getProperty("high");
            String hate = prop.getProperty("hate");
            String normal = prop.getProperty("normal");
            String low = prop.getProperty("low");
            if (selectedRadio.equals(hate)){
                attitudeToBoss = AttitudeToBoss.HATE;
            }
            if (selectedRadio.equals(high)){
                attitudeToBoss = AttitudeToBoss.HIGH;
            }
            if (selectedRadio.equals(normal)){
                attitudeToBoss = AttitudeToBoss.NORMAL;
            }
            if (selectedRadio.equals(low)){
                attitudeToBoss = AttitudeToBoss.LOW;
            }

        int wQ = (int) workQualityStepper.getValue();
        byte workQuality = (byte)wQ;
        if (classList.getSelectedValue().equals(prop.getProperty("employee"))) {
            employee = new Employee(name, profession, salary, attitudeToBoss, workQuality);
        }
        else
        if (classList.getSelectedValue().equals(prop.getProperty("fWorker"))) {
            System.out.println(classList.getSelectedValue());
            employee = new FactoryWorker(name, profession, salary, attitudeToBoss, workQuality);
        }
        else{
            employee = new ShopAssistant(name, profession, salary, attitudeToBoss, workQuality);
        }
        } catch (IOException e) {
        System.out.println("Ошибка: файл " + pathToProperties + " не обнаружен");
        e.printStackTrace();
    }
        employee.setAvatarPath(avatarPath);
        employee.setNotes(notes.getText());
        if (deletedId == -1) {
	        employee.setID(nextId);
	        nextId++;
        } else {
        	employee.setID(deletedId);
        	deletedId = -1;
        }
	    imf.add(employee);
    }

    private void setBackground() {
        Background background = new Background(new ImageIcon(System.getProperty("user.dir") +
                "/src/resources/images/background.png").getImage());

        getContentPane().add(background);
    }

    private void setMenu() {
        menu = new JMenu("File");

        Font font = new Font(fontName, Font.PLAIN, 14);

        menu.setFont(font);

        settings = new JMenu("Settings");
        settings.setFont(font);
        menu.add(settings);

        hotKeysItem = new JMenuItem("Hot keys");
        hotKeysItem.setFont(font);
        hotKeysItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HotKeys dialog = new HotKeys();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int locationX = (screenSize.width - dialog.getSize().width) / 2;
                int locationY = (screenSize.height - dialog.getSize().height) / 2;
                dialog.setBounds(locationX, locationY, dialog.getSize().width, dialog.getSize().height);//по центру экрана

                dialog.setResizable(false);

                dialog.pack();
                dialog.setVisible(true);
            }
        });
        menu.add(hotKeysItem);

        buttonColorItem = new JMenuItem("Button color");
        buttonColorItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               colorChooser = new JColorChooser();
               JFrame frame = new JFrame("Choose Color");
               JPanel panel = new JPanel();

               colorChooser.setPreferredSize(new Dimension(700,400));
               panel.add(colorChooser);

               JButton okButton = new JButton("OK");
               okButton.setPreferredSize(new Dimension(50,40));
               okButton.addActionListener(new java.awt.event.ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent ev) {

                       //TODO: make normal color change, with every StandardButton
                       //clearButton.setBackground(colorChooser.getColor());
                       standardValuesButton.setBackground(colorChooser.getColor());
                       //clearButton.setBackground(colorChooser.getColor());
                       frame.dispose();
                   }
               });
               panel.add(okButton);

               frame.add(panel);
               frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
               frame.setVisible(true);
               frame.setLocationRelativeTo(null);
               frame.pack();
            }
        });
        buttonColorItem.setFont(font);
        settings.add(buttonColorItem);

        standardAvatarItem = new JMenuItem("Standard avatar");
        standardAvatarItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String avatarStandardPath = System.getProperty("user.dir") +
                        "/src/resources/images/standardAvatar.jpg";
                avatarPath = System.getProperty("user.dir") +
                        "/src/resources/images/standardAvatar.jpg";
                try {
                    Image avatarImage = ImageIO.read(new File(avatarStandardPath));
                    avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
                    avatar.setBackground(new Color(0,0,0,0));
                } catch (IOException ex) {
                    //TODO: and what?
                }
            }
        });
        standardAvatarItem.setFont(font);
        settings.add(standardAvatarItem);

        language = new JMenu("Language");
        settings.setFont(font);
        menu.add(language);

        setLanguagePanel(language);

        menu.addSeparator();

        saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        saveItem.addActionListener((ActionEvent e) -> imf.save());
        menu.add(saveItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void setLanguagePanel(JMenu menu) {
        JMenuItem eng = new JRadioButtonMenuItem("English");
        JMenuItem ru = new JRadioButtonMenuItem("Russian");
        JMenuItem de = new JRadioButtonMenuItem("German");

        Font font = new Font(fontName, Font.PLAIN, 14);
        eng.setFont(font);
        ru.setFont(font);
        de.setFont(font);

        ButtonGroup bg = new ButtonGroup();
        bg.add(eng);
        bg.add(ru);
        bg.add(de);

        menu.add(eng);
        menu.add(ru);
        menu.add(de);

        eng.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("eng", "UK");
                setLoc(locale);
                getTable().updateUI();
            }
        });
        ru.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("ru", "RU");
                setLoc(locale);
                getTable().updateUI();
            }
        });
        de.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("de", "DE");
                setLoc(locale);
                getTable().updateUI();
            }
        });
        eng.setSelected(true);

    }

    private void setDefaultCommandTab(){
        avatar.setIcon(new ImageIcon(System.getProperty("user.dir") +
                "/src/resources/images/standardAvatar.jpg"));
        notes.setText("Здесь можно вводить заметки");
        nameField.setText("Name");
        classList.setSelectedIndex(0);
        salarySlider.setValue(20000);
        radio[2].setSelected(true);
        workQualityStepper.setValue(0);
    }

    private void setLoc(Locale locale) {
        //        Locale locale1 = new Locale("ru", "RU");
        //        ResourceBundle bundle = ResourceBundle.getBundle("Language",
        //                new XMLResourceBundleControl());
        //         menu.setText(bundle.getString("file"));
        String pathToProperties = "src/resources/resourceBundles/Language_" + locale + ".xml";
        FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(pathToProperties);
                prop.loadFromXML(fileInputStream);
                menu.setText(prop.getProperty("file"));
                notes.setText(prop.getProperty("notes"));
                nameField.setText(prop.getProperty("name"));
                tabbedPane.setTitleAt(0, prop.getProperty("table"));
                tabbedPane.setTitleAt(1, prop.getProperty("commands"));
                settings.setText(prop.getProperty("settings"));
                hotKeysItem.setText(prop.getProperty("hotKeys"));
                buttonColorItem.setText(prop.getProperty("butColor"));
                standardAvatarItem.setText(prop.getProperty("stAvatar"));
                saveItem.setText(prop.getProperty("save"));
                language.setText(prop.getProperty("language"));
                standardValuesButton.setText(prop.getProperty("stValues"));
                int i = professionComboBox.getSelectedIndex();
                professionComboBox.removeAllItems();
                professionComboBox.insertItemAt(prop.getProperty("programmer"), 0);
                professionComboBox.insertItemAt(prop.getProperty("economist"), 1);
                professionComboBox.insertItemAt(prop.getProperty("manager"), 2);
                professionComboBox.setSelectedIndex(i);
                i = classList.getSelectedIndex();
                classList.setListData(new String[]{prop.getProperty("employee"),
                        prop.getProperty("fWorker"), prop.getProperty("shAssistant")});
                classList.setSelectedIndex(i);
                String[] radioName = {prop.getProperty("hate"), prop.getProperty("low"),
                        prop.getProperty("default"), prop.getProperty("normal"), prop.getProperty("high")};
                for (int j = 0; j < 5; j++){
                    radio[j].setText(radioName[j]);
                }
                tableTab.setClearButtonText(prop.getProperty("clearTable"));
                tableTab.setSearchFieldText(prop.getProperty("search"));
//                tableTab.setTableText(new String[] {}); ToDo переименовать шапку
            } catch (IOException e) {
                System.out.println("Ошибка: файл " + pathToProperties + " не обнаружен");
                e.printStackTrace();
            }
    getTable().updateUI();
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    static String getFontName() {
        return fontName;
    }

    public static JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public static void setBg(String attitude){
        int i = 0;
        switch (attitude){
            case "LOW" : {
                i = 1;
                break;
            }
            case "DEFAULT" : {
                i = 2;
                break;
            }
            case "NORMAL" : {
                i = 3;
                break;
            }
            case "HIGH" : {
                i = 4;
                break;
            }
        }

        for (Enumeration<AbstractButton> buttons = bg.getElements();
             buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (i == 0) {
                button.setSelected(true);
                break;
            }
            i--;
        }
    }

    public static void setProfessionComboBox(String profession){
        Integer index = 0;
        switch (profession){
            case "Manager" : {
                index = 2;
                break;
            }
            case "Economist" : {
                index = 1;
                break;
            }
            case "Programmer" : {
                index = 0;
                break;
            }
        }
        professionComboBox.setSelectedIndex(index);//TODO: might produce NULLPOINTEREXCEPTION
    }

    public static void setClassList(String className){
        Integer index = null;
        switch (className){
            case "Employee" : {
                index = 0;
                break;
            }
            case "FactoryWorker" : {
                index = 1;
                break;
            }
            case "ShopAssistant" : {
                index = 2;
                break;
            }
        }
        classList.setSelectedIndex(index);//TODO: might produce NULLPOINTEREXCEPTION
    }

    public static void setWorkQualityStepper(byte quality){
        workQualityStepper.setValue((int)quality);
    }

    public static void setSalarySlider(int salary){
        salarySlider.setValue(salary);
    }

    public static void setNameField(String name){
        nameField.setText(name);
    }

    public static void setNotes(String note){
        notes.setText(note);
    }

//    public void setUsingBD(boolean usingBD) {
//        this.usingBD = usingBD;
//    }

	public void setNextId(long nextId) {
    	this.nextId = nextId;
	}

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
