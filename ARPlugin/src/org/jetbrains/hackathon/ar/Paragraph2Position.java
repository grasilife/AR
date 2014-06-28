package org.jetbrains.hackathon.ar;

/**
 * Created by Fearfall on 28.06.2014.
 */
public class Paragraph2Position {
    private String audioFileName = "";
    private int paragraphNumber;
    private int audioPosition;

    public Paragraph2Position(String audioFileName, int paragraphNumber, int audioPosition) {
        this.audioFileName = audioFileName;
        this.paragraphNumber = paragraphNumber;
        this.audioPosition = audioPosition;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public int getAudioPosition() {
        return audioPosition;
    }
}
