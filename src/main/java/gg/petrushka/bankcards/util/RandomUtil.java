package gg.petrushka.bankcards.util;

import gg.petrushka.bankcards.service.ConfigData;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class RandomUtil {

    @Getter @Setter
    private static ConfigData configData;

    private static final Random random = new Random();

    public static String generateCode(){
        String codeAlphabet = (String) configData.getData("card-settings.code-alphabet");
        String numberFormat = (String) configData.getData("card-settings.number-format");

        StringBuilder code = new StringBuilder();
        for (char c : numberFormat.toCharArray()) {
            if(c == '#'){
                code.append(codeAlphabet.toCharArray()[random.nextInt(codeAlphabet.length())]);
            } else {
                code.append(c);
            }
        }
        return code.toString();
    }
}
