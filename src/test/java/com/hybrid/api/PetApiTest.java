package com.hybrid.api;

import com.hybrid.base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@Feature("Swagger Petstore API")
public class PetApiTest extends BaseApiTest {

    private long petId;

    @BeforeClass
    @Override
    public void setUpApiClients() {
        super.setUpApiClients();
        petId = System.currentTimeMillis() % 1_000_000;
    }

    @Test(priority = 1, description = "POST /v2/pet creates a new pet and returns its id")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Creates 'AutoDog' in the Petstore and verifies the response contains the correct id and name.")
    public void testAddPet() {
        Map<String, Object> body = Map.of(
                "id",        petId,
                "name",      "AutoDog",
                "status",    "available",
                "category",  Map.of("id", 1, "name", "Dogs"),
                "photoUrls", List.of("https://example.com/autodog.jpg"),
                "tags",      List.of(Map.of("id", 1, "name", "automation"))
        );
        Response res = petClient.post("/v2/pet", body);
        Assert.assertEquals(res.statusCode(), 200, "Expected 200 from Petstore POST /pet");
        Assert.assertEquals(res.jsonPath().getLong("id"), petId);
        Assert.assertEquals(res.jsonPath().getString("name"), "AutoDog");
    }

    @Test(priority = 2, dependsOnMethods = "testAddPet",
          description = "GET /v2/pet/{petId} retrieves the pet created in the previous step")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetById() {
        Response res = petClient.get("/v2/pet/{petId}", petId);
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().getLong("id"), petId);
        Assert.assertEquals(res.jsonPath().getString("status"), "available");
    }

    @Test(priority = 3, description = "GET /v2/pet/findByStatus?status=available returns a non-empty list")
    @Severity(SeverityLevel.NORMAL)
    public void testFindPetsByStatus() {
        Response res = petClient.get("/v2/pet/findByStatus?status=available");
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertFalse(res.jsonPath().getList("$").isEmpty(), "Available pets list must not be empty");
    }

    @Test(priority = 4, dependsOnMethods = "testGetPetById",
          description = "DELETE /v2/pet/{petId} removes the pet and returns 200")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePet() {
        Response res = petClient.delete("/v2/pet/{petId}", petId);
        Assert.assertEquals(res.statusCode(), 200, "Expected 200 after deleting pet");
    }
}
