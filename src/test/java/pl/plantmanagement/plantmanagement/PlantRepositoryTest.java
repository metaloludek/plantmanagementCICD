// package pl.plantmanagement.plantmanagement;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.beans.factory.annotation.Autowired;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import java.util.List;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @AutoConfigureTestDatabase
// @TestPropertySource(locations = "classpath:application-test.properties")
// public class PlantRepositoryTest {

//     @Autowired
//     private PlantRepository plantRepository;

//     @Test
//     public void testGetAll() {
        
//         Plant plant1 = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
//         Plant plant2 = new Plant(2, "Polish2", "Latin2", 2, "Blooming2");
//         plantRepository.save(List.of(plant1, plant2));

        
//         List<Plant> allPlants = plantRepository.getAll();

        
//         assertEquals(2, allPlants.size());
//     }

//     @Test
//     public void testGetById() {
        
//         Plant plant = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
//         plantRepository.save(List.of(plant));

        
//         Plant retrievedPlant = plantRepository.getById(1);

        
//         assertEquals("Polish1", retrievedPlant.getPolish_name());
//         assertEquals("Latin1", retrievedPlant.getLatin_name());
//         assertEquals(1, retrievedPlant.getWatering());
//         assertEquals("Blooming1", retrievedPlant.getBlooming());
//     }

//     @Test
//     public void testUpdate() {
   
//         Plant originalPlant = new Plant(1, "Polish1", "Latin1", 1, "Blooming1");
//         plantRepository.save(List.of(originalPlant));

     
//         Plant updatedPlantData = new Plant(1, "UpdatedPolish", "UpdatedLatin", 2, "UpdatedBlooming");

    
//         plantRepository.update(updatedPlantData);

 
//         Plant retrievedPlant = plantRepository.getById(1);

    
//         assertEquals("UpdatedPolish", retrievedPlant.getPolish_name());
//         assertEquals("UpdatedLatin", retrievedPlant.getLatin_name());
//         assertEquals(2, retrievedPlant.getWatering());
//         assertEquals("UpdatedBlooming", retrievedPlant.getBlooming());
//     }



// }
package pl.plantmanagement.plantmanagement;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Api {

    private List<Plant> plants;

    @Autowired
    private PlantRepository plantRepository;

    public Api() {
        this.plants = new ArrayList<>();
        plants.add(new Plant(0L, "Róża", "Rosa", 3, "Yes"));
        plants.add(new Plant(1L, "Tulipan", "Tulip", 2, "Yes"));
    }

    @GetMapping("/plants")
    @Order(1)
    public List<Plant> getPlants() {
        return plants;
    }

    @GetMapping("/plants/{id}")
    @Order(2)
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        Optional<Plant> optionalPlant = plants.stream()
                .filter(plant -> plant.getId().equals(id))
                .findFirst();

        return optionalPlant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/plants")
    @Order(3)
    public ResponseEntity<Void> addPlant(@RequestBody Plant newPlant) {
        // Automatycznie generuj nowe ID
        Long newId = generateNewId();
        newPlant.setId(newId);

        plants.add(newPlant);

        // Zwróć odpowiedź z nowym ID
        return ResponseEntity.ok().header("Location", "/plants/" + newId).build();
    }

    // Funkcja do generowania nowego ID
    private Long generateNewId() {
        return plants.stream()
                .map(Plant::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @PutMapping("/plants/{id}")
    public ResponseEntity<Void> updatePlant(@PathVariable Long id, @RequestBody Plant updatedPlant) {
        for (int i = 0; i < plants.size(); i++) {
            if (plants.get(i).getId().equals(id)) {
                plants.set(i, updatedPlant);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/plants/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plants.removeIf(plant -> plant.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
