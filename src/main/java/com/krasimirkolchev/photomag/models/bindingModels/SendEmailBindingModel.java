package com.krasimirkolchev.photomag.models.bindingModels;

public class SendEmailBindingModel {
    private String sender;
    private String subject;
    private String textMessage;

    public SendEmailBindingModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
