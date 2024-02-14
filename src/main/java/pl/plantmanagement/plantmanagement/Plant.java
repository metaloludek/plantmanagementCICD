package pl.plantmanagement.plantmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plant {

    public Plant(long l, String polish_name2, String latin_name2, int watering2, String blooming2) {
        //TODO Auto-generated constructor stub
    }
    private int id;
    private String polish_name;
    private String latin_name;
    private int watering;
    private String blooming;
}
