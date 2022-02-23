package cn.ve.thirdparty.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DataBody implements Serializable {
    private String value;
    private String color;
}