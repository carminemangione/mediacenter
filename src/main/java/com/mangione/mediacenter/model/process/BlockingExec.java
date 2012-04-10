package com.mangione.mediacenter.model.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockingExec {

    public BlockingExec(final String command) {
        final List<String> outputLines = new ArrayList<String>();
        final List<String> errorLines = new ArrayList<String>();
        final Process[] process = {null};
        final Thread execThread = new Thread() {
            @Override
            public void run() {
                try {
                    process[0] = Runtime.getRuntime().exec(command);
                    final InputStream inputStream = process[0].getInputStream();
                    Thread outputThread = startThreadToRead(inputStream, outputLines);
                    Thread errorThread = startThreadToRead(inputStream, errorLines);

                    process[0].waitFor();
                    outputThread.join();
                    errorThread.join();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        execThread.start();
        try {
            execThread.join();
            processFinished(process[0], outputLines.toArray(new String[outputLines.size()]),
                    errorLines.toArray(new String[errorLines.size()]));
        } catch (InterruptedException e) {
            //ignore
        }
    }

    private Thread startThreadToRead(final InputStream inputStream, final List<String> outputLines) {
        Thread outputThread = new Thread() {
            public void run() {
                BufferedReader buffrdr = new BufferedReader(new InputStreamReader(inputStream));

                String nextOutputLine;
                try {
                    while ((nextOutputLine = buffrdr.readLine()) != null) {
                        outputLines.add(nextOutputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        outputThread.start();
        return outputThread;
    }

    protected abstract void processFinished(Process proces, String[] output, String[] error);
}
