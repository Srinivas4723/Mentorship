package com.pcs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcs.enums.Role;
import com.pcs.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByIdAndRole(Long userid,Role role);

	List<User> findAllByRole(Role role);

	List<User> findAllByRoleOrRole(Role compensation, Role report);
	
	
}
