// package pl.plantmanagement.plantmanagement;
// import org.junit.jupiter.api.MethodOrderer;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import pl.plantmanagement.plantmanagement.Plant;
// import pl.plantmanagement.plantmanagement.PlantManagementApplication;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest(classes = PlantManagementApplication.class)
// @AutoConfigureMockMvc
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @ActiveProfiles("test")
// public class ApiTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     public void testAddPlant() throws Exception {
//         Plant newPlant = new Plant(0, "Rose", "Rosa", 3, "Yes"); // Change with valid plant data
//         mockMvc.perform(post("/plants")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(asJsonString(newPlant)))
//                 .andExpect(status().isCreated());
//     }

//     @Test
//     public void testGetPlants() throws Exception {
//         mockMvc.perform(get("/plants"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$").isArray());
//     }

//     @Test
//     public void testGetPlantById() throws Exception {
//         int id = 0; // Change with a valid ID
//         mockMvc.perform(get("/plants/{id}", id))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(id));
//     }



//     @Test
//     public void testUpdatePlant() throws Exception {
//         int id = 0; // Change with a valid ID
//         Plant updatedPlant = new Plant(0, "UpdatedRose", "UpdatedRosa", 4, "No"); // Change with valid plant data
//         mockMvc.perform(put("/plants/{id}", id)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(asJsonString(updatedPlant)))
//                 .andExpect(status().isNoContent());
//     }

//     @Test
//     public void testDeletePlant() throws Exception {
//         int id = 0; // Change with a valid ID
//         mockMvc.perform(delete("/plants/{id}", id))
//                 .andExpect(status().isNoContent());
//     }

//     private static String asJsonString(final Object obj) {
//         try {
//             return new ObjectMapper().writeValueAsString(obj);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }
// }

package pl.plantmanagement.plantmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class crudTests {

    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS plants (" +
                    "id INT PRIMARY KEY," +
                    "polish_name VARCHAR(255)," +
                    "latin_name VARCHAR(255)," +
                    "watering INT," +
                    "blooming VARCHAR(3))");
        }
    }

    @Test
    void testRead() throws Exception {
        // Insert a test record directly for reading
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO plants (id, polish_name, latin_name, watering, blooming) VALUES " +
                    "(1, 'Ra', 'Rosa', 1, 'Yes')");
        }

        // Attempt to read the inserted record
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, polish_name, latin_name, watering, blooming FROM plants WHERE id = 1");
            assertTrue(rs.next(), "A plant should be found");

            assertEquals(1, rs.getInt("id"), "Plant ID should match");
            assertEquals("Ra", rs.getString("polish_name"), "Polish name should match");
            assertEquals("Rosa", rs.getString("latin_name"), "Latin name should match");
            assertEquals(1, rs.getInt("watering"), "Watering frequency should match");
            assertEquals("Yes", rs.getString("blooming"), "Blooming status should match");
        }
    }

    @Test
    void testUpdate() throws Exception {
        // Insert a test record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO plants (id, polish_name, latin_name, watering, blooming) VALUES " +
                    "(2, 'Tulipan', 'Tulipa', 1, 'Yes')");
        }

        // Update the record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("UPDATE plants SET polish_name = 'Lilia', latin_name = 'Lilium', watering = 2, blooming = 'No' WHERE id = 2");
        }

        // Verify the update
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, polish_name, latin_name, watering, blooming FROM plants WHERE id = 2");
            assertTrue(rs.next(), "Updated plant should be found");

            assertEquals(2, rs.getInt("id"), "Plant ID should match");
            assertEquals("Lilia", rs.getString("polish_name"), "Updated Polish name should match");
            assertEquals("Lilium", rs.getString("latin_name"), "Updated Latin name should match");
            assertEquals(2, rs.getInt("watering"), "Updated watering frequency should match");
            assertEquals("No", rs.getString("blooming"), "Updated blooming status should match");
        }
    }

    @Test
    void testDelete() throws Exception {
        // Insert a test record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO plants (id, polish_name, latin_name, watering, blooming) VALUES " +
                    "(3, 'Fioek', 'Viola', 3, 'Yes')");
        }

        // Delete the record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM plants WHERE id = 3");
        }

        // Verify deletion
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id FROM plants WHERE id = 3");
            assertFalse(rs.next(), "Deleted plant should not be found");
        }
    }

}