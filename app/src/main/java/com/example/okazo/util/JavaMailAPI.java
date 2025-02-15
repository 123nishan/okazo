package com.example.okazo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.okazo.OtpActivity;
import com.sdsmdg.harjot.vectormaster.utilities.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaMailAPI extends AsyncTask<Void,Void,Void> {
    private Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;
    private String mOtp;
    private String mPassword;
    private String mPhone;
    private String mName;
    private String identifier;
    private String userEmail;
    private Multipart _multipart = new MimeMultipart();
    private ProgressDialog mProgressDialog;

    //Constructor
    public JavaMailAPI(Context mContext, String mEmail, String mSubject,
                       String mMessage,String mOtp,String mPassword,
                       String mPhone,String mName,String userEmail,
                       String identifier) {
        this.mContext =  mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.mOtp=mOtp;
        this.mPassword=mPassword;
        this.mName=mName;
        this.mPhone=mPhone;
        this.userEmail=userEmail;
        this.identifier=identifier;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        mProgressDialog = ProgressDialog.show(mContext,"Sending Verification Code", "Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send
        mProgressDialog.dismiss();

        //Show success toast
        if(identifier.equals("first")) {
            Intent intent = new Intent(mContext, OtpActivity.class);
            //intent.putExtra("otp", mOtp);
            intent.putExtra("email", userEmail);
           // intent.putExtra("password", mPassword);
            //ntent.putExtra("phone", mPhone);
            //intent.putExtra("name", mName);
            mContext.startActivity(intent);

            Toast.makeText(mContext, "Verification Code sent", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(mContext, OtpActivity.class);
            //intent.putExtra("otp", mOtp);
            intent.putExtra("email", userEmail);
//            intent.putExtra("password", mPassword);
//            intent.putExtra("phone", mPhone);
//            intent.putExtra("name", mName);
            mContext.startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Toast.makeText(mContext, "Resent Code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(constants.EMAIL, constants.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(mSession);

            //Setting sender address
            mm.setFrom(new InternetAddress(constants.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            //Adding subject
            mm.setSubject(mSubject);
            //Adding message
            mm.setText(mMessage);
            BodyPart messageBodyPart = new MimeBodyPart();
            InputStream is = mContext.getAssets().open("user_profile.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String str = new String(buffer);
//            str=str.replace("$$USERNAME$$", mEmail);
            str=str.replace("$$EMAIL$$", mMessage+" "+mOtp);
            messageBodyPart.setContent(str,"text/html; charset=utf-8");
            _multipart.addBodyPart(messageBodyPart);
            mm.setContent(_multipart);
            //Sending email
            Transport.send(mm);

//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            messageBodyPart.setText(message);
//
//            Multipart multipart = new MimeMultipart();
//
//            multipart.addBodyPart(messageBodyPart);
//
//            messageBodyPart = new MimeBodyPart();
//
//            DataSource source = new FileDataSource(filePath);
//
//            messageBodyPart.setDataHandler(new DataHandler(source));
//
//            messageBodyPart.setFileName(filePath);
//
//            multipart.addBodyPart(messageBodyPart);

//            mm.setContent(multipart);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
