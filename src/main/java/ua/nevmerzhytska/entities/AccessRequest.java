package ua.nevmerzhytska.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCESS_REQUESTS", indexes = {@Index(columnList = "IP")})
@Data
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @NotNull
    @Column(name = "IP")
    private String ip;

    @NotNull
    @Column(name = "ACCESS_TIME")
    private LocalDateTime accessTime;
}
