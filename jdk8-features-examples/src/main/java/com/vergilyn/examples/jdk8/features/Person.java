package com.vergilyn.examples.jdk8.features;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author VergiLyn
 * @date 2019-07-05
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;
    private String name;
    private String type;
}
