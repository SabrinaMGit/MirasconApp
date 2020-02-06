package center.claims.mirascon.mirascon.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import center.claims.mirascon.mirascon.BuildConfig;
import center.claims.mirascon.mirascon.Interface.OnDialogButtonClickListener;
import center.claims.mirascon.mirascon.R;

public class GlassDamage extends AppCompatActivity {

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private int pic = 0;
    private ImageView imageView1;
    private ImageView imageView2;
    private File photoFile = null;
    private File photoFile1 = null;
    private File photoFile2 = null;
    String mCurrentPhotoPath;
    private Intent cameraIntent;
    private ImageView back;
    private Uri photoURI;
    ArrayList<Uri> uris = new ArrayList<Uri>();

    private Toolbar toolbar;
    private EditText when;
    private EditText where;
    private EditText what;
    private Button sendButton;

    private int mDialogType;
    private String mRequestPermissions = "We are requesting the camera and Gallery permission as it is absolutely necessary for the app to perform it\'s functionality.\nPlease select \"Grant Permission\" to try again and \"Cancel \" to exit the application.";
    private String mRequsetSettings = "You have rejected the camera and Gallery permission for the application. As it is absolutely necessary for the app to perform you need to enable it in the settings of your device.\nPlease select \"Go to settings\" to go to application settings in your device and \"Cancel \" to exit the application.";
    private String mGrantPermissions = "Grant Permissions";
    private String mCancel = "Cancel";
    private String mGoToSettings = "Go To Settings";
    private String mPermissionRejectWarning = "Cannot Proceed Without Permissions";

    //requests for runtime time permissions
    String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    // type of dialog opened in MainActivity
    @IntDef({Camera.DialogType.DIALOG_DENY, Camera.DialogType.DIALOG_NEVER_ASK})
    @Retention(RetentionPolicy.SOURCE)
    @interface DialogType {
        int DIALOG_DENY = 0, DIALOG_NEVER_ASK = 1;
    }

    OnDialogButtonClickListener onDialogButtonClickListener = new OnDialogButtonClickListener() {
        @Override
        public void onPositiveButtonClicked() {
            switch (mDialogType) {
                case Camera.DialogType.DIALOG_DENY:
                    String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
                    if (!hasPermissions(GlassDamage.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) GlassDamage.this, PERMISSIONS, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                    break;
                case Camera.DialogType.DIALOG_NEVER_ASK:
                    //redirectToAppSettings(Camera.this);
                    break;

            }
        }

        @Override
        public void onNegativeButtonClicked() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glassdamage);
        setSupportActionBar(toolbar);
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uris = new ArrayList<Uri>();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"claims@mirascon.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Claims Form");
                i.putExtra(Intent.EXTRA_TEXT, "When: "+ when.getText().toString()+ " \nWhere: "+ where.getText().toString()+"\nWhat: " + what.getText().toString());



                if (photoFile1 != null) {
                    uris.add(pic, FileProvider.getUriForFile(GlassDamage.this, BuildConfig.APPLICATION_ID + ".provider", photoFile1));
                    pic++;
                    Log.v("uris", ""+pic);
                }
                if (photoFile2 != null) {
                    uris.add(pic, FileProvider.getUriForFile(GlassDamage.this, BuildConfig.APPLICATION_ID + ".provider", photoFile2));
                    pic++;
                    Log.v("uris", ""+pic);
                }

                i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(GlassDamage.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initView(){
        this.imageView1 = (ImageView) this.findViewById(R.id.imageView1);
        this.imageView2 = this.findViewById(R.id.imageView2);
        back = findViewById(R.id.navigation_back);
        sendButton = this.findViewById(R.id.send);
        when = findViewById(R.id.when_edittext);
        where = findViewById(R.id.where_edittext);
        what = findViewById(R.id.what_edittext);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View v) { // TODO Auto-generated method stub
        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(GlassDamage.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) GlassDamage.this, PERMISSIONS, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();

                } catch (IOException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
                switch (v.getId()) {

                    case R.id.imageView1:

                        if (photoFile != null) {
                            takePhoto(1);
                        }
                        break;
                    case R.id.imageView2:

                        if (photoFile != null) {
                            takePhoto(2);
                        }
                        break;
                }
            }
        }
    }

    private void takePhoto(int reqestCode) {
        Uri photoUri = FileProvider.getUriForFile(GlassDamage.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);//android.provider.MediaStore.EXTRA_OUTPUT
        startActivityForResult(cameraIntent, reqestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo1;
        Bitmap photo2;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            photo1 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile1 = photoFile;
            imageView1.setImageBitmap(photo1);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photo2 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile2 = photoFile;
            imageView2.setImageBitmap(photo2);
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                } else {
                    boolean showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION);
                    boolean showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION);
                    boolean showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION);
                    if (showRationale1 && showRationale2 && showRationale3) {
                        //explain to user why we need the permissions
                        mDialogType = Camera.DialogType.DIALOG_DENY;
                        // Show dialog with
                        openAlertDialog(mRequestPermissions, mGrantPermissions, mCancel, onDialogButtonClickListener, GlassDamage.this);
                    } else {
                        //explain to user why we need the permissions and ask him to go to settings to enable it
                        mDialogType = Camera.DialogType.DIALOG_NEVER_ASK;
                        openAlertDialog(mRequsetSettings, mGoToSettings, mCancel, onDialogButtonClickListener, GlassDamage.this);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void openAlertDialog(String message, String positiveBtnText, String
            negativeBtnText,
                                       final OnDialogButtonClickListener listener, Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onPositiveButtonClicked();
            }
        });
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onNegativeButtonClicked();
            }
        });

        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.create().show();
    }


}
