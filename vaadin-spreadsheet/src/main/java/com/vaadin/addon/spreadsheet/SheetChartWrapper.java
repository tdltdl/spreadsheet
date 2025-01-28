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
package com.vaadin.addon.spreadsheet;

import static com.vaadin.shared.ui.ContentMode.HTML;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFChart;

import com.vaadin.addon.spreadsheet.client.OverlayInfo;
import com.vaadin.addon.spreadsheet.client.OverlayInfo.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * SheetChartWrapper is an utility class of the Spreadsheet component. In
 * addition to the chart resource, this wrapper contains the chart's visibility
 * state, position and size.
 *
 * @author Vaadin Ltd.
 */
@SuppressWarnings("serial")
public class SheetChartWrapper extends SheetOverlayWrapper
        implements Serializable {

    private static final Logger LOGGER = Logger
            .getLogger(SheetChartWrapper.class.getName());

    // System property used to override the default ChartCreator implementation
    private static final String CHART_CREATOR_IMPL = "spreadsheet.chart.creator.implementation";
    // Default ChartCreator implementation
    private static final String DEFAULT_CHART_CREATOR = "com.vaadin.addon.spreadsheet.charts.converter.DefaultChartCreator";
    private static final String INTEGRATION_MISSING_TEXT = "<b>CHART PLACEHOLDER</b><br/>"
            + "To see charts in your spreadsheet, you need to add the <span class=\"code-snippet\">vaadin-spreadsheet-charts</span> dependency to your project. Alternatively, you can call <span class=\"code-snippet\">Spreadsheet.setChartsEnabled(false)</span> to "
            + "disable them. Visit <a href=\"https://vaadin.com/spreadsheet\">vaadin.com/spreadsheet</a> for more info.";

    private MinimizableComponentContainer wrapper;
    private String connectorId;

    private static ChartCreator chartCreator;
    private final XSSFChart chartXml;
    private final Spreadsheet spreadsheet;

    static {
        String implementation = System.getProperty(CHART_CREATOR_IMPL);

        if (implementation == null) {
            // if property is not set use default implementation
            implementation = DEFAULT_CHART_CREATOR;
        }
        // if property is set without value then getProperty returns "true"
        // and ChartCreator should not be set
        if (implementation.length() > 0 && !implementation.equals("true")) {
            try {
                Class<?> clazz = Class.forName(implementation);
                if (ChartCreator.class.isAssignableFrom(clazz)) {
                    setChartCreator((ChartCreator) clazz.newInstance());
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Vaadin Spreadsheet: To display charts you need to add the chart integration package and Vaadin Charts to the project");
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    public SheetChartWrapper(XSSFChart chartXml, Spreadsheet spreadsheet) {
        super(chartXml.getGraphicFrame().getAnchor());

        this.chartXml = chartXml;
        this.spreadsheet = spreadsheet;

        wrapper = new MinimizableComponentContainer();
        wrapper.setSizeFull();
    }

    private void initContent(XSSFChart chartXml, Spreadsheet spreadsheet) {
        if (wrapper.getContent() == null) {
            Component content;

            if (chartCreator != null) {
                content = chartCreator.createChart(chartXml, spreadsheet);
            } else {
                Label label = new Label(INTEGRATION_MISSING_TEXT, HTML);
                label.addStyleName("overlay-content");
                content = new Panel(label);
            }

            wrapper.setContent(content);
            content.setSizeFull();
        }
    }

    @Override
    public void setOverlayChangeListener(OverlayChangeListener listener) {
        wrapper.setMinimizeListener(listener);
    }

    public static void setChartCreator(ChartCreator newChartCreator) {
        chartCreator = newChartCreator;
    }

    @Override
    public String getId() {
        if (wrapper != null && wrapper.isAttached()) {
            connectorId = wrapper.getConnectorId();
        }

        return connectorId;
    }

    @Override
    public Component getComponent(final boolean init) {
        if (init) {
            initContent(chartXml, spreadsheet);
        }

        return wrapper;
    }

    @Override
    public Type getType() {
        return OverlayInfo.Type.COMPONENT;
    }

    @Override
    public float getHeight(Sheet sheet, float[] rowH) {
        if (wrapper.isMinimized()) {
            return 0;
        }

        return super.getHeight(sheet, rowH);
    }

    @Override
    public float getWidth(Sheet sheet, int[] colW, int defaultColumnWidthPX) {
        if (wrapper.isMinimized()) {
            return 0;
        }

        return super.getWidth(sheet, colW, defaultColumnWidthPX);
    }
}
