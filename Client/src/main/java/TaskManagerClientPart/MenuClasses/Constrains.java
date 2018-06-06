package TaskManagerClientPart.MenuClasses;

import java.awt.*;

/**
 * The class that collect all general functions for working with {@code GridBagConstraints}
 */
public class Constrains {

    /**
     * The method that create a new {@code GridBagConstraints} instance with assigned params
     * @return only {@code GridBagConstraints} instance
     */
    public static GridBagConstraints getLocator(){
        GridBagConstraints result = new GridBagConstraints();
        result.fill = GridBagConstraints.BOTH;
        result.anchor = GridBagConstraints.CENTER;
        result.weightx=1;
        result.weighty=1;
        return  result;
    }

    /**
     * This method set location for constraint
     * @param locator - the {@code GridBagConstraints} instance
     * @param grx - location by x
     * @param gry - location by y
     */
    public static void setLocation(GridBagConstraints locator,int grx, int gry){
        locator.gridx = grx;
        locator.gridy = gry;
    }

    /**
     * The method that set size for constraint
     * @param locator - the {@code GridBagConstraints} instance
     * @param w - width(by x)
     * @param h - height(by y)
     */
    public static void setSize(GridBagConstraints locator, int w, int h){
        locator.gridwidth = w;
        locator.gridheight = h;
    }
}
