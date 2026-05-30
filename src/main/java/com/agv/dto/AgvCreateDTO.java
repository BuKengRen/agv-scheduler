package com.agv.dto;

import lombok.Data;

@Data
public class AgvCreateDTO {
    private String name;
    private Integer battery;
    private String status;
    private String location;
}
