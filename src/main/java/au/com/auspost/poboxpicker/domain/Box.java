package au.com.auspost.poboxpicker.domain;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.apache.commons.codec.language.DoubleMetaphone;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Box {
    private static final DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

    public static final Attribute<Box, Integer> NUMBER = new SimpleAttribute<Box, Integer>() {
        @Override
        public Integer getValue(Box box, QueryOptions queryOptions) {
            return box.getNumber();
        }
    };

    public static final Attribute<Box, String> FIRST_NAME = new SimpleAttribute<Box, String>() {
        @Override
        public String getValue(Box box, QueryOptions queryOptions) {
            return box.getFirstName().toLowerCase();
        }
    };

    public static final Attribute<Box, String> FIRST_NAME_PHONETIC = new SimpleAttribute<Box, String>() {
        @Override
        public String getValue(Box box, QueryOptions queryOptions) {
            return doubleMetaphone.doubleMetaphone(box.getFirstName());
        }
    };

    public static final Attribute<Box, String> ALTERNAME_NAMES = new MultiValueAttribute<Box, String>() {
        @Override
        public Iterable<String> getValues(Box box, QueryOptions queryOptions) {
            return box.getAlternateNames().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
    };

    public static final Attribute<Box, String> ALTERNAME_NAMES_PHONETIC = new MultiValueAttribute<Box, String>() {
        @Override
        public Iterable<String> getValues(Box box, QueryOptions queryOptions) {
            ArrayList<String> phonemes = new ArrayList<>();
            for (String altName : box.getAlternateNames()) {
                phonemes.add(doubleMetaphone.doubleMetaphone(altName));
            }
            return phonemes;
        }
    };

    public static final Attribute<Box, String> LAST_NAME = new SimpleAttribute<Box, String>() {
        @Override
        public String getValue(Box box, QueryOptions queryOptions) {
            return box.getLastName().toLowerCase();
        }
    };

    public static final Attribute<Box, String> LAST_NAME_PHONETIC = new SimpleAttribute<Box, String>() {
        @Override
        public String getValue(Box box, QueryOptions queryOptions) {
            return doubleMetaphone.doubleMetaphone(box.getLastName());
        }
    };

    private Integer number;

    private String firstName;

    private String lastName;

    private List<String> alternateNames;

    private Integer ledNumber;

    public Box(Integer number, String firstName, String lastName, List<String> alternateNames, Integer ledNumber) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alternateNames = alternateNames;
        this.ledNumber = ledNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getAlternateNames() {
        return alternateNames;
    }

    public Integer getLedNumber() {
        return ledNumber;
    }
}
