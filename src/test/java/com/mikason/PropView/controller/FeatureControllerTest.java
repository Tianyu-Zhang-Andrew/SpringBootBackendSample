package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
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
public class FeatureControllerTest {
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
                .post("/feature/deleteAllFeature")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddNewFeature () throws Exception{
        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        Gson gson = new Gson();
        String jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        String featureObjJson =result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        assert(savedFeature.getCode().equals("code1"));
        assert(savedFeature.getDescription().equals("des1"));
    }

    @Test
    public void testEditFeature() throws Exception {
        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        Gson gson = new Gson();
        String jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Edit the feature
        feature.setCode("code2");

        jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/editFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test there is only 1 feature
        requestBuilder = MockMvcRequestBuilders
                .get("/feature/getAllFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.size() == 1);
        assert(featureList.get(0).getId().equals(featureId));
        assert(featureList.get(0).getCode().equals("code2"));
        assert(featureList.get(0).getDescription().equals("des1"));
    }

    @Test
    public void testDeleteFeature() throws Exception{
        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        Gson gson = new Gson();
        String jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
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

        //Get the feature's id
        String featureObjJson2 = result.getResponse().getContentAsString();
        Feature savedFeature2 = gson.fromJson(featureObjJson2, Feature.class);
        long featureId2 = savedFeature2.getId();
        feature2.setId(featureId2);

        //Delete feature2
        jsonObj = gson.toJson(feature2);
        requestBuilder = MockMvcRequestBuilders
                .post("/feature/deleteFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //There should only be 1 feature left
        requestBuilder = MockMvcRequestBuilders
                .get("/feature/getAllFeature")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.size() == 1);
        assert(featureList.get(0).getId().equals(featureId));
        assert(featureList.get(0).getCode().equals("code1"));
        assert(featureList.get(0).getDescription().equals("des1"));
    }

    @Test
    public void testSearchFeature() throws Exception{
        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Save the feature
        Gson gson = new Gson();
        String jsonObj = gson.toJson(feature);

        log.info (jsonObj);
        RequestBuilder requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Create another feature
        Feature feature2 = new Feature("code1", "des2");

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

        //Get the feature's id
        String featureObjJson2 = result.getResponse().getContentAsString();
        Feature savedFeature2 = gson.fromJson(featureObjJson2, Feature.class);
        long featureId2 = savedFeature2.getId();
        feature2.setId(featureId2);

        //Create another feature
        Feature feature3 = new Feature("code2", "des2");

        //Save the feature
        jsonObj = gson.toJson(feature3);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/saveFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the feature's id
        String featureObjJson3 = result.getResponse().getContentAsString();
        Feature savedFeature3 = gson.fromJson(featureObjJson3, Feature.class);
        long featureId3 = savedFeature3.getId();
        feature3.setId(featureId3);

        //Search feature with code "code1"
        Feature exampleFeature = new Feature();
        exampleFeature.setCode("code1");

        jsonObj = gson.toJson(exampleFeature);
        log.info (jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

         result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

         //There should be 2 result (feature and feature2)
        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.size() == 2);
    }
}
