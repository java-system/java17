package system;

import java.util.ArrayList;

public class SimpleList extends ArrayList<Object> {
    public Dynamic toDynamic() {
        return Dynamic.wrap(this);
    }
}
