package net.incongru.tichu.simu.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface NameableEnum {
    List<String> altNames();

    static <E extends Enum<E> & NameableEnum> E byName(Class<E> enumClass, String name) {
        for (E p : enumClass.getEnumConstants()) {
            if (name.equalsIgnoreCase(p.name())) {
                return p;
            }
            // TODO oof -- let's improve this
            if (Lists.transform(p.altNames(), String::toLowerCase).contains(name.toLowerCase())) {
                return p;
            }
        }
        // throw new LineParserException
        throw new IllegalArgumentException(name + " is not a valid " + enumClass.getSimpleName() + ". Known values are: " + allNamesOf(enumClass));
    }

    static <E extends Enum<E> & NameableEnum> String allNamesOf(Class<E> enumClass) {
        final List<String> allNames = new ArrayList<>();
        for (E p : enumClass.getEnumConstants()) {
            allNames.add(p.name().toLowerCase());
            // TODO oof -- let's improve this
            allNames.addAll(Lists.transform(p.altNames(), String::toLowerCase));
        }
        return allNames.stream().collect(Collectors.joining(", "));
    }
}
