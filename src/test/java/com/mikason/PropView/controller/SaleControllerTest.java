package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.compositeKey.SaleKey;
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
public class SaleControllerTest {
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
    public void testAddNewSale() throws Exception{
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

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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
        System.out.println("Here2");
        System.out.println(savedAgent.getSales());


        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test the sale has been saved in the agent
        jsonObj = gson.toJson(agent);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Agent> agentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        assert(agentList.get(0).getSales().size() == 1);
    }

    @Test
    public void testEditSale() throws Exception{
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

        //Create another agent
        Person agentPerson2 = new Person(Title.Mr, "David", "D", "Pumpkin", Gender.Male, "1990-2-15", "045143258", "Davidsemail@outlook.com");
        Agent agent2 = new Agent(agentPerson2, office);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Save agent1
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

        //Save agent2
        jsonObj = gson.toJson(agent2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson2 =result.getResponse().getContentAsString();
        Agent savedAgent2 = gson.fromJson(agentObjJson2, Agent.class);
        long agentId2 = savedAgent2.getId();
        agent2.setId(agentId2);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Edit the sale
        SaleKey editedSaleKey = new SaleKey(date,agent2,client,propertySale);
        Sale newSale = new Sale();
        newSale.setSaleKey(editedSaleKey);
        newSale.setSalePrice((long) 1200000);

        String saleObj = gson.toJson(sale);
        String newSaleObj = gson.toJson(newSale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/editSale")
                .accept(MediaType.APPLICATION_JSON)
                .param("oldSale", saleObj)
                .param("newSale", newSaleObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String editedSaleStr = result.getResponse().getContentAsString();
        System.out.println(editedSaleStr);

        //Check the sale has been deleted from the old agent
        jsonObj = gson.toJson(agent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Agent> agentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        savedAgent = agentList.get(0);
        System.out.println(savedAgent.getSales().size());
        assert(savedAgent.getSales().size() == 0);

        //Check the sale has been added to the new agent
        jsonObj = gson.toJson(agent2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        agentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        savedAgent = agentList.get(0);
        System.out.println(savedAgent.getSales().size());
        assert(savedAgent.getSales().size() == 1);

        //Check the sale has been edited on the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.get(0).getSales().size() == 1);
    }

    @Test
    public void testDeleteSale() throws Exception{
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

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Delete the sale
        jsonObj = gson.toJson(sale);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/deleteSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Check the sale has been deleted from the agent
        jsonObj = gson.toJson(agent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Agent> agentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        savedAgent = agentList.get(0);
        System.out.println(savedAgent.getSales().size());
        assert(savedAgent.getSales().size() == 0);

        //Check the sale has been deleted from the client
        jsonObj = gson.toJson(client);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Client> clientList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        savedClient = clientList.get(0);
        System.out.println(savedClient.getSales().size());
        assert(savedClient.getSales().size() == 0);

        //Check the sale has been deleted from the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        savedPropertySale = propertySaleList.get(0);
        System.out.println(savedPropertySale.getSales().size());
        assert(savedPropertySale.getSales().size() == 0);

        //Check there is no sale in the database now
        requestBuilder =  MockMvcRequestBuilders
                .get("/sale/getAllSale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        String errorStr = result.getResponse().getContentAsString();
        assert(errorStr.equals("There is no record in the database now, database is empty."));
    }

//    @Test
//    public void testSearchSale() throws Exception{
//        //Create a client
//        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
//        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
//        Client client = new Client(clientAddress, clientPerson);
//
//        //Create a property
//        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
//        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);
//
//        //Create an agent
//        Person agentPerson = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
//        Agent agent = new Agent(agentPerson);
//
//        //Create a propertySale
//        PropertySale propertySale = new PropertySale(property, 1000000);
//
//        //Save the client
//        Gson gson = new Gson();
//        String jsonObj = gson.toJson(client);
//
//        RequestBuilder requestBuilder =  MockMvcRequestBuilders
//                .post("/client/saveClient")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Set new client's id
//        String clientObjJson =result.getResponse().getContentAsString();
//        Client savedClient = gson.fromJson(clientObjJson, Client.class);
//        long clientId = savedClient.getId();
//        client.setId(clientId);
//
//        //Save the property
//        jsonObj = gson.toJson(property);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/property/saveProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new property's id
//        String propertyObjJson = result.getResponse().getContentAsString();
//        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
//        long propertyId = savedProperty.getId();
//        property.setId(propertyId);
//
//        //Save the agent
//        jsonObj = gson.toJson(agent);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/agent/saveAgent")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new agent's id
//        String agentObjJson = result.getResponse().getContentAsString();
//        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
//        long agentId = savedAgent.getId();
//        agent.setId(agentId);
//
//        //Save the propertySale
//        jsonObj = gson.toJson(propertySale);
//        log.info(jsonObj);
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/propertySale/savePropertySale")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new propertySale's id
//        String propertySaleObjJson = result.getResponse().getContentAsString();
//        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
//        long propertySaleId = savedPropertySale.getId();
//        propertySale.setId(propertySaleId);
//
//        //Create a new sale
//        String date = "2019-07-12";
//        Sale sale = new Sale(date, agent, client, propertySale, 1100000);
//
//        //Save the Sale
//        jsonObj = gson.toJson(sale);
//        log.info(jsonObj);
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/sale/saveSale")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Test search all sale of agent
//        Sale exampleSale = new Sale();
//        SaleKey exampleSaleKey = new SaleKey();
//        exampleSaleKey.setAgent(agent);
//        exampleSale.setSaleKey(exampleSaleKey);
//
//        jsonObj = gson.toJson(exampleSale);
//        log.info(jsonObj);
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/sale/searchSale")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        List<Sale> saleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Sale>>() {}.getType());
//        System.out.println(saleList);
//        assert(saleList.size() == 1);
//    }
}
