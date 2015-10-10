package org.github.sgoeschl.boon.extension.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.boon.Lists;
import org.boon.core.Predicate;
import org.boon.primitive.CharBuf;

/**
 * Pretty-print a Boon JSON map while filtering and sorting the entries.
 */
public class ModifyingJsonPrettyPrinter
{
    public static String toString(Object object, String... skippedKeys) {
        return toString(object, false, Arrays.asList(skippedKeys));
    }

    public static String toString(Object object, boolean sort, String... skippedKeys) {
        return toString(object, sort, Arrays.asList(skippedKeys));
    }

    public static String toString(Object object, boolean sort, Collection<String> skippedKeys) {
        return toString(object, sort, new AcceptKeyPredicate(skippedKeys));
    }

    /**
     * Pretty-print the JSON object.
     *
     * @param object the JSON to print
     * @param sort Sort the JSON elements
     * @param filter Accepr or skip JSON elements based on the key
     * @return the JSON string
     */
    public static String toString(Object object, boolean sort, Predicate<String> filter) {
        final ModifyingCharBuf modifyingCharBuf = new ModifyingCharBuf(sort, filter);
        return modifyingCharBuf.prettyPrintObject(object, sort, 0).toString();
    }

    private static class ModifyingCharBuf extends CharBuf {

        private final boolean sort;
        private final Predicate filter;

        private ModifyingCharBuf(boolean sort, Predicate filter) {
            this.sort = sort;
            this.filter = filter;
        }

        @Override
        public CharBuf prettyPrintMap(Map map)
        {
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
            final Map<String, Object> result = (sort ? new TreeMap<String, Object>() : new HashMap<String, Object>());

            for (String key : keys) {
                result.put(key, map.get(key));
            }

            return result;
        }
    }

    private static class AcceptKeyPredicate implements Predicate<String>
    {

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
