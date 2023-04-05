package com.asker.asker.controller;

import com.asker.asker.model.Message;
import com.asker.asker.model.OutputMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {
    public int count = 0;

    @GetMapping("/media/kup.mp4")
    @ResponseBody
    public ResponseEntity<Resource> join() throws IOException {

        File filename = new File("C:\\Users\\as\\Desktop\\kup.mp4");
        byte[] Data = Files.readAllBytes(Paths.get("C:\\Users\\as\\Desktop\\kup.mp4"));
        Resource resource = new UrlResource(filename.toURI());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename.getName() + "\"").body(resource);

    }
    @GetMapping("/1")
    public String home(Model model){
        count++;
        model.addAttribute("count", count);
        return "home_page";
    }
    @GetMapping("/player")
    public String player(){
        return "player";
    }
    @GetMapping("/p")
    public String d(){
        return "dolbaeb";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final Message message) throws Exception {

        return new OutputMessage(message.getText());
    }

}
