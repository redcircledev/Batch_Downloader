package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private File currentFile;
    private ArrayList<String> names;
    private ArrayList<String> urls;
    private ArrayList<String> fileExtensions;

    public FileReader(String filePath) {
        try {
            this.currentFile = new File(filePath);
            this.names = new ArrayList<String>();
            this.urls = new ArrayList<String>();
            this.fileExtensions = new ArrayList<String>();
            Scanner myReader = new Scanner(this.currentFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String id = data.substring(0, 6);
                System.out.println(id);
                switch (id) {
                    case "#EXTM3":
                        //We just ignore this case
                        System.out.println("this is just the header: " + data);
                        break;
                    case "#EXTIN":
                        // This cointains the name of the final file
                        System.out.println("this is the title: " + data.substring(11));
                        names.add(data.substring(11));
                        break;
                    case "http:/":
                        //This is the url
                        System.out.println("this is the url: " + data);
                        int indexOfFileExt = data.lastIndexOf(".");
                        System.out.println("this is the file extension: " + data.substring(indexOfFileExt));
                        fileExtensions.add(data.substring(indexOfFileExt));
                        urls.add(data);
                        break;
                    default:
                        break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ArrayList<String> getFileExtensions() {
        return fileExtensions;
    }
}
