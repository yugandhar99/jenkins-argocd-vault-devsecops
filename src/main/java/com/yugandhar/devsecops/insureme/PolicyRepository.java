package com.yugandhar.devsecops.insureme;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy,Integer>{

}
