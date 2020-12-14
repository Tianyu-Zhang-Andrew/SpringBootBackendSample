package com.mikason.PropView.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikason.PropView.dataaccess.commercialEntity.PropertyRent;
import com.mikason.PropView.dataaccess.commercialEntity.PropertySale;
import com.mikason.PropView.dataaccess.documentEntity.MediaContent;
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
public class MediaContentControllerTest {

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
    public void testAddMediaContent() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a mediaContent
        MediaContent mediaContent = new MediaContent("Video", "Link1", propertySale, null);

        //Save the mediaContent
        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another mediaContent
        MediaContent mediaContent2 = new MediaContent("Video", "Link2", null, propertyRent);

        //Save another mediaContent
        jsonObj = gson.toJson(mediaContent2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Check the mediaContent has been saved
        requestBuilder =  MockMvcRequestBuilders
                .get("/mediaContent/getAllMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<MediaContent> mediaContentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<MediaContent>>() {}.getType());
        assert(mediaContentList.size() == 2);
    }

    @Test
    public void testEditMediaContent() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a mediaContent
        MediaContent mediaContent = new MediaContent("Video", "Link1", propertySale, null);

        //Save the mediaContent
        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the mediaContent's id
        String mediaContentObjJson = result.getResponse().getContentAsString();
        MediaContent savedMediaContent = gson.fromJson(mediaContentObjJson, MediaContent.class);
        long mediaContentId = savedMediaContent.getId();
        mediaContent.setId(mediaContentId);

        //Edit the mediaContent
        mediaContent.setPropertySale(null);
        mediaContent.setPropertyRent(propertyRent);
        mediaContent.setContent("Link2");

        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/editMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Check the mediaContent has been removed from the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/searchPropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertySale> propertySaleList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertySale>>() {}.getType());
        assert(propertySaleList.get(0).getSales().size() == 0);

        //Check the mediaContent has been added to the mediaRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/searchPropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyRent> propertyRentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<PropertyRent>>() {}.getType());
        assert(propertyRentList.get(0).getMediaContents().size() == 1);

        //Check the information on mediaContent has been changed
        requestBuilder =  MockMvcRequestBuilders
                .get("/mediaContent/getAllMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<MediaContent> mediaContentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<MediaContent>>() {}.getType());
        assert(mediaContentList.size() == 1);
        assert(mediaContentList.get(0).getPropertySale() == null);
        assert(mediaContentList.get(0).getPropertyRent() != null);
        assert(mediaContentList.get(0).getPropertyRent().getId() == propertyRentId);
        assert(mediaContentList.get(0).getContent().equals("Link2"));
    }

    @Test
    public void testSearchMediaContent() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create another property
        Address address2 = new Address("1a", "1", "Rialto Lane", "Avenue", "Manly", "NSW", "Australia", "2095");
        Property property2 = new Property(3, 2, 2, "Good unit", address2);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property2, 1000000);

        //Save property1
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

        //Save property2
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

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a mediaContent
        MediaContent mediaContent = new MediaContent("Video", "Link1", propertySale, null);

        //Save the mediaContent
        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another mediaContent
        MediaContent mediaContent2 = new MediaContent("Video", "Link2", null, propertyRent);

        //Save another mediaContent
        jsonObj = gson.toJson(mediaContent2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Create another mediaContent
        MediaContent mediaContent3 = new MediaContent("Video", "Link3", null, propertyRent);

        //Save another mediaContent
        jsonObj = gson.toJson(mediaContent3);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Search all mediaContent for the propertyRent
        MediaContent mediaContentExample = new MediaContent();
        mediaContentExample.setPropertyRent(propertyRent);

        jsonObj = gson.toJson(mediaContentExample);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/searchMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        //Should have 2 result
        List<MediaContent> mediaContentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<MediaContent>>() {}.getType());
        assert(mediaContentList.size() == 2);
    }

    @Test
    public void testDeleteMediaContent() throws Exception{
        //Create a property
        Address propertyAddress = new Address("2b", "3", "Linked Road", "Avenue", "Parramatta", "NSW", "Australia", "2017");
        Property property = new Property(3, 1, 1, "Not bad unit", propertyAddress);

        //Create a propertyRent
        PropertyRent propertyRent = new PropertyRent(property, 970, "2019-10-1", "2020-2-1");

        //Create a propertySale
        PropertySale propertySale = new PropertySale(property, 1000000);

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

        //Save the propertyRent
        jsonObj = gson.toJson(propertyRent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertyRent/savePropertyRent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertyRent's id
        String propertyRentObjJson = result.getResponse().getContentAsString();
        PropertyRent savedPropertyRent = gson.fromJson(propertyRentObjJson, PropertyRent.class);
        long propertyRentId = savedPropertyRent.getId();
        propertyRent.setId(propertyRentId);

        //Save the propertySale
        jsonObj = gson.toJson(propertySale);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/propertySale/savePropertySale")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get new propertySale's id
        String propertySaleObjJson = result.getResponse().getContentAsString();
        PropertySale savedPropertySale = gson.fromJson(propertySaleObjJson, PropertySale.class);
        long propertySaleId = savedPropertySale.getId();
        propertySale.setId(propertySaleId);

        //Create a mediaContent
        MediaContent mediaContent = new MediaContent("Video", "Link1", propertySale, null);

        //Save the mediaContent
        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the mediaContent's id
        String mediaContentObjJson = result.getResponse().getContentAsString();
        MediaContent savedMediaContent = gson.fromJson(mediaContentObjJson, MediaContent.class);
        long mediaContentId = savedMediaContent.getId();
        mediaContent.setId(mediaContentId);

        //Create another mediaContent
        MediaContent mediaContent2 = new MediaContent("Video", "Link2", null, propertyRent);

        //Save another mediaContent
        jsonObj = gson.toJson(mediaContent2);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/saveMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        //Get the mediaContent's id
        String mediaContentObjJson2 = result.getResponse().getContentAsString();
        MediaContent savedMediaContent2 = gson.fromJson(mediaContentObjJson2, MediaContent.class);
        long mediaContentId2 = savedMediaContent2.getId();
        mediaContent2.setId(mediaContentId2);

        //Check the there are 2 mediaContents
        requestBuilder =  MockMvcRequestBuilders
                .get("/mediaContent/getAllMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        List<MediaContent> mediaContentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<MediaContent>>() {}.getType());
        assert(mediaContentList.size() == 2);

        //Delete mediaContent1
        jsonObj = gson.toJson(mediaContent);
        log.info(jsonObj);
        requestBuilder =  MockMvcRequestBuilders
                .post("/mediaContent/deleteMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonObj)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        //There is only 1 mediaContents left
        requestBuilder =  MockMvcRequestBuilders
                .get("/mediaContent/getAllMediaContent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        mediaContentList = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<MediaContent>>() {}.getType());
        assert(mediaContentList.size() == 1);
    }
}
