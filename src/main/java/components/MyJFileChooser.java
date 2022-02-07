package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyJFileChooser extends JPanel {

    JButton inputFile;
    JButton outputFolder;
    JLabel inputFileLabel;
    JLabel outputDirLabel;
    JFileChooser chooser;

    String choosertitle;
    String inputFilePath = new String("No File Selected...");
    String outputFileDir = new String("No Directory Selected...");

    public FileDownloader fileDownloader;

    public MyJFileChooser() {
        inputFile = new JButton("Select Input File");
        outputFolder = new JButton("Select Output Folder");
        inputFileLabel = new JLabel("Input File: " + inputFilePath);
        outputDirLabel = new JLabel("Output Dir: " + outputFileDir);
        outputFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e, 1);
            }
        });
        inputFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile(e, 2);
            }
        });
        setLayout(new GridLayout(2, 2));
        add(inputFile);
        add(inputFileLabel);
        add(outputFolder);
        add(outputDirLabel);
    }

    public void chooseFile(ActionEvent e, int mode) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        if (mode == 1) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(true);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (mode == 1) {
                System.out.println("getCurrentDirectory(): "
                        + chooser.getSelectedFile());
                outputFileDir = new String(chooser.getSelectedFile().toString());
                outputDirLabel.setText("Output Dir: " + outputFileDir);
            } else {
                System.out.println("getSelectedFile() : "
                        + chooser.getSelectedFile());
                inputFilePath = new String(chooser.getSelectedFile().toString());
                inputFileLabel.setText("Input File: " + inputFilePath);
            }
        } else {
            System.out.println("No Selection ");
        }
    }

    public String getOutputFileDir() {
        return outputFileDir;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }
}
