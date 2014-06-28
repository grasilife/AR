package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserActivity;
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    public void onConnected() {
        //if(!isLoaded) return;
        System.out.println("add files pressed");

       /* FileChooserDialog dialog = new FileChooserDialog(AddAudioAction.this);
        dialog.addListener(AddAudioAction.this.onFileSelectedListener);
        dialog.setFilter(".*arg");
        dialog.setCanCreateFiles(false);
       //at first load only format file(.arg)
       // dialog.setFolderMode(true);
        dialog.show();*/

        Intent intent = new Intent(this, FileChooserActivity.class);
        intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*arg");
        intent.putExtra(FileChooserActivity.INPUT_CAN_CREATE_FILES, false);
       // this.startActivityForResult(intent, 0);

      /*  startService(new Intent(PowerampAPI.ACTION_API_COMMAND)
                .putExtra(PowerampAPI.COMMAND, PowerampAPI.Commands.OPEN_TO_PLAY)
                .putExtra(PowerampAPI.Track.POSITION, 30)
                .setData(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/eBooks/_120.flac")));*/
    }

    /*private void successfulLoad() {
        runOnUiThread(new Runnable() {
            public void run() {

            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);

       /* Transformer.getInstance().load(file.getPath());
        Toast.makeText(AddAudioAction.this, "Loaded successfully", Toast.LENGTH_SHORT).show();*/
    }


    private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.dismiss();
            Transformer.getInstance().load(file.getPath());
            Toast.makeText(AddAudioAction.this, "Loaded successfully", Toast.LENGTH_SHORT).show();
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
            Toast toast = Toast.makeText(AddAudioAction.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
            toast.show();
        }
    };
}

