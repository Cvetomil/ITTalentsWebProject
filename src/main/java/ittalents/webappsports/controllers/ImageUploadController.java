package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.ImageUploadDTO;
import ittalents.webappsports.exceptions.NotAdminException;
import ittalents.webappsports.exceptions.UserNotLoggedException;
import ittalents.webappsports.models.Picture;
import ittalents.webappsports.repositories.ArticleRepository;
import ittalents.webappsports.repositories.PictureRepository;
import ittalents.webappsports.util.userAuthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class ImageUploadController {

    @Autowired
    ArticleRepository ar;
    @Autowired
    PictureRepository pr;

    public static final String IMAGE_DIR = "C:\\Users\\aleks\\Pictures\\Upload\\";

    @PostMapping("/images/upload/{artId}")
    public void uploadImage(@RequestBody ImageUploadDTO dto, @PathVariable long artId, HttpSession session) throws IOException, UserNotLoggedException, NotAdminException {

        userAuthorities.validateAdmin(session);

        StringBuilder picturePath = new StringBuilder();
        picturePath.append( ar.getOne(artId).getTitle());
        picturePath.append(System.currentTimeMillis());

        Picture picture = new Picture();
        picture.setPath(picturePath.toString());
        picture.setArtId(artId);
        pr.save(picture);

        String base64 = dto.getFileStr();
                byte[] bytes = Base64.getDecoder().decode(base64);

        File newImage = new File(IMAGE_DIR + picturePath.toString() + ".png");
        FileOutputStream fos = new FileOutputStream(newImage);
        fos.write(bytes);

    }

    @GetMapping(value="/images/{name}", produces = "image/png")
    public byte[] downloadImage(@PathVariable("name") String imageName) throws IOException {
        File newImage = new File(IMAGE_DIR +imageName);
        FileInputStream fis = new FileInputStream(newImage);
        return fis.readAllBytes();
    }
}