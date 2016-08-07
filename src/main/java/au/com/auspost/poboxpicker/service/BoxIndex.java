package au.com.auspost.poboxpicker.service;

import au.com.auspost.poboxpicker.domain.Box;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BoxIndex implements InitializingBean {
    private IndexedCollection<Box> boxes = new ConcurrentIndexedCollection<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        boxes.addIndex(NavigableIndex.onAttribute(Box.NUMBER));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.FIRST_NAME));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.FIRST_NAME_PHONETIC));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.ALTERNAME_NAMES));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.LAST_NAME));
        boxes.addIndex(RadixTreeIndex.onAttribute(Box.LAST_NAME_PHONETIC));

        Box box;

        box = new Box(1001, "david", "howe", Arrays.asList("dave", "davo"), 1);
        boxes.add(box);

        box = new Box(3005, "alex", "lewis", Arrays.asList("alexander"), 2);
        boxes.add(box);

        box = new Box(3080, "aman", "sahani", Collections.EMPTY_LIST, 3);
        boxes.add(box);
    }

    public IndexedCollection<Box> getIndex() {
        return boxes;
    }
}
