package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItunesCategoryTest {
    private Itunes.Category category;
    private Itunes.Category subCategory;

    @Before
    public void setup() {
        subCategory = new Itunes.Category("sub", null);
        final List<Itunes.Category> subCategoryList = new ArrayList<>(1);
        subCategoryList.add(subCategory);
        category = new Itunes.Category("category", subCategoryList);
    }


    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Itunes.Category.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToStringWithoutSubCategory() {
        assertEquals("Category{name='sub'}", subCategory.toString());
    }

    @Test
    public void testToStringWithSubCategory() {
        assertEquals("Category{name='category', subCategories=[Category{name='sub'}]}", category.toString());
    }

    @Test
    public void returnEmptyListWhenNoSubCategories() {
        assertTrue(subCategory.getSubCategories().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void categoriesAreUnmodifiable() {
        category.getSubCategories().clear();
    }

    @Test(expected = NullPointerException.class)
    public void nameIsRequired() {
        new Itunes.Category(null, null);
    }

    @Test
    public void name() {
        assertEquals("category", category.getName());
    }
}
