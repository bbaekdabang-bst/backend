package com.bbacks.bst.global.filtering;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BadWordsFiltering implements BadWords{
    private final Set<String> set = new HashSet<>(List.of(koreaWord1));
    private String substituteValue = "*";

    public BadWordsFiltering(String substituteValue) {
        this.substituteValue = substituteValue;
    }

    public BadWordsFiltering() {}

    // 비속어 대체
    public String change(String text) {
        String[] words = set.stream().filter(text::contains).toArray(String[]::new);
        for (String v : words) {
            String sub = this.substituteValue.repeat(v.length());
            text = text.replace(v, sub);
        }
        return text;
    }
}
