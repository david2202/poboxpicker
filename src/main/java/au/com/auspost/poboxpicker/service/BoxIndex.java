package au.com.auspost.poboxpicker.service;

import au.com.auspost.poboxpicker.domain.Box;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.resultset.ResultSet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;

@Component
public class BoxIndex implements InitializingBean {
    private static final DeduplicationOption DEDUPE =
            deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION);

    private IndexedCollection<Box> boxes = new ConcurrentIndexedCollection<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        boxes.addIndex(NavigableIndex.onAttribute(Box.NUMBER));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.FIRST_NAME));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.FIRST_NAME_PHONETIC));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.ALTERNAME_NAMES));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.LAST_NAME));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.LAST_NAME_PHONETIC));

        boxes.add(new Box(1001, "david", "howe", Arrays.asList("dave", "davo"), 0));
        boxes.add(new Box(3005, "alex", "lewis", Arrays.asList("alexander"), 1));
        boxes.add(new Box(3080, "aman", "sahani", Collections.EMPTY_LIST, 2));
        boxes.add(new Box(4024, "peter", "smith", Arrays.asList("pete"), 3));
        boxes.add(new Box(5198, "jon", "smyth", Arrays.asList("jonathon"), 4));
    }

    public ResultSet<Box> query(Query<Box> query) {
        return boxes.retrieve(query, queryOptions(DEDUPE));
    }
}
