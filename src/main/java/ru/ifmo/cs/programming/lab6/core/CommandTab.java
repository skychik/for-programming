package ru.ifmo.cs.programming.lab6.core;
//todo - по двойному клику не удаляется; поработать с моментом обновления
import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
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
import java.util.Objects;

import static ru.ifmo.cs.programming.lab6.core.MyTableTab.getTable;
import static ru.ifmo.cs.programming.lab6.utils.MyColor.foregroundColor;
import static ru.ifmo.cs.programming.lab6.utils.MyColor.opaqueColor;

/**
 * Created by саша on 01.09.2017.
 */
public class CommandTab extends JPanel {

    private InteractiveModeFunctions imf;
    private static StandardButton standardValuesButton;

    public static JTextArea getNotes() {
        return notes;
    }

    public static JTextField getNameField() {
        return nameField;
    }

    private static JTextArea notes = null;
    private static JTextField nameField;
    private static JComboBox professionComboBox;
    private static JSlider salarySlider;
    private static ButtonGroup bg;
    private static JRadioButton[] radio;
    private String selectedRadio;
    private static JSpinner workQualityStepper;
    private Dimension size;
    //    private ArrayDeque<Employee> deque;
    private static JButton avatar;
    private static String avatarPath;
    private FileFilterExt eff;
    private JFileChooser fileChooser;
    private static JList<String> classList;
    private JButton okButton;
    private String[] prof;
    private String pathToProperties;
    private FileInputStream fileInputStream;
    private boolean isNameFieldEmpty = true;
    private boolean isNotesAreaEmpty = true;

    static String fontName = "Gill Sans MT Bold Condensed";
    private Font font = new Font(fontName, Font.ITALIC, 13);
    private LineBorder lineBorder = new LineBorder(Color.BLACK,2);

    public CommandTab(InteractiveModeFunctions imf) {

        this.imf = imf;
        this.setOpaque(false);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(gridBagLayout);

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
        this.add(avatar, constraints);
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
                int result = fileChooser.showOpenDialog(getParent());
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
            e.printStackTrace();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(getParent(),
                    "Выбранный файл ( " +
                            fileChooser.getSelectedFile() + " ) не может быть выбран в качестве аватара");
        }
    }

    public static void setAvatar(String path){
        Image avatarImage;
        try {
            avatarImage = ImageIO.read(new File(path));
            avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            avatar.setBackground(new Color(0,0,0,0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeScrollTextArea(GridBagConstraints constraints){
        notes = new JTextArea(MyMenu.getProp().getProperty("notes"), 15,50);
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

        notes.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (isNotesAreaEmpty) notes.setText(null);
            }

            public void focusLost(FocusEvent e) {
                if (Objects.equals(notes.getText(), "")) {
                    notes.setText(MyMenu.getProp().getProperty("notes"));
                    isNotesAreaEmpty = true;
                } else isNotesAreaEmpty = false;
            }
        });

        this.add(scrollNotes, constraints);
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

        this.add(panel, constraints);
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

        this.add(panel, constraints);
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

        nameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (isNameFieldEmpty) nameField.setText(null);
            }

            public void focusLost(FocusEvent e) {
                if (Objects.equals(nameField.getText(), "")) {
                    nameField.setText(MyMenu.getProp().getProperty("name"));
                    isNameFieldEmpty = true;
                } else isNameFieldEmpty = false;
            }
        });

        this.add(nameField, constraints);
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
        this.add(classList, constraints);
    }

    private void makeProfessionComboBox(GridBagConstraints constraints){
        prof = new String[]{"Programmer", "Economist", "Manager"};
        professionComboBox = new JComboBox<>(prof);
        professionComboBox.setForeground(foregroundColor);
        professionComboBox.setFont(font);
        professionComboBox.setPreferredSize(new Dimension(500, 30));

        this.add(professionComboBox, constraints);
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

        this.add(salarySlider, constraints);
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

        this.add(bossAttitudeRadioPanel, constraints);
    }

    private void makeQualityStepper(GridBagConstraints constraints){
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0,-127,128,5);
        workQualityStepper = new JSpinner(numberModel);
        workQualityStepper.setPreferredSize(new Dimension(500,30));

        this.add(workQualityStepper, constraints);
    }

    private void addEmployee(){
        Employee employee = new Employee();
        String name = nameField.getText();
        String profession = professionComboBox.getSelectedItem().toString();
        int salary = salarySlider.getValue();
        AttitudeToBoss attitudeToBoss = AttitudeToBoss.DEFAULT;
        pathToProperties = "src/resources/resourceBundles/Language_"
                + MyMenu.getSelectedLocale() + ".xml";
        try{
            fileInputStream = new FileInputStream(pathToProperties);
            MyMenu.getProp().loadFromXML(fileInputStream);
            String high = MyMenu.getProp().getProperty("high");
            String hate = MyMenu.getProp().getProperty("hate");
            String normal = MyMenu.getProp().getProperty("normal");
            String low = MyMenu.getProp().getProperty("low");
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
            if (classList.getSelectedValue().equals(MyMenu.getProp().getProperty("employee"))) {
                employee = new Employee(name, profession, salary, attitudeToBoss, workQuality);
            }
            else
            if (classList.getSelectedValue().equals(MyMenu.getProp().getProperty("fWorker"))) {
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
        imf.add(employee);
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
        Integer index = null;
//        if (profession.equals(MyMenu.getProp().getProperty("manager"))){index = 2;}
//        if (profession.equals(MyMenu.getProp().getProperty("economist"))){index = 1;}
//        if (profession.equals(MyMenu.getProp().getProperty("programmer"))){index = 0;}
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

    public static void setButtonColor(Color color){
        standardValuesButton.setBackground(color);
    }

    public static JButton getAvatar(){
        return avatar;
    }

    public static JList getClassList(){
        return classList;
    }

    public static JComboBox getProfessionComboBox(){
        return professionComboBox;
    }

    public static void setAvatarPath(String path){
        avatarPath = path;
    }

    public static void setStandardValuesButton(String text){
        standardValuesButton.setText(text);
    }

    public static JRadioButton[] getRadio(){
        return radio;
    }

    private void setDefaultCommandTab(){
        avatar.setIcon(new ImageIcon(System.getProperty("user.dir") +
                "/src/resources/images/standardAvatar.jpg"));
        notes.setText(MyMenu.getProp().getProperty("notes"));
        nameField.setText(MyMenu.getProp().getProperty("name"));
        classList.setSelectedIndex(0);
        professionComboBox.setSelectedIndex(0);
        salarySlider.setValue(20000);
        radio[2].setSelected(true);
        workQualityStepper.setValue(0);
    }
}
