package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.VideoUploadDTO;
import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Video;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.VideoRepository;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class VideoUploadController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    ArticleRepository ar;

    private static final String VIDEO_DIR = "D:\\videos\\";

    @PostMapping("/videos/upload/{artId}")
    public void uploadVideo(@RequestParam("video") MultipartFile file,@PathVariable long artId, HttpSession session) throws UserException, IOException {
        userAuthorities.validateAdmin(session);

        String videoName = System.currentTimeMillis() + ar.getOne(artId).getTitle();

        byte[] bytes = file.getBytes();
        Video video = new Video();
        video.setArtId(artId);
        video.setPath(VIDEO_DIR);


        File newImage = new File(VIDEO_DIR + videoName.toString() + ".mpg");
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);

    }
}
