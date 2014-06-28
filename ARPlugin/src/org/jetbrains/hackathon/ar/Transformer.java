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

    //new TextPosition(140, 0, 0)
    void load(String path){
        try {
            InputStream fis;
            BufferedReader br;
            String         line;

            fis = new FileInputStream(path);
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
    }

    private void loadChunkFromLine(String line) {
        String[] chunks = line.split(":");
        myItems.add(new Paragraph2Position(chunks[0], Integer.valueOf(chunks[1]), Integer.valueOf(chunks[2])));
    }

    public TextPosition getTextPosition(int audioPosition, String fileName){
        return new TextPosition(150, 0, 0);
    }

    public int getAudioPosition(TextPosition textPosition, String fileName){
        return 25;
    }

    public static Transformer getInstance() {
        return (instance != null) ? instance : (instance = new Transformer());
    }

    public Uri getAudioPath() {
        return Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/eBooks/_120.flac");
    }
}
