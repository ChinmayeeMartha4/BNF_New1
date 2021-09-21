package com.example.mhealth;

import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.EventDetailsModel;
import com.example.mhealth.helper.EventMeetingModel;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.utils.EventMeetingsViewActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class Activity_AddEvent extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String mCurrentPhotoPath = "", mCurrentPhotoPath1="";
    private ImageLoadingUtils utils;
    int events_id=0;
    TextView tvTitleText;
    static EditText etxtFromDate, etxtToDate, etxtEventDate;
    EditText etAttendees,etAbsence,  etMale, etFemale, etBoys, etGirls, etEventOthers;
    EventDetailsModel eventDetailsModel;
    ImageView btnImage1, btnImage2, btnImage3;
    private Bitmap bitmap, bitmap2;
    String image64 = "";
    public static android.app.Dialog submit_alert;
    private Context context=this;
    SharedPrefHelper sharedPrefHelper;
    Spinner spnEvents, spnEventType;
    String strEvents, strEventsType;
    Button btnSave;
    ImageView ivTitleBack;
    SqliteHelper sqliteHelper;
    RelativeLayout rlEventOthers;

    String[] eventsAL={"Select Event", "Campaign", "Nukkad", "Regular Meetings","Screening of Under five Children", "Others"};
    String[] eventTypeAL={"Select Event Type", "Direct", "Indirect", "Both"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_event);

        tvTitleText= findViewById(R.id.tvTitleText);
        etAbsence= findViewById(R.id.etAbsence);
        etxtEventDate= findViewById(R.id.etxtEventDate);
        etMale= (EditText) findViewById(R.id.etMale);
        etFemale= (EditText) findViewById(R.id.etFemale);
        etBoys= (EditText) findViewById(R.id.etBoys);
        etGirls= (EditText) findViewById(R.id.etGirls);
        etEventOthers= (EditText) findViewById(R.id.etEventOthers);
        btnSave= (Button) findViewById(R.id.btnSave);
        btnImage1= findViewById(R.id.btnImage1);
        btnImage2= findViewById(R.id.btnImage2);
        btnImage3= findViewById(R.id.btnImage3);
        ivTitleBack= findViewById(R.id.ivTitleBack);
        spnEvents= findViewById(R.id.spnEvents);
        spnEventType= findViewById(R.id.spnEventType);
        sharedPrefHelper=new SharedPrefHelper(this);
        eventDetailsModel= new EventDetailsModel();
        sqliteHelper=new SqliteHelper(this);
        rlEventOthers=findViewById(R.id.rlEventOthers);

        tvTitleText.setText(R.string.activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            events_id = bundle.getInt("event_id", 0);
        }

        sharedPrefHelper.setInt("events_id",events_id);
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.event_exit_message)+ "?");

                builder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_AddEvent.this, EventMeetingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });



        ArrayAdapter  event_adapter= new ArrayAdapter(this, android.R.layout.simple_spinner_item, eventsAL);
        event_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEvents.setAdapter(event_adapter);

        spnEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!spnEvents.getSelectedItem().toString().trim().equalsIgnoreCase("Select Event")) {
                    if (spnEvents.getSelectedItem().toString().trim() != null) {
                        strEvents = spnEvents.getSelectedItem().toString().trim();
                        if(strEvents.equals("Others")){
                            rlEventOthers.setVisibility(View.VISIBLE);
                        }else{
                            rlEventOthers.setVisibility(View.GONE);

                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter event_type_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypeAL);
        event_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEventType.setAdapter(event_type_adapter);

        spnEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!spnEventType.getSelectedItem().toString().trim().equalsIgnoreCase("Select Event Type")) {
                    if (spnEventType.getSelectedItem().toString().trim() != null) {
                        strEventsType = spnEventType.getSelectedItem().toString().trim();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressLint("NewApi")
    public void show_callender1(View vw) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    public void click_Image(View v) {
        if (Build.VERSION.SDK_INT > 19) {
            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                intentForCamera(getpic, 1);
            } catch (Exception e) {
                Log.d("exp_result:", e.getMessage().toString());
            }
        }
        if (Build.VERSION.SDK_INT < 20) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(),
                    "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    private void intentForCamera(Intent getpic, int requestCode) {
        try {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                getpic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(getpic, requestCode);
            }

        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "branding" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @SuppressLint("ResourceAsColor")
    public Bitmap compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 1024.0f;
        float maxWidth = 812.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        utils = new ImageLoadingUtils(this);
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;


        Matrix scaleMatrix = new Matrix();
//        Matrix scaleMatrix = {0.9846154, 0.0, 4.4307556; 0.0, 0.9846154, 7.8769226][0.0, 0.0, 1.0]};

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        String lat = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        try {
            Location location = getLatitudeLogitude();
            lat = "Lat.: " + location.getLatitude();
            lat += " Long.:" + location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        Paint tPaint = new Paint();
        tPaint.setTextSize(24);
        tPaint.setColor(Color.parseColor("#E51900"));
        tPaint.setStyle(Paint.Style.FILL);

        Paint mpaint = new Paint();
        mpaint.setColor(Color.parseColor("#40FFFFFF"));
        mpaint.setStyle(Paint.Style.FILL);
        if (ratioX < 1) {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 65f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 25f, scaledBitmap.getHeight() - 35f, tPaint);
            canvas.drawText(lat, 25f, scaledBitmap.getHeight() - 10f, tPaint);
        } else {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 120f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 50f, scaledBitmap.getHeight() - 95f, tPaint);
            canvas.drawText(lat, 50f, scaledBitmap.getHeight() - 70f, tPaint);
        }
//        canvas.drawRect(middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,tPaint);

        ExifInterface exif;
        try {

            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledBitmap;

    }

    public Location getLatitudeLogitude() {
        GPSTracker gpsTracker = new GPSTracker(this);
        return gpsTracker.getLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (Build.VERSION.SDK_INT > 19) {
                //for camera intent
                if (requestCode == 1) {
                    try {
                        Uri imageUri = Uri.parse(mCurrentPhotoPath);
                        Uri imageUri1 = Uri.parse(mCurrentPhotoPath1);
                        File file = new File(imageUri.getPath());
                        File file1 = new File(imageUri1.getPath());

                        ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                        bitmap = imageLatLogHeper.compressImage(file + "", "");
                        btnImage1.setImageBitmap(bitmap);
                        btnImage2.setImageBitmap(bitmap);
                        btnImage3.setImageBitmap(bitmap);
                        image64 = encodeTobase64(bitmap);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (Build.VERSION.SDK_INT < 20) {
                try {
                    if (resultCode == RESULT_OK) {

                        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                            File f = new File(Environment.getExternalStorageDirectory().toString());
                            for (File temp : f.listFiles()) {
                                if (temp.getName().equals("temp.jpg")) {
                                    f = temp;
                                    break;
                                }
                            }
                            String selectedImagePath = "";
                            try {
                                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);

                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", "");

                                btnImage1.setImageBitmap(bitmap);
                                btnImage2.setImageBitmap(bitmap);
                                btnImage3.setImageBitmap(bitmap);
                                OutputStream fOut = null;

                                try {
                                    fOut = new FileOutputStream(f);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 5, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    image64 = encodeTobase64(bitmap);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 15, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void click_save(View view) {
        if(CheckValidation()) {
            eventDetailsModel.setNo_of_absence(etAbsence.getText().toString().trim());
            eventDetailsModel.setEvent_date(etxtEventDate.getText().toString().trim());
            eventDetailsModel.setMale(etMale.getText().toString().trim());
            eventDetailsModel.setFemale(etFemale.getText().toString().trim());
            eventDetailsModel.setBoys(etBoys.getText().toString().trim());
            eventDetailsModel.setGirls(etGirls.getText().toString().trim());
            eventDetailsModel.setOthers(etEventOthers.getText().toString().trim());
            eventDetailsModel.setEvents_title(strEvents);
            eventDetailsModel.setEvent_type(strEventsType);
            eventDetailsModel.setImage(image64);

            final long local_id = sqliteHelper.AddEventData(eventDetailsModel, events_id);

            if (local_id > 0) {

                showSubmitDialog(this, getString(R.string.add_attendees),
                        getString(R.string.attendees_done_successfully));

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.try_again),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean CheckValidation() {

        if (etMale.getText().toString().trim().length()==0){
            etMale.setFocusable(true);
            etMale.setError(getString(R.string.please_enter_male_attendees));
            return false;
        }if (etFemale.getText().toString().trim().length()==0){
            etFemale.setFocusable(true);
            etFemale.setError(getString(R.string.please_enter_female_attendees));
            return false;
        }if (etBoys.getText().toString().trim().length()==0){
            etBoys.setFocusable(true);
            etBoys.setError(getString(R.string.please_enter_boys_attendees));
            return false;
        }if (etGirls.getText().toString().trim().length()==0){
            etGirls.setFocusable(true);
            etGirls.setError(getString(R.string.please_enter_girl_attendees));
            return false;
        }
        if (etAbsence.getText().toString().trim().length()==0){
            etAbsence.setFocusable(true);
            etAbsence.setError(getString(R.string.please_enter_not_available_attendees));
            return false;
        }

        if (spnEvents.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spnEvents.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spnEvents.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (spnEventType.getSelectedItemPosition() > 0) {
            String itemvalue = String.valueOf(spnEventType.getSelectedItem());
        } else {
            TextView errorTextview = (TextView) spnEventType.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }

        if (etxtEventDate.getText().toString().trim().length()==0){
            etxtEventDate.setFocusable(true);
            etxtEventDate.setError(getString(R.string.please_enter_event_date));
            return false;
        }

        return true;
    }

    public static void showSubmitDialog(Context context, String infoTitle, String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submit_alert_dialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvInfoTitle = (TextView) submit_alert.findViewById(R.id.tv_info_title);
        TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvInfoTitle.setText(infoTitle);
        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
                Intent intent=new Intent(context, EventMeetingActivity.class);
                intent.putExtra("attendeesStatus","1");
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                String dt = day + "-" + month + "-" + year;

                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());

                /*
                 * Calendar c = Calendar.getInstance(); c.set(year, month, day,
                 * 0, 0); SimpleDateFormat sdf = new
                 * SimpleDateFormat("dd/MM/yyyy");
                 */

                etxtEventDate.setText(formattedDate);

            } catch (Exception e) {
                Log.d("", "");
            }
        }
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment1 extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                String dt = day + "-" + month + "-" + year;

                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());

                /*
                 * Calendar c = Calendar.getInstance(); c.set(year, month, day,
                 * 0, 0); SimpleDateFormat sdf = new
                 * SimpleDateFormat("dd/MM/yyyy");
                 */

                etxtToDate.setText(formattedDate);

            } catch (Exception e) {
                Log.d("", "");
            }
        }
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage("Are you sure to exit from Activity"  +  "?");

        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_AddEvent.this,
                        EventMeetingsViewActivity.class);
                intent.putExtra("eventId", events_id);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}