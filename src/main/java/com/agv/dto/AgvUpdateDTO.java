package com.agv.dto;

import lombok.Data;

@Data
public class AgvUpdateDTO {

    private String status;
    private Integer battery;
    private String location;
}
