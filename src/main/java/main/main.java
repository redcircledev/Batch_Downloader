package main;

import components.FileDownloader;
import components.FileReader;
import components.MyJFileChooser;
import components.ProgressCellRender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Models.TableModel.getTableModel;

public class main {

    //Variables
    private static JButton submit;
    private static JButton clear;
    private static ArrayList<JProgressBar> downloadProgress = new ArrayList<JProgressBar>();
    private static ArrayList<JLabel> downloadLabel = new ArrayList<JLabel>();
    private static ArrayList<FileDownloader> fileDownloaderManager = new ArrayList<FileDownloader>();
    private static int currentRows = 1;
    private static JTable table = new JTable();
    private static DefaultTableModel model = getTableModel();

    private static JFrame frame;
    //Primary Panels
    private static JPanel mainPanel;
    //Secondary Panels
    private static MyJFileChooser northPanel;
    private static JPanel centerPanel;
    private static JPanel southPanel;
    private static JScrollPane scroll;

    public static void main(String args[]) {
        initFrame();
    }

    public static void initFrame() {

        //Frame Settings
        frame = new JFrame("Batch_Downloader");
        //Setting up primary Panels
        mainPanel = new JPanel();
        //Setting up secondary Panels
        northPanel = new MyJFileChooser();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        scroll = new JScrollPane(table);
        //Table settings
        table.setModel(model);
        model.addColumn("Name");
        model.addColumn("Path");
        model.addColumn("Progress");
        table.getColumn("Progress").setCellRenderer(new ProgressCellRender());
        //Setting up the buttons
        initButtons();
        //Setting up panel properties
        centerPanel.setLayout(new GridLayout(currentRows, 1));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        southPanel.setLayout(new GridLayout(1,2));

        //Adding everything to the panels
        southPanel.add(submit);
        southPanel.add(clear);
        centerPanel.add(scroll);
        //Adding to main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        //Frame Settings
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        //Adding main panel to frame
        frame.getContentPane().add(mainPanel, "Center");
        frame.pack();
        frame.setVisible(true);
    }

    public static void initButtons() {
        submit = new JButton("Go");
        clear = new JButton("Clear Completed");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileReader fileReader = new FileReader(northPanel.getInputFilePath());
                ArrayList<String> names = fileReader.getNames();
                ArrayList<String> urls = fileReader.getUrls();
                ArrayList<String> fileExtensions = fileReader.getFileExtensions();
                for (int i = 0; i < names.size(); i++) {
                    FileDownloader fileDownloader = new FileDownloader(urls.get(i), northPanel.getOutputFileDir() + "\\" + names.get(i), fileExtensions.get(i), i, names.get(i));
                    fileDownloaderManager.add(fileDownloader);
                }
                ExecutorService pool = Executors.newFixedThreadPool(4);
                for (int i = 0; i < fileDownloaderManager.size(); i++) {
                    FileDownloader currentDownloader = fileDownloaderManager.get(i);
                    int rcurrentRows = i;
                    int rowIndex = i;
                    Runnable r = new Runnable() {
                        public void run() {

                            currentDownloader.startDownload();
                            model.addRow(new Object[]{currentDownloader.getFileName(), currentDownloader.getFullPath(), currentDownloader.getoCurrentProgress()});
                            System.out.println("DownloadIndex is: " + currentDownloader.getDownloadIndex());

                            while (!currentDownloader.isDownloadOver()) {
                                try {
                                    model.setValueAt(currentDownloader.getoCurrentProgress(), currentDownloader.getDownloadIndex(), 2);
                                    Thread.sleep(150);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            model.setValueAt(currentDownloader.getoCurrentProgress(), currentDownloader.getDownloadIndex(), 2);
                        }
                    };
                    pool.submit(r);
                }
                frame.pack();
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> completedDownloads = new ArrayList<Integer>();
                for (int i = 0; i < fileDownloaderManager.size(); i++) {
                    FileDownloader fileDownloader = fileDownloaderManager.get(i);
                    if (fileDownloader.isDownloadOver()) {
                        model.removeRow(fileDownloader.getDownloadIndex());
                        completedDownloads.add(i);
                    }
                }
                for (int i = 0; i < completedDownloads.size(); i++) {
                    fileDownloaderManager.remove(completedDownloads.get(i));
                }
            }
        });
    }

}
