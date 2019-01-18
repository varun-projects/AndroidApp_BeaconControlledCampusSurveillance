package com.attendancesolution.bams.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.attendancesolution.bams.activities.MainActivity;
import com.attendancesolution.bams.adapters.DBAttendanceAdapter;
import com.attendancesolution.bams.adapters.DBRecentAdapter;
import com.attendancesolution.bams.adapters.HomeAdapter;
import com.attendancesolution.bams.singletonlClasses.AppPrefs;
import com.attendancesolution.bams.singletonlClasses.Utilities;
import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;

import java.net.URL;
import java.util.List;
import java.util.UUID;


/**
 * Created by example on 4/23/2016.
 */
@SuppressWarnings("ALL")
public class MyService extends Service {

    private BluetoothAdapter btAdapter;
    private BluetoothManager btManager;
    private Handler scanHandler = new Handler();

    private int scan_interval_ms = 5000;
    private boolean isScanning = false;
    int counter = 0;
    int power;
    UUID id = null;
    URL url = null;

    private static final double DISTANCE_THRESHOLD_WTF = 0.0;
    private static final double DISTANCE_THRESHOLD_IMMEDIATE = 2.0;
    private static final double DISTANCE_THRESHOLD_NEAR = 7.0;

    private String TAG = "MyService";

    Intent notificationIntent;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    String newtext;
    NotificationManager mNM;
    private final IBinder mBinder = new LocalBinder();
    DBRecentAdapter dbRecentAdapter;
    DBAttendanceAdapter dbAttendanceAdapter;
    AppPrefs appPrefs;

    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    long[] slotStartTimes = {0, 0, 0};
    long[] slotEndTimes = {0, 0, 0};


    Handler attendanceHandler = new Handler();
    boolean handlerStarted = false;
    String slot = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appPrefs = new AppPrefs(this);

        slotStartTimes[0] = System.currentTimeMillis() + 15000;
        slotEndTimes[0] = slotStartTimes[0] + 60000;
        slotStartTimes[1] = slotEndTimes[0] + 15000;
        slotEndTimes[1] = slotStartTimes[1] + 60000;
        slotStartTimes[2] = slotEndTimes[1] + 15000;
        slotEndTimes[2] = slotStartTimes[2] + 60000;


        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        dbRecentAdapter = new DBRecentAdapter(this);
        dbRecentAdapter.open();
        dbAttendanceAdapter = new DBAttendanceAdapter(this);
        dbAttendanceAdapter.open();
        scanHandler.post(scanRunnable);


    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {

            if (isScanning) {
                if (btAdapter != null) {
                    btAdapter.stopLeScan(leScanCallback);
                }
            } else {
                if (btAdapter != null) {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

           /* Log.e(TAG, "Onle Scan");*/
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound) {

                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                final double distance = calculateAccuracy(59, rssi);
                String distanceDescriptor;

                if (distance < DISTANCE_THRESHOLD_WTF) {
                    distanceDescriptor = "UNKNOWN";
                } else if (distance < DISTANCE_THRESHOLD_IMMEDIATE) {
                    distanceDescriptor = "IMMEDIATE";

                } else if (distance < DISTANCE_THRESHOLD_NEAR) {
                    distanceDescriptor = "NEAR";
                } else {
                    distanceDescriptor = "FAR";
                }
                if (System.currentTimeMillis() > slotStartTimes[0] && System.currentTimeMillis() < slotEndTimes[0]) {
                    slot = "slot1";
                } else if (System.currentTimeMillis() > slotStartTimes[1] && System.currentTimeMillis() < slotEndTimes[1]) {
                    slot = "slot2";
                } else if (System.currentTimeMillis() > slotStartTimes[2] && System.currentTimeMillis() < slotEndTimes[2]) {
                    slot = "slot3";
                }
                if (distance < 10) {
                    if (!handlerStarted) {
                        handlerStarted = true;

                        attendanceHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (slot != null)
                                    if (distance < 10) {
                                        markAttendance();
                                        Intent markAttendance = new Intent(HomeAdapter.ACTION_ATTENDANCE_MARKED_RECEIVER);
                                        LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(markAttendance);
                                    } else {
                                        Toast.makeText(MyService.this, "Absent Marked for " + slot, Toast.LENGTH_SHORT).show();
                                    }

                            }
                        }, 50000);
                    }
                }
                Log.e(TAG, "UUID: " + hexToString(uuid) + "\nmajor: " + major + "\nminor" + minor + "\nRSSI" + String.valueOf(rssi) + "\ndistance" +
                        String.valueOf(distance) + "\nProximity" + distanceDescriptor);

                Cursor c = dbRecentAdapter.getRow(Utilities.getDateInString());
                boolean found = false;


                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        String link = c.getString(DBRecentAdapter.COL_LINK);
                        if (link.contentEquals(hexToString(uuid))) {
                            found = true;
                        }
                    } while (c.moveToNext());
                }


                if (uuid != null && !found) {

                    PendingIntent contentIntent = PendingIntent.getActivity(MyService.this, 0, new Intent(MyService.this, MainActivity.class), 0);

                    mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    mBuilder.setSmallIcon(android.R.drawable.btn_dialog);
                    mBuilder.setAutoCancel(true);

                    mBuilder.setContentIntent(contentIntent);

                    Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    mBuilder.setSound(notifSound);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    mBuilder.setContentTitle("Link Recieved");
                    mBuilder.setContentText(hexToString(uuid));
                    mNotificationManager.notify(1, mBuilder.build());
                    notificationIntent = new Intent(MyService.this, MainActivity.class);

                    dbRecentAdapter.insertRow(hexToString(uuid), Utilities.getDayInString(), Utilities.getDateInString(), 0, 1);
                    appPrefs.setIsNewMessage(true);
                }
            } else {
               /* Log.e(TAG, "Eddystone url");*/
                // Parse the payload of the advertisement packet
                // as a list of AD structures.
                List<ADStructure> structures =
                        ADPayloadParser.getInstance().parse(scanRecord);

                // For each AD structure contained in the advertisement packet.
                for (ADStructure structure : structures) {
                    // If the AD structure represents Eddystone UID.

                    // If the AD structure represents Eddystone URL.
                    if (structure instanceof EddystoneURL) {
                        // Eddystone URL

                        EddystoneURL es = (EddystoneURL) structure;

                        int power = es.getTxPower();
                        Log.e(TAG, "Tx Power = " + String.valueOf(power));
                        Log.e(TAG, "URL = " + es.getURL());
                        String url = "";
                        url = es.getURL().toString();


                        Cursor c = dbRecentAdapter.getRow(Utilities.getDateInString());

                        boolean found = false;

                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            do {
                                String link = c.getString(DBRecentAdapter.COL_LINK);
                                if (link.contentEquals(url)) {
                                    found = true;
                                }
                            } while (c.moveToNext());
                        }


                        if (url != null && !found) {

                            PendingIntent contentIntent = PendingIntent.getActivity(MyService.this, 0, new Intent(MyService.this, MainActivity.class), 0);

                            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            mBuilder.setSmallIcon(android.R.drawable.btn_dialog);
                            mBuilder.setAutoCancel(true);

                            mBuilder.setContentIntent(contentIntent);

                            Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            mBuilder.setSound(notifSound);

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            mBuilder.setContentTitle("Link Recieved");
                            mBuilder.setContentText(url);
                            mNotificationManager.notify(1, mBuilder.build());
                            notificationIntent = new Intent(MyService.this, MainActivity.class);

                            dbRecentAdapter.insertRow(url, Utilities.getDayInString(), Utilities.getDateInString(), 0, 0);

                            appPrefs.setIsNewLink(true);
                        }
                    }

                }
            }
        }

    };

    private void markAttendance() {

        Cursor c = dbAttendanceAdapter.getRow(Utilities.getOnlyDateInString());

        if (c.getCount() == 0) {
            dbAttendanceAdapter.insertRow(Utilities.getOnlyDateInString(), Utilities.getDayInString(), Utilities.getOnlyMonthInString(), 3, 1, 1, 0, 0);

        } else {
            c.moveToFirst();
            int attClasses = c.getInt(DBAttendanceAdapter.COL_ATTENDED_CLASSES);
            int totalClasses = c.getInt(DBAttendanceAdapter.COL_TOTAL_CLASSES);

            int slot2 = 0;
            int slot3 = 0;
            if (slot.contentEquals("slot2")) {
                slot2 = 1;

            } else if (slot.contentEquals("slot3")) {
                slot3 = 1;
            }
            if (attClasses <= totalClasses)
                dbAttendanceAdapter.updateRow(Utilities.getOnlyDateInString(),
                        Utilities.getDayInString(), Utilities.getOnlyMonthInString(), 3, attClasses + 1, 1, slot2, slot3);
        }
        Toast.makeText(this, "Attendance Marked for " + slot, Toast.LENGTH_SHORT).show();
        handlerStarted = false;
        slot = null;

    }

    public String hexToString(String txtInHex) {
        String[] msg = txtInHex.split("-");
        String hexMsg = msg[0] + msg[1] + msg[2] + msg[3];

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hexMsg.length(); i += 2) {
            str.append((char) Integer.parseInt(hexMsg.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

    /**
     * bytesToHex method
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}