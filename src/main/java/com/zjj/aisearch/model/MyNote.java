package com.zjj.aisearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: myapp_search
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021年3月04日 17:26:01 星期四
 **/
@Data
@Entity
public class MyNote {
    @Id
    @SequenceGenerator(name = "MyNote_Sequence", sequenceName = "MyNote_Sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MyNote_Sequence")
    private Integer id;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;
}
