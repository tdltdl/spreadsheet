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
package com.vaadin.addon.spreadsheet.charts.converter.confwriter;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.Pane;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.addon.spreadsheet.charts.converter.chartdata.RadarSeriesData;

public class RadarSeriesWriter extends AreaSeriesDataWriter {

    public RadarSeriesWriter(RadarSeriesData series) {
        super(series);
    }

    @Override
    protected void configureChart(Configuration conf) {
        super.configureChart(conf);

        conf.getChart().setPolar(true);
        Pane pane = new Pane();
        pane.setSize("60%");
        conf.addPane(pane);

        YAxis yaxs = new YAxis();
        conf.addyAxis(yaxs);
        yaxs.setGridLineInterpolation("polygon");

        Style style = new Style();
        style.setFontSize("75%");
        conf.getxAxis().getLabels().setStyle(style);
    }

    @Override
    protected void configureDataSeries(DataSeries dataSeriesForWriting) {
        super.configureDataSeries(dataSeriesForWriting);
        if (!getSeriesData().filled) {
            getPlotOptions().setFillColor(new SolidColor(0, 0, 0, 0));
            /*
             * The code line:
             *
             * getPlotOptions().setFillColor(new SolidColor(0,0,0,0));
             *
             * is because of bug HighChart bug #4888 and it is fixed in 4.2.2
             * version and can be replaced by the following line of code when
             * migrate to new HighChart version:
             *
             * getPlotOptions().setFillOpacity(0);
             */
        }

        getPlotOptions().setDashStyle(
                LineSeriesWriterUtils.getDashStyle(getSeriesData().dashStyle));
        getPlotOptions().setMarker(
                LineSeriesWriterUtils.getMarker(getSeriesData().markerSymbol));
    }
}
