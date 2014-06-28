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

public class NavigateFromAudioAction extends Activity implements ApiClientImplementation.ConnectionListener {
    private ApiClientImplementation myApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApi = new ApiClientImplementation(this, this);

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
        Intent intent = new Intent(NavigateFromAudioAction.this, PowerampActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requested code: " + requestCode);
        System.out.println("result code: " + requestCode);
        System.out.println(data.getIntExtra("MY-POSITION", -42));
        try {
            startService(new Intent(PowerampAPI.ACTION_API_COMMAND)
                    .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.PAUSE));
            myApi.setPageStart(Transformer.getInstance().getTextPosition(data.getIntExtra("MY-POSITION", -42), data.getStringExtra("MY-NAME")));

        } catch (ApiException e) {
            showErrorMessage(e.getMessage(), false);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }
}

