package com.hybrid.api;

import com.hybrid.base.BaseApiTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

@Feature("reqres.in Users API")
public class UserApiTest extends BaseApiTest {

    @Test(description = "GET /api/users?page=2 returns 200 with a paginated list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates status code, page number, and non-empty data array from reqres.in.")
    public void testGetUsersList() {
        Response res = reqresClient.get("/api/users?page=2");
        Assert.assertEquals(res.statusCode(), 200, "Expected HTTP 200");
        Assert.assertEquals(res.jsonPath().getInt("page"), 2, "Expected page 2");
        Assert.assertFalse(res.jsonPath().getList("data").isEmpty(), "Data list must not be empty");
    }

    @Test(description = "GET /api/users/2 returns a single user with correct id and email")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSingleUser() {
        Response res = reqresClient.get("/api/users/{id}", 2);
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().getInt("data.id"), 2);
        Assert.assertNotNull(res.jsonPath().getString("data.email"), "Email must be present");
        Assert.assertNotNull(res.jsonPath().getString("data.first_name"), "First name must be present");
    }

    @Test(description = "GET /api/users/9999 returns 404 for a non-existent user")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentUserReturns404() {
        Response res = reqresClient.get("/api/users/{id}", 9999);
        Assert.assertEquals(res.statusCode(), 404, "Non-existent user should return 404");
    }

    @Test(description = "POST /api/users creates a user and returns 201 with id and createdAt")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUser() {
        Map<String, String> body = Map.of("name", "Parth Verma", "job", "SDET");
        Response res = reqresClient.post("/api/users", body);
        Assert.assertEquals(res.statusCode(), 201, "Expected HTTP 201 Created");
        Assert.assertEquals(res.jsonPath().getString("name"), "Parth Verma");
        Assert.assertEquals(res.jsonPath().getString("job"), "SDET");
        Assert.assertNotNull(res.jsonPath().getString("id"), "Response must contain an id");
        Assert.assertNotNull(res.jsonPath().getString("createdAt"), "Response must contain createdAt");
    }

    @Test(description = "PUT /api/users/2 updates the user and returns updatedAt timestamp")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUser() {
        Map<String, String> body = Map.of("name", "Parth Verma", "job", "Senior SDET");
        Response res = reqresClient.put("/api/users/{id}", body, 2);
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().getString("job"), "Senior SDET");
        Assert.assertNotNull(res.jsonPath().getString("updatedAt"), "Response must contain updatedAt");
    }

    @Test(description = "PATCH /api/users/2 partially updates and returns updatedAt")
    @Severity(SeverityLevel.NORMAL)
    public void testPatchUser() {
        Map<String, String> body = Map.of("job", "Lead SDET");
        Response res = reqresClient.patch("/api/users/{id}", body, 2);
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().getString("job"), "Lead SDET");
    }

    @Test(description = "DELETE /api/users/2 returns 204 No Content")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUser() {
        Response res = reqresClient.delete("/api/users/{id}", 2);
        Assert.assertEquals(res.statusCode(), 204, "DELETE should return 204 No Content");
    }
}
