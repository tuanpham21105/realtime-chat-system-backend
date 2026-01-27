package com.chat_system.api_gateway.presentation.rest.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/message")
public class MessageAdminController {
    public MessageAdminController() {}

    // get messages
        // token
        // specification

    // get messages by chat id
        // token
        // chat id
        // specification

    // get messages by sender id
        // token
        // sender id
        // specification

    // get message by id
        // token
        // message id

    // get message file by id
        // token
        // message id

    // post new text message   
        // token
        // sender id
        // chat id
        // content

    // post new file message
        // token
        // sender id
        // chat id
        // file type
        // message file

    // edit text message
        // token
        // message id
        // new message content

    // soft delete message
        // token
        // message id

    // hard delete message 
        // token
        // message id
}
