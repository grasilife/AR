package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import org.geometerplus.android.fbreader.api.ApiClientImplementation;
import java.io.File;

public class AddAudioAction extends Activity implements ApiClientImplementation.ConnectionListener {
    private ApiClientImplementation myApi;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApi = new ApiClientImplementation(this, this);

//        finish();
    }

    @Override
    protected void onResume() {
        isLoaded = true;
        myApi.connect();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        myApi.disconnect();
        super.onDestroy();


    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    public void onConnected() {
        //if(!isLoaded) return;

        FileChooserDialog dialog = new FileChooserDialog(AddAudioAction.this);
        dialog.addListener(AddAudioAction.this.onFileSelectedListener);
        dialog.setFilter(".*arg");
        dialog.setCanCreateFiles(false);
       //at first load only format file(.arg)
       // dialog.setFolderMode(true);
        dialog.show();
      /*  startService(new Intent(PowerampAPI.ACTION_API_COMMAND)
                .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.OPEN_TO_PLAY)
                .putExtra(PowerampAPI.Track.POSITION, 30)
                .setData(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/eBooks/_120.flac")));*/
    }

    private void showErrorMessage(final CharSequence text, final boolean fatal) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (fatal) {
                    setTitle("FATAL ERROR");
                }
                Toast.makeText(AddAudioAction.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.hide();
            Transformer.getInstance().load(file.getPath());
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
            Toast toast = Toast.makeText(AddAudioAction.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
            toast.show();
        }
    };
}

