import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.plantmanagement.plantmanagement.Plant;
import pl.plantmanagement.plantmanagement.PlantManagementApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PlantManagementApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPlants() throws Exception {
        mockMvc.perform(get("/plants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetPlantById() throws Exception {
        int id = 0; // Change with a valid ID
        mockMvc.perform(get("/plants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testAddPlant() throws Exception {
        Plant newPlant = new Plant(2, "Rose", "Rosa", 3, "Yes"); // Change with valid plant data
        mockMvc.perform(post("/plants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newPlant)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePlant() throws Exception {
        int id = 0; // Change with a valid ID
        Plant updatedPlant = new Plant(0, "UpdatedRose", "UpdatedRosa", 4, "No"); // Change with valid plant data
        mockMvc.perform(put("/plants/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedPlant)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePlant() throws Exception {
        int id = 0; // Change with a valid ID
        mockMvc.perform(delete("/plants/{id}", id))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
