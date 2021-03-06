/*
 * Created on Dec 11, 2007
 */
package org.wicketstuff.googlecharts;

/**
 * @author Daniel Spiewak
 */
public abstract class AbstractChartData implements IChartData {

    private ChartDataEncoding encoding;
    private double max;
    private Range[] scaleToDataRanges;

    public AbstractChartData() {
        this(62);
    }

    public AbstractChartData(double max) {
        this(ChartDataEncoding.SIMPLE, max);
    }

    public AbstractChartData(ChartDataEncoding encoding, double max) {
        this.encoding = encoding;
        this.max = max;
    }

    public ChartDataEncoding getEncoding() {
        return encoding;
    }

    public double getMax() {
        return max;
    }

    public void setEncoding(ChartDataEncoding encoding) {
        this.encoding = encoding;
    }

    public void setMax(double max) {
        this.max = max;
    }
    
    public Range[] getScaleToDataRanges(){
    	return scaleToDataRanges;
    }
    public void setScaleToDataRanges(Range[] ranges){
    	this.scaleToDataRanges = ranges;
    }
    
}
