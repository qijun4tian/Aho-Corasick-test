package ac;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author qijun
 * @since 2023/10/19
 */
@Data
@AllArgsConstructor
public class Keyword {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return Objects.equals(word, keyword.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    private String word;

    private List<Integer> categories;
}
