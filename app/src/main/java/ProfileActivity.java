package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends Activity implements ProPicDialogFragment.YesNoListener{
    // Uri for initial camera gallery save
    private Uri imageCaptureUri;
    // Uri for cropped image path
    private Uri profileImageUri;

    // Intent request codes
    public static final int CAMERA_PIC_REQUEST = 1;
    public static final int PIC_CROP = 2;
    public static final int SELECT_PICTURE = 3;

    // Invoke intent to return data
    public static final String RETURN_DATA = "edu.dartmouth.cs.myruns.RETURN_DATA";

    // Instance state save keys for bundle
    private static final String URI_INSTANCE_STATE_KEY = "edu.dartmouth.cs.myruns.URI_KEY";
    private static final String PROFILE_INSTANCE_STATE_KEY = "edu.dartmouth.cs.myruns.PROFILE_KEY";

    private static final String TAG = "MyRuns";

    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        // Retrieve shared preferences
        loadUserData();

        // Retrieve instance state data
        if (savedInstanceState != null) {
            // If phone rotated during camera activity, file location must be retrieved
            imageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);

            // If a new profile picture is taken but not saved, make sure it isn't lost
            // upon phone rotation or other events causing main activity to be recreated
            profileImageUri = savedInstanceState.getParcelable(PROFILE_INSTANCE_STATE_KEY);

            // Check to make sure a new profile picture has been taken
            if (profileImageUri != null) {
                ImageView image = (ImageView) findViewById(R.id.profile_image);

                // Retrieve image data and apply to ImageView
                try {
                    Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(profileImageUri));
                    if (bm != null) {
                        image.setImageBitmap(bm);
                        image.setTag(profileImageUri);
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onYes() {
        camera_click();
    }

    @Override
    public void onNo() {
        gallery_click();
    }

    // Dialog fragment starts and dialog pops up when change button is clicked
    public void changeClick(View view){

        new ProPicDialogFragment().show(getFragmentManager(), "tag");

    }

    //Start the gallery selection activity
    private void gallery_click(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    // Start camera activity, and give it a file path to save to
    private void camera_click() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(RETURN_DATA, true);

        // Make sure device has a camera
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            // Handle IO exception possibility upon file creation
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                String errorMessage = "Could not create photo file";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }

            // If file creation is successful, provide it to the camera intent and start it
            if (photoFile != null) {
                imageCaptureUri=Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    // Helper method for image file creation
    private File createImageFile() throws IOException {

        // Create a unique image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyRuns");
        // Create application photo directory if it doesn't already exist
        if (! storageDir.exists()){
            if (! storageDir.mkdirs()){
                Log.d("MyRuns", "failed to create directory");
                return null;
            }
        }

        // Create the file
        File image = new File(storageDir.getAbsolutePath() + File.separator + imageFileName + ".jpg");

        return image;
    }

    // Handle activity for result return
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // If the photo is successfully taken, begin cropping
            if (requestCode == CAMERA_PIC_REQUEST){
                Log.d(TAG,"handling crop");
                Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageCaptureUri);
                sendBroadcast(scanFileIntent);
                performCrop();
            }

            // If the cropping is successful, add the new picture to the image view along
            // with its file path for future retrieval
            else if(requestCode == PIC_CROP){
                Intent scanFileIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, profileImageUri);
                sendBroadcast(scanFileIntent);

                ImageView image = (ImageView) findViewById(R.id.profile_image);

                try {
                    Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(profileImageUri));
                    if (bm != null) {
                        image.setImageBitmap(bm);
                        image.setTag(profileImageUri);
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            else if(requestCode == SELECT_PICTURE){
                Log.d(TAG,"handling crop");
                Uri tempUri = data.getData();
                File F = new File(getRealPathFromURI(tempUri));
                imageCaptureUri = Uri.fromFile(F);
                performCrop();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);

        cursor.moveToFirst();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        String filename = cursor.getString(column_index);
        cursor.close();

        return filename;
    }

    // Method for cropping image
    private void performCrop() {
        try {
            // Call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            Log.d(TAG,"crop intent created");

            // Give the crop activity the file and type to work with
            cropIntent.setDataAndType(imageCaptureUri, "image/*");

            // Set crop properties
            cropIntent.putExtra("crop", "true");

            // Indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);

            // Indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);

            // Retrieve data on return
            cropIntent.putExtra(RETURN_DATA, true);

            File photoFile = null;

            // Try to create a file for the cropped image
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                String errorMessage = "Could not create photo file";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }

            // If file creation is successful, add the path to the intent and start it
            if (photoFile != null) {
                profileImageUri = Uri.fromFile(photoFile);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, profileImageUri);
                startActivityForResult(cropIntent, PIC_CROP);
            }
        }

        catch (ActivityNotFoundException anfe) {
            //Display error message
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Make sure to save file paths on device destruction for future retrieval
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(URI_INSTANCE_STATE_KEY, imageCaptureUri);
        outState.putParcelable(PROFILE_INSTANCE_STATE_KEY, profileImageUri);
        super.onSaveInstanceState(outState);
    }

    // Called when save button is clicked
    public void onSaveClicked(View v) {

        // Save the user information into a "shared preferences"
        // using private helper function

        saveUserData();

        Toast.makeText(getApplicationContext(), R.string.saved_message,
                Toast.LENGTH_SHORT).show();

        finish();
    }

    public void onCancelClicked(View v) {

        // Exit the app without saving

        Toast.makeText(getApplicationContext(),
                getString(R.string.cancel_message), Toast.LENGTH_SHORT).show();

        finish();

    }

    // Saves all user data in a shared preferences object
    private void saveUserData() {

        Log.d(TAG, "saveUserData()");

        // Getting the shared preferences editor
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.clear();

        // Save profile image uri
        ImageView image = (ImageView) findViewById(R.id.profile_image);
        mKey = getString(R.string.preference_key_profile_uri);
        if (image.getTag() != null){
            mEditor.putString(mKey, image.getTag().toString());
        }

        // Save name information
        mKey = getString(R.string.preference_key_profile_name);
        String mValue = (String) ((EditText) findViewById(R.id.name_input))
                .getText().toString();
        mEditor.putString(mKey, mValue);

        // Save email information
        mKey = getString(R.string.preference_key_profile_email);
        mValue = (String) ((EditText) findViewById(R.id.email_input))
                .getText().toString();
        mEditor.putString(mKey, mValue);

        //Save phone information
        mKey = getString(R.string.preference_key_profile_phone);
        mValue = (String) ((EditText) findViewById(R.id.phone_input))
                .getText().toString();
        mEditor.putString(mKey, mValue);

        // Read which index the radio is checked.
        // Save gender information
        mKey = getString(R.string.preference_key_profile_gender);

        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.gender_input);
        int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
                .getCheckedRadioButtonId()));
        mEditor.putInt(mKey, mIntValue);

        // Save class information
        mKey = getString(R.string.preference_key_profile_class);
        mValue = (String) ((EditText) findViewById(R.id.class_input))
                .getText().toString();
        mEditor.putString(mKey, mValue);

        // Save major information
        mKey = getString(R.string.preference_key_profile_major);
        mValue = (String) ((EditText) findViewById(R.id.major_input))
                .getText().toString();
        mEditor.putString(mKey, mValue);

        // Commit all the changes into the shared preference
        mEditor.commit();

    }

    // Retrieve all saved user data from the shared preference
    private void loadUserData() {

        // We can also use log.d to print to the LogCat

        Log.d(TAG, "loadUserData()");

        // Load and update all profile views

        // Get the shared preferences - create or retrieve the activity
        // preference object
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        // Try to load profile image from uri string
        ImageView image = (ImageView) findViewById(R.id.profile_image);
        mKey = getString(R.string.preference_key_profile_uri);
        String mValue = mPrefs.getString(mKey, null);

        if (mValue != null) {
            Uri saved_profile_uri = Uri.parse(mValue);
            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(saved_profile_uri));
                image.setImageBitmap(bm);
                image.setTag(saved_profile_uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Load the user name
        mKey = getString(R.string.preference_key_profile_name);
        mValue = mPrefs.getString(mKey, "");
        ((EditText) findViewById(R.id.name_input)).setText(mValue);

        // Load the user email
        mKey = getString(R.string.preference_key_profile_email);
        mValue = mPrefs.getString(mKey, "");
        ((EditText) findViewById(R.id.email_input)).setText(mValue);

        // Load the user email
        mKey = getString(R.string.preference_key_profile_phone);
        mValue = mPrefs.getString(mKey, "");
        ((EditText) findViewById(R.id.phone_input)).setText(mValue);

        // Please Load gender info and set radio box
        mKey = getString(R.string.preference_key_profile_gender);

        int mIntValue = mPrefs.getInt(mKey, -1);
        // In case there isn't one saved before:
        if (mIntValue >= 0) {
            // Find the radio button that should be checked.
            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.gender_input))
                    .getChildAt(mIntValue);
            // Check the button.
            radioBtn.setChecked(true);
        }

        // Load the user class
        mKey = getString(R.string.preference_key_profile_class);
        mValue = mPrefs.getString(mKey, "");
        ((EditText) findViewById(R.id.class_input)).setText(mValue);

        // Load the user major
        mKey = getString(R.string.preference_key_profile_major);
        mValue = mPrefs.getString(mKey, "");
        ((EditText) findViewById(R.id.major_input)).setText(mValue);

    }
}
