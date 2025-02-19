/*
 * Vaadin Spreadsheet Addon
 *
 * Copyright (C) 2013-2025 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.addon.spreadsheet.test.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.shared.SpreadsheetState;

public class EmptyFileTest {

    @Test
    public void loadFile_emptySheet_firstRowRendered() throws Exception {
        File f = getTestSheetFile("empty.xlsx");

        Spreadsheet s = new Spreadsheet(f);

        SpreadsheetState state = getSpreadsheetState(s);

        assertTrue("Row heights not sent to client", state.rowH != null);

        for (int i = 0; i < state.rowH.length; i++) {
            assertTrue("Row is zero height, should be default",
                    state.rowH[i] > 0);
        }

    }

    private SpreadsheetState getSpreadsheetState(Spreadsheet s) {
        Method method = null;
        try {
            method = s.getClass().getDeclaredMethod("getState");
            method.setAccessible(true);
            Object val = method.invoke(s);
            return (SpreadsheetState) val;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.setAccessible(false);
            }

        }
        fail("Could not get state with reflection");
        return null;
    }

    private File getTestSheetFile(String testSheetFileName) {
        File file = null;

        try {
            file = new File(EmptyFileTest.class.getClassLoader()
                    .getResource(
                            "test_sheets" + File.separator + testSheetFileName)
                    .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assertNotNull("Spreadsheet file null", file);
        assertTrue("Spreadsheet file does not exist", file.exists());
        return file;
    }

}
