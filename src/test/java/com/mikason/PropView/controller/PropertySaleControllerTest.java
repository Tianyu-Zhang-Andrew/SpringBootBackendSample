package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
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
public class PropertySaleControllerTest {

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
    public void testAddNewPropertySale() throws Exception{
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson =result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        System.out.println(savedSaleProperty);

        //Create another propertySale
        PropertySale propertySale2 = new PropertySale(property, 1100000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder);

        //Check all PropertySales that have been saved
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertySale/getAllPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.size() == 2);

        //Test both propertySales have been added in the property
        jsonObj = gson.toJson(property);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propertyList.get(0).getPropertySales().size() == 2);
    }

    @Test
    public void testEditPropertySale() throws Exception {
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

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        System.out.println(savedSaleProperty);
        long propertySaleId = savedSaleProperty.getId();
        propertySale.setId(propertySaleId);

        //Edit the propertySale
        //Change property
        propertySale.setProperty(property2);

        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/editPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        System.out.println(savedSaleProperty);
        assert(savedSaleProperty.getProperty().getId().equals(property2.getId()));

        //Check the propertySale is deleted from the old property
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
        assert(savedProperty.getPropertySales().size() == 0);

        //Test the propertySale has been added to the new property
        jsonObj = gson.toJson(property2);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .content(jsonObj)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList2 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propertyList2.get(0).getPropertySales().size() == 1);

        //Change suggested property price
        propertySale.setSuggestedSalesPrice((long) 1200000);
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/editPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        assert(savedSaleProperty.getSuggestedSalesPrice() == 1200000);
    }

    @Test
    public void testSearchPropertySale() throws Exception{
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

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedSaleProperty.getId();
        propertySale.setId(propertySaleId);

        //Create another propertySale
        PropertySale propertySale2 = new PropertySale(property2, 90000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale2);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new propertySale's id
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        propertySaleId = savedSaleProperty.getId();
        propertySale2.setId(propertySaleId);

        //Create the third propertySale
        PropertySale propertySale3 = new PropertySale(property, 1200000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale3);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new propertySale's id
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        propertySaleId = savedSaleProperty.getId();
        propertySale3.setId(propertySaleId);

        //Search all propertySale related to property with 1 bathroom
        Property sampleProperty = new Property();
        sampleProperty.setNumBathrooms(1);
        PropertySale samplePropertySale = new PropertySale();
        samplePropertySale.setProperty(sampleProperty);

        jsonObj = gson.toJson(samplePropertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .param("propertySale", jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Should have one result(propertySale2)
        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.size() == 1);
        assert(propertySaleList.get(0).getId().equals(propertySale2.getId()));

        //Search all property with suggested price higher than 9500000
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .param("lowPriceLimit", "950000")
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Should have one result(propertySale1, propertySale3)
        propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.size() == 2);

        //Search all property with suggested price lower than 1100000 with 1 bathroom
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .param("propertySale", jsonObj)
                .param("highPriceLimit", "1100000")
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Should have one result(propertySale2)
        propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.size() == 1);
        assert(propertySaleList.get(0).getId().equals(propertySale2.getId()));
    }

    @Test
    public void testDeletePropertySale() throws Exception{
        //Create a property
        Address address = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Property property = new Property(3, 2, 2, "Good unit", address);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Get new property's id
        String propertyObjJson =result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get the new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedSaleProperty.getId();
        propertySale.setId(propertySaleId);

        //Create another propertySale
        PropertySale propertySale2 = new PropertySale(property, 1100000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder);

        //Get the new propertySale's id
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        propertySaleId = savedSaleProperty.getId();
        propertySale2.setId(propertySaleId);

        //Create the third propertySale
        PropertySale propertySale3 = new PropertySale(property, 90000);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale3);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        //Get new propertySale's id
        propertySaleObjJson = result.getResponse().getContentAsString();
        savedSaleProperty = gson.fromJson(propertySaleObjJson, PropertySale.class);
        propertySaleId = savedSaleProperty.getId();
        propertySale3.setId(propertySaleId);

        //Check all PropertySales that have been saved
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertySale/getAllPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.size() == 3);

        //Delete one propertySale
        jsonObj = gson.toJson(propertySale2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/deletePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder);

        //There should be only two propertySale left
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertySale/getAllPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();
        propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        System.out.println(propertySaleList.size());
        assert(propertySaleList.size() == 2);

        //Check the propertySale is deleted from the property
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
        System.out.println(savedProperty.getPropertySales());
        assert(savedProperty.getPropertySales().size() == 2);

        //Delete all propertySale
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/deleteAllPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //There should be only no propertySale left
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertySale/getAllPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn();

        propertySaleObjJson = result.getResponse().getContentAsString();

        assert(propertySaleObjJson.equals("There is no record in the database now, database is empty."));
    }

}
