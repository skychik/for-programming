package ru.ifmo.cs.programming.lab6.core;

import org.apache.log4j.helpers.DateTimeDateFormat;
import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab6.utils.Clock;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private JMenuItem reloadItem;
    private JMenu language;
    private JColorChooser colorChooser;

    private static Locale locale = new Locale("eng", "UK");
    private FileInputStream fileInputStream;
    private static Properties prop = new Properties();
    private String fontName = "Gill Sans MT Bold Condensed";
    private static String pathToProperties = "src/resources/resourceBundles/Language_" + getMyLocale() + ".xml";

    public static Locale getMyLocale() {
        return locale;
    }

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

//	    reloadItem = new JMenuItem("Save");
//	    reloadItem.setFont(font);
//	    reloadItem.addActionListener((ActionEvent e) -> {
//		    // TODO: reload
//	    });
//	    this.add(reloadItem);

        saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        saveItem.addActionListener((ActionEvent e) -> imf.save());
        this.add(saveItem);

        try {
            setLoc();
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
        JMenuItem jp = new JRadioButtonMenuItem(prop.getProperty("jp"));
        JMenuItem rs = new JRadioButtonMenuItem(prop.getProperty("rs"));
        JMenuItem bulg = new JRadioButtonMenuItem(prop.getProperty("bg"));

        Font font = new Font(fontName, Font.PLAIN, 14);
        eng.setFont(font);
        ru.setFont(font);
        jp.setFont(font);
        rs.setFont(font);
        bulg.setFont(font);

        ButtonGroup bg = new ButtonGroup();
        bg.add(eng);
        bg.add(ru);
        bg.add(jp);
        bg.add(rs);
        bg.add(bulg);

        menu.add(eng);
        menu.add(ru);
        menu.add(jp);
        menu.add(rs);
        menu.add(bulg);

        eng.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("eng", "UK");
                try {
                    setLoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().revalidate();
                getTable().repaint();
            }
        });
        ru.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("ru", "RU");
                try {
                    setLoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().revalidate();
                getTable().repaint();
            }
        });
        jp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("jp", "JP");
                try {
                    setLoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().revalidate();
                getTable().repaint();
            }
        });
        rs.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("rs", "RS");
                try {
                    setLoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().revalidate();
                getTable().repaint();
            }
        });
        bulg.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                locale = new Locale("bg", "BG");
                try {
                    setLoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                getTable().revalidate();
                getTable().repaint();
            }
        });

        eng.setSelected(true);
    }

    public static Properties getProp(){
        return prop;
    }

    private void setLoc() throws IOException {
        //        Locale locale1 = new Locale("ru", "RU");
        //        ResourceBundle bundle = ResourceBundle.getBundle("Language",
        //                new XMLResourceBundleControl());
        //         menu.setText(bundle.getString("file"));
        try {
            pathToProperties = "src/resources/resourceBundles/Language_" + getMyLocale() + ".xml";
            fileInputStream = new FileInputStream(pathToProperties);
            prop.loadFromXML(fileInputStream);
            this.setText(prop.getProperty("file"));
            if (CommandTab.isIsNotesChanged() == false)
            CommandTab.setNotes(prop.getProperty("notes"));
            if (CommandTab.isIsNameChanged() == false)
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

        Clock.setDateFormat(DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                DateFormat.DEFAULT, getMyLocale()));

            String[] options = {prop.getProperty("yes"), prop.getProperty("no")};
            MyTableTab.setOptions(options);
        } catch (IOException e) {
            System.out.println("Ошибка: файл " + pathToProperties + " не обнаружен");
            e.printStackTrace();
        }
        getTable().revalidate();
        getTable().repaint();
    }

    public static Locale getSelectedLocale(){
        return locale;
    }

}
