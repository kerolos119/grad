package org.example.repo;

import org.example.document.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {


    //البحث عن المستخدم بواسطة البريد الاليكتروني
    Users findByEmail(String email);
    //التحقق من وجود البريد اليكتروني
    boolean existsByEmail(String email); // لاحظ إضافة حرف s في exists

    Users findByUserName(String userName);
    boolean existsByUserName(String userName);
    Page<Users> findAll(Specification<Users> spec, Pageable pageable);
}
