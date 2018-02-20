package org.whstsa.library.util;

import org.whstsa.library.api.Identifiable;

import java.util.*;

public class ListUtils {
    public static <T extends Identifiable> T findIdentifiable(List<T> identifiableList, String name) {
        Map<Integer, T> scores = new HashMap<>();
        identifiableList.forEach(item -> {
            int score = 0;
            String comparingName = item.getName();
            if (name.equals(comparingName)) {
                score += 10;
            } else if (name.equalsIgnoreCase(comparingName)) {
                score += 8;
            }

            if (name.contains(comparingName)) {
                score += 3;
            } else if (name.toLowerCase().contains(comparingName.toLowerCase())) {
                score += 2;
            }

            if (score > 0) {
                scores.put(score, item);
            }
        });

        Optional<Map.Entry<Integer, T>> itemEntry = scores.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).findFirst();
        if (itemEntry.isPresent()) {
            return itemEntry.get().getValue();
        }

        return null;
    }
}
