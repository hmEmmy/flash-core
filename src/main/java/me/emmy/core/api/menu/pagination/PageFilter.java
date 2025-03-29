package me.emmy.core.api.menu.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
@RequiredArgsConstructor
public class PageFilter<T> {
    private final String name;
    private final Predicate<T> predicate;
    private boolean enabled;

    /**
     * Test if the filter is enabled and the predicate passes
     *
     * @param t the object to test
     * @return true if the filter is enabled and the predicate passes
     */
    public boolean test(T t) {
        return !enabled || predicate.test(t);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PageFilter && ((PageFilter<?>) object).getName().equals(name);
    }
}