package com.mangione.mediacenter.model.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BlockingExec {

    private final boolean showOutputAndError;

    public BlockingExec(final String[] command) {
        this(command, false);
    }

    public BlockingExec(final String[] command, final boolean showOutputAndError) {
        this.showOutputAndError = showOutputAndError;
        final List<String> outputLines = new ArrayList<String>();
        final List<String> errorLines = new ArrayList<String>();
        final Process[] process = {null};
        final Thread execThread = new Thread() {
            @Override
            public void run() {

                try {
                    System.out.println(Arrays.toString(command));

                    process[0] = Runtime.getRuntime().exec(command);
                    final InputStream inputStream = process[0].getInputStream();
                    final InputStream errorStream = process[0].getErrorStream();
                    Thread outputThread = startThreadToRead(inputStream, outputLines);
                    Thread errorThread = startThreadToRead(errorStream, errorLines);

                    outputThread.join();
                    errorThread.join();
                    if (showOutputAndError) {
                        System.out.println("Process output: ");
                        for (String outputLine : outputLines) {
                            System.out.println(outputLine);
                        }
                        System.out.println("Error output: ");
                        for (String errorLine : errorLines) {
                            System.out.println(errorLine);
                        }
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
            processFinished(process[0], outputLines.toArray(new String[outputLines.size()]),
                    errorLines.toArray(new String[errorLines.size()]));
        } catch (InterruptedException e) {
            //ignore
        }
    }

    private Thread startThreadToRead(final InputStream inputStream, final List<String> outputLines) {
        if (showOutputAndError) {
            System.out.println("BlockingExec.startThreadToRead");
        }
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
