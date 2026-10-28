package org.example.sort;

import java.util.Comparator;
import java.util.List;

public class SelectionSort<T> implements SortAlgorithm<T> {
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        for (int i = 0; i < list.size(); i++) {
            int minIndex = i;

            for (int j = i + 1; j < list.size(); j++) {
                if (comparator.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            T temp = list.get(i);
            list.set(i, list.get(minIndex));
            list.set(minIndex, temp);
        }
    }
}
