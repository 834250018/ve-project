package cn.ve.commons.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author ve
 * @date 2021/7/28
 */
public class ImgUtils {

    /**
     * 图片压缩
     *
     * @param source
     * @param result
     * @throws IOException
     */
    public static void compress(File source, File result, float outputQuality) throws IOException {
        Thumbnails.of(source).scale(1f).outputQuality(outputQuality).toOutputStream(new FileOutputStream(result));
    }

    /**
     * 图片压缩
     *
     * @param is
     * @param result
     * @throws IOException
     */
    public static void compress(InputStream is, File result, float outputQuality) throws IOException {
        Thumbnails.of(is).scale(1f).outputQuality(outputQuality).toOutputStream(new FileOutputStream(result));
    }

    /**
     * 图片压缩
     *
     * @param url
     * @param result
     * @throws IOException
     */
    public static void compress(URL url, File result, float outputQuality) throws IOException {
        Thumbnails.of(url).scale(1f).outputQuality(outputQuality).toOutputStream(new FileOutputStream(result));
    }

    /**
     * 图片添加水印
     *
     * @param is
     * @param result
     * @param watermark
     * @throws IOException
     */
    public static void addWatermark(InputStream is, File result, String watermark) throws IOException {
        BufferedImage img = ImageIO.read(is);
        addWatermark0(result, watermark, img);
    }

    /**
     * 图片添加水印
     *
     * @param url
     * @param result
     * @param watermark
     * @throws IOException
     */
    public static void addWatermark(URL url, File result, String watermark) throws IOException {
        BufferedImage img = ImageIO.read(url);
        addWatermark0(result, watermark, img);
    }

    /**
     * 图片添加水印
     *
     * @param source
     * @param result
     * @param watermark
     * @throws IOException
     */
    public static void addWatermark(File source, File result, String watermark) throws IOException {
        BufferedImage img = ImageIO.read(source);
        addWatermark0(result, watermark, img);
    }

    private static void addWatermark0(File result, String watermark, BufferedImage img) throws IOException {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, width, height, null);
        graphics.setColor(Color.BLUE);
        graphics.shear(0.1, -0.4);
        graphics.setFont(new Font(null, Font.PLAIN, 50));
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .1f));
        for (int i = 0; i < width; i += 200 + (watermark.length() * 10)) {
            for (int j = 0; j < height * 2; j += 200) {
                graphics.drawString(watermark, i, j);
            }
        }
        graphics.dispose();
        FileOutputStream fos = new FileOutputStream(result);
        ImageIO.write(bufferedImage, "jpeg", fos);
        fos.close();
    }
}
