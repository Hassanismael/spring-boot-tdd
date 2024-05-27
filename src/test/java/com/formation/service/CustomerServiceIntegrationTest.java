package com.formation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.formation.web.error.ConflictException;
import com.formation.web.error.NotFoundException;
import com.formation.web.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest // 1: On charge le contexte Spring
//@ActiveProfiles("test")  // 2: On charge le profile test
@AutoConfigureTestDatabase(replace= Replace.ANY) // 3: On remplace la base de données par une base de données de test
public class CustomerServiceIntegrationTest {

  @Autowired
  CustomerService service;

  @Test
  void getAllCustomers(){
    
    // 4: On appelle le service pour récupérer la liste des clients
    List<Customer> customers = this.service.getAllCustomers();

    // 5: On vérifie que le service retourne bien 5 clients
    assertEquals(5, customers.size());
  }

  @Test
  void getCustomer(){

    // 6: On appelle le service pour récupérer un client avec l'identifiant "054b145c-ddbc-4136-a2bd-7bf45ed1bef7"
    Customer customer = this.service.getCustomer("054b145c-ddbc-4136-a2bd-7bf45ed1bef7");

    // 7: On vérifie que le client retourné n'est pas null
    assertNotNull(customer);

    // 8: On vérifie que le prénom du client est bien "Cally"
    assertEquals("Cally", customer.getFirstName());
  }

  @Test
  void getCustomer_NotFound(){
    // 9: On appelle le service pour récupérer un client avec un identifiant qui n'existe pas
    assertThrows(NotFoundException.class, () -> this.service.getCustomer("d972b30f-21cc-411f-b374-685ce23cd317"), "should have thrown an exception");
  }

  @Test
  void addCustomer(){
    // 10: On crée un nouveau client
    Customer customer = new Customer("", "John", "Doe", "jdoe@test.com", "555-515-1234", "1234 Main Street; Anytown, KS 66110");
   
    // 11: On appelle le service pour ajouter le client
    customer = this.service.addCustomer(customer);

    // 12: On vérifie que le client a bien été ajouté
    assertTrue(StringUtils.isNotBlank(customer.getCustomerId()));

    // 13: On vérifie que le prénom du client est bien "John"
    assertEquals("John", customer.getFirstName());

    // 14: On appelle le service pour supprimer le client
    this.service.deleteCustomer(customer.getCustomerId());
  }

  @Test
  void addCustomer_alreadyExists(){
    // 15: On crée un nouveau client avec une adresse email qui existe déjà
    Customer customer = new Customer("", "John", "Doe", "penatibus.et@lectusa.com", "555-515-1234", "1234 Main Street; Anytown, KS 66110");
    
    // 16: On appelle le service pour ajouter le client
    assertThrows(ConflictException.class, () -> this.service.addCustomer(customer));
  }

  @Test
  void updateCustomer(){
    // 17: On crée un nouveau client
    Customer customer = new Customer("", "John", "Doe", "jdoe@test.com", "555-515-1234", "1234 Main Street; Anytown, KS 66110");
    
    // 18: On appelle le service pour ajouter le client
    customer = this.service.addCustomer(customer);

    // 19: On modifie le prénom du client
    customer.setFirstName("Jane");

    // 20: On appelle le service pour mettre à jour le client
    customer = this.service.updateCustomer(customer);

    // 21: On vérifie que le prénom du client a bien été modifié
    assertEquals("Jane", customer.getFirstName());

    // 22: On appelle le service pour supprimer le client
    this.service.deleteCustomer(customer.getCustomerId());
  }

}
