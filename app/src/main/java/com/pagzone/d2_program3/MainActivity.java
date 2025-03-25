package com.pagzone.d2_program3;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ScrollView svMain;
    private LinearLayout llForm, llApp, llTop, llBottom;
    private LinearLayout llGender, llAge, llAgeLabel; // TOP
    private LinearLayout llHeight, llWeight, llHeightLabel, llWeightLabel, llHeightButtons, llWeightButtons; // BOTTOM
    private LinearLayout llCaloricNeeds; // OUTSIDE

    private TextView txtTitle;
    private TextView txtGender, txtAge, txtAgePlaceholder, txtHeight, txtHeightLabel, txtWeight, txtWeightLabel;
    private TextView txtDailyActLevelLabel, txtCaloricNeedsLabel, txtCaloricNeeds; // OUTSIDE
    private TextView txtResult; // RESULT
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private EditText etAge, etHeight, etWeight;
    private Spinner spinDailyActLevel;
    private Button btnCM, btnIN;
    private Button btnKG, btnLB;

    private Button btnConvert;

    private boolean isFemale, isCM = true, isKG = true;
    private double selectedMultiplier, BMI, finalCaloricNeed;
    private double heightH, weightH;

    private double[] dailyActivityLevelMultipliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initChildren();
        initListeners();

        String[] dailyActivityLevelTitles = {"Lightly Active", "Moderately Active", "Active", "Very Active"};
        dailyActivityLevelMultipliers = new double[]{1.375, 1.55, 1.725, 1.9};

        ArrayAdapter<String> dailyActivityLevelAdapter = new ArrayAdapter<>(this, R.layout.spin_layout, dailyActivityLevelTitles);

        spinDailyActLevel.setAdapter(dailyActivityLevelAdapter);
    }

    private void initViews() {
        // COLORS
        int firstColor = Color.parseColor("#f9a8a8");
        int secondColor = Color.parseColor("#fccccc");
        int thirdColor = Color.parseColor("#d52d2d");

        // SCROLL VIEW
        svMain = findViewById(R.id.sv_main);

        // LINEAR LAYOUT, LAYOUT PARAMS
        //LEFT
        LinearLayout.LayoutParams leftLayoutParams =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        leftLayoutParams.setMarginEnd(50);
        //RIGHT
        LinearLayout.LayoutParams rightLayoutParams =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        //CaloricNeeds
        LinearLayout.LayoutParams caloricNeedsLayoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        caloricNeedsLayoutParams.setMarginStart(15);

        // LINEAR LAYOUT
        llForm = new LinearLayout(this);
        llForm.setOrientation(LinearLayout.VERTICAL);

        llApp = new LinearLayout(this);
        llApp.setOrientation(LinearLayout.VERTICAL);
        llApp.setPadding(16, 16, 16, 16);

        llTop = new LinearLayout(this);
        llTop.setPadding(0, 0, 0, 50);

        llBottom = new LinearLayout(this);
        llBottom.setPadding(0, 0, 0, 50);

        llGender = new LinearLayout(this);
        llGender.setLayoutParams(leftLayoutParams);
        llGender.setOrientation(LinearLayout.VERTICAL);
        llGender.setBackgroundColor(firstColor);
        llGender.setPadding(10, 10, 10, 10);
        llGender.setMinimumHeight(400);

        llAge = new LinearLayout(this);
        llAge.setLayoutParams(rightLayoutParams);
        llAge.setOrientation(LinearLayout.VERTICAL);
        llAge.setBackgroundColor(firstColor);
        llAge.setPadding(10, 10, 10, 10);
        llAge.setMinimumHeight(400);

        llAgeLabel = new LinearLayout(this);

        llHeight = new LinearLayout(this);
        llHeight.setLayoutParams(leftLayoutParams);
        llHeight.setOrientation(LinearLayout.VERTICAL);
        llHeight.setBackgroundColor(secondColor);
        llHeight.setPadding(10, 10, 10, 10);
        llHeight.setMinimumHeight(400);

        llHeightLabel = new LinearLayout(this);

        llHeightButtons = new LinearLayout(this);

        llWeight = new LinearLayout(this);
        llWeight.setLayoutParams(rightLayoutParams);
        llWeight.setOrientation(LinearLayout.VERTICAL);
        llWeight.setBackgroundColor(secondColor);
        llWeight.setPadding(10, 10, 10, 10);
        llWeight.setMinimumHeight(400);

        llWeightLabel = new LinearLayout(this);

        llWeightButtons = new LinearLayout(this);

        // CALORIC NEEDS
        llCaloricNeeds = new LinearLayout(this);

        // TEXT VIEWS
        txtTitle = new TextView(this);
        txtTitle.setText("Daily Caloric Intake");
        txtTitle.setPadding(6, 6, 6, 6);
        txtTitle.setTextSize(24);
        txtTitle.setTypeface(null, Typeface.BOLD);
        txtTitle.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        txtTitle.setBackgroundColor(Color.parseColor("#E63939"));
        txtTitle.setTextColor(Color.WHITE);

        txtGender = new TextView(this);
        txtGender.setText("What is your GENDER?");

        txtAge = new TextView(this);
        txtAge.setText("How old are you?");

        txtHeight = new TextView(this);
        txtHeight.setText("How tall are you?");

        txtHeightLabel = new TextView(this);
        txtHeightLabel.setText("CM");

        txtWeight = new TextView(this);
        txtWeight.setText("How much do you weigh?");

        txtWeightLabel = new TextView(this);
        txtWeightLabel.setText("KG");

        txtAgePlaceholder = new TextView(this);
        txtAgePlaceholder.setText("Years Old");

        txtDailyActLevelLabel = new TextView(this);
        txtDailyActLevelLabel.setText("How Active are you in daily basis?");
        txtDailyActLevelLabel.setTextSize(18);

        txtCaloricNeedsLabel = new TextView(this);
        txtCaloricNeedsLabel.setText("Caloric Needs: ");
        txtCaloricNeedsLabel.setTextSize(16);
        txtCaloricNeeds = new TextView(this);
        txtCaloricNeeds.setLayoutParams(caloricNeedsLayoutParams);
        txtCaloricNeeds.setTextSize(16);
        txtCaloricNeeds.setMinimumHeight(16);
        txtCaloricNeeds.setPadding(10, 10, 10, 10);
        txtCaloricNeeds.setTextColor(Color.WHITE);
        txtCaloricNeeds.setBackgroundColor(thirdColor);

        txtResult = new TextView(this);
        txtResult.setTextSize(16);

        // RADIO GROUPS
        rgGender = new RadioGroup(this);

        // RADIO BUTTONS
        rbMale = new RadioButton(this);
        rbMale.setText("Male");

        rbFemale = new RadioButton(this);
        rbFemale.setText("Female");

        // EDIT TEXT
        etAge = new EditText(this);
        etAge.setMinimumWidth(200);
        etAge.setInputType(InputType.TYPE_CLASS_NUMBER);

        etHeight = new EditText(this);
        etHeight.setMinimumWidth(200);
        etHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        etWeight = new EditText(this);
        etWeight.setMinimumWidth(200);
        etWeight.setInputType(InputType.TYPE_CLASS_NUMBER);

        // SPINNERS
        spinDailyActLevel = new Spinner(this);

        // BUTTONS
        // Height
        btnCM = new Button(this);
        btnCM.setText("CM");
        btnCM.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.primaryColor));
        btnCM.setTextColor(Color.WHITE);
        btnIN = new Button(this);
        btnIN.setText("IN");
        btnIN.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.primaryColor));
        btnIN.setTextColor(Color.WHITE);

        // Weight
        btnKG = new Button(this);
        btnKG.setText("KG");
        btnKG.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.primaryColor));
        btnKG.setTextColor(Color.WHITE);
        btnLB = new Button(this);
        btnLB.setText("LB");
        btnLB.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.primaryColor));
        btnLB.setTextColor(Color.WHITE);

        btnConvert = new Button(this);
        btnConvert.setText("CONVERT");
        btnConvert.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.primaryColor));
        btnConvert.setTextColor(Color.WHITE);
    }

    private void initChildren() {
        // RADIO GROUP
        rgGender.addView(rbMale);
        rgGender.addView(rbFemale);

        // LINEAR LAYOUT
        //Gender
        llGender.addView(txtGender);
        llGender.addView(rgGender);
        //Age
        llAgeLabel.addView(etAge);
        llAgeLabel.addView(txtAgePlaceholder);
        llAge.addView(txtAge);
        llAge.addView(llAgeLabel);
        //->Top
        llTop.addView(llGender);
        llTop.addView(llAge);

        //Height
        llHeightLabel.addView(etHeight);
        llHeightLabel.addView(txtHeightLabel);
        llHeightButtons.addView(btnCM);
        llHeightButtons.addView(btnIN);
        llHeight.addView(txtHeight);
        llHeight.addView(llHeightLabel);
        llHeight.addView(llHeightButtons);

        //Weight
        llWeightLabel.addView(etWeight);
        llWeightLabel.addView(txtWeightLabel);
        llWeightButtons.addView(btnKG);
        llWeightButtons.addView(btnLB);
        llWeight.addView(txtWeight);
        llWeight.addView(llWeightLabel);
        llWeight.addView(llWeightButtons);

        //->Bottom
        llBottom.addView(llHeight);
        llBottom.addView(llWeight);

        //Caloric Needs
        llCaloricNeeds.addView(txtCaloricNeedsLabel);
        llCaloricNeeds.addView(txtCaloricNeeds);

        //->App
        llApp.addView(llTop);
        llApp.addView(llBottom);
        llApp.addView(txtDailyActLevelLabel);
        llApp.addView(spinDailyActLevel);
        llApp.addView(llCaloricNeeds);
        llApp.addView(btnConvert);
        llApp.addView(txtResult);

        //-->Form
        llForm.addView(txtTitle);
        llForm.addView(llApp);

        // SCROLL VIEW
        svMain.addView(llForm);
    }

    private void initListeners() {
        spinDailyActLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    double heightValue = Double.parseDouble(etHeight.getText().toString());
                    double weightValue = Double.parseDouble(etWeight.getText().toString());
                    double ageValue = Double.parseDouble(etAge.getText().toString());

                    if (isFemale) {
                        BMI = 655.1 + (0.563 * weightValue) + (1.850 * heightValue) - (4.676 * ageValue);
                    } else {
                        BMI = 66.47 + (13.75 * weightValue) + (5.003 * heightValue) - (6.755 * ageValue);
                    }

                    selectedMultiplier = dailyActivityLevelMultipliers[position];

                    finalCaloricNeed = BMI * selectedMultiplier;

                    txtCaloricNeeds.setText(String.valueOf(finalCaloricNeed));
                    Toast toast = Toast.makeText(MainActivity.this, String.format("CALORIC NEEDS: %.2f", finalCaloricNeed), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                    toast.show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "INVALID INPUT!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rbMale.setOnClickListener(v -> {
            isFemale = false;
        });

        rbFemale.setOnClickListener(v -> {
            isFemale = true;
        });

        btnCM.setOnClickListener(v -> {
            if (isCM) return;

            String heightValue = etHeight.getText().toString();

            if (heightValue.isEmpty()) return;

            double height = Double.parseDouble(heightValue);

            isCM = true;
            txtHeightLabel.setText("CM");
            etHeight.setText(String.format("%.2f", height * 2.54));

            heightH = height * 2.54;
        });

        btnIN.setOnClickListener(v -> {
            if (!isCM) return;

            String heightValue = etHeight.getText().toString();

            if (heightValue.isEmpty()) return;

            double height = Double.parseDouble(heightValue);

            isCM = false;
            txtHeightLabel.setText("IN");
            etHeight.setText(String.format("%.2f", height / 2.54));

            heightH = height / 2.54;
        });

        btnKG.setOnClickListener(v -> {
            if (isKG) return;

            String weightValue = etWeight.getText().toString();

            if (weightValue.isEmpty()) return;

            double weight = Double.parseDouble(weightValue);

            isKG = true;
            txtWeightLabel.setText("KG");
            etWeight.setText(String.format("%.2f", weight / 2.2));

            weightH = weight / 2.2;
        });

        btnLB.setOnClickListener(v -> {
            if (!isKG) return;

            String weightValue = etWeight.getText().toString();

            if (weightValue.isEmpty()) return;

            double weight = Double.parseDouble(weightValue);

            isKG = false;
            txtWeightLabel.setText("LB");

            etWeight.setText(String.format("%.2f", weight * 2.2));
            weightH = weight * 2.2;
        });

        btnConvert.setOnClickListener(v -> {
            String heightLabel = txtHeightLabel.getText().toString();
            String weightLabel = txtWeightLabel.getText().toString();
            txtResult.setText(String.format("Height: %.2f %s\nWeight: %.2f %s", heightH, heightLabel, weightH, weightLabel));
        });
    }
}
