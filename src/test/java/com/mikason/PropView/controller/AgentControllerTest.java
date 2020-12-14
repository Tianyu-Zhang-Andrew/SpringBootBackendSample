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
public class AgentControllerTest {
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
    public void testAddNewAgent() throws Exception {
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

        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(person,office);

        jsonObj = gson.toJson(agent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String agentObjJson =result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        Person savedPerson = savedAgent.getPerson();

        assert(savedPerson.getTitle().equals(Title.Mr));
        assert(savedPerson.getFirstName().equals("Puff"));
        assert(savedPerson.getMiddleName().equals("M"));
        assert(savedPerson.getLastName().equals("Dragon"));
        assert(savedPerson.getGender().equals(Gender.Male));
        assert(savedPerson.getBirthDate().equals("1991-3-25"));
        assert(savedPerson.getMobileNumber().equals("045115328"));
        assert(savedPerson.getEmail().equals("Puffsemail@outlook.com"));

        //Test the agent has been saved in the office
        jsonObj = gson.toJson(office);
        requestBuilder = MockMvcRequestBuilders
                .post("/office/searchOffice")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Office> officeList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Office>>() {}.getType());
        assert(officeList.get(0).getAgents().size() == 1);
    }

    @Test
    public void testEditNewAgent() throws Exception {
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

        //Create an agent
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(person,office);

        jsonObj = gson.toJson(agent);
        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new agent's id
        String agentObjJson =result.getResponse().getContentAsString();
        Agent savedAgent = gson.fromJson(agentObjJson, Agent.class);
        long id = savedAgent.getId();
        agent.setId(id);

        //Edit the new agent
        person.setEmail("PuffsBackupEmail@outlook.com");
        agent.setPerson(person);

        String newJsonObj = gson.toJson(agent);

        requestBuilder = MockMvcRequestBuilders
                .post("/agent/editAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(newJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        agentObjJson =result.getResponse().getContentAsString();

        Agent newSavedAgent = gson.fromJson(agentObjJson, Agent.class);
        Person agentPersonInf = newSavedAgent.getPerson();

        assert(agentPersonInf.getEmail().equals("PuffsBackupEmail@outlook.com"));
    }

    @Test
    public void testDeleteAgent() throws Exception {
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

        //Create an agent
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent = new Agent(person,office);

        jsonObj = gson.toJson(agent);

        requestBuilder =  MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String savedAgent = result.getResponse().getContentAsString();

        requestBuilder = MockMvcRequestBuilders
                .post("/agent/deleteAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(savedAgent)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testSearchAgent() throws Exception {
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

        //Create agent1
        Person person1 = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Agent agent1 = new Agent(person1, office);

        jsonObj = gson.toJson(agent1);
         requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Create agent2
        Person person2 = new Person(Title.Mr, "Tom", "M", "Bush", Gender.Male, "1999-7-16", "0451268743", "Tomsemail@outlook.com");
        Agent agent2 = new Agent(person2, office);

        jsonObj = gson.toJson(agent2);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Create agent3
        Person person3 = new Person(Title.Ms, "Alice", "R", "Adams", Gender.Female, "1998-9-20", "0452263563", "Alicesemail@outlook.com");
        Agent agent3 = new Agent(person3, office);

        jsonObj = gson.toJson(agent3);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/saveAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        //Search for all male agent
        Person samplePerson = new Person();
        samplePerson.setGender(Gender.Male);
        Agent sampleAgent = new Agent();
        sampleAgent.setPerson(samplePerson);

        jsonObj = gson.toJson(sampleAgent);
        requestBuilder = MockMvcRequestBuilders
                .post("/agent/searchAgent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should return 2 results
        List<Agent> agentList1 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Agent>>() {}.getType());
        assert(agentList1.size() == 2);
        System.out.println(result.getResponse().getContentAsString());
    }
}
