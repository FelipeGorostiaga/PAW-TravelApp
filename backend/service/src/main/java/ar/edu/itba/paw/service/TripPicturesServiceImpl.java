package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
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
public class TripPicturesServiceImpl implements TripPicturesService {

    @Autowired
    TripPicturesDao tpd;

    private static final int RESOLUTION = 1000;

    @Override
    public TripPicture create(Trip trip, byte[] image) {

        try {
            byte[] resizedImage = resizeImageAsJPG(image, RESOLUTION);
            return tpd.create(trip, resizedImage);
        } catch (IOException e) {
            return tpd.create(trip, image);
        }
    }

    @Override
    public Optional<TripPicture> findByTripId(long tripId) {
        return tpd.findByTripId(tripId);
    }

    @Override
    public boolean deleteByTripId(long tripId) {
        return tpd.deleteByTripId(tripId);
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

        return encoderOutputStream.toByteArray();
    }
}
