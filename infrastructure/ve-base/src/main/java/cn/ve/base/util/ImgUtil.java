package cn.ve.base.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

/**
 * @author ve
 * @date 2021/7/28
 */
public class ImgUtil {

    private static final int BLACK = new Color(0, 0, 0).getRGB();
    private static final int WHITE = new Color(255, 255, 255).getRGB();
    private static final double X_DOUBLE = 0.65d;

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

    public static void gray(File input, File output) throws IOException {
        if (output.exists()) {
            output.delete();
        }
        BufferedImage bufferedImage = ImageIO.read(input);
        BufferedImage grayImage =
            new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                final int color = bufferedImage.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int)(0.3 * r + 0.59 * g + 0.11 * b);
                int newPixel = colorToRGB(255, gray, gray, gray);
                grayImage.setRGB(i, j, newPixel);
            }
        }
        ImageIO.write(grayImage, "png", output);
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

    public static void binarizationByBindX(File input, File out) throws IOException {
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        float[] rgb = new float[3];
        // 坐标
        double[][] coordinate = new double[width][height];
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                coordinate[x][y] = avg;
            }
        }
        double sw;
        for (int x = 0; x < width; x++) {
            double summ = 0d;
            for (int i = 0; i < height; i++) {
                summ += coordinate[x][i];
            }
            sw = summ * X_DOUBLE / height;
            for (int y = 0; y < height; y++) {
                if (coordinate[x][y] <= sw) {
                    bi.setRGB(x, y, BLACK);
                } else {
                    bi.setRGB(x, y, WHITE);
                }
            }
        }

        ImageIO.write(bi, "png", out);
    }

    public static void binarizationByBindY(File input, File out) throws IOException {
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        float[] rgb = new float[3];
        // 坐标
        double[][] coordinate = new double[width][height];
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                coordinate[x][y] = avg;
            }
        }
        double sw;
        for (int y = 0; y < height; y++) {
            double summ = 0d;
            for (int i = 0; i < width; i++) {
                summ += coordinate[i][y];
            }
            sw = summ * 0.63 / width;
            for (int x = 0; x < width; x++) {
                if (coordinate[x][y] <= sw) {
                    bi.setRGB(x, y, BLACK);
                } else {
                    bi.setRGB(x, y, WHITE);
                }
            }
        }

        ImageIO.write(bi, "png", out);
    }

    /**
     * @param input
     * @param out
     * @param s     0.65d
     * @throws IOException
     */
    public static void binarizationByAll(File input, File out, double s) throws IOException {
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        float[] rgb = new float[3];
        // 坐标
        double[][] coordinate = new double[width][height];
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        double sum = 0d;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                coordinate[x][y] = avg;
                sum += coordinate[x][y];
            }
        }
        double sw = sum * s / width / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (coordinate[x][y] <= sw) {
                    bi.setRGB(x, y, BLACK);
                } else {
                    bi.setRGB(x, y, WHITE);
                }
            }
        }

        ImageIO.write(bi, "png", out);
    }

    /**
     * @param input
     * @param out
     * @param s     0.68d
     * @throws IOException
     */
    public static void binarizationByScope(File input, File out, double s) throws IOException {
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        float[] rgb = new float[3];
        // 坐标
        double[][] coordinate = new double[width][height];
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                coordinate[x][y] = avg;
            }
        }
        double sw;
        int scope = 20;
        for (int x = 0; x < width; x++) {
            int xOffset = Math.max(0, x - scope), xLimit = Math.min(x + scope, width);
            for (int y = 0; y < height; y++) {
                int yOffset = Math.max(0, y - scope), yLimit = Math.min(y + scope, height);
                double summ = 0d;
                for (int i = xOffset; i < xLimit; i++) {
                    for (int j = yOffset; j < yLimit; j++) {
                        summ += coordinate[i][j];
                    }
                }
                sw = summ * s / (xLimit - xOffset) / (yLimit - yOffset);

                if (coordinate[x][y] <= sw) {
                    bi.setRGB(x, y, BLACK);
                } else {
                    bi.setRGB(x, y, WHITE);
                }
            }
        }

        ImageIO.write(bi, "png", out);
    }

    public static void compose(File input, File input2, File out) throws IOException {
        BufferedImage image = ImageIO.read(input);
        BufferedImage image2 = ImageIO.read(input2);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isWhite(image.getRGB(x, y)) && isWhite(image2.getRGB(x, y))) {
                    bi.setRGB(x, y, WHITE);
                }
            }
        }
        ImageIO.write(bi, "png", out);
    }

    private static boolean isWhite(int v) {
        int rgb0 = (v & 0xff0000) >> 16;
        int rgb1 = (v & 0xff00) >> 8;
        int rgb2 = (v & 0xff);

        return rgb0 == 255 && rgb1 == 255 && rgb2 == 255;
    }

    public static void clearBlackPoint(File input, File out) throws IOException {
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        float[] rgb = new float[3];
        // 坐标
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    bi.setRGB(x, y, WHITE);
                    continue;
                }
                bi.setRGB(x, y, image.getRGB(x, y));
                if (isBlack(image.getRGB(x - 1, y))) {
                    continue;
                }
                if (isBlack(image.getRGB(x, y - 1))) {
                    continue;
                }
                if (isBlack(image.getRGB(x + 1, y))) {
                    continue;
                }
                if (isBlack(image.getRGB(x, y + 1))) {
                    continue;
                }
                bi.setRGB(x, y, WHITE);
            }
        }
        ImageIO.write(bi, "png", out);
    }

    private static boolean isBlack(int v) {
        int rgb0 = (v & 0xff0000) >> 16;
        int rgb1 = (v & 0xff00) >> 8;
        int rgb2 = (v & 0xff);

        return rgb0 == 0 && rgb1 == 0 && rgb2 == 0;
    }

    public static void cutImage(File input, File output, int x, int y, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(input);
        // 保存裁剪后的图片
        ImageIO.write(image.getSubimage(x, y, width, height), "png", output);
    }

    public static String imgBase64(String path) {
        /**
         *  对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则返回
         */
        String imgBase64 = "";
        if (path.startsWith("http")) {
            imgBase64 = path;
        } else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int)file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = Base64.getEncoder().encodeToString(content);
            } catch (IOException e) {
                e.printStackTrace();
                return imgBase64;
            }
        }

        return imgBase64;
    }
}
