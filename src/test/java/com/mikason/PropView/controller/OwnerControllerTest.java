package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.peopleEntity.Gender;
import com.mikason.PropView.dataaccess.peopleEntity.Owner;
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
public class OwnerControllerTest {
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
                .post("/owner/deleteAllOwner")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddNewOwner() throws Exception {
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Owner owner = new Owner("2019-10-12", person);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(owner);
        log.info(jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String ownerObjJson =result.getResponse().getContentAsString();
        Owner savedOwner = gson.fromJson(ownerObjJson, Owner.class);
        Person savedPerson = savedOwner.getPerson();

        assert(savedOwner.getDateOfPurchase().equals("2019-10-12"));
        assert(savedPerson.getTitle().equals(Title.Mr));
        assert(savedPerson.getFirstName().equals("Puff"));
        assert(savedPerson.getMiddleName().equals("M"));
        assert(savedPerson.getLastName().equals("Dragon"));
        assert(savedPerson.getGender().equals(Gender.Male));
        assert(savedPerson.getBirthDate().equals("1991-3-25"));
        assert(savedPerson.getMobileNumber().equals("045115328"));
        assert(savedPerson.getEmail().equals("Puffsemail@outlook.com"));
    }

    @Test
    public void testEditOwner() throws Exception{
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Owner owner = new Owner("2019-10-12", person);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(owner);
        log.info(jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        String ownerObjJson =result.getResponse().getContentAsString();
        Owner savedOwner = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId = savedOwner.getId();
        owner.setId(ownerId);

        //Edit the new owner
        person.setEmail("PuffsBackupEmail@outlook.com");
        owner.setPerson(person);

        jsonObj = gson.toJson(owner);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/owner/editOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        ownerObjJson =result.getResponse().getContentAsString();
        savedOwner = gson.fromJson(ownerObjJson, Owner.class);
        assert(savedOwner.getPerson().getEmail().equals("PuffsBackupEmail@outlook.com"));
    }

    @Test
    public void testSearchOwner() throws Exception{
        //Create an owner
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Owner owner = new Owner("2019-10-12", person);

        //Save the owner
        Gson gson = new Gson();
        String jsonObj = gson.toJson(owner);
        log.info(jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        String ownerObjJson =result.getResponse().getContentAsString();
        Owner savedOwner = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId = savedOwner.getId();
        owner.setId(ownerId);

        //Create another owner
        Person person2 = new Person(Title.Mr, "Tom", "M", "Bush", Gender.Male, "1999-7-16", "0451268743", "Tomsemail@outlook.com");
        Owner owner2 = new Owner("2019-5-15", person2);

        //Save the owner
        jsonObj = gson.toJson(owner2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        ownerObjJson = result.getResponse().getContentAsString();
        Owner savedOwner2 = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId2 = savedOwner2.getId();
        owner2.setId(ownerId2);

        //Create the third owner
        Person person3 = new Person(Title.Ms, "Alice", "R", "Adams", Gender.Female, "1998-9-20", "0452263563", "Alicesemail@outlook.com");
        Owner owner3 = new Owner("2019-5-15", person3);

        //Save the owner
        jsonObj = gson.toJson(owner3);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        ownerObjJson = result.getResponse().getContentAsString();
        Owner savedOwner3 = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId3 = savedOwner3.getId();
        owner3.setId(ownerId3);

        //Search all male owner
        Person examplePerson = new Person();
        examplePerson.setGender(Gender.Male);
        Owner exampleOwner = new Owner();
        exampleOwner.setPerson(examplePerson);

        jsonObj = gson.toJson(exampleOwner);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/owner/searchOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //There should be 2 result
        List<Owner> ownerList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Owner>>() {}.getType());
        assert(ownerList.size() == 2);
    }

    @Test
    public void testDeleteOwner() throws Exception{
        //Create an owner
        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
        Owner owner = new Owner("2019-10-12", person);

        //Save the owner
        Gson gson = new Gson();
        String jsonObj = gson.toJson(owner);
        log.info(jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        String ownerObjJson =result.getResponse().getContentAsString();
        Owner savedOwner = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId = savedOwner.getId();
        owner.setId(ownerId);

        //Create another owner
        Person person2 = new Person(Title.Mr, "Tom", "M", "Bush", Gender.Male, "1999-7-16", "0451268743", "Tomsemail@outlook.com");
        Owner owner2 = new Owner("2019-5-15", person2);

        //Save the owner
        jsonObj = gson.toJson(owner2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/owner/saveOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the new owner's id
        ownerObjJson = result.getResponse().getContentAsString();
        Owner savedOwner2 = gson.fromJson(ownerObjJson, Owner.class);
        long ownerId2 = savedOwner2.getId();
        owner2.setId(ownerId2);

        //Delete owner2
        jsonObj = gson.toJson(owner2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/owner/deleteOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //There should only be 1 owner now
        requestBuilder =  MockMvcRequestBuilders
                .get("/owner/getAllOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Owner> ownerList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Owner>>() {}.getType());
        assert(ownerList.size() == 1);
    }

}
