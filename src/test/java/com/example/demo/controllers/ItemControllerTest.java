package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByIdTest(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(fakeItem()));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fakeItem(), response.getBody());
    }

    @Test
    public void getItemByNameTest(){
        List<Item> items = new ArrayList<>();
        items.add(fakeItem());
        when(itemRepository.findByName("Fake Item")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Fake Item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items, response.getBody());
    }

    private Item fakeItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Fake Item");
        item.setDescription("This is a fake");
        item.setPrice(BigDecimal.valueOf(10.0));

        return item;
    }

}
