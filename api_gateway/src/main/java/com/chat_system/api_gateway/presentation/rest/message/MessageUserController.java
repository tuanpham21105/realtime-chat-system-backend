package com.chat_system.api_gateway.presentation.rest.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageUserController {
    public MessageUserController() {}

    // get messages by chat id
        // token
        // chat id
        // specification

    // get messages by message id
        // token 
        // message id

    // get nessage file by id
        // token
        // message id

    // post new text message
        // token
        // chat id
        // content

    // post new file message
        // token
        // chat id
        // type
        // file

    // edit text message content
        // token
        // message id
        // content

    // soft delete message
        // token
        // message id
}
