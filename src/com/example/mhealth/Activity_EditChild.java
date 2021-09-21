package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class Activity_EditChild extends Activity implements AdapterView.OnItemSelectedListener {


    TextView txtEditChild, txtHHID, txtChildName, txtParent, txtDOB, txtGender, txtBirthWeight, txtBirthHeight, txtBirthMuac, txtChildHB, txtEdema, txtDisability;
    TextView txtBirthOrder, txtFooter;
    Spinner etxtHHID;
    EditText etxtChildName, etxtParent, etxtDOB, etxtGender, etxtBirthWeight, etxtBirthHeight, etxtBirthMuac,
            etxtChildHB, etxtDisability, etxtBirthOrder, etxtEdema;
    Button btnEditChild;

    String strEditChild, strHHID, strdob, strbweight, strBorder, strParent, strdisablity, strbirthHeight, strMuac, strEdema, strfullNameOfChild;
    String strgender, strSubmit, strDone, strTryagain, strYes, strNo, strCancel, strFooter, strOnlyAlpha, strMandatory, strChildHB;

    int childID = 0;

    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;

    Child child;


    ProgressDialog progressDialog;


    ServerHelper serverhelper;

    private Bitmap bitmap;
    private int user_id;

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        initialization();
        setLanguage();

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Edit/Edit_Child_Registration_HB_List/ Edit Child Registration HB");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        Bundle b = getIntent().getExtras();
        childID = b.getInt("index");

        setData();
    }

    public void setData() {
        String[] cinfo = sqliteHelper.getChildInfo(childID);
        //etxtHHID.setText(cinfo[0].toString());
        etxtChildName.setText(cinfo[1].toString());
        etxtParent.setText(cinfo[2].toString());
        etxtDOB.setText(cinfo[3].toString());
        etxtGender.setText(cinfo[4].toString());
        etxtBirthWeight.setText(cinfo[5].toString());
        etxtBirthHeight.setText(cinfo[6].toString());
        etxtBirthMuac.setText(cinfo[7]);
        etxtChildHB.setText(cinfo[8]);
        etxtDisability.setText(cinfo[9].toString());
        etxtBirthOrder.setText(cinfo[10]);
        etxtEdema.setText("0");
        populateList(etxtHHID, "eligible_family", "familyid", "house_number",
                "Select Household Id", "", cinfo[0].toString());
    }

    public void initialization() {

        child = new Child();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);

        txtEditChild = (TextView) findViewById(R.id.txtEditChild);
        txtHHID = (TextView) findViewById(R.id.txtHholdId);
        txtChildName = (TextView) findViewById(R.id.txtChildName);
        txtParent = (TextView) findViewById(R.id.txtParent);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtBirthHeight = (TextView) findViewById(R.id.txtBirthHeight);
        txtBirthWeight = (TextView) findViewById(R.id.txtBirthWeight);
        txtBirthMuac = (TextView) findViewById(R.id.txtBirthMuac);
        txtChildHB = (TextView) findViewById(R.id.txtChildHB);
        txtBirthOrder = (TextView) findViewById(R.id.txtBirthOrder);
        txtDisability = (TextView) findViewById(R.id.txtDisability);
        txtEdema = (TextView) findViewById(R.id.txtEdema);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        etxtHHID = (Spinner) findViewById(R.id.etxtHHID);
        etxtParent = (EditText) findViewById(R.id.etxtParent);
        etxtGender = (EditText) findViewById(R.id.etxtGender);
        etxtBirthHeight = (EditText) findViewById(R.id.etxtBirthHeight);
        etxtBirthWeight = (EditText) findViewById(R.id.etxtBirthWeight);
        etxtDOB = (EditText) findViewById(R.id.etxtDOB);
        etxtChildName = (EditText) findViewById(R.id.etxtEditChildName);
        etxtBirthMuac = (EditText) findViewById(R.id.etxtBirthMuac);
        etxtChildHB = (EditText) findViewById(R.id.etxtEditChildHB);
        etxtDisability = (EditText) findViewById(R.id.etxtDisability);
        etxtBirthOrder = (EditText) findViewById(R.id.etxtBirthOrder);
        etxtEdema = (EditText) findViewById(R.id.etxtEdema);

        btnEditChild = (Button) findViewById(R.id.btnEditChild);

    }

    private void setLanguage() {

        String languageId = sph.getString("Language", "1");// getting languageId
        strEditChild = sqliteHelper.LanguageChanges(ConstantValue.LANChild, languageId);
        strHHID = sqliteHelper.LanguageChanges(ConstantValue.LANHHId, languageId);
        strdob = sqliteHelper.LanguageChanges(ConstantValue.LANDateOfBirth, languageId);
        strbweight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight, languageId);
        strBorder = sqliteHelper.LanguageChanges(ConstantValue.LANBirthOrder, languageId);
        strParent = sqliteHelper.LanguageChanges(ConstantValue.LANMother, languageId);
        strdisablity = sqliteHelper.LanguageChanges(ConstantValue.LANDisability, languageId);
        strbirthHeight = sqliteHelper.LanguageChanges(ConstantValue.LANBirthHeight, languageId);
        strMuac = sqliteHelper.LanguageChanges(ConstantValue.LANMuac, languageId);
        strEdema = sqliteHelper.LanguageChanges(ConstantValue.LANEdema, languageId);
        strfullNameOfChild = sqliteHelper.LanguageChanges(ConstantValue.LANChildName, languageId);
        strgender = sqliteHelper.LanguageChanges(ConstantValue.LANGender, languageId);
        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);

        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone, languageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, languageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, languageId);
        strChildHB = sqliteHelper.LanguageChanges(ConstantValue.LANChildHb, languageId);


        // setting text
        txtEditChild.setText(strEditChild);
        txtDOB.setText(strdob);
        txtBirthWeight.setText(strbweight);
        txtBirthOrder.setText(strBorder);
        txtParent.setText(strParent);
        txtBirthHeight.setText(strbirthHeight);
        txtBirthMuac.setText(strMuac);
        txtChildHB.setText(strChildHB);
        txtEdema.setText(strEdema);
        txtChildName.setText(strfullNameOfChild);
        txtGender.setText(strgender);
        txtHHID.setText(strHHID);
        txtDisability.setText(strdisablity);
        btnEditChild.setText(strSubmit);

        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr, String selec) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinner(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                Activity_EditChild.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(selec)) {
                spinner.setSelection(i);
                i = spinner.getCount();
            }
        }
    }

    @SuppressLint("NewApi")
    public void click_save(View v) {
        String houseNo;

        String strHouseHoldId = (etxtHHID.getSelectedItem().toString());
        String[] houseHoldId = strHouseHoldId.split("\\(");
        houseNo = houseHoldId[0].trim();

        String hb = etxtChildHB.getText().toString();
        String chName = etxtChildName.getText().toString();

        if (hb.equals("")
                || Float.parseFloat(hb) < 5.00
                || Float.parseFloat(hb) > 20.00
        ) {
          /*  if (hb.equals("")) {
                String[] util = Utility.split(strChildHB);
                String childhb = util[0];
                etxtChildHB.setError(childhb + " " + strMandatory);
                etxtChildHB.setFocusable(true);
            } else if (Float.parseFloat(hb) > 20.00) {
                etxtChildHB.setError("HB should be less than 20");
                etxtChildHB.setFocusable(true);
            } else if (Float.parseFloat(hb) < 5.00) {
                etxtChildHB.setError("HB should be greater than 5");
                etxtChildHB.setFocusable(true);
            }*/
        } else {
            String[] editable = {Integer.toString(childID), hb, chName, houseNo};


            sqliteHelper.setEditChild(editable);
            Toast.makeText(getApplicationContext(), strEditChild + " " + strDone, Toast.LENGTH_LONG).show();// child registration done!!
            Intent intent = new Intent(this, ActivityEdit.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strEditChild + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_EditChild.this,
                        Activity_EditChildListing.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_child_reg, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}