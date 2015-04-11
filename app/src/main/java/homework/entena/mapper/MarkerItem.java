package homework.entena.mapper;

import java.util.Date;

public class MarkerItem {
    public double latitude;
    public double longitude;
    public Date created;
    public boolean highLighted;

    public MarkerItem(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        created = new Date();
        highLighted = false;
    }

}
