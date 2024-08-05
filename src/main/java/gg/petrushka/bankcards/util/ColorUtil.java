package gg.petrushka.bankcards.util;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    public static String colorizeString(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return message;
    }

    public static List<String> colorizeList(List<String> list) {
        ArrayList<String> newList = new ArrayList<>();
        list.forEach(line -> newList.add(colorizeString(line)));
        return newList;
    }
}
