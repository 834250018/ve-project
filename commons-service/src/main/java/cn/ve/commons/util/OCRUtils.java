package cn.ve.commons.util;

import cn.ve.base.util.ImgUtil;
import cn.ve.commons.pojo.IdCardOCRDTO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author ve
 * @date 2021/8/4
 */
public class OCRUtils {

    public static ITesseract instance = getiTesseract();

    private static ITesseract getiTesseract() {
        ITesseract instance = new Tesseract();
        //相对目录，这个时候tessdata目录和src目录平级
        //        instance.setDatapath("commons-service/src/main/resources/tessdata");
        instance.setDatapath("/app/ocrdata");
        //选择字库文件（只需要文件名，不需要后缀名）
        instance.setLanguage("chi_sim");
        return instance;
    }

    /**
     * 通过图片获取身份证信息(身份证ocr)
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static IdCardOCRDTO idCardOCR(File input) throws Exception {
        BufferedImage read = ImageIO.read(input);
        int width = read.getWidth();
        int height = read.getHeight();
        File grayFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        ImgUtil.gray(input, grayFile);
        // 截取身份证号码
        String idNoOCRResult = idCardOCR0(Boolean.FALSE, grayFile, 0, (int)(height * 0.75), width, (int)(height * 0.166666));
        // 截取身份证个人信息
        String ocrText =
            idCardOCR0(Boolean.TRUE, grayFile, (int)(width * 0.07650), (int)(height * 0.11666), (int)(width * 0.546448),
                (int)(height * 0.583333));
        IdCardOCRDTO of = IdCardOCRDTO.of(ocrText, idNoOCRResult);
        grayFile.delete();
        return of;
    }

    private static String idCardOCR0(boolean defaultS, File input, int x, int y, int width, int height)
        throws Exception {
        File cutImg = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        // 截取图片
        ImgUtil.cutImage(input, cutImg, x, y, width, height);
        // 二值化
        File tempFile1 = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        File tempFile2 = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        ImgUtil.binarizationByScope(cutImg, tempFile1, defaultS ? 0.68d : 0.68d);
        ImgUtil.binarizationByAll(cutImg, tempFile2, defaultS ? 0.65d : 0.68d);
        cutImg.delete();
        // 合并
        File compose = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        ImgUtil.compose(tempFile1, tempFile2, compose);
        System.out.println(tempFile1);
        tempFile1.delete();
        tempFile2.delete();
        // 字库匹配
        String text = instance.doOCR(compose);
        compose.delete();
        System.out.println(text);
        return text;
    }

    public static String bankCardOCR(File input) throws Exception {
        File gray = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        File tempFile1 = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        File tempFile2 = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
        // 截取
        ImgUtil.gray(input, gray);
        // 二值化
        ImgUtil.binarizationByAll(gray, tempFile, 0.65d);
        ImgUtil.binarizationByScope(gray, tempFile1, 0.68d);
        // 合并
        ImgUtil.compose(tempFile, tempFile1, tempFile2);
        // 字库匹配
        String text = instance.doOCR(tempFile2);
        gray.delete();
        tempFile.delete();
        tempFile1.delete();
        tempFile2.delete();
        return text;
    }

}