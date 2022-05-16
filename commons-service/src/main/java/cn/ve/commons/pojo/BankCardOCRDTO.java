package cn.ve.commons.pojo;

import cn.ve.base.util.StringConstant;
import cn.ve.commons.util.BankUtil;
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
public class BankCardOCRDTO implements Serializable {
    private static final String[] BANK_NAME =
            new String[]{"中国银行", "农业银行", "工商银行", "招商银行", "广发银行", "邮储银行", "光大银行", "民生银行"};

    private String bankName;
    private String bankCardNo;
    private String fileUri;
    private static final String BANK_NAME_PATTERN = "中国..银行";

    public static BankCardOCRDTO of(String result) {
        BankCardOCRDTO bankCardOCRDTO = new BankCardOCRDTO();
        // 处理特殊符号
        String s = result.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        String numStr = s.replaceAll("[^(0-9)]+", StringConstant.DOT);
        bankCardOCRDTO.setBankCardNo(numStr);
        Pattern namePattern = Pattern.compile(BANK_NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(s);
        if (nameMatcher.find()) {
            bankCardOCRDTO.setBankName(nameMatcher.group(0));
        }
        if (StringUtils.isBlank(bankCardOCRDTO.getBankName())) {
            for (String b : BANK_NAME) {
                if (s.contains(b)) {
                    bankCardOCRDTO.setBankName(b);
                    break;
                }
            }
        }
        if (StringUtils.isBlank(bankCardOCRDTO.getBankName())) {
            bankCardOCRDTO.setBankName(BankUtil.getname(bankCardOCRDTO.getBankCardNo()));
        }

        return bankCardOCRDTO;
    }

}
