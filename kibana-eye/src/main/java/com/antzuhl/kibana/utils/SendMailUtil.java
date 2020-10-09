package com.antzuhl.kibana.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author AntzUhl
 * @Date 17:02
 */
public class SendMailUtil {

    public static void send(String title, String content, String user) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "");// 主机名
        properties.put("mail.smtp.port", "");// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "false");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress("你的发件邮箱"));
        // 设置收件人邮箱地址
        List<String> userList = Arrays.asList(user.split(";"));
        InternetAddress []arr = null;
        if (userList.size() == 1) {
            arr = new InternetAddress[]{new InternetAddress(userList.get(0))};
        } else {
            arr  = userList.toArray(new InternetAddress[userList.size()]);
        }
        message.setRecipients(Message.RecipientType.TO,
                arr);
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人
        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件内容
        message.setText(content);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("你的发件邮箱", "密码");
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
