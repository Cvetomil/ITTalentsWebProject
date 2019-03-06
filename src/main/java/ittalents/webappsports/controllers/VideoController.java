package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.*;
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
import java.util.Optional;

@RestController
public class VideoController {
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
        video.setPath(VIDEO_DIR + videoName);


        File newImage = new File(VIDEO_DIR + videoName + ".mpg");
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);
    }
    @DeleteMapping("/videos/delete/{id}")
    public Video deleteVideo(@PathVariable("id") long id, HttpSession session) throws UserException, NotFoundException, MediaException {
        userAuthorities.validateAdmin(session);

        Video video = getVideoFromDB(id);

        videoRepository.delete(video);

        File file = new File(video.getPath());
        if(file.delete()){
            return video;
        }
        throw new MediaException("Video could not be deleted");
    }
    @GetMapping("/videos/{id}")
    public Video downloadVideo(@PathVariable("id") long id) throws NotFoundException{
        return getVideoFromDB(id);
    }
    private Video getVideoFromDB(long id) throws NotFoundException{
        Video video;
        Optional<Video> videoOptional = videoRepository.findById(id);
        if(!videoOptional.isPresent()){
            throw new NotFoundException("Video not found");
        }
        video = videoOptional.get();

        return video;
    }
}
