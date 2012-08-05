package com.mangione.mediacenter.model.mplayerx;

import com.mangione.mediacenter.model.process.BlockingExec;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class KillMplayerX extends BlockingExec {

    public static void main(String[] args) {
        new KillMplayerX();
    }

    public KillMplayerX() {
        super(new String[]{"ps", "-e"});
    }

    protected void processFinished(Process process, String[] output, String[] error) {
        List<String> processesToKill = new ArrayList<String>();
        for (String psLine : output) {
            if(psLine.contains("MPlayerX")){
                processesToKill.add(StringUtils.split(psLine, " ")[0]);
            }
        }

        if(!processesToKill.isEmpty()){
            final String[] killCommand = new String[]{"kill", "-9 ",  StringUtils.join(processesToKill, " ")};
            new BlockingExec(killCommand){
                @Override
                protected void processFinished(Process proces, String[] output, String[] error) {
                }
            };
        }
    }
}
