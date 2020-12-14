package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PropertyRentControllerTest {
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
    public void testAddPropertyRent()throws Exception {
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Save a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 960, "2019-3-5", "2021-5-1");

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

        //Get the saved propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Save another propertyRent
        PropertyRent propertyRent1 = new PropertyRent(property, 900, "2017-3-5", "2018-5-1");

        jsonObj = gson.toJson(propertyRent1);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the saved propertyRent's id
        String propertyRentObjJson1 = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent1 = gson.fromJson(propertyRentObjJson1, PropertyRent.class);
        long propertyRentId1 = savedPropertyRent1.getId();
        propertyRent1.setId(propertyRentId1);

        //Check all propertyRent have been saved with property
        jsonObj = gson.toJson(property);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .content(jsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyRents().size() == 2);

        //Check all propertyRent have been saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyRent/getAllPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        List<PropertyRent> propertyRentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        assert(propertyRentList.size() == 2);
    }

    @Test
    public void testEditPropertyRent() throws Exception{
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create another property
        Address address2 = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property2 = new Property(3, 1, 1, "Not bad unit", address2);
        String jsonObj2 = gson.toJson(property2);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj2)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 960, "2019-3-5", "2021-5-1");

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

        //Get the saved propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Editing propertyRent
        propertyRent.setSuggestedRentalPrice((long) 1000);

        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/editPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        propertyRentObjJson = result.getResponse().getContentAsString();
        savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        assert(savedPropertyRent.getSuggestedRentalPrice() == 1000);

        //Change property in propertyRent
        propertyRent.setProperty(property2);

        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/editPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        propertyRentObjJson = result.getResponse().getContentAsString();
        savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        assert(savedPropertyRent.getProperty().getId() == propertyId2);

        //check the propertyRent has been moved from property
        jsonObj = gson.toJson(property);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .content(jsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyRents().size() == 0);


        //Check the propertyRent has been added to property2
        jsonObj = gson.toJson(property2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .content(jsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyRents().size() == 1);
    }

    @Test
    public void testSearchPropertyRent() throws Exception{
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create another property
        Address address2 = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property2 = new Property(3, 1, 1, "Not bad unit", address2);
        String jsonObj2 = gson.toJson(property2);

        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj2)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 960, "2019-3-5", "2021-5-1");

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

        //Get the saved propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Create another propertyRent
        PropertyRent propertyRent2 = new PropertyRent(property, 800, "2019-3-5", "2021-5-1");

        jsonObj = gson.toJson(propertyRent2);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the saved propertyRent's id
        String propertyRentObjJson2 = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent2 = gson.fromJson(propertyRentObjJson2, PropertyRent.class);
        long propertyRentId2 = savedPropertyRent2.getId();
        propertyRent2.setId(propertyRentId2);

        //Create the third propertyRent
        PropertyRent propertyRent3 = new PropertyRent(property2, 950, "2019-3-5", "2021-5-1");

        jsonObj = gson.toJson(propertyRent3);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the saved propertyRent's id
        String propertyRentObjJson3 = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent3 = gson.fromJson(propertyRentObjJson3, PropertyRent.class);
        long propertyRentId3 = savedPropertyRent3.getId();
        propertyRent3.setId(propertyRentId3);

        //Search all propertyRent with 1 bathroom
        Property sampleProperty = new Property();
        sampleProperty.setNumBathrooms(1);
        PropertyRent samplePropertyRent = new PropertyRent();
        samplePropertyRent.setProperty(sampleProperty);

        jsonObj = gson.toJson(samplePropertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/searchPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .param("propertyRent", jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should have one result(propertyRent3)
        List<PropertyRent> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        assert(propertySaleList.size() == 1);
        assert(propertySaleList.get(0).getId().equals(propertyRent3.getId()));


        //Search all propertyRent with available price lower between 900 - 1000 with 2 bathroom
        sampleProperty.setNumBathrooms(2);
        samplePropertyRent.setProperty(sampleProperty);

        jsonObj = gson.toJson(samplePropertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/searchPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .param("propertyRent", jsonObj)
                .param("lowPriceLimit", "900")
                .param("highPriceLimit", "1000")
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should have one result(propertyRent1)
        propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        assert(propertySaleList.size() == 1);
        assert(propertySaleList.get(0).getId().equals(propertyRent.getId()));
    }

    @Test
    public void testDeletePropertyRent() throws Exception{
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 960, "2019-3-5", "2021-5-1");

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

        //Get the saved propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Test delete propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertyRent/deletePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Check the propertyRent has been deleted from the property
        jsonObj = gson.toJson(property);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .content(jsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyRents().size() == 0);

        //Check the propertyRent has been deleted
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyRent/getAllPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        String errorObjJson = result.getResponse().getContentAsString();
        assert(errorObjJson.equals("There is no record in the database now, database is empty."));
    }

}
