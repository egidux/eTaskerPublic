package org.eTasker.repository;

import org.eTasker.model.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<Object, Long> {
	
	Object findByName(String name);
}
