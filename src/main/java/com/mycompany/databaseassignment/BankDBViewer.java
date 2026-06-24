/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseassignment;

/**
 *
 * @author Jameel
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BankDBViewer extends JFrame implements ActionListener {
    private JLabel tableLabel;
    private JLabel totalLabel;
    private JLabel statusLabel;
    private JComboBox<String> tableSelector;
    private JButton loadButton;
    private JButton exitButton;
    private JPanel hostPanel;
    private JTable table;
    private JScrollPane scroller;
    private Vector<String> heads;
    private Vector<Object> row;
    private Vector<Vector<Object>> rows;

    private static DatabaseManager dbManager = new DatabaseManager();
    private static boolean connected = false;

    public static void main(String[] args) {
        try {
            dbManager.connect("BankDB.accdb");
            connected = true;
        } catch (ClassNotFoundException cnfEx) {
            System.out.println("Unable to load driver");
        } catch (java.sql.SQLException sqlEx) {
            System.out.println("Cannot connect to database");
        }
        BankDBViewer frame = new BankDBViewer();
        frame.setSize(700, 400);
        frame.setVisible(true);
        frame.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent winEvent) {
                    try {
                        dbManager.disconnect();
                        System.exit(0);
                    } catch (java.sql.SQLException sqlEx) {
                        System.out.println("Error on closing connection");
                    }
                }
            });
    }

    public BankDBViewer() {
        hostPanel = new JPanel();
        statusLabel = new JLabel("● Not connected");
        if (connected) {
            statusLabel.setText("● Connected");
            statusLabel.setForeground(new Color(0, 160, 0));
        } else {
            statusLabel.setForeground(Color.RED);
        }
        hostPanel.add(statusLabel);
        tableLabel = new JLabel("Select Table:");
        hostPanel.add(tableLabel);
        tableSelector = new JComboBox<String>();
        if (connected) {
            try {
                Vector<String> tableNames = dbManager.getTableNames();
                for (int i = 0; i < tableNames.size(); i++) {
                    tableSelector.addItem(tableNames.get(i));
                }
            } catch (java.sql.SQLException sqlEx) {
                System.out.println("SQL error");
                System.exit(1);
            }
        }
        hostPanel.add(tableSelector);
        totalLabel = new JLabel("total = 0");
        hostPanel.add(totalLabel);
        loadButton = new JButton("Load Table");
        loadButton.addActionListener(this);
        hostPanel.add(loadButton);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        hostPanel.add(exitButton);
        add(hostPanel, BorderLayout.SOUTH);
        setTitle("BankDB Viewer");
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == exitButton) {
            System.exit(0);
        } else if (event.getSource() == loadButton) {
            String selectedTable = (String) tableSelector.getSelectedItem();
            loadTable(selectedTable);
        }
    }

    public void loadTable(String tableName) {
        try {
            int colCount = dbManager.getColumnCount(tableName);
            heads = new Vector<String>();
            for (int i = 1; i <= colCount; i++) {
                heads.add("Column " + i);
            }
            rows = dbManager.getTableData(tableName);
            totalLabel.setText("total = " + rows.size());
            if (scroller != null) {
                remove(scroller);
            }
            table = new JTable(rows, heads);
            scroller = new JScrollPane(table);
            add(scroller, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (java.sql.SQLException sqlEx) {
            System.out.println("SQL error");
            System.exit(1);
        }
    }
}