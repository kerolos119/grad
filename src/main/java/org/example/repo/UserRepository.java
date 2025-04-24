package org.example.repo;

import org.example.document.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {


    //البحث عن المستخدم بواسطة البريد الاليكتروني
    Users findByEmail(String email);
    //التحقق من وجود البريد اليكتروني
// تحقق من وجود الإيميل فقط
    boolean existsByEmail(String email);


    // التحقق من وجود البريد الإلكتروني واسم المستخدم
    boolean existsByEmailAndUsername(String email, String username);


    Users findByUsername(String username);

    boolean existsByUsername(String username);

    Page<Users> findAll(Specification<Users> spec, Pageable pageable);



    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Users u " +
            "WHERE u.email = :email " +
            "AND u.userId = :userId " +
            "AND :role IN (SELECT r FROM u.roles r)")
    boolean existsUserWithCredentials(
            @Param("email") String email,
            @Param("userId") Long userId,
            @Param("role") String role
    );
}
