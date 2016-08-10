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

        boxes.add(new Box(1001, "David", "Howe", Arrays.asList("Dave", "Davo"), 0));
        boxes.add(new Box(3005, "Alex", "Lewis", Arrays.asList("Alexander"), 1));
        boxes.add(new Box(3080, "Aman", "Sahani", Collections.EMPTY_LIST, 2));
        boxes.add(new Box(4024, "Peter", "Smith", Arrays.asList("Pete"), 3));
        boxes.add(new Box(5198, "Jon", "Smyth", Arrays.asList("Jonathon"), 4));
        boxes.add(new Box(8000, "George", "Aligianis", Collections.EMPTY_LIST, 5));
        boxes.add(new Box(8000, "Andrew", "Richmond", Arrays.asList("Drew", "Andy"), 6));
        boxes.add(new Box(8000, "Mark", "Philippoussis", Collections.EMPTY_LIST, 7));
    }

    public ResultSet<Box> query(Query<Box> query) {
        return boxes.retrieve(query, queryOptions(DEDUPE));
    }
}
