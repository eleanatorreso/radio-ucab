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
import android.graphics.Color;
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

import info.androidhive.radioucab.Controlador.HomeFragment;
import info.androidhive.radioucab.Controlador.MainActivity;
import info.androidhive.radioucab.R;

public class ServicioRadio extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PLAY_INIT = "PLAY_INIT";
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
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

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

    private void initMedia() {
        mediaPlayer = new MediaPlayer();
        manejoActivity.progressBarStreaming(true);
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        String deviceVersion = Build.VERSION.RELEASE;
        if (manejoActivity.currentVersionL()) {
            mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            mediaSession = new MediaSession(this, "Radio UCAB");
            mController = mediaSession.getController();
            mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSession.setActive(true);
        } else {
            Log.i("Servicio Streaming", "No soporta notif. grandes");
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_PLAY_INIT)) {
                mediaPlayer.start();
                crearNotificacion(R.drawable.ic_stat_pause, "Pausar", 3);
                Log.i("Receiver", "Start transmision");
                manejoActivity.cambioReproductor("Play");
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                mediaPlayer.pause();
                crearNotificacion(R.drawable.ic_stat_play, "Play", 1);
                Log.i("Receiver", "Pausar transmision");
                manejoActivity.cambioReproductor("Pausar");
            } else if (intent.getAction().equals(ACTION_STOP)) {
                Log.i("Receiver", "Terminar transmision");
                ServicioRadio.mediaPlayer.stop();
                manejoActivity.cambioReproductor("Stop");
                managerNotification = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                managerNotification.cancel(1);
            } else if (intent.getAction().equals(ACTION_PLAY)) {
                initMedia();
                try {
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
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction("abrir_ventana");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        try {
            Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.ic_radio_ucab_caratula);
            Resources res = this.getResources();
            int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
            int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
            if (manejoActivity.currentVersionL()) {
                notification = new Notification.Builder(this)
                        // Hide the timestamp
                        .setShowWhen(false)
                        .setStyle(new Notification.MediaStyle()
                                .setMediaSession(mediaSession.getSessionToken())
                                .setShowActionsInCompactView(0, 1))
                        .setColor(getResources().getColor(R.color.azul_radio_ucab))
                        .setLargeIcon(notificationLargeIconBitmap)
                        .setSmallIcon(R.drawable.ic_silueta_radio)
                        .setTicker("Escuchas Radio UCAB")
                        .setOngoing(true)
                        .setContentText("Artista/Locutor")
                        .setContentTitle("Radio UCAB")
                        .addAction(icon, title, retreivePlaybackAction(action))
                        .addAction(R.drawable.ic_stat_stop, "Parar", retreivePlaybackAction(2))
                        .setContentIntent(contentIntent)
                        .build();
            } else {
                notification = new Notification.Builder(this)
                        .setTicker("Escuchas Radio UCAB")
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_silueta_radio)
                        .setContentText("Artista/Locutor")
                        .setContentTitle("Radio UCAB")
                        .addAction(icon, title, retreivePlaybackAction(action))
                        .addAction(R.drawable.ic_stat_stop, "Parar", retreivePlaybackAction(2))
                        .setContentIntent(contentIntent)
                        .build();
                notification.icon = R.drawable.ic_silueta_radio;
            }
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
                action = new Intent(ACTION_PLAY_INIT);
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
        manejoActivity.progressBarStreaming(false);
        manejoActivity.cambioReproductor("Pausar/Stop");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("Prueba Porcentaje", percent + "");
        if (!mp.isPlaying()) {
            if (onBufferingUpdateCount > 300) {
                createAlertDialog("llegue a 300, porcentaje: " + percent);
            }
            onBufferingUpdateCount++;
            return;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("Servicio Streaming", "ERROR:" + what + " extra:" + extra);
        if (what == 261 && extra == -1003) {
            createAlertDialog("Error al conectarse al Streaming, vuelva a intentarlo.");
        }
        else {
            createAlertDialog("Se ha producido un error, vuelva a intentarlo.");
        }
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
            manejoActivity.cambioReproductor("Stop");
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        stopSelf();
    }

    public static void onPause() {
        mediaPlayer.pause();
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

}