package info.androidhive.radioucab.Logica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;

import info.androidhive.radioucab.R;

public class ServicioRadio extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PLAY_MUSIC = "PLAY_MUSIC";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_STOP = "STOP";
    private static MediaPlayer mediaPlayer = null;
    private MediaSessionManager mManager;
    private MediaSession mediaSession;
    private MediaController mController;
    private static WifiManager.WifiLock wifiLock;
    private Notification notification;
    private NotificationManager managerNotification;
    private int onBufferingUpdateCount;
    private LocalBroadcastManager broadcaster;
    public static final String result = "Servicio";
    public static final String mensaje = "Mensaje Servicio";

    public ServicioRadio() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    public void sendResult(String message) {
        Intent intent = new Intent(result);
        if(message != null)
            intent.putExtra(mensaje, message);
            broadcaster.sendBroadcast(intent);
    }

    private void initMedia() {
        mediaPlayer = new MediaPlayer();
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        String deviceVersion = Build.VERSION.RELEASE;
        if (currentVersionSupportBigNotification()) {
            mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            mediaSession = new MediaSession(this, "Radio UCAB");
            mController = mediaSession.getController();
            mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setActive(true);
        } else {
            Log.i("Servicio Streaming", "No soporta notif. grandes");
        }
    }

    public boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 21) {
            return true;
        }
        return false;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_PLAY_MUSIC)) {
                mediaPlayer.start();
                crearNotificacion(R.drawable.ic_stat_pause, "Pausar", 3);
                Log.i("Receiver", "Start transmision");
                sendResult("Pausar");
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                mediaPlayer.pause();
                crearNotificacion(R.drawable.ic_stat_play, "Play", 1);
                Log.i("Receiver", "Pausar transmision");
                sendResult("Play");
            } else if (intent.getAction().equals(ACTION_STOP)) {
                Log.i("Receiver", "Terminar transmision");
                ServicioRadio.mediaPlayer.stop();
                sendResult("Stop");
                managerNotification = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                managerNotification.cancel(1);
            } else if (intent.getAction().equals(ACTION_PLAY)) {

                //mediaPlayer = new MediaPlayer();
                initMedia();
                try {
                /*   IntentFilter filter = new IntentFilter();
                   filter.addAction(ACTION_STOP);
                   filter.addAction(ACTION_PAUSE);
                   filter.addAction(ACTION_PLAY);
                   registerReceiver(notificationButtonReceiver, filter);*/
                    //mediaPlayer.setDataSource("http://31.200.244.181:8010");
                    mediaPlayer.setDataSource("http://31.200.244.181:8110/");
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                            .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
                    wifiLock.acquire();
                    Log.i("Servicio Streaming", "Player seteado");
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync(); // prepare async to not block main thread
                    mediaPlayer.setOnErrorListener(this);
                    mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    return START_STICKY;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private void crearNotificacion(int icon, String title, int action) {
        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            //BitmapDrawable contactPicDrawable = (BitmapDrawable) ContactsUtils.getContactPic(mContext, contactId);
           // Bitmap contactPic = contactPicDrawable.getBitmap();

            Resources res = this.getResources();
            int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
            int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
            bm = Bitmap.createScaledBitmap(bm, width, height, false);

            if (currentVersionSupportBigNotification()) {
                notification = new Notification.Builder(this)
                        // Hide the timestamp
                        .setShowWhen(false)
                        .setStyle(new Notification.MediaStyle()
                                // Attach our MediaSession token
                                .setMediaSession(mediaSession.getSessionToken())
                                        // Show our playback controls in the compat view
                                .setShowActionsInCompactView(0, 1))
                                // Set the Notification color
                                 .setColor(0xff002b54)

                        //.setColor(Color.parseColor("#FFFFFFFF"))
                                // Set the large and small icon
                      //  .setLargeIcon(bm)
                        .setSmallIcon(R.drawable.ic_silueta_radio)
                        .setTicker("Escuchas Radio UCAB")
                        .setOngoing(true)
                                // Set Notification content information
                        .setContentText("Artista/Locutor")
                        .setContentTitle("Radio UCAB")
                                // Add some playback controls
                        .addAction(icon, title, retreivePlaybackAction(action))
                        .addAction(R.drawable.ic_stat_stop, "Parar", retreivePlaybackAction(2))
                        .build();
                // Do something with your TransportControls
                final MediaController.TransportControls controls = mediaSession.getController().getTransportControls();

            } else {
                notification = new Notification.Builder(this)
                        // Hide the timestamp
                        // Set the large and small icon
                        .setTicker("Escuchas Radio UCAB")
                        .setOngoing(true)
                                // Set Notification content information
                        .setSmallIcon(R.drawable.ic_silueta_radio)
                        .setContentText("Artista/Locutor")
                        .setContentTitle("Radio UCAB")
                                // Add some playback controls
                        .addAction(icon, title, retreivePlaybackAction(action))
                        .addAction(R.drawable.ic_stat_stop, "Parar", retreivePlaybackAction(2))
                        .build();
                notification.icon = R.drawable.ic_silueta_radio;
            }

            //((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(1, notification);
            managerNotification = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            managerNotification.notify(1, notification);
        } catch (Exception e) {
            int x = 1;
        }
    }

    private PendingIntent retreivePlaybackAction(int which) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(this, ServicioRadio.class);
        switch (which) {
            case 1:
                // Play
                action = new Intent(ACTION_PLAY_MUSIC);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 1, action, 0);
                return pendingIntent;
            case 2:
                // Stop
                action = new Intent(ACTION_STOP);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 2, action, 0);
                return pendingIntent;
            case 3:
                // Pause
                action = new Intent(ACTION_PAUSE);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 3, action, 0);
                return pendingIntent;
            default:
                break;
        }
        return null;
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
        Log.i("Servicio Streaming", "Start Radio");
        String songName = "Nombre de la Cancion";
        crearNotificacion(R.drawable.ic_stat_pause, "Pausar", 3);
        Log.i("Servicio Streaming", "Inicie Notificacion");
        sendResult("Pausar/Stop");
    }

    /**
     * Called when MediaPlayer is ready
     */
    /*
    public void onPrepared(MediaPlayer player) {
        player.start();
        Log.i("Servicio Streaming", "Start Radio");
        String songName = "Nombre de la Cancion";

        PendingIntent mainIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);

        //Stop intent
        Intent stopReceiver = new Intent();
        stopReceiver.setAction(ACTION_STOP);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 1, stopReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        //Play intent
        Intent playReceiver = new Intent();
        playReceiver.setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 2, playReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        //Pause intent
        Intent pauseReceiver = new Intent();
        pauseReceiver.setAction(ACTION_PAUSE);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(this, 3, pauseReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(mainIntent)
                .setSmallIcon(R.drawable.ic_silueta_musica)
                .setTicker("Escuchas Radio UCAB")
                .setOngoing(true)
                .setContentTitle("Radio UCAB")
                .setContentText("Sonando: " + songName)
                .addAction(R.drawable.ic_stat_pause, "Pausar", pendingIntentPause)
                .addAction(R.drawable.ic_stop, "Parar", pendingIntentStop);
        notification = builder.build();
        notification.icon = R.drawable.ic_silueta_radio;


        // Gets an instance of the NotificationManager service
        managerNotification = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        managerNotification.notify(1, notification);

        //  startForeground(1, notification);
        Log.i("Servicio Streaming", "Inicie Notificacion");
    }
*/
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("Prueba Porcentaje", percent + "");
        if (!mp.isPlaying()) {
//          android.util.Log.d(TAG,"onBufferingUpdate(): onBufferingUpdateCount = "+onBufferingUpdateCount);
            if (onBufferingUpdateCount > 300) {
                createAlertDialog("llegue a 300, porcentaje: " + percent);
                //restartMP();
            }
            onBufferingUpdateCount++;
            return;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("Servicio Streaming", "ERROR:" + what + " extra:" + extra);
        if (what == 261 && extra == -1003)
            createAlertDialog("Error al conectarse al Streaming, vuelva a intentarlo.");
        mp.reset();
        stopForeground(true);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            wifiLock.release();
            if (managerNotification != null)
                managerNotification.cancelAll();
            stopForeground(true);
            stopSelf();
            Log.i("Servicio Streaming", "finalice el streaming");
            // unregisterReceiver(notificationButtonReceiver);
            sendResult("Stop");
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        stopSelf();
    }

    public static void onPause() {
        mediaPlayer.pause();
//        buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));

    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        mediaPlayer.release();
        return false;
    }

    public void createAlertDialog(String mensaje) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage(mensaje);
            builder.setCancelable(true);
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            Dialog alert = builder.create();
            alert.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    private BroadcastReceiver notificationButtonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_PAUSE)) {
                Log.i("Receiver", "Pausar streaming");
                ServicioRadio.onPause();

            } else if (intent.getAction().equals(ACTION_STOP)) {
                Log.i("Receiver", "Terminar transmision");
                ServicioRadio.mediaPlayer.stop();
                managerNotification = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                managerNotification.cancel(1);
                //   context.stopService(new Intent(context, ServicioRadio.class));
                //     android.os.Process.killProcess(android.os.Process.myPid());
                //   System.exit(1);
                //setResultCode(RESULT_OK);
            } else if (intent.getAction().equals(ACTION_PLAY)) {
                Log.i("Receiver", "Play streaming");
            }
        }
    };*/

}