package com.mxmbro.sesame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveLoadSystem extends AppCompatActivity {

    public static void writeFile(String filePath, String value) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            bufferedWriter.write(value);
            bufferedWriter.newLine();
        } finally {
            bufferedWriter.close();
        }
    }

    public void readFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        try {
            String textLine = bufferedReader.readLine();
            do {
                System.out.println(textLine);

                textLine = bufferedReader.readLine();
            } while (textLine != null);
        } finally {
            bufferedReader.close();
        }
    }
}
