package au.com.auspost.poboxpicker.service;

import au.com.auspost.poboxpicker.domain.Box;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;

@Component
public class BoxSearchService {
    @Autowired
    private BoxIndex boxIndex;

    private DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

    public List<Box> search(String searchTerm) {
        if (StringUtils.isNumeric(searchTerm)) {
            return searchBoxNumber(Integer.valueOf(searchTerm));
        } else {
            return searchBoxName(searchTerm);
        }
    }

    private List<Box> searchBoxName(String searchTerm) {
        String[] names = searchTerm.split(" ");
        ResultSet<Box> result = null;
        if (names.length == 1) {
            result = singleSearchTerm(names[0]);
        } else {
            result = multipleSearchTerms(names);
        }
        List<Box> boxes = buildBoxList(result);
        return boxes;
    }

    private ResultSet<Box> singleSearchTerm(String name) {
        ResultSet<Box> result;
        // Try an exact or phonetic match on last name
        Query<Box> query =  or(
                                equal(Box.LAST_NAME, name),
                                equal(Box.LAST_NAME_PHONETIC, phonetic(name))
                            );
        result = boxIndex.query(query);

        // Next try an exact match on first name or alternate names
        if (result.isEmpty()) {
            query = or(
                        equal(Box.FIRST_NAME, name),
                        contains(Box.ALTERNAME_NAMES, name)
                    );
            result = boxIndex.query(query);
        }

        // Next try a phonetic match on first name or alternate names
        if (result.isEmpty()) {
            query = or(
                        equal(Box.FIRST_NAME_PHONETIC, phonetic(name)),
                        contains(Box.ALTERNAME_NAMES_PHONETIC, phonetic(name))
                    );
            result = boxIndex.query(query);
        }
        return result;
    }

    private ResultSet<Box> multipleSearchTerms(String[] names) {
        String lastName = names[names.length - 1];
        List<String> firstNames = Arrays.asList((String[]) ArrayUtils.subarray(names, 0, names.length - 1));
        List<String> firstNamesPhonetic = phonetic(firstNames);
        ResultSet<Box> result;

        // Try an exact match on last name and first name
        Query<Box> query = and(
                                equal(Box.LAST_NAME, lastName),
                                in(Box.FIRST_NAME, firstNames)
                           );
        result = boxIndex.query(query);

        // Next try an exact match on last name and alternate names
        if (result.isEmpty()) {
            query = and(
                    equal(Box.LAST_NAME, lastName),
                    in(Box.ALTERNAME_NAMES, firstNames)
            );
            result = boxIndex.query(query);
        }

        // Next try a phonetic match on last name and an exact or phonetic match on first name or alternate names
        if (result.isEmpty()) {
            query = and(
                        equal(Box.LAST_NAME_PHONETIC, phonetic(lastName)),
                        or(
                                in(Box.FIRST_NAME, firstNames),
                                in(Box.FIRST_NAME_PHONETIC, firstNamesPhonetic),
                                in(Box.ALTERNAME_NAMES, firstNames),
                                in(Box.ALTERNAME_NAMES_PHONETIC, firstNamesPhonetic)
                        )
                    );
            result = boxIndex.query(query);
        }
        return result;
    }

    private List<Box> searchBoxNumber(Integer number) {
        Query<Box> query = equal(Box.NUMBER, number);
        ResultSet<Box> result = boxIndex.query(query);

        List<Box> boxes = buildBoxList(result);
        return boxes;
    }

    private List<Box> buildBoxList(ResultSet<Box> result) {
        if (result == null) {
            return Collections.EMPTY_LIST;
        } else {
            List<Box> boxes = new ArrayList<>();
            for (Box box : result) {
                boxes.add(box);
            }
            return boxes;
        }
    }

    private String phonetic(String name) {
        return doubleMetaphone.doubleMetaphone(name);
    }

    private List<String> phonetic(List<String> names) {
        List<String> phoneticNames = new ArrayList<>(names.size());
        for (String name : names) {
            phoneticNames.add(phonetic(name));
        }
        return phoneticNames;
    }
}
