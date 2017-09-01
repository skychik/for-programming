package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static ru.ifmo.cs.programming.lab6.core.MyTableTab.getTable;

/**
 * Created by саша on 01.09.2017.
 */

public class MyMenu extends JMenu{
    private InteractiveModeFunctions imf;
    private JMenu settings;
    private JMenuItem hotKeysItem;
    private JMenuItem buttonColorItem;
    private JMenuItem standardAvatarItem;
    private JMenuItem saveItem;
    private JMenu language;
    private JColorChooser colorChooser;
    private String[] prof;
    private static Locale locale = new Locale("eng", "UK");
    private static Properties prop = new Properties();
    private String fontName = "Gill Sans MT Bold Condensed";

    public MyMenu(InteractiveModeFunctions imf){

        this.imf = imf;
        this.setText("File");
        Font font = new Font(fontName, Font.PLAIN, 14);
        this.setFont(font);

        settings = new JMenu("Settings");
        settings.setFont(font);
        this.add(settings);

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
        this.add(hotKeysItem);

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
                        CommandTab.setButtonColor(colorChooser.getColor());
                        MyTableTab.setButtonColor(colorChooser.getColor());
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

        standardAvatarItem = new JMenuItem(prop.getProperty("stAvatar"));
        standardAvatarItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String avatarStandartPath = System.getProperty("user.dir") +
                        "/src/resources/images/standardAvatar.jpg";
                CommandTab.setAvatarPath(System.getProperty("user.dir") +
                        "/src/resources/images/standardAvatar.jpg");
                try {
                    Image avatarImage = ImageIO.read(new File(avatarStandartPath));
                    CommandTab.getAvatar().setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
                    CommandTab.getAvatar().setBackground(new Color(0,0,0,0));
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        standardAvatarItem.setFont(font);
        settings.add(standardAvatarItem);

        language = new JMenu("Language");
        settings.setFont(font);
        this.add(language);

        setLanguagePanel(language);

        this.addSeparator();

        saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        saveItem.addActionListener((ActionEvent e) -> {
            try {
                imf.save();
            } catch (IOException e1) {
                imf.exit();
            }
        });
        this.add(saveItem);

        try {
            setLoc(locale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLanguagePanel(JMenu menu) {
        FileInputStream fileInputStream;
        String pathToProperties = "src/resources/resourceBundles/Language_" + locale + ".xml";
        try {
            fileInputStream = new FileInputStream(pathToProperties);
            prop.loadFromXML(fileInputStream);
        } catch (IOException e) {
            System.out.println("Ошибка: файл " + pathToProperties + " не обнаружен");
            e.printStackTrace();
        }
        JMenuItem eng = new JRadioButtonMenuItem(prop.getProperty("eng"));
        JMenuItem ru = new JRadioButtonMenuItem(prop.getProperty("ru"));
        JMenuItem de = new JRadioButtonMenuItem(prop.getProperty("de"));
        JMenuItem no = new JRadioButtonMenuItem(prop.getProperty("norw"));

        Font font = new Font(fontName, Font.PLAIN, 14);
        eng.setFont(font);
        ru.setFont(font);
        de.setFont(font);
        no.setFont(font);

        ButtonGroup bg = new ButtonGroup();
        bg.add(eng);
        bg.add(ru);
        bg.add(de);
        bg.add(no);

        menu.add(eng);
        menu.add(ru);
        menu.add(de);
        menu.add(no);

        eng.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("eng", "UK");
                try {
                    setLoc(locale);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().updateUI();
            }
        });
        ru.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("ru", "RU");
                try {
                    setLoc(locale);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().updateUI();
            }
        });
        de.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("de", "DE");
                try {
                    setLoc(locale);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().updateUI();
            }
        });
        no.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("no", "No", "B");
                try {
                    setLoc(locale);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().updateUI();
            }
        });
        eng.setSelected(true);

    }

    public static Properties getProp(){
        return prop;
    }

    private void setLoc(Locale locale) throws IOException {
        //        Locale locale1 = new Locale("ru", "RU");
        //        ResourceBundle bundle = ResourceBundle.getBundle("Language",
        //                new XMLResourceBundleControl());
        //         menu.setText(bundle.getString("file"));
        String pathToProperties = "src/resources/resourceBundles/Language_" + locale + ".xml";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(pathToProperties);
            prop.loadFromXML(fileInputStream);
            this.setText(prop.getProperty("file"));
            if (CommandTab.getNotes().equals("") || CommandTab.getNotes().equals(prop.getProperty("notes")))
            CommandTab.setNotes(prop.getProperty("notes"));
            if (CommandTab.getNameField().equals("") || CommandTab.getNameField().equals(prop.getProperty("notes")))
            CommandTab.setNameField(prop.getProperty("name"));
            MainFrame.getTabbedPane().setTitleAt(0, prop.getProperty("table"));
            MainFrame.getTabbedPane().setTitleAt(1, prop.getProperty("commands"));
            settings.setText(prop.getProperty("settings"));
            hotKeysItem.setText(prop.getProperty("hotKeys"));
            buttonColorItem.setText(prop.getProperty("butColor"));
            standardAvatarItem.setText(prop.getProperty("stAvatar"));
            saveItem.setText(prop.getProperty("save"));
            language.setText(prop.getProperty("language"));
            CommandTab.setStandardValuesButton(prop.getProperty("stValues"));
            int j = CommandTab.getProfessionComboBox().getSelectedIndex();
            CommandTab.getProfessionComboBox().removeAllItems();
            CommandTab.getProfessionComboBox().insertItemAt(prop.getProperty("programmer"), 0);
            CommandTab.getProfessionComboBox().insertItemAt(prop.getProperty("economist"), 1);
            CommandTab.getProfessionComboBox().insertItemAt(prop.getProperty("manager"), 2);
            CommandTab.getProfessionComboBox().setSelectedIndex(j);
            int i = CommandTab.getClassList().getSelectedIndex();
            CommandTab.getClassList().setListData(new String[]{prop.getProperty("employee"),
                    prop.getProperty("fWorker"), prop.getProperty("shAssistant")});
            CommandTab.getClassList().setSelectedIndex(i);
            String[] radioName = {prop.getProperty("hate"), prop.getProperty("low"),
                    prop.getProperty("default"), prop.getProperty("normal"), prop.getProperty("high")};
            for (int k = 0; k < 5; k++){
                CommandTab.getRadio()[k].setText(radioName[k]);
            }
            MyTableTab.setClearButtonText(prop.getProperty("clearTable"));
            MyTableTab.setSearchFieldText(prop.getProperty("search"));
            MyTableTab.setClearQ(prop.getProperty("clearQ"));
            MyTableTab.setConfirmation(prop.getProperty("confirmation"));
            String[] options = {prop.getProperty("yes"), prop.getProperty("no")};
            MyTableTab.setOptions(options);
//            MyTableTab.setTableText(new String[] {}); //ToDo переименовать шапку
        } catch (IOException e) {
            System.out.println("Ошибка: файл " + pathToProperties + " не обнаружен");
            e.printStackTrace();
        }
        getTable().updateUI();
    }

    public static Locale getSelectedLocale(){
        return locale;
    }

}
