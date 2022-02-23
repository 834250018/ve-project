package cn.ve.commons.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ve
 * @date 2021/8/4
 */
@Data
public class IdCardOCRDTO implements Serializable {
    private static final String[] NATION =
        new String[] {"汉", "壮", "满", "回", "苗", "维吾尔", "土家", "彝", "蒙古", "藏", "布依", "侗", "瑶", "朝鲜", "白", "哈尼", "哈萨克", "黎",
            "傣", "畲", "傈僳", "仡佬", "东乡", "高山", "拉祜", "水", "佤", "纳西", "羌", "土", "佬", "锡伯", "柯尔克孜", "达斡尔", "景颇", "毛南",
            "撒拉", "塔吉克", "阿昌", "普米", "鄂温克", "怒", "京", "基诺", "德昂", "保安", "俄罗斯", "裕固", "乌兹别克", "门巴", "鄂伦春", "独龙", "塔塔尔",
            "赫哲", "珞巴", "布朗族"};

    private String name;
    private String gender;
    private String nation;
    private String birthday;
    private String address;
    private String idNo;

    private String fileUri;

    public static IdCardOCRDTO of(String baseInfoText, String idNoText) {
        IdCardOCRDTO idCardOCRDTO = new IdCardOCRDTO();
        String s = baseInfoText.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        if (!s.contains("姓名")) {
            Pattern namePattern = Pattern.compile(".名");
            Matcher nameMatcher = namePattern.matcher(s);
            if (nameMatcher.find()) {
                s = s.replaceAll(nameMatcher.group(0), "姓名");
            }
        }
        if (s.contains("佳址")) {
            s = s.replaceAll("佳址", "住址");
        }
        if (s.contains("任址")) {
            s = s.replaceAll("任址", "住址");
        }
        if (s.contains("性剧")) {
            s = s.replaceAll("性剧", "性别");
        }
        if (!s.contains("汉")) {
            Pattern nationPattern = Pattern.compile("汊");
            Matcher nationMatcher = nationPattern.matcher(s);
            if (nationMatcher.find()) {
                s = s.replaceAll(nationMatcher.group(0), "汉");
            }
        }
        Pattern birthdayPattern = Pattern.compile("\\d*年\\d*月\\d*日");
        Matcher birthdayMatcher = birthdayPattern.matcher(s);
        if (birthdayMatcher.find()) {
            idCardOCRDTO.setBirthday(birthdayMatcher.group(0));
        }
        String name = s.substring(s.indexOf("姓名") + 2).split("性别")[0];
        if (name.length() < 5) {
            idCardOCRDTO.setName(name);
        } else {
            idCardOCRDTO.setName(name.substring(0, 5));
        }
        String gender = s;
        if (s.contains("性别")) {
            gender = s.split("性别")[1];
        }
        if (s.contains("民族")) {
            gender = s.split("民族")[0];
        }
        Pattern genderPattern = Pattern.compile("[男女]");
        Matcher genderMatcher = genderPattern.matcher(gender);
        if (genderMatcher.find()) {
            idCardOCRDTO.setGender(genderMatcher.group(0));
        }
        for (String n : NATION) {
            if (s.contains(n)) {
                idCardOCRDTO.setNation(n);
                break;
            }
        }
        String address = s.substring(s.indexOf("住址") + 2).split("公民身份号码")[0];
        idCardOCRDTO.setAddress(address);
        if (!StringUtils.isBlank(idCardOCRDTO.getBirthday()) && idCardOCRDTO.getAddress()
            .contains(idCardOCRDTO.getBirthday())) {
            idCardOCRDTO.setAddress(idCardOCRDTO.getAddress().split(idCardOCRDTO.getBirthday())[1]);
        }
        if (idCardOCRDTO.getAddress().contains("出生")) {
            idCardOCRDTO.setAddress(idCardOCRDTO.getAddress().split("出生")[1]);
        }
        if (idCardOCRDTO.getAddress().contains("民族")) {
            idCardOCRDTO.setAddress(idCardOCRDTO.getAddress().split("民族")[1]);
        }
        if (idCardOCRDTO.getAddress().contains("性别")) {
            idCardOCRDTO.setAddress(idCardOCRDTO.getAddress().split("性别")[1]);
        }
        if (idCardOCRDTO.getAddress().contains("姓名")) {
            idCardOCRDTO.setAddress(idCardOCRDTO.getAddress().split("姓名")[1]);
        }
        Pattern idNoPattern = Pattern.compile("\\d{15,18}X*");
        Matcher idNoMatcher = idNoPattern.matcher(idNoText.replaceAll("[^\\dX]", ""));
        if (idNoMatcher.find()) {
            idCardOCRDTO.setIdNo(idNoMatcher.group(0));
        }
        return idCardOCRDTO;
    }

}
