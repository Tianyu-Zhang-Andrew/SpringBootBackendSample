package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.commercialEntity.Rent;
import com.mikason.PropView.dataaccess.compositeKey.RentKey;
import com.mikason.PropView.dataaccess.documentEntity.RentalAgreement;
import com.mikason.PropView.dataaccess.estateEntity.Address;
import com.mikason.PropView.dataaccess.estateEntity.Property;
import com.mikason.PropView.dataaccess.peopleEntity.Client;
import com.mikason.PropView.dataaccess.peopleEntity.Gender;
import com.mikason.PropView.dataaccess.peopleEntity.Person;
import com.mikason.PropView.dataaccess.peopleEntity.Title;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RentalAgreementControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void clear() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/deleteAllProperty")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
                .post("/agent/deleteAllAgent")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
                .post("/client/deleteAllClient")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
                .post("/office/deleteAllOffice")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddRentalAgreement()throws Exception{
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Create an agent
        Person agentPerson = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(agentPerson, office);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Save the client
        Gson gson = new Gson();
        String jsonObj = gson.toJson(client);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Set new client's id
        String clientObjJson =result.getResponse().getContentAsString();
        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        long clientId = savedClient.getId();
        client.setId(clientId);

        //Save the property
        jsonObj = gson.toJson(property);

        requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Save the office
        jsonObj = gson.toJson(office);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Save the agent
        jsonObj = gson.toJson(agent);

        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson = result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        long agentId = savedAgent.getId();
        agent.setId(agentId);

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Create a rent
        Rent rent = new Rent("2019-10-2", agent, client, propertyRent, 950, "2020-1-31");

        //Save the Rent
        jsonObj = gson.toJson(rent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rent/saveRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get Rent's id
        String rentObjJson = result.getResponse().getContentAsString();
        Rent savedRent = gson.fromJson(rentObjJson, Rent.class);
        RentKey rentKey = savedRent.getRentKey();
        rent.setRentKey(rentKey);

        //Create a new rentalAgreement
        RentalAgreement rentalAgreement = new RentalAgreement(rent, "Link");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement = gson.fromJson(rentalAgreementObjJson, RentalAgreement.class);
        Long rentalAgreementId = savedRentalAgreement.getId();
        rentalAgreement.setId(rentalAgreementId);

        //Create another new rentalAgreement
        RentalAgreement rentalAgreement2 = new RentalAgreement(rent, "Link2");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson2 = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement2 = gson.fromJson(rentalAgreementObjJson2, RentalAgreement.class);
        Long rentalAgreementId2 = savedRentalAgreement2.getId();
        rentalAgreement2.setId(rentalAgreementId2);

        //Test there is 2 rentalAgreement saved
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/rentalAgreement/getAllRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<RentalAgreement> propertyRentalAgreementList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<RentalAgreement>>() {}.getType());
        assert(propertyRentalAgreementList.size() == 2);

        //Check the change has been made in propertyRent
        PropertyRent examplePropertyRent = new PropertyRent();
        examplePropertyRent.setId(propertyRent.getId());
        jsonObj = gson.toJson(examplePropertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/searchPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyRent> propertyRentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        for (Rent nextRent : propertyRentList.get(0).getRents()) {
            assert(nextRent.getRentalAgreements().size() == 2);
        }
    }

    @Test
    public void testEditRentalAgreement()throws Exception {
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Create an agent
        Person agentPerson = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(agentPerson,office);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Save the client
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

        //Set new client's id
        String clientObjJson = result.getResponse().getContentAsString();
        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        long clientId = savedClient.getId();
        client.setId(clientId);

        //Save the property
        jsonObj = gson.toJson(property);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Save the office
        jsonObj = gson.toJson(office);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Save the agent
        jsonObj = gson.toJson(agent);

        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson = result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        long agentId = savedAgent.getId();
        agent.setId(agentId);

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Create a rent
        Rent rent = new Rent("2019-10-2", agent, client, propertyRent, 950, "2020-1-31");

        //Save the Rent
        jsonObj = gson.toJson(rent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rent/saveRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get Rent's id
        String rentObjJson = result.getResponse().getContentAsString();
        Rent savedRent = gson.fromJson(rentObjJson, Rent.class);
        RentKey rentKey = savedRent.getRentKey();
        rent.setRentKey(rentKey);

        //Create another rent
        Rent rent2 = new Rent("2019-1-1", agent, client, propertyRent, 950, "2019-10-1");

        //Save the Rent
        jsonObj = gson.toJson(rent2);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rent/saveRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get Rent's id
        String rentObjJson2 = result.getResponse().getContentAsString();
        Rent savedRent2 = gson.fromJson(rentObjJson2, Rent.class);
        RentKey rentKey2 = savedRent2.getRentKey();
        rent2.setRentKey(rentKey2);

        //Create a new rentalAgreement
        RentalAgreement rentalAgreement = new RentalAgreement(rent, "Link");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement = gson.fromJson(rentalAgreementObjJson, RentalAgreement.class);
        Long rentalAgreementId = savedRentalAgreement.getId();
        rentalAgreement.setId(rentalAgreementId);

        //Edit the rentAgreement
        rentalAgreement.setRent(rent2);
        rentalAgreement.setContent("Link2");

        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rentalAgreement/editRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Check there is only one rentAgreement
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/rentalAgreement/getAllRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<RentalAgreement> propertyRentalAgreementList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<RentalAgreement>>() {}.getType());
        assert(propertyRentalAgreementList.size() == 1);
        assert(propertyRentalAgreementList.get(0).getId().equals(rentalAgreementId));
        assert(propertyRentalAgreementList.get(0).getContent().equals("Link2"));
        assert(propertyRentalAgreementList.get(0).getRent().getRentEndDate().equals("2019-10-1"));
    }

    @Test
    public void testSearchRentalAgreement()throws Exception{
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Create an agent
        Person agentPerson = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(agentPerson,office);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Save the client
        Gson gson = new Gson();
        String jsonObj = gson.toJson(client);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/client/saveClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Set new client's id
        String clientObjJson =result.getResponse().getContentAsString();
        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        long clientId = savedClient.getId();
        client.setId(clientId);

        //Save the property
        jsonObj = gson.toJson(property);

        requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Save the office
        jsonObj = gson.toJson(office);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Save the agent
        jsonObj = gson.toJson(agent);

        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson = result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        long agentId = savedAgent.getId();
        agent.setId(agentId);

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Create a rent
        Rent rent = new Rent("2019-10-2", agent, client, propertyRent, 950, "2020-1-31");

        //Save the Rent
        jsonObj = gson.toJson(rent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rent/saveRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get Rent's id
        String rentObjJson = result.getResponse().getContentAsString();
        Rent savedRent = gson.fromJson(rentObjJson, Rent.class);
        RentKey rentKey = savedRent.getRentKey();
        rent.setRentKey(rentKey);

        //Create a new rentalAgreement
        RentalAgreement rentalAgreement = new RentalAgreement(rent, "Link");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement = gson.fromJson(rentalAgreementObjJson, RentalAgreement.class);
        Long rentalAgreementId = savedRentalAgreement.getId();
        rentalAgreement.setId(rentalAgreementId);

        //Create another new rentalAgreement
        RentalAgreement rentalAgreement2 = new RentalAgreement(rent, "Link2");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson2 = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement2 = gson.fromJson(rentalAgreementObjJson2, RentalAgreement.class);
        Long rentalAgreementId2 = savedRentalAgreement2.getId();
        rentalAgreement2.setId(rentalAgreementId2);

        //Search rentalAgreement
        RentalAgreement exampleRentalAgreement = new RentalAgreement();
        exampleRentalAgreement.setContent("Link2");

        jsonObj = gson.toJson(exampleRentalAgreement);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/rentalAgreement/searchRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should have one result
        List<RentalAgreement> rentalAgreementList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<RentalAgreement>>() {}.getType());
        assert(rentalAgreementList.size() == 1);
        assert(rentalAgreementList.get(0).getId().equals(rentalAgreementId2));
    }

    @Test
    public void testDeleteRentalAgreement()throws Exception {
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Create an agent
        Person agentPerson = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(agentPerson,office);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Save the client
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

        //Set new client's id
        String clientObjJson = result.getResponse().getContentAsString();
        Client savedClient = gson.fromJson(clientObjJson, Client.class);
        long clientId = savedClient.getId();
        client.setId(clientId);

        //Save the property
        jsonObj = gson.toJson(property);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Save the office
        jsonObj = gson.toJson(office);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Save the agent
        jsonObj = gson.toJson(agent);

        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson = result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        long agentId = savedAgent.getId();
        agent.setId(agentId);

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Create a rent
        Rent rent = new Rent("2019-10-2", agent, client, propertyRent, 950, "2020-1-31");

        //Save the Rent
        jsonObj = gson.toJson(rent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rent/saveRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get Rent's id
        String rentObjJson = result.getResponse().getContentAsString();
        Rent savedRent = gson.fromJson(rentObjJson, Rent.class);
        RentKey rentKey = savedRent.getRentKey();
        rent.setRentKey(rentKey);

        //Create a new rentalAgreement
        RentalAgreement rentalAgreement = new RentalAgreement(rent, "Link");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement = gson.fromJson(rentalAgreementObjJson, RentalAgreement.class);
        Long rentalAgreementId = savedRentalAgreement.getId();
        rentalAgreement.setId(rentalAgreementId);

        //Create another new rentalAgreement
        RentalAgreement rentalAgreement2 = new RentalAgreement(rent, "Link2");

        //Save the rentalAgreement
        jsonObj = gson.toJson(rentalAgreement2);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rentalAgreement/saveRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get rentalAgreement's id
        String rentalAgreementObjJson2 = result.getResponse().getContentAsString();
        RentalAgreement savedRentalAgreement2 = gson.fromJson(rentalAgreementObjJson2, RentalAgreement.class);
        Long rentalAgreementId2 = savedRentalAgreement2.getId();
        rentalAgreement2.setId(rentalAgreementId2);

        //Delete rentalAgreement
        jsonObj = gson.toJson(rentalAgreement);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/rentalAgreement/deleteRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test there is 1 rentalAgreement left
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/rentalAgreement/getAllRentalAgreement")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<RentalAgreement> propertyRentalAgreementList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<RentalAgreement>>() {}.getType());
        assert(propertyRentalAgreementList.size() == 1);

        //Check the change has been made in propertyRent
        PropertyRent examplePropertyRent = new PropertyRent();
        examplePropertyRent.setId(propertyRent.getId());
        jsonObj = gson.toJson(examplePropertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/searchPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyRent> propertyRentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        for (Rent nextRent : propertyRentList.get(0).getRents()) {
            assert(nextRent.getRentalAgreements().size() == 1);
        }
    }

}