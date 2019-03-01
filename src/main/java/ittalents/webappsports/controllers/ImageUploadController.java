package ittalents.webappsports.controllers;

import ittalents.webappsports.dto.ImageUploadDTO;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class ImageUploadController {

    public static final String IMAGE_DIR = "C:\\Users\\aleks\\Pictures\\Upload\\";

    @PostMapping("/images")
    public void uploadImage(@RequestBody ImageUploadDTO dto) throws IOException {
        String base64 = dto.getFileStr();
                byte[] bytes = Base64.getDecoder().decode(base64);
        String name = "upload.png";
        File newImage = new File(IMAGE_DIR +name);
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