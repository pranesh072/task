package pranesh.realtytask.gcm;

/**
 * Created by pranesh on 13-May-16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import pranesh.realtytask.MainActivity;
import pranesh.realtytask.R;

import java.util.ArrayList;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    //GcmMessageQueueDatabase db;//tujhe bhi tp store karane

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        // db = new GcmMessageQueueDatabase(this);
        //String message = data.getString("message");
        String msg = parseGcmMessage(data);


        sendNotification1(msg);
//        db.open();
//        db.insertMsgType1(msg);
//        db.close();


        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */


        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     * <p/>
     * message GCM message received.
     */
    private void sendNotification1(String msg) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }



    private String parseGcmMessage(Bundle data) {
        //parse kara le yha pr
        //maine apne logic se parse kiya h ..tu apne data structure ke hisab se karlena
        //
        String msg = null;
        System.out.println("bundle data : " + data);


        String data1 = data.getString("data");

        // msg = new GcmMessage(data1);


        return data1;
    }


}