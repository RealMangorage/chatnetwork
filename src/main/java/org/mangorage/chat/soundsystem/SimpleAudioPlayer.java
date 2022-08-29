package org.mangorage.chat.soundsystem;

import org.mangorage.chat.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SimpleAudioPlayer {

    public static int soundsPlaying = 0;

    public static void playSound(RegistryObject<Sound> soundObject) {
        if (soundsPlaying > 5)
            return;

        Sound sound = soundObject.get();

        new Thread() {
            public void run() {
                soundsPlaying++;
                sound.play();
                sound.reset();
                soundsPlaying--;
            }
        }.start();
    }

}