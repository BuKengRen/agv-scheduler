package com.agv.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Agv implements Serializable {

    private Integer id;
    private String name;
    private String status;
    private Integer battery;
    private String location;
}
