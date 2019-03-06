package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.ImageUploadDTO;
import ittalents.webappsports.exceptions.*;
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
import java.util.Optional;

@RestController
public class ImageController {

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
    @DeleteMapping("/image/{id}")
    public Picture deletePicture(@PathVariable("id") long id, HttpSession session) throws UserException,NotFoundException,MediaException {
        userAuthorities.validateAdmin(session);
        Picture picture;
        Optional<Picture> pictureOptional = pr.findById(id);
        if(!pictureOptional.isPresent()){
            throw new NotFoundException("Picture not found");
        }
        picture = pictureOptional.get();

        pr.delete(picture);


        File file = new File(IMAGE_DIR + picture.getPath());
        if(file.delete()){
            return picture;
        }
        else{
            throw new MediaException("Picture could not be deleted");
        }


    }
}