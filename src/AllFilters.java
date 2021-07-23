/*
 * Allfilters is used to aggregate and apply multiple filters
 * For data to satisfy an Allfilters object, the data must satisfy each filter included in the Allfilters object
 */

import java.util.ArrayList;

public class AllFilters implements Filter {
    ArrayList<Filter> filters;

    public AllFilters() {
        filters = new ArrayList<Filter>();
    }

    public void addFilter(Filter f) {
        filters.add(f);
    }

    @Override
    public boolean satisfies(String id) {
        for(Filter f : filters) {
            if (! f.satisfies(id)) {
                return false;
            }
        }

        return true;
    }

}
