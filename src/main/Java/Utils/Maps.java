package Utils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maps {

    public static Map<String, Object> asFlattendMap(Map<String, Object> map) {
        return map.entrySet().stream()
                .flatMap(Maps::flatten)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Stream<Map.Entry<String, ?>> flatten(Map.Entry<String, ?> entry) {
        if (entry.getValue() instanceof Map) {
            return ((Map<String,?>) entry.getValue()).entrySet().stream().flatMap(Maps::flatten);
        }
        return Stream.of(entry);
    }
}
