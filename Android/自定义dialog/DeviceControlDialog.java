package com.eshare.dlna.dmc.gui.dialog;

import com.eshare.emusic.client.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

public class DeviceControlDialog extends AlertDialog{

public DeviceControlDialog(Context context, int theme) {
    super(context, theme);
}

public DeviceControlDialog(Context context) {
    super(context);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_device_control);
}
}