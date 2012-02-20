package com.mangione.mediacenter.model.mplayerx;

import com.mangione.mediacenter.model.process.BlockingExec;
import com.mangione.mediacenter.model.videofile.VideoFile;
import org.apache.commons.lang.StringUtils;

public class LaunchMplayerXAndWaitForTerminate extends BlockingExec {

    public LaunchMplayerXAndWaitForTerminate(VideoFile videoFile) {
        super(videoFile.getLaunchMovieCommand());
    }

    @Override
    protected void processFinished(Process proces, String[] output) {
        final boolean[] mplayerChildStillRunning = {true};


        while (mplayerChildStillRunning[0]) {
            new BlockingExec("ps -e") {

                @Override
                protected void processFinished(Process proces, String[] output) {
                    mplayerChildStillRunning[0] = false;
                    while (!mplayerChildStillRunning[0]) {
                        for (int i = 0; i < output.length && !mplayerChildStillRunning[0]; i++) {
                            mplayerChildStillRunning[0] = output[i].contains("MPlayerX") &&
                            Integer.parseInt(StringUtils.split(output[i], " ")[0]) == getMPlayerXChildPid();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                }
            };
        }

        new KillMplayerX();
    }

    private int getMPlayerXChildPid() {
        final int[] mplayerParentId = {0};
        final int[] mplayerChildId = {-1};
        new BlockingExec("ps -ef") {
            @Override
            protected void processFinished(Process process, String[] output) {
                for (String nextPsLine : output) {
                    if (nextPsLine.contains("MPlayerX")) {
                        final int currentProcessId = Integer.parseInt(StringUtils.split(nextPsLine, " ")[1]);
                        final int parentProcessId = Integer.parseInt(StringUtils.split(nextPsLine, " ")[2]);
                        if (mplayerParentId[0] == 0) {
                            mplayerParentId[0] = currentProcessId;
                        } else {
                            if (mplayerParentId[0] != parentProcessId) {
                                System.out.println("MPlayerChild found without matching parentID");
                            }
                            mplayerChildId[0] = currentProcessId;
                        }
                    }
                }
            }
        };
        return mplayerChildId[0];
    }

}