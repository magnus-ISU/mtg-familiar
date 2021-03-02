package com.gelakinetic.mtgfam.helpers;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.gelakinetic.mtgfam.R;
import com.gelakinetic.mtgfam.helpers.view.ReliableColorPie;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GraphHelper {
    private final Context c;
    private final DeckStatsGenerator mStatGenerator;

    /**
     * Creates a new GraphHelper
     * @param mStatGenerator DeckStatsGenerator to get stats from
     * @param c Context to get colors with
     */
    public GraphHelper(DeckStatsGenerator mStatGenerator, Context c) {
        this.mStatGenerator = mStatGenerator;
        this.c = c;
    }

    /**
     * Formats a given PieChart for displaying type statistics
     * @param chartToFill PieChart to format
     */
    public void fillTypeGraph(PieChart chartToFill) {
        PieData typeData = createTypeData(mStatGenerator.getTypeStats());
        chartToFill.setData(typeData);
        //mTypeChart.setDrawEntryLabels(false);
        chartToFill.getDescription().setEnabled(false);
        chartToFill.getLegend().setEnabled(false);
        //mTypeChart.getLegend().setWordWrapEnabled(true);
        //mTypeChart.getLegend().setMaxSizePercent((float) 0.1);
        chartToFill.setCenterText("Type Distribution");
        chartToFill.setTouchEnabled(false);
    }

    /**
     * Formats a given ReliableColorPie for displaying color statistics
     * @param pieToFill ReliableColorPie to format
     */
    public void fillColorGraph(ReliableColorPie pieToFill) {
        PieData colorData = createColorData(mStatGenerator.getColorStats());
        pieToFill.setData(colorData);
        pieToFill.setDrawEntryLabels(false);
        pieToFill.setTouchEnabled(false);
        pieToFill.getDescription().setEnabled(false);
        pieToFill.getLegend().setEnabled(false);
        pieToFill.setCenterText("Color Distribution");
    }

    /**
     * Formats a given BarChart for displaying cmc statistics
     * @param chartToFill BarChart to format
     */
    public void fillCmcGraph(BarChart chartToFill) {
        BarData cmcData = createCmcData(mStatGenerator.getCmcStats());
        chartToFill.setData(cmcData);
        chartToFill.setTouchEnabled(false);
        chartToFill.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value == 7) {
                    return "7+";
                } else {
                    return Integer.toString((int) value);
                }
            }
        });
        chartToFill.getXAxis().setDrawGridLines(false);
        chartToFill.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chartToFill.getAxisLeft().setEnabled(false);
        chartToFill.getAxisRight().setEnabled(false);
        chartToFill.getDescription().setEnabled(false);
        chartToFill.getLegend().setEnabled(false);
    }

    /**
     * Creates a PieData from the type Map supplied by the DeckStatsGenerator
     * @param typeMap Map with type information
     * @return PieData containing type information
     */
    private PieData createTypeData(Map<String, Float> typeMap) {
        List<PieEntry> typeEntries = new ArrayList<>();
        for (String type : typeMap.keySet()) {
            if (typeMap.get(type) != 0) {
                typeEntries.add(new PieEntry(typeMap.get(type), type));
            }
        }
        PieDataSet typeSet = new PieDataSet(typeEntries, "");
        typeSet.setColors(new int[] {R.color.bpblack, R.color.bpBlue, R.color.bpDarker_red, R.color.glyph_green, R.color.timeshifted_light, R.color.glyph_red, R.color.colorCheckbox_light}, c);
        PieData typeData = new PieData(typeSet);
        typeData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPieLabel(float value, PieEntry pieEntry) {
                return "";
            }
        });
        return typeData;
    }

    /**
     * Creates a PieData from the color Map supplied by the DeckStatsGenerator
     * @param colorMap Map with color information
     * @return PieData containing color information
     */
    private PieData createColorData(Map<String, Float> colorMap) {
        List<PieEntry> colorEntries = new ArrayList<>();
        for (String color : colorMap.keySet()) {
            if (colorMap.get(color) != 0) {
                if (!color.isEmpty()) {
                    colorEntries.add(new PieEntry(colorMap.get(color), color));
                } else {
                    colorEntries.add(new PieEntry(colorMap.get(color), "Colorless"));
                }
            }
        }
        PieDataSet colorSet = new PieDataSet(colorEntries, "Card Colors");
        colorSet.setColors(new int[] {R.color.glyph_white, R.color.icon_blue, R.color.bpblack, R.color.icon_red, R.color.icon_green, R.color.light_grey}, c);
        PieData colorData = new PieData(colorSet);
        colorData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPieLabel(float value, PieEntry pieEntry) {
                return "";
            }
        });
        return colorData;
    }

    /**
     * Creates a formatted BarData from the cmc Map supplied by the DeckStatsGenerator
     * @param cmcMap Map containing number of cards with each cmc
     * @return Formatted BarData containing the cmc information
     */
    private BarData createCmcData(Map<Integer, Integer> cmcMap) {
        List<BarEntry> cmcEntries = new ArrayList<>();
        for (Integer cmc : cmcMap.keySet()) {
            cmcEntries.add(new BarEntry(cmc, cmcMap.get(cmc)));
        }
        BarDataSet cmcSet = new BarDataSet(cmcEntries, "CMC Graph");
        cmcSet.setColors(new int[] {R.color.dark_grey}, c);
        BarData cmcData = new BarData(cmcSet);
        cmcData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                if (barEntry.getY() == 0) {
                    return "";
                } else {
                    return Integer.toString((int) barEntry.getY()); //Don't show decimals
                }
            }
        });
        return cmcData;
    }
}
