package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.ImageDTO;
import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.exceptions.MediaException;
import ittalents.webappsports.exceptions.NotFoundException;
import ittalents.webappsports.exceptions.UserException;
import ittalents.webappsports.models.Article;
import ittalents.webappsports.models.Picture;
import ittalents.webappsports.models.User;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.PictureRepository;
import ittalents.webappsports.util.MsgResponse;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
public class ImageController extends SportalController {

    @Autowired
    ArticleRepository ar;
    @Autowired
    PictureRepository pr;

    private static final String IMAGE_DIR = "C:\\Users\\aleks\\Pictures\\Upload\\";

    //upload image to database and save to server
    @PostMapping("/images/upload/{artId}")
    public Picture uploadImage(@RequestBody ImageDTO dto, @PathVariable long artId, HttpSession session)
            throws IOException, BadRequestException, UserException {
        User admin = userAuthorities.validateAdmin(session);
        Article article = checkArticlePresence(artId);
        validateArticleAuthor(admin, article);
        String imgPath = article.getTitle() + System.currentTimeMillis();
        Picture picture = new Picture();
        picture.setPath(IMAGE_DIR + imgPath);
        picture.setArtId(artId);
        pr.save(picture);

        String base64 = dto.getFileStr();
        byte[] bytes = Base64.getDecoder().decode(base64);
        File newImage = new File(IMAGE_DIR + imgPath + ".png");
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);
        fos.close();
        return picture;
    }

    //download image
    @GetMapping(value = "/images/{id}", produces = "image/png")
    public byte[] getImage(@PathVariable("id") long pictureId) throws IOException, NotFoundException {
        Optional<Picture> pictureOptional = pr.findById(pictureId);
        Picture pic;
        if (!pictureOptional.isPresent()) {
            throw new NotFoundException("Picture does not exist");
        }
        pic = pictureOptional.get();

        File newImage = new File(pic.getPath());
        FileInputStream fis = new FileInputStream(newImage + ".png");
        return fis.readAllBytes();
    }


    //delete picture
    @DeleteMapping("/images/{pictureId}")
    public MsgResponse deletePicture(@PathVariable("pictureId") long id, HttpSession session)
            throws UserException, NotFoundException, MediaException, BadRequestException {
        User admin = userAuthorities.validateAdmin(session);
        Picture picture;
        Optional<Picture> pictureOptional = pr.findById(id);
        if (!pictureOptional.isPresent()) {
            throw new NotFoundException("Picture not found");
        }
        picture = pictureOptional.get();
        Article article = checkArticlePresence(picture.getArtId());
        validateArticleAuthor(admin, article);
        File file = new File(picture.getPath() + ".png");
        if (file.delete()) {
            pr.delete(picture);
            return new MsgResponse(HttpStatus.OK.value(), "image deleted successfully");
        }
        throw new MediaException("Image cannot be deleted!");
    }
}