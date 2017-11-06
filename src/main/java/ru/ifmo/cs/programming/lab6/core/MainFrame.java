package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab6.utils.Background;
import ru.ifmo.cs.programming.lab6.utils.Clock;
import ru.ifmo.cs.programming.lab6.utils.MyColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private InteractiveModeFunctions imf;
//    private boolean usingBD = false;
    private JMenu menu;
    private Background mainPanel;
    private static JTabbedPane tabbedPane;
    private JPanel commandTab;
    JPanel panel;
    private Dimension size;
//    private ArrayDeque<Employee> deque;
    private MyTableTab tableTab;
    private GridBagConstraints constraints;
    private JPanel clockPanel;

    static String fontName = "Gill Sans MT Bold Condensed";

    public MainFrame(InteractiveModeFunctions imf) {
        super("CRUD application");

        this.imf = imf;

        size = new Dimension(1200, 800);
        setPreferredSize(size);

        setIconImage(new ImageIcon(
                System.getProperty("user.dir") + "/src/resources/images/icon.png").getImage());

//        this.deque = deque;

        UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
        UIManager.put("TabbedPane.tabsOpaque", Boolean.TRUE);
        UIManager.put("TabbedPane.selected", new Color(152, 156, 153, 96));

        setMainPanel();
        setContentPane(mainPanel);

        menu = new MyMenu(imf);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Диалоговое окно для подтвеждения закрытия программы
        addWindowListener (new WindowAdapter() {
                                 @Override
                                 public void windowClosing(WindowEvent e) {
                 // Потверждение закрытия окна JFrame
                 Object[] options = { MyMenu.getProp().getProperty("yes"), MyMenu.getProp().getProperty("no") };
                 int n = JOptionPane.showOptionDialog(e.getWindow(), MyMenu.getProp().getProperty("closeW"),
                         MyMenu.getProp().getProperty("confirmation"), JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                 if (n == 0) {
                     e.getWindow().setVisible(false);
                     imf.exit();
                 }
             }
         });
    }

    private void setMainPanel() {
//        constraints = new com.intellij.uiDesigner.core.GridConstraints(
//                2, 1, 1, 1,
//                GridConstraints.ANCHOR_NORTH,
//                com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
//                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
//                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//                null,
//                new Dimension(200, 200),
//                null,
//                0,
//                false
//        );
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 130;
        constraints.ipadx = 1200;
        constraints.gridy = 0;
        constraints.gridx = 0;
        mainPanel = new Background(new ImageIcon(System.getProperty("user.dir") +
                "/src/resources/images/background.png").getImage());
        OverlayLayout overlayLayout = new OverlayLayout(mainPanel);
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setOpaque(false);
        mainPanel.setLayout(overlayLayout);
        setTabbedPane();
        panel.add(
                tabbedPane,
                constraints
        );

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 1200;
        constraints.ipady = 30;
        Clock clock = new Clock();
        clock.setBackground(MyColor.backgroundEighthAlphaColor);
        panel.add(clock,
                constraints
        );
        mainPanel.add(panel);

    }

    private void setTabbedPane() {
        tabbedPane = new JTabbedPane();
        /*tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
            protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
        });*/

        tableTab = new MyTableTab(imf);
        tabbedPane.addTab("Table", tableTab);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        commandTab = new CommandTab(imf);
        tabbedPane.addTab("Commands", commandTab);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //Прозрачность фона вкладок
//        tabbedPane.setOpaque(false);
//        tabbedPane.putClientProperty("TabbedPane.contentOpaque", Boolean.FALSE);
//        tabbedPane.putClientProperty("TabbedPane.tabsOpaque", Boolean.FALSE);

        //Шрифт
        tabbedPane.setFont(new Font(fontName, Font.PLAIN, 18));

        //Цвет
        tabbedPane.setForeground(new Color(152, 156, 153));
        tabbedPane.setBackground(MyColor.backgroundEighthAlphaColor);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public static JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public Dimension getSize() {
        return size;
    }

    static String getFontName() {
        return fontName;
    }

    //    /*private void makeSaveButton(GridBagConstraints constraints) {
    //        StandardButton saveButton = new StandardButton("Save");
    //
    //        //saveButton.setForeground(Color.BLACK);
    ////        saveButton.setBackground(new Color(152, 156, 153, 32));
    //        //saveButton.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 15, 5, 15)));
    //        //saveButton.setBorderPainted(false);
    //        saveButton.setBackground(MyColor.backgroundEighthAlphaColor);
    //
    //        saveButton.addActionListener(e -> save(AppGUI.getDeque()));
    //
    //        //tableTab.add(saveButton, constraints);
    //    }*/

    //    public void setUsingBD(boolean usingBD) {
    //        this.usingBD = usingBD;
    //    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
