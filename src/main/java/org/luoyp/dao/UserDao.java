package org.luoyp.dao;

import org.luoyp.StepUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<StepUser, String>
{
}
