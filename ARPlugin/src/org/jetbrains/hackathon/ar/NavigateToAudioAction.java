package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import com.maxmpz.poweramp.player.PowerampAPI;
import org.geometerplus.android.fbreader.api.ApiClientImplementation;
import org.geometerplus.android.fbreader.api.ApiException;
import org.geometerplus.android.fbreader.api.TextPosition;

public class NavigateToAudioAction extends Activity implements ApiClientImplementation.ConnectionListener {
    private ApiClientImplementation myApi;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApi = new ApiClientImplementation(this, this);

        Transformer instance = Transformer.getInstance();
        if(!instance.isLoaded())
        {
              finish();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    @Override
    protected void onResume() {
        myApi.connect();
        isLoaded = true;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        myApi.disconnect();
        super.onDestroy();
    }

    public void onConnected() {

        System.out.println("to audio pressed");
        try {
            Transformer instance = Transformer.getInstance();
            if(!instance.isLoaded())
            {
              //  finish();
                return;
            }

            TextPosition pageStart = myApi.getPageStart();

            AudioPosition audioPosition = instance.getAudioPosition(pageStart, "");
            startService(new Intent(PowerampAPI.ACTION_API_COMMAND)
                    .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.OPEN_TO_PLAY)
                    .putExtra(PowerampAPI.Track.POSITION, audioPosition.getSecondsNumber())
                            .setData(instance.getAudioPath(audioPosition)));
            finish();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(final CharSequence text, final boolean fatal) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (fatal) {
                    setTitle("FATAL ERROR");
                }
                Toast.makeText(NavigateToAudioAction.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

