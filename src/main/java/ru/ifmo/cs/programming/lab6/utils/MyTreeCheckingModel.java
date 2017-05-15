package ru.ifmo.cs.programming.lab6.utils;

import eu.floraresearch.lablib.gui.checkboxtree.DefaultTreeCheckingModel;

import javax.swing.tree.TreeModel;

public class MyTreeCheckingModel extends DefaultTreeCheckingModel {

    public MyTreeCheckingModel(TreeModel model) {
        super(model);
    }
}
