package com.mxmbro.sesame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;

public class LogSystem extends AppCompatActivity{


    public static char[] getPassword() throws Exception {
        char[] password = new char[0];
        try (ObjectInputStream inP = new ObjectInputStream(new FileInputStream("user.save"))) {
            PrivateKey priv = (PrivateKey) inP.readObject();
            inP.readObject();
        }
        return password;

    }

    public static void setPassword(char[] pss0) throws Exception {
        try (ObjectOutputStream outP = new ObjectOutputStream(new FileOutputStream("user.save"))) {
        }

    }
}
