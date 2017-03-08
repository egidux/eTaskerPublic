package org.eTasker.repository;

import org.eTasker.model.Responsible_person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsiblePersonRepository extends JpaRepository<Responsible_person, Long> {
}
