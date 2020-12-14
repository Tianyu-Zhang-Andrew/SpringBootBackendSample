package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.Agent;
import com.mikason.PropView.dataaccess.estateEntity.Office;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.commercialEntity.Sale;
import com.mikason.PropView.dataaccess.compositeKey.SaleKey;
import com.mikason.PropView.dataaccess.documentEntity.SaleDocument;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SaleDocumentControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void clear() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
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
    public void testAddSaleDocument()throws Exception{
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

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Create a new saleDocument
        SaleDocument saleDocument = new SaleDocument(sale, "Link1");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument = gson.fromJson(saleDocumentObjJson, SaleDocument.class);
        Long saleDocumentId = savedSaleDocument.getId();
        saleDocument.setId(saleDocumentId);

        //Create another new saleDocument
        SaleDocument saleDocument2 = new SaleDocument(sale, "Link2");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson2 = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument2 = gson.fromJson(saleDocumentObjJson2, SaleDocument.class);
        Long saleDocumentId2 = savedSaleDocument2.getId();
        saleDocument2.setId(saleDocumentId2);

        //Test there is 2 saleDocuments saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/saleDocument/getAllSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<SaleDocument> propertySaleDocumentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<SaleDocument>>() {}.getType());
        assert(propertySaleDocumentList.size() == 2);

        //Check the change has been made in propertySale
        PropertySale examplePropertySale = new PropertySale();
        examplePropertySale.setId(propertySale.getId());
        jsonObj = gson.toJson(examplePropertySale);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        for (Sale nextSale : propertySaleList.get(0).getSales()) {
            assert(nextSale.getSaleDocuments().size() == 2);
        }
    }

    @Test
    public void testEditSaleDocument() throws Exception{
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
        Agent agent = new Agent(agentPerson,office);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Create another sale
        String date2 = "2018-05-2";
        Sale sale2 = new Sale(date2, agent, client, propertySale, 1000000);

        //Save the Sale
        jsonObj = gson.toJson(sale2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson2 = result.getResponse().getContentAsString();
        Sale savedSale2 = gson.fromJson(saleObjJson2, Sale.class);
        SaleKey saleKey2 = savedSale2.getSaleKey();
        sale2.setSaleKey(saleKey2);

        //Create a new saleDocument
        SaleDocument saleDocument = new SaleDocument(sale, "Link1");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument = gson.fromJson(saleDocumentObjJson, SaleDocument.class);
        Long saleDocumentId = savedSaleDocument.getId();
        saleDocument.setId(saleDocumentId);

        //Edit the saleDocument
        saleDocument.setSale(sale2);
        saleDocument.setContent("Link2");

        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/saleDocument/editSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Check there is only one saleDocument
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/saleDocument/getAllSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<SaleDocument> saleDocumentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<SaleDocument>>() {}.getType());
        assert(saleDocumentList.size() == 1);
        assert(saleDocumentList.get(0).getId().equals(saleDocumentId));
        assert(saleDocumentList.get(0).getContent().equals("Link2"));
        assert(saleDocumentList.get(0).getSale().getSalePrice() == 1000000);
    }

    @Test
    public void testSearchSaleDocument() throws Exception{
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
        Agent agent = new Agent(agentPerson,office);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Create a new saleDocument
        SaleDocument saleDocument = new SaleDocument(sale, "Link1");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument = gson.fromJson(saleDocumentObjJson, SaleDocument.class);
        Long saleDocumentId = savedSaleDocument.getId();
        saleDocument.setId(saleDocumentId);

        //Create another new saleDocument
        SaleDocument saleDocument2 = new SaleDocument(sale, "Link2");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson2 = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument2 = gson.fromJson(saleDocumentObjJson2, SaleDocument.class);
        Long saleDocumentId2 = savedSaleDocument2.getId();
        saleDocument2.setId(saleDocumentId2);

        //Search saleDocument with content "Link2"
        SaleDocument exampleSaleDocument = new SaleDocument();
        exampleSaleDocument.setContent("Link2");

        jsonObj = gson.toJson(exampleSaleDocument);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/searchSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should have one result
        List<SaleDocument> saleDocumentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<SaleDocument>>() {}.getType());
        assert(saleDocumentList.size() == 1);
        assert(saleDocumentList.get(0).getId().equals(saleDocumentId2));
    }

    @Test
    public void testDeleteSaleDocument() throws Exception{
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
        Agent agent = new Agent(agentPerson,office);

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Create a new sale
        String date = "2019-07-12";
        Sale sale = new Sale(date, agent, client, propertySale, 1100000);

        //Save the Sale
        jsonObj = gson.toJson(sale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/sale/saveSale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the sale id
        String saleObjJson = result.getResponse().getContentAsString();
        Sale savedSale = gson.fromJson(saleObjJson, Sale.class);
        SaleKey saleKey = savedSale.getSaleKey();
        sale.setSaleKey(saleKey);

        //Create a new saleDocument
        SaleDocument saleDocument = new SaleDocument(sale, "Link1");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument = gson.fromJson(saleDocumentObjJson, SaleDocument.class);
        Long saleDocumentId = savedSaleDocument.getId();
        saleDocument.setId(saleDocumentId);

        //Create another new saleDocument
        SaleDocument saleDocument2 = new SaleDocument(sale, "Link2");

        //Save the saleDocument
        jsonObj = gson.toJson(saleDocument2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/saleDocument/saveSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get saleDocument's id
        String saleDocumentObjJson2 = result.getResponse().getContentAsString();
        SaleDocument savedSaleDocument2 = gson.fromJson(saleDocumentObjJson2, SaleDocument.class);
        Long saleDocumentId2 = savedSaleDocument2.getId();
        saleDocument2.setId(saleDocumentId2);

        //Test there is 2 saleDocuments saved
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/saleDocument/getAllSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<SaleDocument> propertySaleDocumentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<SaleDocument>>() {}.getType());
        assert(propertySaleDocumentList.size() == 2);

        //Delete saleDocument
        jsonObj = gson.toJson(saleDocument);
        log.info(jsonObj);
        requestBuilder = MockMvcRequestBuilders
                .post("/saleDocument/deleteSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Should only has 1 saleDocument left (saleDocument2)
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .get("/saleDocument/getAllSaleDocument")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        propertySaleDocumentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<SaleDocument>>() {}.getType());
        assert(propertySaleDocumentList.size() == 1);
        System.out.println(propertySaleDocumentList.get(0));
        assert(propertySaleDocumentList.get(0).getId().equals(saleDocumentId2));
    }
}
