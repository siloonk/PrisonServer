package io.github.siloonk.prisonServer.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    public static String formatNumber(double n, int iteration) {
        double d = ((double) (long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        //this determines the class, i.e. 'k', 'm' etc
        //this decides whether to trim the decimals
        // (int) d * 10 / 10 drops the decimal
        return d < 1000? //this determines the class, i.e. 'k', 'm' etc
                (isRound || d > 9.99 ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration]
                : formatNumber(d, iteration+1);
    }

    public static ArrayList<Component> convertStringListToComponentList(List<String> list) {
        ArrayList<Component> components = new ArrayList<>();
        for (String line : list) {
            components.add(MiniMessage.miniMessage().deserialize(line));
        }
        return components;
    }
}
