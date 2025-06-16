package com.handicrafts.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handicrafts.dto.OrderDetailDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for transferring data between different layers of the application
 * @param <T> The type of object to be transferred
 */
@Component
public class TransferDataUtil<T> {
    // Builder serializeNulls để parse cả các trường null
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * Convert an object to JSON string
     * @param object The object to be converted (can be a single model or a list)
     * @return JSON representation of the object
     */
    public String toJson(Object object) {
        return (object instanceof List) ? toJsonByList((List<T>) object) : toJsonByModel((T) object);
    }

    /**
     * Convert a single model to JSON string
     * @param model The model to be converted
     * @return JSON representation of the model
     */
    private String toJsonByModel(T model) {
        return gson.toJson(model);
    }

    /**
     * Convert a list of models to JSON string
     * @param list The list to be converted
     * @return JSON representation of the list
     */
    private String toJsonByList(List<T> list) {
        return gson.toJson(list);
    }

    // Example usage (can be moved to a test class in Spring Boot)
    public static void main(String[] args) {
        List<OrderDetailDTO> list = new ArrayList<>();
        list.add(new OrderDetailDTO());
        list.add(new OrderDetailDTO());

        // Test với List<OrderDetailDTO> (List<T>)
        System.out.println(new TransferDataUtil<OrderDetailDTO>().toJson(list));

        // Test với OrderDetailDTO (T)
        System.out.println(new TransferDataUtil<OrderDetailDTO>().toJson(new OrderDetailDTO()));
    }
}
