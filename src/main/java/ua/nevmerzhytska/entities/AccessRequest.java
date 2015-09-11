package ua.nevmerzhytska.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nevmerzhytska.converters.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCESS_REQUESTS", indexes = {@Index(columnList = "IP")})
@Data
@NoArgsConstructor
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
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime accessTime;

    public AccessRequest(String ip, LocalDateTime accessTime) {
        this.ip = ip;
        this.accessTime = accessTime;
    }
}
