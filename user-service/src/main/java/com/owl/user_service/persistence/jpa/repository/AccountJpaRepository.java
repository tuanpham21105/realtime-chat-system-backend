package com.owl.user_service.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.owl.user_service.persistence.jpa.entity.Account;

public interface AccountJpaRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account>
{
    // save(account)
    // findById(id)
    // findAll()
    // deleteById(id)
    // existsById(id)
    // count()

    //findByIdContainingIgnoreCase
    public List<Account> findByIdContainingIgnoreCase(String keywords);

    //findByUsername
    public Account findByUsername(String username);

    //findByUsernameContainingIgnoreCase
    public List<Account> findByUsernameContainingIgnoreCase(String keywords);

    //findFirstByOrderByIdDesc
    public Account findFirstByOrderByIdDesc();

    //updateStatusById
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.status = :status WHERE a.id = :id") 
    public void updateStatusById(@Param("id") String id, @Param("status") Boolean status);

    //updateUpdatedDateById
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.updatedDate = CURRENT_TIMESTAMP WHERE a.id = :id")
    public void updateUpdatedDateById(@Param("id") String id);
}
