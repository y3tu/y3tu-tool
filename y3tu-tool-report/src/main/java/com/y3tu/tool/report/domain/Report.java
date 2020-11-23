package com.y3tu.tool.report.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author y3tu
 */
@Entity
@Table(name = "report")
@Data
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


}
