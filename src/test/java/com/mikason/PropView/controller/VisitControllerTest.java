package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.commercialEntity.Duration;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.commercialEntity.Visit;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
//@Transactional
public class VisitControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void clear() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/visit/deleteAllVisit")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
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
    public void testAddOneVisit() throws Exception{

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

        Duration dur1 = new Duration(2019,7,25,3,55,4,15);

        //Create a visit
        Visit visit = new Visit(agent, property, client, dur1.toString());

        jsonObj = gson.toJson(visit);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String visitObjJson =result.getResponse().getContentAsString();

        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        //Try to save a visit with wrong id
        long wrongId = 123;
        agent.setId(wrongId);
        visit.setAgent(agent);

        jsonObj = gson.toJson(visit);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        visitObjJson =result.getResponse().getContentAsString();
        String expectStr = "Could not find agent 123";
        assert(visitObjJson.equals(expectStr));
    }

    @Test
    public void testAddTwoVisit() throws Exception{
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

        //Create two agents
        Person agentPerson1 = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent1 = new Agent(agentPerson1, office);

        Person agentPerson2 = new Person(Title.Mr, "David", "D", "Pumpkin", Gender.Male, "1990-2-15", "045143258", "Davidsemail@outlook.com");
        Agent agent2 = new Agent(agentPerson2,office);

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
        String propertyObjJson =result.getResponse().getContentAsString();
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
        jsonObj = gson.toJson(agent1);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson1 =result.getResponse().getContentAsString();
        Agent savedAgent1 = gson.fromJson(agentObjJson1, Agent.class);
        long agentId1 = savedAgent1.getId();
        agent1.setId(agentId1);

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

        Duration dur = new Duration(2019,7,25,3,55,4,15);

        //Create a visit
        Visit visit1 = new Visit(agent1, property, client, dur.toString());

        jsonObj = gson.toJson(visit1);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get visit's id
        String visitObjJson =result.getResponse().getContentAsString();
        Visit savedVisit = gson.fromJson(visitObjJson, Visit.class);

        System.out.println(savedVisit);

        Duration dur2 = new Duration(2019,8,25,3,55,4,15);

        //Add another visit
        Visit visit2 = new Visit(agent2, property, client, dur2.toString());

        jsonObj = gson.toJson(visit2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();


        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testEditVisit() throws Exception{
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

        //Create two agents
        Person agentPerson1 = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent1 = new Agent(agentPerson1, office);

        Person agentPerson2 = new Person(Title.Mr, "David", "D", "Pumpkin", Gender.Male, "1990-2-15", "045143258", "Davidsemail@outlook.com");
        Agent agent2 = new Agent(agentPerson2, office);

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
        String propertyObjJson =result.getResponse().getContentAsString();
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
        jsonObj = gson.toJson(agent1);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson1 =result.getResponse().getContentAsString();
        Agent savedAgent1 = gson.fromJson(agentObjJson1, Agent.class);
        long agentId1 = savedAgent1.getId();
        agent1.setId(agentId1);

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

        Duration dur = new Duration(2019,7,25,3,55,4,15);

        //Create a visit
        Visit visit = new Visit(agent1, property, client, dur.toString());

        jsonObj = gson.toJson(visit);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get visit's id
        String visitObjJson =result.getResponse().getContentAsString();
        Visit savedVisit = gson.fromJson(visitObjJson, Visit.class);
        visit.setId(savedVisit.getId());

        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        //Edit saved visit
        visit.setAgent(agent2);

        jsonObj = gson.toJson(visit);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/editVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSearchVisit() throws Exception {
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

        //Create two agents
        Person agentPerson1 = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent1 = new Agent(agentPerson1,office);

        Person agentPerson2 = new Person(Title.Mr, "David", "D", "Pumpkin", Gender.Male, "1990-2-15", "045143258", "Davidsemail@outlook.com");
        Agent agent2 = new Agent(agentPerson2,office);

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
        String propertyObjJson =result.getResponse().getContentAsString();
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
        jsonObj = gson.toJson(agent1);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new agent's id
        String agentObjJson1 =result.getResponse().getContentAsString();
        Agent savedAgent1 = gson.fromJson(agentObjJson1, Agent.class);
        long agentId1 = savedAgent1.getId();
        agent1.setId(agentId1);

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

        //Create a visit
        Visit visit1 = new Visit();
        visit1.setAgent(agent1);
        visit1.setClient(client);
        visit1.setProperty(property);
        Duration dur1 = new Duration(2019,7,25,15,55,16,15);
        visit1.setDuration(dur1.toString());

        jsonObj = gson.toJson(visit1);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another visit
        Visit visit2 = new Visit();
        visit2.setAgent(agent2);
        visit2.setClient(client);
        visit2.setProperty(property);
        Duration dur2 = new Duration(2019,7,25,14,35,15,10);
        visit2.setDuration(dur2.toString());

        jsonObj = gson.toJson(visit2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create the third visit
        Visit visit3 = new Visit();
        visit3.setAgent(agent2);
        visit3.setClient(client);
        visit3.setProperty(property);
        Duration dur3 = new Duration(2019,7,25,14,10,14,25);
        visit3.setDuration(dur3.toString());

        jsonObj = gson.toJson(visit3);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Search all visits guided by agent2
        Visit sampleVisit1 = new Visit();
        sampleVisit1.setAgent(agent2);

        String visitObj = gson.toJson(sampleVisit1);
        System.out.println(visitObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/searchVisit")
                .param("visit", visitObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 result
        List<Client> visitList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Visit>>() {}.getType());
        System.out.println(visitList);
        assert(visitList.size() == 2);

        //Search all visit guided by agent2 during 14:00 to 14:30
        Visit sampleVisit2 = new Visit();
        sampleVisit2.setAgent(agent2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String startDateStr = "2019-07-25 14:00";
        String endDateStr = "2019-07-25 14:30";
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);

        visitObj = gson.toJson(sampleVisit2);
        String startDateJsonObj = gson.toJson(startDate);
        String endDateJsonObj = gson.toJson(endDate);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/searchVisit")
                .param("visit", visitObj)
                .param("start", startDateJsonObj)
                .param("end", endDateJsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 1 result
        visitList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Visit>>() {}.getType());
        System.out.println(visitList);
        assert(visitList.size() == 1);

        //Search for all visits
        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 3 result
        visitList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Visit>>() {}.getType());
        System.out.println(visitList);
        assert(visitList.size() == 3);

        //Search all visit during 14:00 to 15:00
        Visit sampleVisit3 = new Visit();
        sampleVisit2.setProperty(property);

        startDateStr = "2019-07-25 14:00";
        endDateStr = "2019-07-25 15:00";
        startDate = sdf.parse(startDateStr);
        endDate = sdf.parse(endDateStr);

        visitObj = gson.toJson(sampleVisit3);
        startDateJsonObj = gson.toJson(startDate);
        endDateJsonObj = gson.toJson(endDate);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/searchVisit")
                .param("visit", visitObj)
                .param("start", startDateJsonObj)
                .param("end", endDateJsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 result
        visitList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Visit>>() {}.getType());
        System.out.println(visitList);
        assert(visitList.size() == 2);

    }

    @Test
    public void testDeleteVisit() throws Exception{
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

        Duration dur1 = new Duration(2019,7,25,3,55,4,15);

        //Create a visit
        Visit visit = new Visit(agent, property, client, dur1.toString());

        jsonObj = gson.toJson(visit);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/saveVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String visitObjJson =result.getResponse().getContentAsString();
        Visit savedVisit = gson.fromJson(visitObjJson, Visit.class);

        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        jsonObj = gson.toJson(savedVisit);
        requestBuilder =  MockMvcRequestBuilders
                .post("/visit/deleteVisit")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        requestBuilder = MockMvcRequestBuilders
                .get("/visit/getAllVisit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        String exceptStr = "There is no record in the database now, database is empty.";
        assert(result.getResponse().getContentAsString().equals(exceptStr));
    }
}
