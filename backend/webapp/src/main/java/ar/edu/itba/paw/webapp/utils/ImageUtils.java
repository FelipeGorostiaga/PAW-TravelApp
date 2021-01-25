package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public class ImageUtils {

    private static final long MAX_UPLOAD_SIZE = 5242880;

    public static byte[] validateImage(ConstraintViolationsDTO constraintViolationsDTO, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            System.out.println(image.getName());
            System.out.println(image.getContentType());
            String contentType = image.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Invalid image format", "imageInput"));
            } else if (image.getSize() > MAX_UPLOAD_SIZE) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Image size is too big", "imageInput"));
            }
            return image.getBytes();
        }
        return null;
    }

    public static boolean validateImage(FormDataContentDisposition fileMetaData, int size) {
        String filename = fileMetaData.getFileName();
        Optional<String> extensionOptional = Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        if (!extensionOptional.isPresent()) {
            return false;
        }
        if (!extensionOptional.get().equals("jpeg") && !extensionOptional.get().equals("png") && !extensionOptional.get().equals("jpg")) {
            return false;
        }
        return size <= MAX_UPLOAD_SIZE;
    }

    public static BufferedImage toBufferedImage(byte[] array) {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] resizeToProfileSize(byte[] array, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = toBufferedImage(array);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return toByteArray(resizedImage, "jpeg");
    }

    public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, format, outputStream);
        return outputStream.toByteArray();

    }
}
