package com.mangione.mediacenter.model.process;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class KillMplayerX extends BlockingExec {

    public static void main(String[] args) {
        new KillMplayerX();
    }

    public KillMplayerX() {
        super("ps -e");
    }

    protected void processFinished(String[] output) {
        List<String> processesToKill = new ArrayList<String>();
        for (String psLine : output) {
            if(psLine.contains("MPlayerX")){
                processesToKill.add(StringUtils.split(psLine, " ")[0]);
            }
        }

        if(!processesToKill.isEmpty()){
            final String killCommand = "kill -9 " + StringUtils.join(processesToKill, " ");
            new BlockingExec(killCommand){
                @Override
                protected void processFinished(String[] output) {
                    System.out.println(killCommand);
                }
            };
        }
    }
}
