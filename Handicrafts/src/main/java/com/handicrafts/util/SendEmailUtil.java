package com.handicrafts.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class SendEmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);
    private static Environment env;

    @Autowired
    public SendEmailUtil(Environment environment) {
        SendEmailUtil.env = environment;
        logger.info("SendEmailUtil được khởi tạo với Environment: {}", environment != null ? "available" : "null");
    }

    private static String getFromEmail() {
        if (env == null) {
            String errorMsg = "Environment has not been initialized. Make sure Spring has initialized this bean.";
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        String email = env.getProperty("spring.mail.username");
        logger.debug("Lấy email người gửi: {}", email);
        return email;
    }

    private static String getPassword() {
        if (env == null) {
            String errorMsg = "Environment has not been initialized. Make sure Spring has initialized this bean.";
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        return env.getProperty("spring.mail.password");
    }

    public static boolean sendVerificationCode(String toEmail, String verifiedCode) {
        logger.info("Đang gửi mã xác thực đến email: {}", toEmail);

        try {
            // Email và password của người gửi
            String fromEmail = getFromEmail();
            // Sử dụng password của application
            String password = getPassword();

            // Khai bảo các thuộc tính cấu hình gửi mail
            Properties properties = new Properties();
            properties.put("mail.smtp.host", env.getProperty("spring.mail.host", "smtp.gmail.com"));
            properties.put("mail.smtp.port", env.getProperty("spring.mail.port", "587"));
            properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
            properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));

            logger.debug("Cấu hình email: host={}, port={}, auth={}, starttls={}",
                    properties.getProperty("mail.smtp.host"),
                    properties.getProperty("mail.smtp.port"),
                    properties.getProperty("mail.smtp.auth"),
                    properties.getProperty("mail.smtp.starttls.enable"));

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
            Message msg = new MimeMessage(session);
            // Kiểu nội dung
            msg.addHeader("content-type", "text/HTML; charset=UTF-8");
            // Người gửi
            msg.setFrom(new InternetAddress(fromEmail));
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
            logger.info("Đã gửi email xác thực thành công đến: {}", toEmail);
            return true;
        } catch (MessagingException e) {
            logger.error("Lỗi khi gửi email xác thực đến {}: {}", toEmail, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi gửi email: {}", e.getMessage(), e);
            return false;
        }
    }


public static void sendOrderNotify(String toEmail, int orderId, int status) {
        // Email và password của người gửi
        String fromEmail = getFromEmail();
        // Sử dụng password của application
        String password = getPassword();

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", env.getProperty("spring.mail.host", "smtp.gmail.com"));
        properties.put("mail.smtp.port", env.getProperty("spring.mail.port", "587"));
        properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
        properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));

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
            msg.setFrom(new InternetAddress(fromEmail));
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
        String fromEmail = getFromEmail();
        // Sử dụng password của application
        String password = getPassword();
        String toEmail = "dwnq.coding@gmail.com";

        // Khai bảo các thuộc tính cấu hình gửi mail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", env.getProperty("spring.mail.host", "smtp.gmail.com"));
        properties.put("mail.smtp.port", env.getProperty("spring.mail.port", "587"));
        properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
        properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));

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
            msg.setFrom(new InternetAddress(fromEmail));
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
