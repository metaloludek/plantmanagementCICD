package pl.plantmanagement.plantmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class crudTests {

    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "password");
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
                    "(1, 'Stokrotka', 'Bellis perennis', 1, 'Yes')");
        }

        // Attempt to read the inserted record
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, polish_name, latin_name, watering, blooming FROM plants WHERE id = 1");
            assertTrue(rs.next(), "A plant should be found");

            assertEquals(1, rs.getInt("id"), "Plant ID should match");
            assertEquals("Stokrotka", rs.getString("polish_name"), "Polish name should match");
            assertEquals("Bellis perennis", rs.getString("latin_name"), "Latin name should match");
            assertEquals(1, rs.getInt("watering"), "Watering frequency should match");
            assertEquals("Yes", rs.getString("blooming"), "Blooming status should match");
        }
    }

    @Test
    void testUpdate() throws Exception {
        // Insert a test record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO plants (id, polish_name, latin_name, watering, blooming) VALUES " +
                    "(2, 'Ostrokrzew', 'Ilex', 1, 'Yes')");
        }

        // Update the record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("UPDATE plants SET polish_name = 'Dracena', latin_name = 'Dracaena', watering = 2, blooming = 'No' WHERE id = 2");
        }

        // Verify the update
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, polish_name, latin_name, watering, blooming FROM plants WHERE id = 2");
            assertTrue(rs.next(), "Updated plant should be found");

            assertEquals(2, rs.getInt("id"), "Plant ID should match");
            assertEquals("Dracena", rs.getString("polish_name"), "Updated Polish name should match");
            assertEquals("Dracaena", rs.getString("latin_name"), "Updated Latin name should match");
            assertEquals(2, rs.getInt("watering"), "Updated watering frequency should match");
            assertEquals("No", rs.getString("blooming"), "Updated blooming status should match");
        }
    }

    @Test
    void testDelete() throws Exception {
        // Insert a test record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO plants (id, polish_name, latin_name, watering, blooming) VALUES " +
                    "(3, 'Pepinia', 'Peperomia', 3, 'Yes')");
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