package com.mangione.mediacenter.model.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockingExec {

    public BlockingExec(final String command) {
        final List<String> outputLines = new ArrayList<String>();
        final Thread execThread = new Thread() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader buffrdr = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String nextOutputLine;
                    while ((nextOutputLine = buffrdr.readLine()) != null) {
                        outputLines.add(nextOutputLine);
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        execThread.start();
        try {
            execThread.join();
            processFinished(outputLines.toArray(new String[outputLines.size()]));
        } catch (InterruptedException e) {
            //ignore
        }
    }

    protected abstract void processFinished(String[] output);
}
