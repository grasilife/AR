package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import com.maxmpz.poweramp.player.PowerampAPI;
import org.geometerplus.android.fbreader.api.ApiClientImplementation;

public class NavigateToAudioAction extends Activity implements ApiClientImplementation.ConnectionListener {
    private ApiClientImplementation myApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApi = new ApiClientImplementation(this, this);

        finish();
    }

    @Override
    protected void onResume() {
        myApi.connect();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        myApi.disconnect();
        super.onDestroy();
    }

    public void onConnected() {
        System.out.println("to audio pressed");
        startService(new Intent(PowerampAPI.ACTION_API_COMMAND)
                .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.OPEN_TO_PLAY)
                .putExtra(PowerampAPI.Track.POSITION, 30)
                .setData(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/eBooks/_120.flac")));
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

