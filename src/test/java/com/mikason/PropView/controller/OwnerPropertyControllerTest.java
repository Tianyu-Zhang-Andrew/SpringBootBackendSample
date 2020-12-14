package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.OwnerProperty;
import com.mikason.PropView.dataaccess.compositeKey.OwnerPropertyKey;
import com.mikason.PropView.dataaccess.estateEntity.Address;
import com.mikason.PropView.dataaccess.estateEntity.Property;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OwnerPropertyControllerTest {
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
                .post("/ownerProperty/deleteAllOwnerProperty")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
                .post("/property/deleteAllProperty")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        requestBuilder = MockMvcRequestBuilders
                .post("/owner/deleteAllOwner")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddOwnerProperty()throws Exception{
        //Create a owner
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

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

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

        //Create a ownerProperty
        OwnerPropertyKey ownerPropertyKey = new OwnerPropertyKey(owner, property);
        OwnerProperty ownerProperty = new OwnerProperty(ownerPropertyKey);

        jsonObj = gson.toJson(ownerProperty);

        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/saveOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new ownerProperty's id
        String ownerPropertyObjJson = result.getResponse().getContentAsString();
        OwnerProperty savedOwnerProperty = gson.fromJson(ownerPropertyObjJson, OwnerProperty.class);
        OwnerPropertyKey savedOwnerPropertyKey = savedOwnerProperty.getOwnerPropertyKey();
        ownerProperty.setOwnerPropertyKey(savedOwnerPropertyKey);

        //Test there is 1 ownerProperty saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/ownerProperty/getAllOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<OwnerProperty> ownerPropertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<OwnerProperty>>() {}.getType());
        assert(ownerPropertyList.size() == 1);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getProperty().getId() == propertyId);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getOwner().getId() == ownerId);

        //Test the ownerProperty has been saved in the property
        jsonObj = gson.toJson(property);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.get(0).getOwnerProperties().size() == 1);

        //Test the ownerProperty has been saved in the owner
        jsonObj = gson.toJson(owner);
        requestBuilder = MockMvcRequestBuilders
                .post("/owner/searchOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Owner> ownerList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Owner>>() {}.getType());
        assert(ownerList.get(0).getOwnerProperties().size() == 1);
    }

    @Test
    public void testEditOwnerProperty() throws Exception{
        //Create a owner
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

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

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

        //Create another property
        Address address2 = new Address("3c", "5", "Parramatta street", "Avenue", "Parramatta", "NSW", "Australia", "2041");
        Property property2 = new Property(1, 1, 1, "Poor unit", address2);

        String jsonObj2 = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj2)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a ownerProperty
        OwnerPropertyKey ownerPropertyKey = new OwnerPropertyKey(owner, property);
        OwnerProperty ownerProperty = new OwnerProperty(ownerPropertyKey);

        jsonObj = gson.toJson(ownerProperty);

        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/saveOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new ownerProperty's id
        String ownerPropertyObjJson = result.getResponse().getContentAsString();
        OwnerProperty savedOwnerProperty = gson.fromJson(ownerPropertyObjJson, OwnerProperty.class);
        OwnerPropertyKey savedOwnerPropertyKey = savedOwnerProperty.getOwnerPropertyKey();
        ownerProperty.setOwnerPropertyKey(savedOwnerPropertyKey);

        //Edit the ownerProperty
        OwnerPropertyKey ownerPropertyKey2 = new OwnerPropertyKey(owner, property2);
        OwnerProperty newOwnerProperty = new OwnerProperty(ownerPropertyKey2);

        String ownerPropertyObj = gson.toJson(ownerProperty);
        String newOwnerPropertyObj = gson.toJson(newOwnerProperty);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/editOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .param("oldOwnerProperty", ownerPropertyObj)
                .param("newOwnerProperty", newOwnerPropertyObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Test there is 1 ownerProperty saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/ownerProperty/getAllOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<OwnerProperty> ownerPropertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<OwnerProperty>>() {}.getType());
        assert(ownerPropertyList.size() == 1);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getProperty().getId() == propertyId2);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getOwner().getId() == ownerId);

        //Test the ownerProperty has been deleted from property
        jsonObj = gson.toJson(property);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.get(0).getOwnerProperties().size() == 0);

        //Test the ownerProperty has been saved in property2
        jsonObj = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.get(0).getOwnerProperties().size() == 1);
    }

    @Test
    public void testDeleteOwnerProperty() throws Exception{
        //Create a owner
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

        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

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

        //Create another property
        Address address2 = new Address("3c", "5", "Parramatta street", "Avenue", "Parramatta", "NSW", "Australia", "2041");
        Property property2 = new Property(1, 1, 1, "Poor unit", address2);

        String jsonObj2 = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/saveProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj2)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new property's id
        String propertyObjJson2 = result.getResponse().getContentAsString();
        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
        long propertyId2 = savedProperty2.getId();
        property2.setId(propertyId2);

        //Create a ownerProperty
        OwnerPropertyKey ownerPropertyKey = new OwnerPropertyKey(owner, property);
        OwnerProperty ownerProperty = new OwnerProperty(ownerPropertyKey);

        jsonObj = gson.toJson(ownerProperty);

        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/saveOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new ownerProperty's id
        String ownerPropertyObjJson = result.getResponse().getContentAsString();
        OwnerProperty savedOwnerProperty = gson.fromJson(ownerPropertyObjJson, OwnerProperty.class);
        OwnerPropertyKey savedOwnerPropertyKey = savedOwnerProperty.getOwnerPropertyKey();
        ownerProperty.setOwnerPropertyKey(savedOwnerPropertyKey);

        //Create another ownerProperty
        OwnerPropertyKey ownerPropertyKey2 = new OwnerPropertyKey(owner, property2);
        OwnerProperty ownerProperty2 = new OwnerProperty(ownerPropertyKey2);

        jsonObj = gson.toJson(ownerProperty2);

        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/saveOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new ownerProperty's id
        String ownerPropertyObjJson2 = result.getResponse().getContentAsString();
        OwnerProperty savedOwnerProperty2 = gson.fromJson(ownerPropertyObjJson2, OwnerProperty.class);
        OwnerPropertyKey savedOwnerPropertyKey2 = savedOwnerProperty2.getOwnerPropertyKey();
        ownerProperty2.setOwnerPropertyKey(savedOwnerPropertyKey2);

        //Delete the second ownerProperty
        jsonObj = gson.toJson(ownerProperty2);

        requestBuilder =  MockMvcRequestBuilders
                .post("/ownerProperty/deleteOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //Test there is only 1 ownerProperty left, and it's the first ownerProperty
        requestBuilder =  MockMvcRequestBuilders
                .get("/ownerProperty/getAllOwnerProperty")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<OwnerProperty> ownerPropertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<OwnerProperty>>() {}.getType());
        assert(ownerPropertyList.size() == 1);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getProperty().getId() == propertyId);
        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getOwner().getId() == ownerId);

        //Test the ownerProperty has been deleted from property2
        jsonObj = gson.toJson(property2);
        requestBuilder = MockMvcRequestBuilders
                .post("/property/searchProperty")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Property> propList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Property>>() {}.getType());
        assert(propList.get(0).getOwnerProperties().size() == 0);

        //Test the ownerProperty has been deleted form owner
        jsonObj = gson.toJson(owner);
        requestBuilder = MockMvcRequestBuilders
                .post("/owner/searchOwner")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<Owner> ownerList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Owner>>() {}.getType());
        assert(ownerList.get(0).getOwnerProperties().size() == 1);
    }

//    @Test
//    public void testSearchOwnerProperty() throws Exception{
//        //Create a owner
//        Person person = new Person(Title.Mr, "Puff", "M", "Dragon", Gender.Male, "1991-3-25", "045115328", "Puffsemail@outlook.com");
//        Owner owner = new Owner("2019-10-12", person);
//
//        //Save the owner
//        Gson gson = new Gson();
//        String jsonObj = gson.toJson(owner);
//        log.info(jsonObj);
//        RequestBuilder requestBuilder =  MockMvcRequestBuilders
//                .post("/owner/saveOwner")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get the new owner's id
//        String ownerObjJson =result.getResponse().getContentAsString();
//        Owner savedOwner = gson.fromJson(ownerObjJson, Owner.class);
//        long ownerId = savedOwner.getId();
//        owner.setId(ownerId);
//
//        //Create a property
//        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
//        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);
//
//        //Save the property
//        jsonObj = gson.toJson(property);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/property/saveProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new property's id
//        String propertyObjJson = result.getResponse().getContentAsString();
//        Property savedProperty = gson.fromJson(propertyObjJson, Property.class);
//        long propertyId = savedProperty.getId();
//        property.setId(propertyId);
//
//        //Create another property
//        Address address2 = new Address("3c", "5", "Parramatta street", "Avenue", "Parramatta", "NSW", "Australia", "2041");
//        Property property2 = new Property(1, 1, 1, "Poor unit", address2);
//
//        String jsonObj2 = gson.toJson(property2);
//        requestBuilder = MockMvcRequestBuilders
//                .post("/property/saveProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj2)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new property's id
//        String propertyObjJson2 = result.getResponse().getContentAsString();
//        Property savedProperty2 = gson.fromJson(propertyObjJson2, Property.class);
//        long propertyId2 = savedProperty2.getId();
//        property2.setId(propertyId2);
//
//        //Create a ownerProperty
//        OwnerPropertyKey ownerPropertyKey = new OwnerPropertyKey(owner, property);
//        OwnerProperty ownerProperty = new OwnerProperty(ownerPropertyKey);
//
//        jsonObj = gson.toJson(ownerProperty);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/ownerProperty/saveOwnerProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new ownerProperty's id
//        String ownerPropertyObjJson = result.getResponse().getContentAsString();
//        OwnerProperty savedOwnerProperty = gson.fromJson(ownerPropertyObjJson, OwnerProperty.class);
//        OwnerPropertyKey savedOwnerPropertyKey = savedOwnerProperty.getOwnerPropertyKey();
//        ownerProperty.setOwnerPropertyKey(savedOwnerPropertyKey);
//
//        //Create another ownerProperty
//        OwnerPropertyKey ownerPropertyKey2 = new OwnerPropertyKey(owner, property2);
//        OwnerProperty ownerProperty2 = new OwnerProperty(ownerPropertyKey2);
//
//        jsonObj = gson.toJson(ownerProperty2);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/ownerProperty/saveOwnerProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //Get new ownerProperty's id
//        String ownerPropertyObjJson2 = result.getResponse().getContentAsString();
//        OwnerProperty savedOwnerProperty2 = gson.fromJson(ownerPropertyObjJson2, OwnerProperty.class);
//        OwnerPropertyKey savedOwnerPropertyKey2 = savedOwnerProperty2.getOwnerPropertyKey();
//        ownerProperty2.setOwnerPropertyKey(savedOwnerPropertyKey2);
//
//        //Search ownerProperty with property2
//        OwnerPropertyKey exampleOwnerPropertyKey = new OwnerPropertyKey();
//        exampleOwnerPropertyKey.setProperty(property2);
//        OwnerProperty exampleOwnerProperty = new OwnerProperty();
//        exampleOwnerProperty.setOwnerPropertyKey(exampleOwnerPropertyKey);
//
//        jsonObj = gson.toJson(ownerProperty2);
//
//        requestBuilder =  MockMvcRequestBuilders
//                .post("/ownerProperty/searchOwnerProperty")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonObj)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //There should be only 1 result
//        List<OwnerProperty> ownerPropertyList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<OwnerProperty>>() {}.getType());
//        assert(ownerPropertyList.size() == 1);
//        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getProperty().getId() == propertyId2);
//        assert(ownerPropertyList.get(0).getOwnerPropertyKey().getOwner().getId() == ownerId);
//    }
}
