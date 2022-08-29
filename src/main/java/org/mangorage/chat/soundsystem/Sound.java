package org.mangorage.chat.soundsystem;
import org.mangorage.chat.Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    AudioInputStream in;

    AudioFormat decodedFormat;

    AudioInputStream din;

    AudioFormat baseFormat;

    SourceDataLine line;

    private boolean loop;

    private BufferedInputStream stream;

    // private ByteArrayInputStream stream;

    /**
     * recreate the stream
     *
     */
    public void reset() {
        try {
            stream.reset();
            in = AudioSystem.getAudioInputStream(stream);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            line = getLine(decodedFormat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            line.close();
            din.close();
            in.close();
        } catch (IOException e) {
        }
    }

    public File getSound(String file) throws URISyntaxException {
        URL resource = getClass().getResource(file);
        if (resource == null) {
            throw new IllegalStateException("resource was null");
        }

        File fileN = new File(resource.toURI());; // return a file
        System.out.println(fileN.getAbsolutePath() +" : > " + fileN.exists());
        return fileN;
    }

    Sound(String sound) {
        this.loop = false;
        try {
            // ByteArrayOutputStream out = new ByteArrayOutputStream();
            // byte[] buffer = new byte[1024];
            // int read = raw.read(buffer);
            // while( read > 0 ) {
            // out.write(buffer, 0, read);
            // read = raw.read(buffer);
            // }
            // stream = new ByteArrayInputStream(out.toByteArray());
            File file = getSound(sound);

            in = AudioSystem.getAudioInputStream(file);
            din = null;

            if (in != null) {
                baseFormat = in.getFormat();

                decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED, baseFormat
                        .getSampleRate(), 16, baseFormat.getChannels(),
                        baseFormat.getChannels() * 2, baseFormat
                        .getSampleRate(), false);

                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                line = getLine(decodedFormat);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat)
            throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public void play() {
        try {
            boolean firstTime = true;
            while (firstTime || loop) {

                firstTime = false;
                byte[] data = new byte[4096];

                if (line != null) {

                    line.start();
                    int nBytesRead = 0;

                    while (nBytesRead != -1) {
                        nBytesRead = din.read(data, 0, data.length);
                        if (nBytesRead != -1)
                            line.write(data, 0, nBytesRead);
                    }

                    line.drain();
                    line.stop();
                    line.close();

                    reset();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}