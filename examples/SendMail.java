/* Send FusionExport files as attachments via mail
 * 
 * Sending email using javax.mail
 * 
 * Provide the metadata for email (line no. 85 & 88)
 * 
 * Provide your SMTP credential (line no. 90 & 91)
 *
 * Provide your SMTP details for setting up (line no. 96 to 100)
 * 
 * This sample was tested using Gmail SMTP configuration
 * 
*/

import com.fusioncharts.fusionexport.client.*; // import sdk

// Import Email Helper Packages 
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

public class SendMail {
    public static void main(String[] args) throws Exception {
	
		StringBuilder chartConf = new StringBuilder();
		chartConf.append("[");
		chartConf.append("  {");
		chartConf.append("    \"type\": \"column2d\",");
		chartConf.append("    \"renderAt\": \"chart-container\",");
		chartConf.append("    \"width\": \"600\",");
		chartConf.append("    \"height\": \"200\",");
		chartConf.append("    \"dataFormat\": \"json\",");
		chartConf.append("    \"dataSource\": {");
		chartConf.append("      \"chart\": {");
		chartConf.append("        \"caption\": \"Number of visitors last week\",");
		chartConf.append("        \"subCaption\": \"Bakersfield Central vs Los Angeles Topanga\"");
		chartConf.append("      },");
		chartConf.append("      \"data\": [");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Mon\",");
		chartConf.append("          \"value\": \"15123\"");
		chartConf.append("        },");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Tue\",");
		chartConf.append("          \"value\": \"14233\"");
		chartConf.append("        },");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Wed\",");
		chartConf.append("          \"value\": \"25507\"");
		chartConf.append("        }");
		chartConf.append("      ]");
		chartConf.append("    }");
		chartConf.append("  }");
		chartConf.append("]");		
		
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        //config.set("chartConfig", "chart-config.json");
        config.set("chartConfig",chartConf.toString());
        config.set("type", "pdf");
		
        String[] files = em.export(config,".",true);
        
        EmailSend(files);
        
    }
    
    public static void EmailSend(String[] files) {
    	
        // Recipient's email ID needs to be mentioned.
        String to = "<RECEIVERS'S EMAIL>";

        // Sender's email ID needs to be mentioned
        String from = "<SENDER'S EMAIL>";

        final String username = "<USERNAME>";//change accordingly
        final String password = "<PASSWORD>";//change accordingly

        // Assuming you are sending email through smtp.gmaill.com
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
           new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
              }
           });
            	
    	
    	try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("FusionExport");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Hello,\n\nKindly find the attachment of FusionExport exported files.\n\nThank you!");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            for(String file : files) {
	            // Part two is attachment
	            messageBodyPart = new MimeBodyPart();
	            DataSource source = new FileDataSource(file);
	            messageBodyPart.setDataHandler(new DataHandler(source));
	            messageBodyPart.setFileName(java.nio.file.Paths.get(file).getFileName().toString());
	            multipart.addBodyPart(messageBodyPart);
            }
            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("FusionExport JAVA Client: Email Sent");
     
         } catch (MessagingException e) {
            throw new RuntimeException(e);
         }
    }
}

