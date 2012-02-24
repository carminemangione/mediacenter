package com.mangione.mediacenter.model.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockingExec {

    public BlockingExec(final String command) {
        final List<String> outputLines = new ArrayList<String>();
        final Process[] process = {null};
        final Thread execThread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("command = " + command);
                    process[0] = Runtime.getRuntime().exec(command);
                    BufferedReader buffrdr = new BufferedReader(new InputStreamReader(process[0].getInputStream()));

                    String nextOutputLine;
                    while ((nextOutputLine = buffrdr.readLine()) != null) {
                        outputLines.add(nextOutputLine);
                    }
                    process[0].waitFor();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        execThread.start();
        try {
            execThread.join();
            processFinished(process[0], outputLines.toArray(new String[outputLines.size()]));
        } catch (InterruptedException e) {
            //ignore
        }
    }

    protected abstract void processFinished(Process proces, String[] output);
}
