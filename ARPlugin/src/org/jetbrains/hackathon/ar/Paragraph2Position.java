package org.jetbrains.hackathon.ar;

/**
 * Created by Fearfall on 28.06.2014.
 */
public class Paragraph2Position {
    private int paragraphNumber;
    private AudioPosition audioPosition;

    public Paragraph2Position(String audioFileName, int paragraphNumber, int audioPosition) {
        this.paragraphNumber = paragraphNumber;
        this.audioPosition = new AudioPosition(audioPosition, audioFileName);
    }

    public String getAudioFileName() {
        return audioPosition.getFilePath();
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public int getSeconds() {
        return audioPosition.getSecondsNumber();
    }

    public AudioPosition getAudioPosition() {
        return audioPosition;
    }
}
