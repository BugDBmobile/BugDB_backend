package com.bugdb.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author hanch
 */
public class mailtest {
    
    public mailtest(){}
    
    
    
    public void send(int bugNo,String recipient){
        Authenticator auth = new Email_autherticator();
        System.out.println("正在发送邮件");
        Properties props=new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "141.146.118.10");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
        
        Session s=Session.getInstance(props,auth);
        s.setDebug(true);
        
        MimeMessage message=new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress("BugDB@oracle.com","Machine Info"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Bug:"+bugNo+" is assigned to you--"+new SimpleDateFormat("yyyy-MM-dd ").format(new Date()));
          
            message.setContent("Bug:"+bugNo+" is assigned to you--","text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport=s.getTransport("smtp");
        transport.connect("141.146.118.10","han.h.chen","Ch8869997");//发邮件人帐户密码
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();
        } catch (Exception ex) {
            Logger.getLogger(mailtest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

class Email_autherticator extends Authenticator
    {   
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication("han.h.chen@oracle.com", "Ch8869997");
        }
    }