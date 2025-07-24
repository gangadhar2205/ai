package com.mortgages.ai.authentication.repository;

import com.mortgages.ai.authentication.request.UserReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReqRepository extends JpaRepository<UserReq, String> {

    public UserReq findByUserId(String userId);

    public UserReq findByUserName(String userName);
}
