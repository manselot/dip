package com.asker.asker.controller;

import com.asker.asker.model.Message;
import com.asker.asker.model.OutputMessage;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.LinkedList;
import java.util.List;

import com.asker.asker.model.Room;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
    Room room = new Room();
    @Value("${upload.path}")
    private static String uploadPath;

    /*@GetMapping("/media/kup.mp4")
    @ResponseBody
    public ResponseEntity<Resource> join() throws IOException {

        File filename = new File("C:\\Users\\as\\Desktop\\kup.mp4");
        byte[] Data = Files.readAllBytes(Paths.get("C:\\Users\\as\\Desktop\\kup.mp4"));
        Resource resource = new UrlResource(filename.toURI());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename.getName() + "\"").body(resource);

    }*/
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable String id,
                       @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(uploadPath + id)));
                stream.write(bytes);
                stream.close();
                return "Вы удачно загрузили " + id + " в " + uploadPath + "-uploaded !";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + id + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + id + " потому что файл пустой.";
        }
    }
    @GetMapping("/Create")
    public String CreateRoom( Model model){
        String id = room.generationId();
        while (!room.roomisCreated(Integer.parseInt(id))) {
            id = room.generationId();
        }
        model.addAttribute("id", id);
        return "video";
    }

    @GetMapping("/Join/{id}")
    public String CreateRoom(@PathVariable String id, Model model){
        model.addAttribute("id", id);
        return "video";
    }


    @GetMapping("/p")
    public String d(){
        return "video";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final Message message) throws Exception {

        return new OutputMessage(message.getText(),message.getCurrentTime(),message.getRoom());
    }

}
