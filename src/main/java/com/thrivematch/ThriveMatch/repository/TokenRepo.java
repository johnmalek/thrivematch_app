package com.thrivematch.ThriveMatch.repository;

import com.thrivematch.ThriveMatch.model.TokenEntity;
import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<TokenEntity, Integer> {

    @Query(value = """
      select t from TokenEntity t inner join t.user u
      where u.id = :id and (t.expired = false or t.revoked = false)
      """)
    List<TokenEntity> findAllValidTokensByUser(Integer id);

    Optional<TokenEntity> findByToken(String token);
}
