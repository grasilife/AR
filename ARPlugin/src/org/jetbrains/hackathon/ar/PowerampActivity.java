package org.jetbrains.hackathon.ar;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.maxmpz.poweramp.player.PowerampAPI;
import com.maxmpz.poweramp.player.RemoteTrackTime;

public class PowerampActivity extends Activity implements RemoteTrackTime.TrackTimeListener {

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRemoteTrackTime = new RemoteTrackTime(this);
        mRemoteTrackTime.setTrackTimeListener(this);

        registerAndLoadStatus();
        mRemoteTrackTime.registerAndLoadStatus();
    }

    private static final String TAG = "MainActivity";

    private Intent mTrackIntent;
    private Intent mStatusIntent;

    private Bundle mCurrentTrack;

    private RemoteTrackTime mRemoteTrackTime;

    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy");
        try {
            unregister();
            mRemoteTrackTime.setTrackTimeListener(null);
            mRemoteTrackTime.unregister();

            mRemoteTrackTime = null;
            mTrackReceiver = null;
            mStatusReceiver = null;
        } catch (Exception ex) {
            Log.e(TAG, "", ex);
        }

        super.onDestroy();
    }

    private void registerAndLoadStatus() {
        // Note, it's not necessary to set mStatusIntent/mPlayingModeIntent this way here,
        // but this approach can be used with null receiver to get current sticky intent without broadcast receiver.
        mTrackIntent = registerReceiver(mTrackReceiver, new IntentFilter(PowerampAPI.ACTION_TRACK_CHANGED));
    }


    private void unregister() {
        if (mTrackIntent != null) {
            try {
                unregisterReceiver(mTrackReceiver);
            } catch (Exception ex) {
            } // Can throw exception if for some reason broadcast receiver wasn't registered.
        }
        if (mStatusReceiver != null) {
            try {
                unregisterReceiver(mStatusReceiver);
            } catch (Exception ex) {
            }
        }
    }

    private BroadcastReceiver mTrackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("mTrackReceiver");
            mTrackIntent = intent;
            processTrackIntent();
            Log.w(TAG, "mTrackReceiver " + intent);
        }
    };

    private void processTrackIntent() {

        mCurrentTrack = null;

        if (mTrackIntent != null) {
            mCurrentTrack = mTrackIntent.getBundleExtra(PowerampAPI.TRACK);
            if (mCurrentTrack != null) {
                System.out.println("position: " + mCurrentTrack.getInt(PowerampAPI.Track.POSITION));

                int duration = mCurrentTrack.getInt(PowerampAPI.Track.DURATION);
                mRemoteTrackTime.updateTrackDuration(duration); // Let ReomoteTrackTime know about current song duration.
            }
        }
    }

    private BroadcastReceiver mStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mStatusIntent = intent;
            System.out.println("mStatusReceiver");
            updateStatusUI();
        }
    };

    private void updateStatusUI() {
        Log.w(TAG, "updateStatusUI");
        if (mStatusIntent != null) {
            boolean paused = true;

            int status = mStatusIntent.getIntExtra(PowerampAPI.STATUS, -1);

            // Each status update can contain track position update as well.
            int pos = mStatusIntent.getIntExtra(PowerampAPI.Track.POSITION, -1);
            if (pos != -1) {
                mRemoteTrackTime.updateTrackPosition(pos);
            }

            switch (status) {
                case PowerampAPI.Status.TRACK_PLAYING:
                    paused = mStatusIntent.getBooleanExtra(PowerampAPI.PAUSED, false);
                    startStopRemoteTrackTime(paused);
                    break;

                case PowerampAPI.Status.TRACK_ENDED:
                case PowerampAPI.Status.PLAYING_ENDED:
                    mRemoteTrackTime.stopSongProgress();
                    break;
            }
        }
    }

    // Commands RemoteTrackTime to start or stop showing song progress.
    void startStopRemoteTrackTime(boolean paused) {
        if (!paused) {
            mRemoteTrackTime.startSongProgress();
        } else {
            mRemoteTrackTime.stopSongProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTrackDurationChanged(int duration) {
        System.out.println("activity: duration changed");
    }

    @Override
    public void onTrackPositionChanged(int position) {
        System.out.println("activity: position changed1: " + position + " " + mCurrentTrack);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("MY-POSITION", position);
        resultIntent.putExtra("MY-NAME", mCurrentTrack.getString(PowerampAPI.Track.PATH));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

