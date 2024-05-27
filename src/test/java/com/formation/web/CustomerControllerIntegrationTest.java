package com.formation.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.web.model.Customer;

@SpringBootTest  // 1: On charge le contexte Spring
@AutoConfigureMockMvc  // 2: On configure le MockMvc
@ActiveProfiles("test") // 3: On charge le profile test 
@AutoConfigureTestDatabase(replace = Replace.ANY) // 4: On remplace la base de données par une base de données de test
@Tag("web")
public class CustomerControllerIntegrationTest {

  @Autowired // 5: On injecte le MockMvc
  MockMvc mockMvc;

  @Test
  void getAllCustomers() throws Exception {
    // 6: On appelle le service pour récupérer la liste des clients
    this.mockMvc.perform(get("/customers")).andExpect(status().isOk())
        .andExpect(content().string(containsString("054b145c-ddbc-4136-a2bd-7bf45ed1bef7")))
        .andExpect(content().string(containsString("38124691-9643-4f10-90a0-d980bca0b27d")));
  }

  @Test
  void getCustomer() throws Exception {
    this.mockMvc.perform(get("/customers/054b145c-ddbc-4136-a2bd-7bf45ed1bef7"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("054b145c-ddbc-4136-a2bd-7bf45ed1bef7")));
  }

  @Test
  void getCustomer_notFound() throws Exception {
    this.mockMvc.perform(get("/customers/2b31469c-da3d-469f-9900-d00b5e4e352f"))
                .andExpect(status().isNotFound());
  }

  @Test
  void addCustomer() throws Exception {
    Customer customer = new Customer("", "John", "Doe", "jdoe@test.com", "555-515-1234", "1234 Main St, Smallville KS 66083");
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(customer);
    this.mockMvc.perform(post("/customers").content(jsonString).contentType("application/json")).andExpect(status().isCreated())
        .andExpect(content().string(containsString("jdoe@test.com")));
  }

  @Test
  void addCustomer_exists() throws Exception {
    Customer customer = new Customer("","Cally","Reynolds","penatibus.et@lectusa.com","(901) 166-8355","556 Lakewood Park, Bismarck, ND 58505");
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(customer);
    this.mockMvc.perform(post("/customers")
                .content(jsonString).contentType("application/json"))
                .andExpect(status().isConflict());
  }

  @Test
  void updateCustomer() throws Exception {
    Customer customer = new Customer("c04ca077-8c40-4437-b77a-41f510f3f185","Jack","Bower","quam.quis.diam@facilisisfacilisis.org","(831) 996-1240","2 Rockefeller Avenue, Waco, TX 76796");
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(customer);
    this.mockMvc.perform(put("/customers/c04ca077-8c40-4437-b77a-41f510f3f185")
        .content(jsonString).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Jack")))
        .andExpect(content().string(containsString("Bower")));
  }

  @Test
  void updateCustomer_badRequest() throws Exception {
    Customer customer = new Customer("c04ca077-8c40-4437-b77a-41f510f3f185","Jack","Bower","quam.quis.diam@facilisisfacilisis.org","(831) 996-1240","2 Rockefeller Avenue, Waco, TX 76796");
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(customer);
    this.mockMvc.perform(put("/customers/2b31469c-da3d-469f-9900-d00b5e4e352f")
    .content(jsonString).contentType("application/json"))
    .andExpect(status().isBadRequest());
  }

  @Test
  void deleteCustomer() throws Exception {
    this.mockMvc.perform(delete("/customers/3b6c3ecc-fad7-49db-a14a-f396ed866e50")).andExpect(status().isResetContent());
  }

}