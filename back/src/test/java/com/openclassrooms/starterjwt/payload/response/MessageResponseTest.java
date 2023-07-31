package com.openclassrooms.starterjwt.payload.response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {
    @Test
    public void testMessageResponse() {
        String messageText = "Hello, World!";
        MessageResponse messageResponse = new MessageResponse(messageText);
        assertEquals(messageText, messageResponse.getMessage());
    }

    @Test
    public void testMessageResponseSetMessage() {
        MessageResponse messageResponse = new MessageResponse("");
        String newMessage = "This is a new message.";
        messageResponse.setMessage(newMessage);
        assertEquals(newMessage, messageResponse.getMessage());
    }
}