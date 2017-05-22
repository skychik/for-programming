package ru.ifmo.cs.programming.lab6.utils;

import eu.floraresearch.lablib.gui.checkboxtree.DefaultCheckboxTreeCellRenderer;
import ru.ifmo.cs.programming.lab6.App;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class MyCheckBoxTreeCellRenderer extends DefaultCheckboxTreeCellRenderer {
    MyCheckBoxTreeCellRenderer() {
        super();
        this.label.setBackgroundNonSelectionColor(MyColor.whiteTextColor);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        label.setForeground(MyColor.whiteTextColor);

        return c;
    }
}
