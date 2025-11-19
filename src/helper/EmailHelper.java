package helper;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailHelper {

    public static void sendWorkoutPlanEmail(String toEmail, String full_name, 
                                             String plan_name, String exercises, 
                                             String start_date, String end_date) {
        final String fromEmail = "gracelynacz541@gmail.com"; 
        final String password = "xfqt dygl qlew pdvq"; 

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true"); 

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "My Gym Rats"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your New Workout Plan: " + plan_name);

            String msgContent = "Hi " + full_name + ",\n\n"
                              + "Your trainer has assigned a new workout plan:\n\n"
                              + "Plan Name: " + plan_name + "\n"
                              + "Exercises: " + exercises + "\n"
                              + "Start Date: " + start_date + "\n"
                              + "End Date: " + end_date + "\n\n"
                              + "Stay consistent and train hard!\n\n"
                              + "Best Regards,\nYour Gym Team";

            message.setText(msgContent);

            Transport.send(message);
            System.out.println("✔ Workout plan emailed successfully to " + full_name);

        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}

