package ua.nevmerzhytska.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.nevmerzhytska.entities.AccessRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessRequestRepository extends CrudRepository<AccessRequest, Integer> {

    List<AccessRequest> findByIpAndAccessTimeBetween(String ip, LocalDateTime start, LocalDateTime end);
}
