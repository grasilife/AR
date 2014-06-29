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
        Transformer instance = Transformer.getInstance();
        if(!instance.isLoaded())
        {
            finish();
        }
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
        int intExtra = data.getIntExtra("MY-POSITION", -42);
        System.out.println(intExtra);
        try {
            Intent service = new Intent(PowerampAPI.ACTION_API_COMMAND)
                    .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.PAUSE);
            startService(service);
            String stringExtra = data.getStringExtra("MY-NAME");
            TextPosition textPosition = Transformer.getInstance().getTextPosition(intExtra, stringExtra);
            
            if(textPosition == null)
                Toast.makeText(NavigateFromAudioAction.this, "Not loaded " + intExtra + " " + stringExtra, Toast.LENGTH_SHORT).show();
            else
            {
                myApi.setPageStart(textPosition);
                myApi.highlightArea(textPosition, new TextPosition(textPosition.ParagraphIndex + 1, 0, 0));

               // myApi.highlightArea(textPosition, textPosition);
            }
            stopService(service);

        } catch (ApiException e) {
            showErrorMessage(e.getMessage(), false);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
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

