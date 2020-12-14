package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.estateEntity.Address;
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

import javax.transaction.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class OfficeControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void clear() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/agent/deleteAllAgent")
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
    public void testAddNewOffice()throws Exception{
        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Save the office
        Gson gson = new Gson();
        String jsonObj = gson.toJson(office);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Create another office
        Address officeAddress2 = new Address("3b", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office2 = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress2);

        //Save the office
        jsonObj = gson.toJson(office2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson2 = result.getResponse().getContentAsString();
        Office savedOffice2 = gson.fromJson(officeObjJson2, Office.class);
        long officeId2 = savedOffice2.getId();
        office2.setId(officeId2);

        //Test there is 2 offices saved
        requestBuilder = MockMvcRequestBuilders
                .get("/office/getAllOffice")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should return 2 results
        List<Office> officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.size() == 2);
    }

    @Test
    public void testEditOffice() throws Exception{
        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Save the office
        Gson gson = new Gson();
        String jsonObj = gson.toJson(office);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Create another office
        Address officeAddress2 = new Address("3b", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office2 = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress2);

        //Save the office
        jsonObj = gson.toJson(office2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson2 = result.getResponse().getContentAsString();
        Office savedOffice2 = gson.fromJson(officeObjJson2, Office.class);
        long officeId2 = savedOffice2.getId();
        office2.setId(officeId2);

        //Create agent1
        Person person1 = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent1 = new Agent(person1, office);

        jsonObj = gson.toJson(agent1);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the agent1's id
        String agentObjJson1 = result.getResponse().getContentAsString();
        Agent savedAgent1 = gson.fromJson(agentObjJson1, Agent.class);
        long agentId1 = savedAgent1.getId();
        agent1.setId(agentId1);

        //Create agent2
        Person person2 = new Person(Title.Mr, "Tom", "M", "Bush", Gender.Male, "1999-7-16", "0451268743", "Tomsemail@outlook.com");
        Agent agent2 = new Agent(person2, office2);

        jsonObj = gson.toJson(agent2);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the agent2's id
        String agentObjJson2 = result.getResponse().getContentAsString();
        Agent savedAgent2 = gson.fromJson(agentObjJson2, Agent.class);
        long agentId2 = savedAgent2.getId();
        agent2.setId(agentId2);

        //Change agent2 to office1
        agent2.setOffice(office);

        jsonObj = gson.toJson(agent2);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/editAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //test the agent has been removed from office2
        jsonObj = gson.toJson(office2);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Office> officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.get(0).getAgents().size() == 0);

        //Test the agent has been added to office
        jsonObj = gson.toJson(office);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.get(0).getAgents().size() == 2);

        //Delete agent2
        jsonObj = gson.toJson(agent2);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/deleteAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test the agent has been removed from office
        jsonObj = gson.toJson(office);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.get(0).getAgents().size() == 1);
        assert(officeList.get(0).getAgents().get(0).getId().equals(agentId1));

        //Edit office
        office.setEmailAddress("New Email Address");

        jsonObj = gson.toJson(office);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/editOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test the office has been edited
        Office sampleOffice = new Office();
        sampleOffice.setId(officeId);

        jsonObj = gson.toJson(sampleOffice);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.get(0).getEmailAddress().equals("New Email Address"));

        //Test the change has been made in the agent
        Agent sampleAgent = new Agent();
        sampleAgent.setId(agentId1);

        jsonObj = gson.toJson(sampleAgent);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Agent> agentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        assert(agentList.get(0).getOffice().getEmailAddress().equals("New Email Address"));
    }

    @Test
    public void testSearchOffice() throws Exception{
        //Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Save the office
        Gson gson = new Gson();
        String jsonObj = gson.toJson(office);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Create another office
        Address officeAddress2 = new Address("3b", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office2 = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress2);

        //Save the office
        jsonObj = gson.toJson(office2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson2 = result.getResponse().getContentAsString();
        Office savedOffice2 = gson.fromJson(officeObjJson2, Office.class);
        long officeId2 = savedOffice2.getId();
        office2.setId(officeId2);

        //Test search office with unitNumber 3b
        Address sampleAddress = new Address();
        sampleAddress.setUnitNumber("3b");
        Office sampleOffice = new Office();
        sampleOffice.setAddress(sampleAddress);

        jsonObj = gson.toJson(sampleOffice);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should return 1 results
        List<Office> officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.size() == 1);
        assert(officeList.get(0).getId().equals(officeId2));
    }

    @Test
    public void testDeleteOffice() throws Exception{
//Create an office
        Address officeAddress = new Address("3a", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress);

        //Save the office
        Gson gson = new Gson();
        String jsonObj = gson.toJson(office);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson = result.getResponse().getContentAsString();
        Office savedOffice = gson.fromJson(officeObjJson, Office.class);
        long officeId = savedOffice.getId();
        office.setId(officeId);

        //Create another office
        Address officeAddress2 = new Address("3b", "4", "Rose street", "street", "Ztland", "NSW", "Australia", "2017");
        Office office2 = new Office("Ztland office", "0451258836", "123456", "manlyOffice@gmail.com", officeAddress2);

        //Save the office
        jsonObj = gson.toJson(office2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/saveOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new office's id
        String officeObjJson2 = result.getResponse().getContentAsString();
        Office savedOffice2 = gson.fromJson(officeObjJson2, Office.class);
        long officeId2 = savedOffice2.getId();
        office2.setId(officeId2);

        //Delete office2
        jsonObj = gson.toJson(office2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/office/deleteOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test there is only 1 office left
        requestBuilder = MockMvcRequestBuilders
                .get("/office/getAllOffice")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should return 2 results
        List<Office> officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.size() == 1);
        assert(officeList.get(0).getId().equals(officeId));
    }
}
