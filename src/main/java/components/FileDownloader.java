package components;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends Component {

    private String url;
    private String target;
    private String fileExtension;
    private int oCurrentProgress;
    private int downloadIndex;
    private String fullPath;
    private String fileName;
    private boolean isDownloadOver = false;

    public FileDownloader() {

    }

    public FileDownloader(String url, String target, String fileExtension, int downloadIndex, String fileName) {
        this.url = url;
        this.target = target;
        this.fileExtension = fileExtension;
        this.downloadIndex = downloadIndex;
        this.fileName = fileName;
    }

    public void startDownload() {
        fullPath = new String(target + fileExtension);
        oCurrentProgress = 0;

        Runnable updatethread = new Runnable() {
            public void run() {
                try {
                    String sUrl = url;
                    String sTarget = target;
                    String sFileExtension = fileExtension;
                    URL url = new URL(sUrl);
                    HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                    long completeFileSize = httpConnection.getContentLength();

                    java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(sTarget + sFileExtension);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(
                            fos, 1024);
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;
                        // calculate progress
                        final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100d);
                        // update progress bar
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                oCurrentProgress = currentProgress;
                            }
                        });
                        bout.write(data, 0, x);
                    }
                    oCurrentProgress = 100;
                    isDownloadOver = true;
                    bout.close();
                    in.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        };
        new Thread(updatethread).start();
    }

    public boolean isDownloadOver() {
        return isDownloadOver;
    }

    public int getoCurrentProgress() {
        return oCurrentProgress;
    }

    public String getFullPath() {
        return fullPath;
    }

    public int getDownloadIndex() {
        return downloadIndex;
    }

    public String getFileName() {
        return fileName;
    }
}
