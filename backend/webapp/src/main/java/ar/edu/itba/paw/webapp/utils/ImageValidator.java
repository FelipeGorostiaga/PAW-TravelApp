package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageValidator {

    private static final long MAX_UPLOAD_SIZE = 5242880;

    public static byte[] validateImage(ConstraintViolationsDTO constraintViolationsDTO, MultipartFile image) throws IOException {
        if(image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Invalid image format", "imageInput"));
            }
            else if(image.getSize() > MAX_UPLOAD_SIZE) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Image size is too big", "imageInput"));
            }
            return image.getBytes();
        }
        return null;
    }
}
