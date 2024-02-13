package pl.plantmanagement.plantmanagement;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class PlantRepositoryTest {

    @Autowired
    private PlantRepository plantRepository;

    @Test
    public void testGetAll() {
        
        Plant plant1 = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
        Plant plant2 = new Plant(2, "Polish2", "Latin2", 2, "Blooming2");
        plantRepository.save(List.of(plant1, plant2));

        
        List<Plant> allPlants = plantRepository.getAll();

        
        assertEquals(2, allPlants.size());
    }

    @Test
    public void testGetById() {
        
        Plant plant = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
        plantRepository.save(List.of(plant));

        
        Plant retrievedPlant = plantRepository.getById(1);

        
        assertEquals("Polish1", retrievedPlant.getPolish_name());
        assertEquals("Latin1", retrievedPlant.getLatin_name());
        assertEquals(1, retrievedPlant.getWatering());
        assertEquals("Blooming1", retrievedPlant.getBlooming());
    }

    @Test
    public void testUpdate() {
   
        Plant originalPlant = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
        plantRepository.save(List.of(originalPlant));

     
        Plant updatedPlantData = new Plant(1, "UpdatedPolish", "UpdatedLatin", 2, "UpdatedBlooming");

    
        plantRepository.update(updatedPlantData);

 
        Plant retrievedPlant = plantRepository.getById(1);

    
        assertEquals("UpdatedPolish", retrievedPlant.getPolish_name());
        assertEquals("UpdatedLatin", retrievedPlant.getLatin_name());
        assertEquals(2, retrievedPlant.getWatering());
        assertEquals("UpdatedBlooming", retrievedPlant.getBlooming());
    }



}
