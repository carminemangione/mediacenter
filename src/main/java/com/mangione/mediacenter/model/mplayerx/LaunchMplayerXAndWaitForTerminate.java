package com.mangione.mediacenter.model.mplayerx;

import com.mangione.mediacenter.model.process.BlockingExec;
import com.mangione.mediacenter.model.videofile.VideoFile;

public class LaunchMplayerXAndWaitForTerminate extends BlockingExec {

    public LaunchMplayerXAndWaitForTerminate(VideoFile videoFile) {
        super(videoFile.getLaunchMovieCommand());
    }

    @Override
    protected void processFinished(Process proces, String[] output, String[] error) {
        final int[] numberOfMPlayersRunning = {2};
        boolean successfulLaunch = false;

        while (numberOfMPlayersRunning[0] == 2 || !successfulLaunch) {
            new BlockingExec("ps -e") {
                @Override
                protected void processFinished(Process process, String[] output, String[] error) {
                    numberOfMPlayersRunning[0] = 0;
                    for (String anOutput : output) {
                        numberOfMPlayersRunning[0] += anOutput.contains("MPlayerX") ? 1 : 0;
                    }
                }
            };

            successfulLaunch = successfulLaunch || numberOfMPlayersRunning[0] > 0;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //ignore
            }
        }

        new KillMplayerX();
    }


}