package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.estateEntity.Address;
import com.mikason.PropView.dataaccess.estateEntity.Property;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class PropertyControllerTest {

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
                .post("/property/deleteAllProperty")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddNewProperty() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        log.info (jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("numRooms").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("numBathrooms").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("numGarages").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("Good unit"))
                .andReturn();

        String propObjJson =result.getResponse().getContentAsString();
        Property prop = gson.fromJson(propObjJson, Property.class);
        Address propAddress = prop.getAddress();

        assert(propAddress.getUnitNumber().equals("1a"));
        assert(propAddress.getStreetNumber().equals("1"));
        assert(propAddress.getStreetName().equals("Rialto Lane"));
        assert(propAddress.getStreetType().equals("Avenue"));
        assert(propAddress.getSuburb().equals("Manly"));
        assert(propAddress.getState().equals("NSW"));
        assert(propAddress.getCountry().equals("Australia"));
        assert(propAddress.getPostCode().equals("2095"));
    }

    @Test
    public void testEditProperty() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        //Create a new property
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propObjJson =result.getResponse().getContentAsString();
        Property prop = gson.fromJson(propObjJson, Property.class);
        long id = prop.getId();

        Property newProperty = new Property(4, 2, 1, "Good unit", address);
        newProperty.setId(id);

        gson = new Gson();
        String newJsonObj = gson.toJson(newProperty);

        //Edit the new property
        requestBuilder = MockMvcRequestBuilders
                .post("/property/editProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(newJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("numRooms").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("numBathrooms").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("numGarages").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("Good unit"))
                .andReturn();

        propObjJson = result.getResponse().getContentAsString();
        prop = gson.fromJson(propObjJson, Property.class);
        Address propAddress = prop.getAddress();

        assert(propAddress.getUnitNumber().equals("1a"));
        assert(propAddress.getStreetNumber().equals("1"));
        assert(propAddress.getStreetName().equals("Rialto Lane"));
        assert(propAddress.getStreetType().equals("Avenue"));
        assert(propAddress.getSuburb().equals("Manly"));
        assert(propAddress.getState().equals("NSW"));
        assert(propAddress.getCountry().equals("Australia"));
        assert(propAddress.getPostCode().equals("2095"));
    }

    @Test
    public void testDeleteProperty() throws Exception {
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        //Create a new property
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String propObjJson =result.getResponse().getContentAsString();

        //Delete the Property
        requestBuilder = MockMvcRequestBuilders
                .post("/property/deleteProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(propObjJson)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Test
    public void testFindProperty() throws Exception {

        Gson gson = new Gson();

        //Create 3 new property
        Address address1 = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property1 = new Property(3, 2, 2, "Good unit", address1);
        String jsonObj1 = gson.toJson(property1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        Address address2 = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property2 = new Property(3, 1, 1, "Not bad unit", address2);
        String jsonObj2 = gson.toJson(property2);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj2)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        Address address3 = new Address("3c", "5", "Parramatta street", "Avenue", "Parramatta", "NSW", "Australia", "2041");
        Property property3 = new Property(1, 1, 1, "Poor unit", address3);
        String jsonObj3 = gson.toJson(property3);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj3)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Search for property has 3 rooms
        Property sampleProperty1 = new Property();
        sampleProperty1.setNumRooms(3);
        String SearchJsonObj = gson.toJson(sampleProperty1);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(SearchJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 property (property2 and property3)
        List<Property> propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.size() == 2);
        System.out.println(result.getResponse().getContentAsString());

        //Search all property in Parramatta
        Address sampleAddress1 = new Address();
        sampleAddress1.setSuburb("Parramatta");
        Property sampleProperty2 = new Property();
        sampleProperty2.setAddress(sampleAddress1);
        SearchJsonObj = gson.toJson(sampleProperty2);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(SearchJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 2 property (property1 and property2)
        propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.size() == 2);
        System.out.println(result.getResponse().getContentAsString());

        //Search all property in Parramatta which has 3 rooms.
        Address sampleAddress2 = new Address();
        sampleAddress2.setSuburb("Parramatta");
        Property sampleProperty3 = new Property();
        sampleProperty3.setNumRooms(3);
        sampleProperty3.setAddress(sampleAddress2);
        SearchJsonObj = gson.toJson(sampleProperty3);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(SearchJsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should get 1 property (property2)
        propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.size() == 1);
        System.out.println(result.getResponse().getContentAsString());
    }

}
