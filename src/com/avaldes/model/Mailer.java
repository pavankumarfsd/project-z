package com.avaldes.model;

import java.util.Properties;    

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;    
import javax.mail.internet.*; 

public class Mailer{  
    public static void send(String from,String password,String to,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication(MailCredentials.GMAIL_ID,MailCredentials.GMAIL_PASSWORD);  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub); 
           StringBuilder email = new StringBuilder();
//           email.append("<html><head><style type='text/css'>h1.custom {font-family: impact, sans-serif; "
//                   + "font-size: 30px;color: #5aa8f5;font-style: bold;text-align: left;text-shadow: 2px 3px 5px #444;}</style></head>"
//                   + "<body>"
//                   + "<h1 class=\"custom\">Actor Management Interface</h1>"
//                   + "</body></html>");
//
//           message.setContent(email.toString(),"text/html");  
           message.setText(msg);
           
          //3) create MimeBodyPart object and set your message text     
//           BodyPart messageBodyPart1 = new MimeBodyPart();  
//           messageBodyPart1.setText("This is message body");  
//             
//           //4) create new MimeBodyPart object and set DataHandler object to this object      
//           MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
//         
//           String filename = "F:\\Freelancing\\PartTimeTrainings\\FullStackDevelopmentTracker.xlsx";//change accordingly  
//           DataSource source = new FileDataSource(filename);  
//           messageBodyPart2.setDataHandler(new DataHandler(source));  
//           messageBodyPart2.setFileName(filename);  
//            
//            
//           //5) create Multipart object and add MimeBodyPart objects to this object      
//           Multipart multipart = new MimeMultipart();  
//           multipart.addBodyPart(messageBodyPart1);  
//           multipart.addBodyPart(messageBodyPart2);  
//         
//           //6) set the multiplart object to the message object  
//           message.setContent(multipart );  

           
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    }  
}  

//public class Mailer{    
// public static void main(String[] args) {    
//     //from,password,to,subject,message  
//     Mailer.send("chaudharyanish1990@gmail.com","ILOvemyfamily9!","chaudharyanish1990@gmail.com","test Mail","How r u?");  
//     //change from, password and to  
// }    
//} 