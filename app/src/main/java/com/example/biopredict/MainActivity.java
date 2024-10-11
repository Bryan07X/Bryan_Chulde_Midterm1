package com.example.biopredict;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.tensorflow.lite.Interpreter;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables para los campos de entrada
    EditText inputField1;
    EditText inputField2;
    EditText inputField3;
    EditText inputField4;
    EditText inputField5;
    EditText inputField6;
    EditText inputField7;
    EditText inputField8;
    EditText inputField9;

    Button predictBtn;

    TextView resultTV;

    Interpreter interpreter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Inicializar los campos de entrada
        inputField1 = findViewById(R.id.editTextNumber1);
        inputField2 = findViewById(R.id.editTextNumber2);
        inputField3 = findViewById(R.id.editTextNumber3);
        inputField4 = findViewById(R.id.editTextNumber4);
        inputField5 = findViewById(R.id.editTextNumber5);
        inputField6 = findViewById(R.id.editTextNumber6);
        inputField7 = findViewById(R.id.editTextNumber7);
        inputField8 = findViewById(R.id.editTextNumber8);
        inputField9 = findViewById(R.id.editTextNumber9);

        predictBtn = findViewById(R.id.button);
        resultTV = findViewById(R.id.textView);

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores de entrada
                String input1 = inputField1.getText().toString();
                String input2 = inputField2.getText().toString();
                String input3 = inputField3.getText().toString();
                String input4 = inputField4.getText().toString();
                String input5 = inputField5.getText().toString();
                String input6 = inputField6.getText().toString();
                String input7 = inputField7.getText().toString();
                String input8 = inputField8.getText().toString();
                String input9 = inputField9.getText().toString();

                // Convertir los valores a flotantes
                float value1 = Float.parseFloat(input1);
                float value2 = Float.parseFloat(input2);
                float value3 = Float.parseFloat(input3);
                float value4 = Float.parseFloat(input4);
                float value5 = Float.parseFloat(input5);
                float value6 = Float.parseFloat(input6);
                float value7 = Float.parseFloat(input7);
                float value8 = Float.parseFloat(input8);
                float value9 = Float.parseFloat(input9);

                // Crear la matriz de entrada para el modelo con 9 entradas
                float[][] inputs = new float[1][9];
                inputs[0][0] = value1;
                inputs[0][1] = value2;
                inputs[0][2] = value3;
                inputs[0][3] = value4;
                inputs[0][4] = value5;
                inputs[0][5] = value6;
                inputs[0][6] = value7;
                inputs[0][7] = value8;
                inputs[0][8] = value9;

                // Realizar la inferencia
                float result = doInference(inputs);

                // Mostrar el resultado
                resultTV.setText("Result: " + result);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public float doInference(float[][] input) {
        float[][] output = new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("diabetes_model.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }


}