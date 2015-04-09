package homework.entena.mapper;

import java.util.Date;

public class MarkerItem {
    public float latitude;
    public float longitude;
    public Date created;

    public MarkerItem(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        created = new Date();
    }

}
