package com.mangione.mediacenter.model;

import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;

import java.io.*;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:43:50 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class MediaPlayer {
    private Thread thread;
    private String applicationName;
    private MediaCenterControllerInterface mediaCenterControllerInterface;

    public MediaPlayer(final String[] command,
                       final String applicationName,
                       final MediaCenterControllerInterface mediaCenterControllerInterface) {
        this.applicationName = applicationName;
        this.mediaCenterControllerInterface = mediaCenterControllerInterface;
        new Thread() {
            public void run() {
                try {
                    launchCommand(command);
                    Thread.sleep(1000);
                    File js = writeJavaScript();
                    giveMediaPlayerFocus(new String[]{"osascript", js.getPath()});
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void giveMediaPlayerFocus(final String[] command) throws IOException {
        launchCommand(command);
    }


    private File writeJavaScript() throws IOException {
        File tempFile = File.createTempFile("activatevlc", "scpt");
        tempFile.deleteOnExit();
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        StringBuffer sb = new StringBuffer("tell application \"" + applicationName + "\" \n activate \n end tell ");
        bw.write(sb.toString());
        bw.newLine();
        bw.flush();
        bw.close();
        return tempFile;
    }

    private Thread launchCommand(final String[] command) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(command);
                    InputStream istrm = process.getInputStream();
                    InputStreamReader istrmrdr = new InputStreamReader(istrm);
                    BufferedReader buffrdr = new BufferedReader(istrmrdr);

                    String data;
                    while ((data = buffrdr.readLine()) != null) {
                        System.out.println(data);
                    }
                    process.waitFor();
                    mediaCenterControllerInterface.playerFinishedPlaying();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        return thread;
    }

}
