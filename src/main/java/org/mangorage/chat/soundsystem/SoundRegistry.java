package org.mangorage.chat.soundsystem;

import org.mangorage.chat.Main;
import sun.audio.AudioPlayer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class SoundRegistry {




    public static final RegistryObject<Sound> PING = new RegistryObject<>(() -> new Sound("ping.wav"));







}
