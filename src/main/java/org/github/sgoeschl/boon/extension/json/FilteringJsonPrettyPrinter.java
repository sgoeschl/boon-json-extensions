package org.github.sgoeschl.boon.extension.json;

import io.advantageous.boon.core.Lists;
import io.advantageous.boon.core.Predicate;
import io.advantageous.boon.primitive.CharBuf;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.util.Arrays.asList;

/**
 * Pretty-print a Boon JSON map while filtering and sorting the entries.
 */
public final class FilteringJsonPrettyPrinter {

    private FilteringJsonPrettyPrinter() {}

    public static String print(Object object, String... skippedKeys) {
        return print(object, false, asList(skippedKeys));
    }

    public static String print(Object object, boolean sort) {
        return print(object, sort, new String[0]);
    }

    public static String print(Object object, boolean sort, String... skippedKeys) {
        return print(object, sort, asList(skippedKeys));
    }

    public static String print(Object object, boolean sort, Collection<String> skippedKeys) {
        return print(object, sort, new AcceptKeyPredicate(skippedKeys));
    }

    /**
     * Pretty-print the JSON object.
     *
     * @param object The JSON object to print
     * @param sort   Sort the JSON elements alphabetically or keep their natural order
     * @param filter Accept or skip JSON elements based on the key
     * @return the JSON string
     */
    private static String print(Object object, boolean sort, Predicate<String> filter) {
        final ModifyingCharBuf modifyingCharBuf = new ModifyingCharBuf(sort, filter);
        return modifyingCharBuf.prettyPrintObject(object, sort, 0).toString();
    }

    /**
     * Some ugly and intrusive hack using a lot of
     * internal knowledge to get it working.
     */
    private static class ModifyingCharBuf extends CharBuf {

        private final boolean sort;
        private final Predicate filter;

        private ModifyingCharBuf(boolean sort, Predicate filter) {
            this.sort = sort;
            this.filter = filter;
        }

        @Override
        public CharBuf prettyPrintMap(Map map) {
            return this.prettyPrintMap(map, 0);
        }

        @Override
        @SuppressWarnings("unchecked")
        public CharBuf prettyPrintMap(Map source, int indent) {

            if (source == null) {
                return null;
            }

            Map map = createMap(source, sort, filter);
            return super.prettyPrintMap(map, indent);
        }

        private Map createMap(Map<String, Object> map, boolean sort, Predicate<String> predicate) {

            final List<String> keys = Lists.filterBy(map.keySet(), predicate);
            final Map<String, Object> result = (sort ? new TreeMap<String, Object>() : new LinkedHashMap<String, Object>());

            for (String key : keys) {
                result.put(key, map.get(key));
            }

            return result;
        }
    }

    private static final class AcceptKeyPredicate implements Predicate<String> {

        private final Set<String> skippedKeys;

        public AcceptKeyPredicate(Collection<String> skippedKeys) {
            this.skippedKeys = new HashSet<>(skippedKeys);
        }

        @Override
        public boolean test(String input) {
            return input != null && !skippedKeys.contains(input);
        }
    }
}
