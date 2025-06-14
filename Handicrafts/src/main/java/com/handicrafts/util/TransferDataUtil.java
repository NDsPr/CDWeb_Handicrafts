package com.handicrafts.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltw.bean.OrderDetailBean;

import java.util.ArrayList;
import java.util.List;

public class TransferDataUtil<T> {
    // Builder serializeNulls để parse cả các trường null
    Gson gson = new GsonBuilder().serializeNulls().create();

    // Kiểm tra xem param truyền vào là model (T) hay một list của model đó (List<T>)
    public String toJson(Object object) {
        return (object instanceof List) ? toJsonByList((List<T>) object) : toJsonByModel((T) object);
    }

    private String toJsonByModel(T model) {
        return gson.toJson(model);
    }

    private String toJsonByList(List<T> list) {
        return gson.toJson(list);
    }

    public static void main(String[] args) {
        List<OrderDetailBean> list = new ArrayList<>();
        list.add(new OrderDetailBean());
        list.add(new OrderDetailBean());

        // Test với List<OrderDetailBean> (List<T>)
        System.out.println(new TransferDataUtil<OrderDetailBean>().toJson(list));

        // Test với OrderDetailBean (T)
        System.out.println(new TransferDataUtil<OrderDetailBean>().toJson(new OrderDetailBean()));
    }
}
