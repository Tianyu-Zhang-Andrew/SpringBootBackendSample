package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyFeature;
import com.mikason.PropView.dataaccess.compositeKey.PropertyFeatureKey;
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
public class PropertyFeatureControllerTest {
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

        requestBuilder = MockMvcRequestBuilders
                .post("/feature/deleteAllFeature")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddPropertyFeature()throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        jsonObj = gson.toJson(feature);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new feature's id
        String featureObjJson =result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Create a propertyFeature
        PropertyFeatureKey propertyFeatureKey = new PropertyFeatureKey(property, feature);
        PropertyFeature propertyFeature = new PropertyFeature(propertyFeatureKey);

        //Save the propertyFeature
        jsonObj = gson.toJson(propertyFeature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/savePropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Check there is 1 propertyFeature
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyFeature/getAllPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyFeature> propertyFeatureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyFeature>>() {}.getType());
        assert(propertyFeatureList.size() == 1);

        //Test the propertyFeature is saved in the property
        jsonObj = gson.toJson(property);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyFeatures().size() == 1);

        for(PropertyFeature savedPropertyFeature : savedProperty.getPropertyFeatures()){
            assert(savedPropertyFeature.getPropertyFeatureKey().getProperty().getId().equals(propertyId));
            break;
        }

        //Test the propertyFeature has been saved in the feature
        jsonObj = gson.toJson(feature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        savedFeature = featureList.get(0);
        assert(savedFeature.getPropertyFeatures().size() == 1);

        for(PropertyFeature savedPropertyFeature : savedFeature.getPropertyFeatures()){
            assert(savedPropertyFeature.getPropertyFeatureKey().getFeature().getId().equals(featureId));
            break;
        }
    }

    @Test
    public void testEditPropertyFeature() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create another property
        Address address2 = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property2 = new Property(3, 2, 2, "Good unit", address2);

        jsonObj = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new feature's id
        String featureObjJson =result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Create another feature
        Feature feature2 = new Feature("code2", "des2");

        //Save the feature
        jsonObj = gson.toJson(feature2);

        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new feature's id
        String featureObjJson2 =result.getResponse().getContentAsString();
        Feature savedFeature2 = gson.fromJson(featureObjJson2, Feature.class);
        long featureId2 = savedFeature2.getId();
        feature2.setId(featureId2);

        //Create a propertyFeature
        PropertyFeatureKey propertyFeatureKey = new PropertyFeatureKey(property, feature);
        PropertyFeature propertyFeature = new PropertyFeature(propertyFeatureKey);

        jsonObj = gson.toJson(propertyFeature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/savePropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Edit the propertyFeature
        PropertyFeatureKey newPropertyFeatureKey = new PropertyFeatureKey(property2, feature);
        PropertyFeature newPropertyFeature = new PropertyFeature(newPropertyFeatureKey);

        String propertyFeatureObj = gson.toJson(propertyFeature);
        String newPropertyFeatureObj = gson.toJson(newPropertyFeature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/editPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .param("oldPropertyFeature", propertyFeatureObj)
                .param("newPropertyFeature", newPropertyFeatureObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test there is only 1 propertyFeature left
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyFeature/getAllPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyFeature> propertyFeatureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyFeature>>() {}.getType());
        assert(propertyFeatureList.size() == 1);

        //Test the old PropertyFeature has been deleted from property
        jsonObj = gson.toJson(property);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyFeatures().size() == 0);

        //Test the newPropertyFeature has been added to the property2
        Property exampleProperty = new Property();
        exampleProperty.setId(propertyId2);
        jsonObj = gson.toJson(exampleProperty);

        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList2 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList2.get(0);
        assert(savedProperty.getPropertyFeatures().size() == 1);

        for(PropertyFeature savedPropertyFeature : savedProperty.getPropertyFeatures()){
            assert(savedPropertyFeature.getPropertyFeatureKey().getProperty().getId().equals(propertyId2));
            break;
        }

        //Edit the propertyFeature
        PropertyFeatureKey newPropertyFeatureKey2 = new PropertyFeatureKey(property2, feature2);
        PropertyFeature newPropertyFeature2 = new PropertyFeature(newPropertyFeatureKey2);

        propertyFeature.setPropertyFeatureKey(newPropertyFeatureKey);
        propertyFeatureObj = gson.toJson(propertyFeature);
        newPropertyFeatureObj = gson.toJson(newPropertyFeature2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/editPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .param("oldPropertyFeature", propertyFeatureObj)
                .param("newPropertyFeature", newPropertyFeatureObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test there is only 1 propertyFeature left
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyFeature/getAllPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        propertyFeatureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyFeature>>() {}.getType());
        assert(propertyFeatureList.size() == 1);

        //Test the propertyFeature has been removed form the old feature
        jsonObj = gson.toJson(feature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        savedFeature = featureList.get(0);
        assert(savedFeature.getPropertyFeatures().size() == 0);

        //Test the newPropertyFeature has been added to the feature2
        jsonObj = gson.toJson(feature2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList2 = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        savedFeature = featureList2.get(0);
        assert(savedFeature.getPropertyFeatures().size() == 1);

        for(PropertyFeature savedPropertyFeature : savedFeature.getPropertyFeatures()){
            assert(savedPropertyFeature.getPropertyFeatureKey().getFeature().getId().equals(featureId2));
            break;
        }

    }

    @Test
    public void testDeletePropertyFeature() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Save the property
        Gson gson = new Gson();
        String jsonObj = gson.toJson(property);

        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson = result.getResponse().getContentAsString();
        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
        long propertyId = savedProperty.getId();
        property.setId(propertyId);

        //Create another property
        Address address2 = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property2 = new Property(3, 2, 2, "Good unit", address2);

        jsonObj = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new feature's id
        String featureObjJson =result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Create a propertyFeature
        PropertyFeatureKey propertyFeatureKey = new PropertyFeatureKey(property,feature);
        PropertyFeature propertyFeature = new PropertyFeature(propertyFeatureKey);

        jsonObj = gson.toJson(propertyFeature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/savePropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another propertyFeature
        PropertyFeatureKey propertyFeatureKey2 = new PropertyFeatureKey(property2,feature);
        PropertyFeature propertyFeature2 = new PropertyFeature(propertyFeatureKey2);

        jsonObj = gson.toJson(propertyFeature2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/savePropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Delete propertyFeature2
        jsonObj = gson.toJson(propertyFeature2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyFeature/deletePropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test there is only 1 propertyFeature left
        requestBuilder =  MockMvcRequestBuilders
                .get("/propertyFeature/getAllPropertyFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyFeature> propertyFeatureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyFeature>>() {}.getType());
        assert(propertyFeatureList.size() == 1);

        //Test the propertyFeature has been deleted from property2
        jsonObj = gson.toJson(property2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        savedProperty = propertyList.get(0);
        assert(savedProperty.getPropertyFeatures().size() == 0);

        //Test the propertyFeature has been deleted from feature
        jsonObj = gson.toJson(feature);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        savedFeature = featureList.get(0);
        assert(savedFeature.getPropertyFeatures().size() == 1);

    }

}
