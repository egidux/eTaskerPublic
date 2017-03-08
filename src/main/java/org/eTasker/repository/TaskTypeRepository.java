package org.eTasker.repository;

import org.eTasker.model.Task_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<Task_type, Long> {

}
