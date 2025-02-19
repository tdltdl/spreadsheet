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
package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import com.vaadin.addon.spreadsheet.elements.SheetCellElement;
import com.vaadin.addon.spreadsheet.elements.SpreadsheetElement;

public class CustomFormatTest extends AbstractSpreadsheetTestCase {

    private SpreadsheetElement spreadSheet;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        headerPage.loadFile("custom_format.xlsx", this);

        spreadSheet = $(SpreadsheetElement.class).first();

        setLocale(Locale.US);
    }

    @Test
    public void assertCorrectValuesAfterLoading() {
        assertNumbersColumn();
        assertLiteralsColumn();
        assertEmptyColumn();
    }

    private void assertNumbersColumn() {
        // four part format
        assertCellValue("C1", "5.0");
        assertCellValue("C2", "(5.0)");
        assertCellValue("C3", "-");
        assertCellValue("C4", "\"formatted text\"");

        // three part format
        assertCellValue("C6", "5.0");
        assertCellValue("C7", "(5.0)");
        assertCellValue("C8", "-");
        assertCellValue("C9", "text");

        // two part format
        assertCellValue("C11", "5.0");
        assertCellValue("C12", "(5.0)");
        assertCellValue("C13", "0.0");
        assertCellValue("C14", "text");

        // one part format
        assertCellValue("C16", "5.0");
        assertCellValue("C17", "-5.0");
        assertCellValue("C18", "0.0");
        assertCellValue("C19", "text");
    }

    private void assertLiteralsColumn() {
        final String literal = "literal";

        // four part format
        assertCellValue("E1", literal);
        assertCellValue("E2", literal);
        assertCellValue("E3", literal);
        assertCellValue("E4", literal);

        // three part format
        assertCellValue("E6", literal);
        assertCellValue("E7", literal);
        assertCellValue("E8", literal);
        assertCellValue("E9", "text");

        // two part format
        assertCellValue("E11", literal);
        assertCellValue("E12", literal);
        assertCellValue("E13", literal);
        assertCellValue("E14", "text");

        // one part format
        assertCellValue("E16", literal);
        assertCellValue("E17", "-" + literal);
        assertCellValue("E18", literal);
        assertCellValue("E19", "text");
    }

    private void assertEmptyColumn() {
        final String empty = "";

        // four part format
        assertCellValue("G1", empty);
        assertCellValue("G2", empty);
        assertCellValue("G3", empty);
        assertCellValue("G4", empty);

        // three part format
        assertCellValue("G6", empty);
        assertCellValue("G7", empty);
        assertCellValue("G8", empty);
        assertCellValue("G9", "text");

        // two part format
        assertCellValue("G11", empty);
        assertCellValue("G12", empty);
        assertCellValue("G13", empty);
        assertCellValue("G14", "text");

        // one part format
        assertCellValue("G16", empty);
        assertCellValue("G17", "-" + empty);
        assertCellValue("G18", empty);
        assertCellValue("G19", "text");
    }

    @Test
    public void customFormatFourParts_enterNumberAndChangeLocale_getsFormatted() {
        assertCellFormatAfterLocaleChange("C1", "=5555555.5", "5.555.555,5",
                Locale.ITALY);
    }

    @Test
    public void customFormatThreeParts_enterNumberAndChangeLocale_getsFormatted() {
        assertCellFormatAfterLocaleChange("C6", "=5555555.5", "5.555.555,5",
                Locale.ITALY);
    }

    @Test
    public void customFormatTwoParts_enterNumberAndChangeLocale_getsFormatted() {
        assertCellFormatAfterLocaleChange("C11", "=5555555.5", "5.555.555,5",
                Locale.ITALY);
    }

    @Test
    public void customFormatOnePart_enterNumberAndChangeLocale_getsFormatted() {
        assertCellFormatAfterLocaleChange("C16", "=5555555.5", "5.555.555,5",
                Locale.ITALY);
    }

    private void assertCellFormatAfterLocaleChange(String cellID, String value,
            String expected, Locale locale) {
        SheetCellElement formatCell = spreadSheet.getCellAt(cellID);
        formatCell.setValue(value);
        setLocale(locale);

        // numbers are right-aligned
        assertEquals("right", formatCell.getCssValue("text-align"));
        assertEquals(expected, formatCell.getValue());
    }

    private void assertCellValue(String cell, String value) {
        assertEquals(value, spreadSheet.getCellAt(cell).getValue());
    }
}
