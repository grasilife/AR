package org.jetbrains.hackathon.ar;

/**
 * Created by Fearfall on 29.06.2014.
 */
public class AudioPosition {
    private int secondsNumber = 0;
    private String filePath;
    public AudioPosition(int secondsNumber, String filePath) {
        this.secondsNumber = secondsNumber;
        this.filePath = filePath;
    }

    public int getSecondsNumber() {
        return secondsNumber;
    }

    public String getFilePath() {
        return filePath;
    }
}
