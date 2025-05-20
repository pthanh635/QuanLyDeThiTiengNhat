package AI;

import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;

public class AudioPlayerUtil {

    public static void playAudio(String path) {
        if (path.endsWith(".wav")) {
            playWav(path);
        } else if (path.endsWith(".mp3")) {
            playMp3(path);
        } else {
            System.out.println("Định dạng không hỗ trợ: " + path);
        }
    }

    private static void playWav(String path) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void playMp3(String path) {
        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(path);
                Player player = new Player(fis);
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
