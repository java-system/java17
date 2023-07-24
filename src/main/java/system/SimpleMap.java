package system;

import java.util.LinkedHashMap;

public class SimpleMap extends LinkedHashMap<String, Object> {
    public Dynamic toDynamic() {
        return Dynamic.wrap(this);
    }
}
