package com.stocxtune.api.service;

import com.stocxtune.api.model.stock.Asset;
import com.stocxtune.api.model.stock.StockSearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MyJsonMapper {

    ObjectMapper objectMapper;

    @Autowired
    public MyJsonMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public Asset getStockFromNode(JsonNode node) throws JsonProcessingException {
        return this.objectMapper.treeToValue(node, Asset.class);
    }

    public List<StockSearchResult> getAllFromNode(JsonNode node) throws JsonProcessingException {

        if (node.getNodeType() == JsonNodeType.ARRAY) {

            List<StockSearchResult> all = new ArrayList<>();
            Iterator<JsonNode> elements = node.elements();


            while (elements.hasNext())
                all.add(this.objectMapper.treeToValue(elements.next(), StockSearchResult.class));

            return all;
        } else
            throw new IllegalArgumentException("Node passed is not an JsonArray");
    }


}
