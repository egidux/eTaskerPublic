package org.eTasker.repository;

import org.eTasker.model.Responsibleperson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsiblePersonRepository extends JpaRepository<Responsibleperson, Long> {
}
