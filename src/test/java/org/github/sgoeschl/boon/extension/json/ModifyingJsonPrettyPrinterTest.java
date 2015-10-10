package org.github.sgoeschl.boon.extension.json;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.boon.Lists.list;
import static org.boon.Maps.map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModifyingJsonPrettyPrinterTest {

    private static List<?> json = list(
        map("name", "Engineering",
            "employees", list(
                map("id", 1, "salary", 100, "firstName", "Rick", "lastName", "Hightower",
                    "contactInfo", map("phoneNumbers",
                        list("555-555-0000")
                    )
                ),
                map("id", 2, "salary", 200, "firstName", "John", "lastName", "Smith",
                    "contactInfo", map("phoneNumbers", list("555-555-1215",
                        "555-555-1214", "555-555-1213"))),
                map("id", 3, "salary", 300, "firstName", "Drew", "lastName", "Donaldson",
                    "contactInfo", map("phoneNumbers", list("555-555-1216"))),
                map("id", 4, "salary", 400, "firstName", "Nick", "lastName", "LaySacky",
                    "contactInfo", map("phoneNumbers", list("555-555-1217")))

            )
        ),
        map("name", "HR",
            "employees", list(
                map("id", 5, "salary", 100, "departmentName", "HR",
                    "firstName", "Dianna", "lastName", "Hightower",
                    "contactInfo",
                    map("phoneNumbers", list("555-555-1218"))),
                map("id", 6, "salary", 200, "departmentName", "HR",
                    "firstName", "Derek", "lastName", "Smith",
                    "contactInfo",
                    map("phoneNumbers", list("555-555-1219"))),
                map("id", 7, "salary", 300, "departmentName", "HR",
                    "firstName", "Tonya", "lastName", "Donaldson",
                    "contactInfo", map("phoneNumbers", list("555-555-1220"))),
                map("id", 8, "salary", 400, "departmentName", "HR",
                    "firstName", "Sue", "lastName", "LaySacky",
                    "contactInfo", map("phoneNumbers", list("555-555-9999")))

            )
        ),
        map("name", "Manufacturing", "employees", Collections.EMPTY_LIST),
        map("name", "Sales", "employees", Collections.EMPTY_LIST),
        map("name", "Marketing", "employees", Collections.EMPTY_LIST)
    );

    @Test
    public void shouldPrintPrettyPrintJson() {
        System.out.println(ModifyingJsonPrettyPrinter.toString(json, true, "id"));
    }

    @Test
    public void shouldPrettyPrintJson() {
        assertTrue(ModifyingJsonPrettyPrinter.toString(json).contains("id"));
    }

    @Test
    public void shouldRemoveSingleElement() {
        assertFalse(ModifyingJsonPrettyPrinter.toString(json, "id").contains("id"));
    }

    @Test
    public void shouldRemoveElementTree() {
        assertFalse(ModifyingJsonPrettyPrinter.toString(json, "employees").contains("contactInfo"));
    }

    @Test
    public void shouldHandleNullJson() {
        assertEquals("null", ModifyingJsonPrettyPrinter.toString(null));
    }


}
