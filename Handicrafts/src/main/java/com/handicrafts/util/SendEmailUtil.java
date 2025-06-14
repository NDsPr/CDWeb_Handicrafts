package com.handicrafts.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class SendEmailUtil {
    private static final ResourceBundle fromEmailInfo = ResourceBundle.getBundle("fromEmail");

    public static void sendVerificationCode(String toEmail, String verifiedCode) {
        // Email và password của người gửi
        String fromEmail = fromEmailInfo.getString("fromEmail");
        // Sử dụng password của application
        String password = fromEmailInfo.getString("password");

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");       // Host: smtp.gmail.com (Gmail)
        properties.put("mail.smtp.port", "587");                 // Port: 587 (TLS)
        properties.put("mail.smtp.auth", "true");               // Authentication: true
        properties.put("mail.smtp.starttls.enable", "true");    // Start TLS: true

        // Tạo ra một Authenticator để đăng nhập vào tài khoản gửi mail
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        // Tạo ra phiên làm việc của JavaMail (Khác với Session trong Servlet)
        Session session = Session.getInstance(properties, authenticator);

        // Gửi email
        try {
            Message msg = new MimeMessage(session);
            // Kiểu nội dung
            msg.addHeader("content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress("dddhandicraft.contact@gmail.com"));
            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // Tiêu đề
            msg.setSubject("DDD. - Xác minh tài khoản");
            // Ngày gửi
            msg.setSentDate(new Date());
            // Nội dung
            msg.setContent("<html>" +
                    "<body>" +
                    "<h1 style=\"color: #b07911\">DDD. - Nghệ thuật mỹ nghệ</h1>" +
                    "<hr/>" +
                    "<h3>Mã xác minh của bạn là (Viết hoa):<h3>" +
                    "<h1>" + verifiedCode + "</h1>" +
                    "<p>Vui lòng không tiết lộ mã này cho bất kỳ ai. Trân trọng cảm ơn quý khách đã lựa chọn DDD. - Nghệ thuật mỹ nghệ.</p>" +
                    "<body>" +
                    "</html>", "text/html; charset=utf-8");

            // Gửi nội dung đi
            Transport.send(msg);
            System.out.println("Done!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendVerificationLink(String toEmail, String verifyLink) {
        // Email và password của người gửi
        String fromEmail = fromEmailInfo.getString("fromEmail");
        // Sử dụng password của application
        String password = fromEmailInfo.getString("password");

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");       // Host: smtp.gmail.com (Gmail)
        properties.put("mail.smtp.port", "587");                 // Port: 587 (TLS)
        properties.put("mail.smtp.auth", "true");               // Authentication: true
        properties.put("mail.smtp.starttls.enable", "true");    // Start TLS: true

        // Tạo ra một Authenticator để đăng nhập vào tài khoản gửi mail
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        // Tạo ra phiên làm việc của JavaMail (Khác với Session trong Servlet)
        Session session = Session.getInstance(properties, authenticator);

        // Gửi email
        try {
            Message msg = new MimeMessage(session);
            // Kiểu nội dung
            msg.addHeader("content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress("dddhandicraft.contact@gmail.com"));
            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // Tiêu đề
            msg.setSubject("DDD. - Xác minh tài khoản");
            // Ngày gửi
            msg.setSentDate(new Date());
            // Nội dung
            msg.setContent("<html>" +
                    "<body>" +
                    "<h1 style=\"color: #b07911\">DDD. - Nghệ thuật mỹ nghệ</h1>" +
                    "<hr/>" +
                    "<h3 style=\"color: black;\">Ấn vào đường link này để tiếp tục lấy lại mật khẩu của bạn: <a href=\"" + verifyLink + "\" style=\"text-decoration: none;\">Xác thực quên mật khẩu</a><h3>" +
                    "<p style=\"font-weight: 500; color: black;\">Trân trọng cảm ơn quý khách đã lựa chọn DDD. - Nghệ thuật mỹ nghệ.</p>" +
                    "<body>" +
                    "</html>", "text/html; charset=utf-8");

            // Gửi nội dung đi
            Transport.send(msg);
            System.out.println("Done!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendOrderNotify(String toEmail, int orderId, int status) {
        // Email và password của người gửi
        String fromEmail = fromEmailInfo.getString("fromEmail");
        // Sử dụng password của application
        String password = fromEmailInfo.getString("password");

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");       // Host: smtp.gmail.com (Gmail)
        properties.put("mail.smtp.port", "587");                 // Port: 587 (TLS)
        properties.put("mail.smtp.auth", "true");               // Authentication: true
        properties.put("mail.smtp.starttls.enable", "true");    // Start TLS: true

        // Tạo ra một Authenticator để đăng nhập vào tài khoản gửi mail
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        // Tạo ra phiên làm việc của JavaMail (Khác với Session trong Servlet)
        Session session = Session.getInstance(properties, authenticator);

        // Gửi email
        try {
            Message msg = new MimeMessage(session);
            // Kiểu nội dung
            msg.addHeader("content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress("dddhandicraft.contact@gmail.com"));
            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // Tiêu đề
            msg.setSubject("DDD. - Thông báo đơn hàng");
            // Ngày gửi
            msg.setSentDate(new Date());
            // Nội dung
            msg.setContent("<html>" +
                    "<body>" +
                    "<h1 style=\"color: #b07911\">DDD. - Nghệ thuật mỹ nghệ</h1>" +
                    "<hr/>" +
                    "<h3 style=\"color: black;\">DDD. thông báo: Bạn đã order đơn hàng số: " + orderId + ", trạng thái hiện tại: " + statusString(status) + "<h3>" +
                    "<p style=\"font-weight: 500; color: black;\">Trân trọng cảm ơn quý khách đã lựa chọn DDD. - Nghệ thuật mỹ nghệ.</p>" +
                    "<body>" +
                    "</html>", "text/html; charset=utf-8");

            // Gửi nội dung đi
            Transport.send(msg);
            System.out.println("Done!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String statusString(int status) {
        String result = "";
        switch (status) {
            case 0:
                result = "Đã hủy";
                break;
            case 1:
                result = "Chờ xác nhận";
                break;
            case 2:
                result = "Đẫ xác nhận";
                break;
            case 3:
                result = "Đang vận chuyển";
                break;
            case 4:
                result = "Thành công";
                break;
        }
        return result;
    }

    public static void sendDeleteNotify(int id, String email, int deleteId, String address) {
        // Email và password của người gửi
        String fromEmail = fromEmailInfo.getString("fromEmail");
        // Sử dụng password của application
        String password = fromEmailInfo.getString("password");
        String toEmail = "dwnq.coding@gmail.com";

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");       // Host: smtp.gmail.com (Gmail)
        properties.put("mail.smtp.port", "587");                 // Port: 587 (TLS)
        properties.put("mail.smtp.auth", "true");               // Authentication: true
        properties.put("mail.smtp.starttls.enable", "true");    // Start TLS: true

        // Tạo ra một Authenticator để đăng nhập vào tài khoản gửi mail
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        // Tạo ra phiên làm việc của JavaMail (Khác với Session trong Servlet)
        Session session = Session.getInstance(properties, authenticator);

        // Gửi email
        try {
            Message msg = new MimeMessage(session);
            // Kiểu nội dung
            msg.addHeader("content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress("dddhandicraft.contact@gmail.com"));
            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // Tiêu đề
            msg.setSubject("DDD. - Thông báo xóa " + address + " (Admin)");
            // Ngày gửi
            msg.setSentDate(new Date());
            // Nội dung
            msg.setContent("<html>" +
                    "<body>" +
                    "<h1 style=\"color: #b07911\">DDD. - Nghệ thuật mỹ nghệ</h1>" +
                    "<hr/>" +
                    "<h3 style=\"color: black;\">DDD. thông báo: Vị trí xóa: " + address + ", id " + address + " xóa: " + deleteId + ". Id người xóa: " + id + ", email người xóa: " + email + "<h3>" +
                    "<body>" +
                    "</html>", "text/html; charset=utf-8");

            // Gửi nội dung đi
            Transport.send(msg);
            System.out.println("Done!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
