package ittalents.webappsports.controllers;

import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.MediaException;
import ittalents.webappsports.exceptions.NotFoundException;
import ittalents.webappsports.exceptions.UserException;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.User;
import ittalents.webappsports.models.Video;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.VideoRepository;
import ittalents.webappsports.util.MsgResponse;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
public class VideoController extends SportalController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    ArticleRepository ar;

    private static final String VIDEO_DIR = "C:\\Users\\aleks\\Pictures\\Upload\\";

    //upload video to database and save to server
    @PostMapping("/videos/upload/{artId}")
    public Video uploadVideo(@RequestParam("video") MultipartFile file, @PathVariable long artId, HttpSession session)
            throws UserException, IOException, BadRequestException {
        User admin = userAuthorities.validateAdmin(session);
        Article article = checkArticlePresence(artId);
        validateArticleAuthor(admin, article);
        String videoName = System.currentTimeMillis() + article.getTitle();

        byte[] bytes = file.getBytes();
        Video video = new Video();
        video.setArtId(artId);
        video.setPath(VIDEO_DIR + videoName);
        videoRepository.save(video);

        File newVideo = new File(VIDEO_DIR + videoName + ".mpg");
        FileOutputStream fos = new FileOutputStream(newVideo);
        fos.write(bytes);
        fos.close();
        return video;
    }

    //delete video
    @DeleteMapping("/videos/delete/{videoId}")
    public MsgResponse deleteVideo(@PathVariable("videoId") long videoId, HttpSession session)
            throws UserException, NotFoundException, MediaException, BadRequestException {
        User admin = userAuthorities.validateAdmin(session);
        Video video = getVideoFromDB(videoId);
        Article article = checkArticlePresence(video.getArtId());
        validateArticleAuthor(admin, article);
        File file = new File(video.getPath() + ".mpg");
        if (file.delete()) {
            videoRepository.delete(video);
            return new MsgResponse(HttpStatus.OK.value(), "video deleted successfully");
        }
        throw new MediaException("Video cannot be deleted!");
    }

    //download video
    @GetMapping(value = "/videos/{id}", produces = "video/mpg")
    public byte[] downloadVideo(@PathVariable("id") long id) throws NotFoundException, IOException {
        Video videoFromDB = getVideoFromDB(id);
        FileInputStream fis = new FileInputStream(new File(videoFromDB.getPath() + ".mpg"));
        byte[] video = fis.readAllBytes();
        fis.close();
        return video;
    }

    private Video getVideoFromDB(long id) throws NotFoundException {
        Video video;
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (!videoOptional.isPresent()) {
            throw new NotFoundException("Video not found");
        }
        video = videoOptional.get();

        return video;
    }
}
