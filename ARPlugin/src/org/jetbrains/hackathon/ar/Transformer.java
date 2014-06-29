package org.jetbrains.hackathon.ar;

import android.net.Uri;
import android.os.Environment;
import org.geometerplus.android.fbreader.api.TextPosition;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fearfall on 28.06.2014.
 */
public class Transformer {
    private static Transformer instance;
    private List<Paragraph2Position> myItems = new LinkedList<Paragraph2Position>();
    private boolean myLoaded = false;
    private boolean myUpdated;
    private String myPath2Music;

    //new TextPosition(140, 0, 0)
    void load(String path){
        try {
            InputStream fis;
            BufferedReader br;
            String         line;
            File file = new File(path);
            myPath2Music = path;
            File[] formatFiles = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getPath().endsWith(".rl");
                }
            });
            if(formatFiles.length != 1) return;

            fis = new FileInputStream(formatFiles[0]);
            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                loadChunkFromLine(line);
            }
            br.close();
            br = null;
            fis = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myLoaded = true;
    }

    private void loadChunkFromLine(String line) {
        String[] chunks = line.split(":");
        if(chunks.length != 3) return;
        myItems.add(new Paragraph2Position(chunks[0], Integer.valueOf(chunks[2]), Integer.valueOf(chunks[1])));
    }

    public TextPosition getTextPosition(int audioPosition, String fileName){
        TextPosition textPosition = null;
        for( Paragraph2Position par : myItems){
            if(fileName.endsWith(par.getAudioFileName()))
                if(audioPosition > par.getSeconds())
                    textPosition = new TextPosition(par.getParagraphNumber(), 0, 0);
                if(audioPosition < par.getSeconds())
                    break;
        }
        return textPosition;
    }

    public AudioPosition getAudioPosition(TextPosition textPosition, String fileName){
        AudioPosition audioPosition = null;
        for( Paragraph2Position par : myItems){
                if(textPosition.ParagraphIndex > par.getParagraphNumber())
                    audioPosition = par.getAudioPosition();
            if(textPosition.ParagraphIndex < par.getParagraphNumber())
                break;
        }
        return audioPosition;
    }

    public static Transformer getInstance() {
        return (instance != null) ? instance : (instance = new Transformer());
    }

    public Uri getAudioPath(AudioPosition audioPosition) {
        return Uri.parse("file://" +myPath2Music + "/" + audioPosition.getFilePath());
    }

    public boolean isLoaded() {
        return myLoaded;
    }

    public boolean isUpdated() {
        return myUpdated;
    }

    public void setUpdated(boolean myUpdated) {
        this.myUpdated = myUpdated;
    }
}
