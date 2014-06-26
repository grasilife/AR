package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.maxmpz.poweramp.player.PowerampAPI;
import org.geometerplus.android.fbreader.api.ApiClientImplementation;
import org.geometerplus.android.fbreader.api.ApiException;
import org.geometerplus.android.fbreader.api.TextPosition;

public class NavigateFromAudioAction extends Activity implements ApiClientImplementation.ConnectionListener {
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
        System.out.println("from audio pressed");
        try {
            myApi.setPageStart(new TextPosition(100, 0, 0));
            startService(new Intent(PowerampAPI.ACTION_API_COMMAND).putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.TOGGLE_PLAY_PAUSE));
        } catch (ApiException e) {
            showErrorMessage(e.getMessage(), false);
        }
    }

    private void showErrorMessage(final CharSequence text, final boolean fatal) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (fatal) {
                    setTitle("FATAL ERROR");
                }
                Toast.makeText(NavigateFromAudioAction.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

