package com.mangione.imageplayer;

import com.mangione.imageplayer.player.ImagePlayerController;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File rootDirectory = args.length > 0 ? new File(args[0]) : new File("/Volumes/Pictures");
        new ImagePlayerController(rootDirectory);
    }
}
