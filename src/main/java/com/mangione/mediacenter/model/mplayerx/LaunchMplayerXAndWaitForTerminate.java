package com.mangione.mediacenter.model.mplayerx;

import com.mangione.mediacenter.model.process.BlockingExec;
import com.mangione.mediacenter.model.videofile.VideoFile;

public class LaunchMplayerXAndWaitForTerminate extends BlockingExec {

    public static LaunchMplayerXAndWaitForTerminate launchPlayerAndWait(VideoFile videoFile) {
        new KillMplayerX();
        return new LaunchMplayerXAndWaitForTerminate(videoFile);
    }

    public LaunchMplayerXAndWaitForTerminate(VideoFile videoFile) {
        super(videoFile.getLaunchMovieCommand(), false);
    }

    @Override
    protected void processFinished(Process proces, String[] output, String[] error) {
        final int[] numberOfMPlayersRunning = {2};
        boolean running = true;

        while (numberOfMPlayersRunning[0] > 0 && running) {
            new BlockingExec(new String[]{"ps",  "-e"}) {
                @SuppressWarnings({"EmptyCatchBlock"})
                @Override
                protected void processFinished(Process process, String[] output, String[] error) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                    numberOfMPlayersRunning[0] = 0;
                    for (String anOutput : output) {
                        numberOfMPlayersRunning[0] += anOutput.toUpperCase().contains("VLC") ? 1 : 0;
                    }
                }
            };

            running = numberOfMPlayersRunning[0] > 0;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //ignore
            }
        }

        System.out.println("VLC terminated.");
    }

}