package com.guduo.wp.common.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by csonezp on 2017/2/13.
 */
@Data
@Entity
@Table
public class WpTest implements Serializable {

    private static final long serialVersionUID = 2351195184076653306L;
    @Id
    @GeneratedValue
    private int     id;
    private String name;
}
