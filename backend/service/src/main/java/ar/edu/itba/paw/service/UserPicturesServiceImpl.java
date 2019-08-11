package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class UserPicturesServiceImpl implements UserPicturesService {

    @Autowired
    private UserPicturesDao upd;

    private static final int RESOLUTION = 800;

    @Override
    public UserPicture create(User user, byte[] image) {
        try {
            byte[] resizedImage = resizeImageAsJPG(image, RESOLUTION);
            return upd.create(user, resizedImage);
        }
        catch (IOException ex) {
            return upd.create(user, image);
        }
    }

    @Override
    public Optional<UserPicture> findByUserId(long userId) {
        return upd.findByUserId(userId);
    }

    @Override
    public boolean deleteByUserId(long userId) {
        return upd.deleteByUserId(userId);
    }

    private byte[] resizeImageAsJPG(byte[] pImageData, int pMaxWidth) throws IOException {
        ImageIcon imageIcon = new ImageIcon(pImageData);
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        if (pMaxWidth > 0 && width > pMaxWidth) {
            double ratio = (double) pMaxWidth / imageIcon.getIconWidth();
            height = (int) (imageIcon.getIconHeight() * ratio);
            width = pMaxWidth;
        }
        BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(imageIcon.getImage(), 0, 0, width, height, null);
        g2d.dispose();
        ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();

        ImageIO.write(bufferedResizedImage, "jpeg", encoderOutputStream);

        return  encoderOutputStream.toByteArray();
    }

}
