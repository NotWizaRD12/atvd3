import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.github.javafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetStoreTest {

    private int petId = 122221;
    private String petName = "KAndrushchak";
    private Faker faker;
    private String randomCategoryName;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        faker = new Faker();
        randomCategoryName = faker.animal().name();
    }

    @Test(priority = 1)
    public void testCreatePet() {
        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", randomCategoryName);

        Map<String, Object> petPayload = new HashMap<>();
        petPayload.put("id", petId);
        petPayload.put("category", category);
        petPayload.put("name", petName);
        petPayload.put("status", "available");
        petPayload.put("photoUrls", Arrays.asList(faker.internet().url()));

        given()
            .contentType(ContentType.JSON)
            .body(petPayload)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("id", equalTo(petId))
            .body("name", equalTo(petName));
    }

    @Test(priority = 2)
    public void testGetPetById() {
        given()
            .pathParam("petId", petId)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(200)
            .body("id", equalTo(petId))
            .body("name", equalTo(petName));
    }

    @Test(priority = 3)
    public void testUpdatePet() {
        Map<String, Object> petPayload = new HashMap<>();
        petPayload.put("id", petId);
        petPayload.put("name", petName);
        petPayload.put("status", "sold");

        given()
            .contentType(ContentType.JSON)
            .body(petPayload)
        .when()
            .put("/pet")
        .then()
            .statusCode(200)
            .body("status", equalTo("sold"));
    }
}
