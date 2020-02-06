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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import center.claims.mirascon.mirascon.BuildConfig;
import center.claims.mirascon.mirascon.Interface.OnDialogButtonClickListener;
import center.claims.mirascon.mirascon.R;

public class Camera extends Activity {


    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private int pic = 0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private File photoFile = null;
    private File photoFile1 = null;
    private File photoFile2 = null;
    private File photoFile3 = null;
    private File photoFile4 = null;
    String mCurrentPhotoPath;
    private Intent cameraIntent;
    private ImageView back;
    private Uri photoURI;
    ArrayList<Uri> uris = new ArrayList<Uri>();
    private String f_name_text;
    private String l_name_text;
    private String lp_text;
    private String customer_id_text;
    private String phone_text;
    private String email_text;
    private String latLng_text;

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
    @IntDef({DialogType.DIALOG_DENY, DialogType.DIALOG_NEVER_ASK})
    @Retention(RetentionPolicy.SOURCE)
    @interface DialogType {
        int DIALOG_DENY = 0, DIALOG_NEVER_ASK = 1;
    }

    OnDialogButtonClickListener onDialogButtonClickListener = new OnDialogButtonClickListener() {
        @Override
        public void onPositiveButtonClicked() {
            switch (mDialogType) {
                case DialogType.DIALOG_DENY:
                    String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
                    if (!hasPermissions(Camera.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) Camera.this, PERMISSIONS, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                    break;
                case DialogType.DIALOG_NEVER_ASK:
                    //redirectToAppSettings(Camera.this);
                    break;

            }
        }

        @Override
        public void onNegativeButtonClicked() {

        }
    };

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.imageView1 = (ImageView) this.findViewById(R.id.imageView1);
        this.imageView2 = this.findViewById(R.id.imageView2);
        this.imageView3 = this.findViewById(R.id.imageView3);
        this.imageView4 = this.findViewById(R.id.imageView4);
        back = findViewById(R.id.navigation_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button sendButton = this.findViewById(R.id.send);
        uris = new ArrayList<Uri>();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                f_name_text = null;
                l_name_text = null;
                lp_text = null;
                customer_id_text = null;
                phone_text = null;
                email_text = null;
                latLng_text = null;
            } else {
                f_name_text = extras.getString("fName");
                l_name_text = extras.getString("lName");
                lp_text = extras.getString("lp");
                customer_id_text = extras.getString("customer");
                phone_text = extras.getString("phone");
                email_text = extras.getString("email");
                latLng_text = extras.getString("latLng");
            }
        } else {
            f_name_text = (String) savedInstanceState.getSerializable("fName");
            l_name_text = (String) savedInstanceState.getSerializable("lName");
            lp_text = (String) savedInstanceState.getSerializable("lp");
            customer_id_text = (String) savedInstanceState.getSerializable("customer");
            phone_text = (String) savedInstanceState.getSerializable("phone");
            email_text = (String) savedInstanceState.getSerializable("email");
            latLng_text = (String)savedInstanceState.getSerializable("latLng");
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"claims@mirascon.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Claims Form");
                i.putExtra(Intent.EXTRA_TEXT, "First Name: " + f_name_text + ",\nLast Name: " + l_name_text + ",\nLicense Plate: " + lp_text + ",\n Customer Ientification: " + customer_id_text + ",\n Phone Number: " + phone_text + ",\n EMail: " + email_text + ",\n Location: " + latLng_text);

                if (photoFile1 != null) {
                    uris.add(pic, FileProvider.getUriForFile(Camera.this, BuildConfig.APPLICATION_ID + ".provider", photoFile1));
                    pic++;
                    Log.v("uris", ""+pic);
                }
                if (photoFile2 != null) {
                    uris.add(pic, FileProvider.getUriForFile(Camera.this, BuildConfig.APPLICATION_ID + ".provider", photoFile2));
                    pic++;
                    Log.v("uris", ""+pic);
                }
                if (photoFile3 != null) {
                    uris.add(pic, FileProvider.getUriForFile(Camera.this, BuildConfig.APPLICATION_ID + ".provider", photoFile3));
                    pic++;
                    Log.v("uris", ""+pic);
                }
                if (photoFile4 != null) {
                    uris.add(pic, FileProvider.getUriForFile(Camera.this, BuildConfig.APPLICATION_ID + ".provider", photoFile4));
                    pic++;
                    Log.v("uris", ""+pic);
                }

                i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Camera.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View v) { // TODO Auto-generated method stub
        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(Camera.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) Camera.this, PERMISSIONS, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
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
                    case R.id.imageView3:
                        if (photoFile != null) {
                            takePhoto(3);
                        }
                        break;
                    case R.id.imageView4:
                        if (photoFile != null) {
                            takePhoto(4);
                        }
                        break;
                }
            }
        }
    }

    private void takePhoto(int reqestCode) {
        Uri photoUri = FileProvider.getUriForFile(Camera.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);//android.provider.MediaStore.EXTRA_OUTPUT
        startActivityForResult(cameraIntent, reqestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photo1;
        Bitmap photo2;
        Bitmap photo3;
        Bitmap photo4;
        if (requestCode == 1 && resultCode == RESULT_OK) {

            //change this
            //extras = data.getExtras();
            //photo1 = (Bitmap) extras.get("data");
            photo1 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile1 = photoFile;
            imageView1.setImageBitmap(photo1);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photo2 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile2 = photoFile;
            imageView2.setImageBitmap(photo2);
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            photo3 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile3 = photoFile;
            imageView3.setImageBitmap(photo3);
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            photo4 = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            galleryAddPic();
            photoFile4 = photoFile;
            imageView4.setImageBitmap(photo4);
        }
        // Bitmap photo2 = BitmapFactory.decodeFile(photoFile2.getAbsolutePath());

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
                        mDialogType = DialogType.DIALOG_DENY;
                        // Show dialog with
                        openAlertDialog(mRequestPermissions, mGrantPermissions, mCancel, onDialogButtonClickListener, Camera.this);
                    } else {
                        //explain to user why we need the permissions and ask him to go to settings to enable it
                        mDialogType = DialogType.DIALOG_NEVER_ASK;
                        openAlertDialog(mRequsetSettings, mGoToSettings, mCancel, onDialogButtonClickListener, Camera.this);
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

