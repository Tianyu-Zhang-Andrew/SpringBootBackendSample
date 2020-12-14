package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.estateEntity.Address;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.peopleEntity.Gender;
import com.mikason.PropView.dataaccess.peopleEntity.Person;
import com.mikason.PropView.dataaccess.peopleEntity.Title;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class ClientControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

//    @After
//    public void clear() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/client/deleteAllClient")
//                .accept(MediaType.APPLICATION_JSON);
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isNoContent());
//    }

    @Test
    public void testAddNewClient() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person person = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(address, person);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(client);
        log.info(jsonObj);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();
        String clientObjJson =result.getResponse().getContentAsString();

        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        Address clientAddress = savedClient.getAddress();
        Person clientPersonInf = savedClient.getPerson();

        assert(clientAddress.getUnitNumber().equals("1a"));
        assert(clientAddress.getStreetNumber().equals("1"));
        assert(clientAddress.getStreetName().equals("Rialto Lane"));
        assert(clientAddress.getStreetType().equals("Avenue"));
        assert(clientAddress.getSuburb().equals("Manly"));
        assert(clientAddress.getState().equals("NSW"));
        assert(clientAddress.getCountry().equals("Australia"));
        assert(clientAddress.getPostCode().equals("2095"));

        assert(clientPersonInf.getTitle().equals(Title.Mr));
        assert(clientPersonInf.getFirstName().equals("Bob"));
        assert(clientPersonInf.getMiddleName().equals("L"));
        assert(clientPersonInf.getLastName().equals("Smith"));
        assert(clientPersonInf.getGender().equals(Gender.Male));
        assert(clientPersonInf.getBirthDate().equals("1998-6-23"));
        assert(clientPersonInf.getMobileNumber().equals("0451256728"));
        assert(clientPersonInf.getEmail().equals("Bobsemail@outlook.com"));
    }

    @Test
    public void testEditNewClient() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Person person = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(address, person);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(client);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new client's id
        String clientObjJson =result.getResponse().getContentAsString();
        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        long id = savedClient.getId();

        //Edit the new client
        person.setEmail("BobsBackupEmail@outlook.com");
        Client newClient = new Client(address, person);
        newClient.setId(id);

        gson = new Gson();
        String newJsonObj = gson.toJson(newClient);


        requestBuilder = MockMvcRequestBuilders
                .post("/client/editClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(newJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        clientObjJson =result.getResponse().getContentAsString();

        savedClient = gson.fromJson(clientObjJson, Client.class);
        Person clientPersonInf = savedClient.getPerson();

        assert(clientPersonInf.getEmail().equals("BobsBackupEmail@outlook.com"));
    }

    @Test
    public void testDeleteClient() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Person person = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(address, person);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(client);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String savedClient = result.getResponse().getContentAsString();

        requestBuilder = MockMvcRequestBuilders
                .post("/client/deleteClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(savedClient)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testSearchClient() throws Exception {

        //Create client1
        Address address1 = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Person person1 = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client1 = new Client(address1, person1);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(client1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Create client2
        Address address2 = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Person person2 = new Person(Title.Mr, "Tom", "M", "Bush", Gender.Male, "1999-7-16", "0451268743", "Tomsemail@outlook.com");
        Client client2 = new Client(address2, person2);

        gson = new Gson();
        jsonObj = gson.toJson(client2);

        requestBuilder = MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Create client3
        Address address3 = new Address("3c", "5", "Parramatta street", "Avenue", "Parramatta", "NSW", "Australia", "2041");
        Person person3 = new Person(Title.Ms, "Alice", "R", "Adams", Gender.Female, "1998-9-20", "0452263563", "Alicesemail@outlook.com");
        Client client3 = new Client(address3, person3);

        gson = new Gson();
        jsonObj = gson.toJson(client3);

        requestBuilder = MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Search for all clients live in parramatta
        Address sampleAddress1 = new Address();
        sampleAddress1.setSuburb("Parramatta");
        Client sampleClient1 = new Client();
        sampleClient1.setAddress(sampleAddress1);

        gson = new Gson();
        jsonObj = gson.toJson(sampleClient1);

        requestBuilder = MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 property (client2 and client3)
        List<Client> clientList1 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        assert(clientList1.size() == 2);
        System.out.println(result.getResponse().getContentAsString());

        //Search for all male clients
        Person samplePerson1 = new Person();
        samplePerson1.setGender(Gender.Male);
        Client sampleClient2 = new Client();
        sampleClient2.setPerson(samplePerson1);

        gson = new Gson();
        jsonObj = gson.toJson(sampleClient2);

        requestBuilder = MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 property (client1 and client2)
        List<Client> clientList2 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        assert(clientList2.size() == 2);
        System.out.println(result.getResponse().getContentAsString());

        //Search for all female clients who live in parramatta
        Address sampleAddress2 = new Address();
        sampleAddress2.setSuburb("Parramatta");
        Person samplePerson2 = new Person();
        samplePerson2.setGender(Gender.Female);
        Client sampleClient3 = new Client(sampleAddress2, samplePerson2);

        gson = new Gson();
        jsonObj = gson.toJson(sampleClient3);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 1 property (client3)
        List<Client> clientList3 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        assert(clientList3.size() == 1);
        System.out.println(result.getResponse().getContentAsString());
    }
}
