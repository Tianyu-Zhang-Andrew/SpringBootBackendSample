package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.ClientCriteria;
import com.mikason.PropView.dataaccess.commercialEntity.Feature;
import com.mikason.PropView.dataaccess.compositeKey.ClientCriteriaKey;
import com.mikason.PropView.dataaccess.estateEntity.Address;
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
public class ClientCriteriaControllerTest {
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

        requestBuilder = MockMvcRequestBuilders
                .post("/client/deleteAllClient")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddNewClientCriteria() throws Exception{
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Create a clientCriteria
        ClientCriteriaKey clientCriteriaKey = new ClientCriteriaKey(client, feature);
        ClientCriteria clientCriteria = new ClientCriteria(clientCriteriaKey);

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

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Save the clientCriteria
        jsonObj = gson.toJson(clientCriteria);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/saveClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test there is 1 clientCriteria saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/clientCriteria/getAllClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<ClientCriteria> ownerClientCriteriaList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<ClientCriteria>>() {}.getType());
        assert(ownerClientCriteriaList.size() == 1);
        assert (ownerClientCriteriaList.get(0).getClientCriteriaKey().getFeature().getId().equals(featureId));
        assert(ownerClientCriteriaList.get(0).getClientCriteriaKey().getClient().getId().equals(clientId));

        //Test the clientCriteria has been saved in the client
        jsonObj = gson.toJson(client);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Client> clientList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        assert(clientList.get(0).getClientCriteria().size() == 1);

        //Test the clientCriteria has been saved in the feature
        jsonObj = gson.toJson(feature);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.get(0).getClientCriteria().size() == 1);
    }

    @Test
    public void testEditClientCriteria() throws Exception{
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

        //Create a clientCriteria
        ClientCriteriaKey clientCriteriaKey = new ClientCriteriaKey(client, feature);
        ClientCriteria clientCriteria = new ClientCriteria(clientCriteriaKey);

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

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Save the clientCriteria
        jsonObj = gson.toJson(clientCriteria);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/saveClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //edit the feature
        feature.setDescription("des2");

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

        //Test the change has been made in clientCriteria
        requestBuilder =  MockMvcRequestBuilders
                .get("/clientCriteria/getAllClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<ClientCriteria> ClientCriteriaList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<ClientCriteria>>() {}.getType());
        assert(ClientCriteriaList.get(0).getClientCriteriaKey().getFeature().getDescription().equals("des2"));

        //Create another feature
        Feature feature2 = new Feature("code2", "des3");

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

        //Edit the clientCriteria
        ClientCriteriaKey clientCriteriaKey2 = new ClientCriteriaKey(client, feature2);
        ClientCriteria clientCriteria2 = new ClientCriteria(clientCriteriaKey2);

        String clientCriteriaObj = gson.toJson(clientCriteria);
        String newClientCriteriaObj = gson.toJson(clientCriteria2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/editClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .param("oldClientCriteria", clientCriteriaObj)
                .param("newClientCriteria", newClientCriteriaObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test the change has been made
        requestBuilder =  MockMvcRequestBuilders
                .get("/clientCriteria/getAllClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        ClientCriteriaList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<ClientCriteria>>() {}.getType());
        assert(ClientCriteriaList.get(0).getClientCriteriaKey().getFeature().getId().equals(featureId2));

        //Test the clientCriteria has been deleted from feature
        jsonObj = gson.toJson(feature);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.get(0).getClientCriteria().size() == 0);

        //Test the clientCriteria has been added to feature2
        jsonObj = gson.toJson(feature2);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.get(0).getClientCriteria().size() == 1);
    }

    @Test
    public void testDeleteClientCriteria() throws  Exception{
        //Create a client
        Address clientAddress = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW","Australia", "2095");
        Person clientPerson = new Person(Title.Mr, "Bob", "L", "Smith", Gender.Male, "1998-6-23", "0451256728", "Bobsemail@outlook.com");
        Client client = new Client(clientAddress, clientPerson);

        //Create a feature
        Feature feature = new Feature("code1", "des1");

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

        //Get the feature's id
        String featureObjJson = result.getResponse().getContentAsString();
        Feature savedFeature = gson.fromJson(featureObjJson, Feature.class);
        long featureId = savedFeature.getId();
        feature.setId(featureId);

        //Create another feature
        Feature feature2 = new Feature("code2", "des3");

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

        //Create a clientCriteria
        ClientCriteriaKey clientCriteriaKey = new ClientCriteriaKey(client, feature);
        ClientCriteria clientCriteria = new ClientCriteria(clientCriteriaKey);

        //Save the clientCriteria
        jsonObj = gson.toJson(clientCriteria);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/saveClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another clientCriteria
        ClientCriteriaKey clientCriteriaKey2 = new ClientCriteriaKey(client, feature2);
        ClientCriteria clientCriteria2 = new ClientCriteria(clientCriteriaKey2);

        //Save the clientCriteria
        jsonObj = gson.toJson(clientCriteria2);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/saveClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Delete clientCriteria2
        jsonObj = gson.toJson(clientCriteria2);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/clientCriteria/deleteClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test there is only 1 clientCriteria left
        requestBuilder =  MockMvcRequestBuilders
                .get("/clientCriteria/getAllClientCriteria")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<ClientCriteria> clientCriteriaList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<ClientCriteria>>() {}.getType());
        assert(clientCriteriaList.size() == 1);

        //Test the clientCriteria has been deleted from feature2
        jsonObj = gson.toJson(feature2);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/feature/searchFeature")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> featureList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Feature>>() {}.getType());
        assert(featureList.get(0).getClientCriteria().size() == 0);

        //Test the clientCriteria has been deleted form client
        jsonObj = gson.toJson(client);
        log.info (jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/client/searchClient")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Client> clientList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Client>>() {}.getType());
        assert(clientList.get(0).getClientCriteria().size() == 1);
    }

}
