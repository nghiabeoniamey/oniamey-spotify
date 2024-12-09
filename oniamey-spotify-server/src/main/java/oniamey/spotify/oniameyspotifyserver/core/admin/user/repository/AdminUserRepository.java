package oniamey.spotify.oniameyspotifyserver.core.admin.user.repository;

import oniamey.spotify.oniameyspotifyserver.core.admin.user.model.request.AdminFindUserRequest;
import oniamey.spotify.oniameyspotifyserver.core.admin.user.model.response.AdminUserResponse;
import oniamey.spotify.oniameyspotifyserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminUserRepository extends UserRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY u.created_date DESC) AS catalog,
                u.id AS id,
                u.user_name AS name,
                u.email AS email,
                u.password_hash AS password,
                u.subscription_type AS subscriptionType,
                u.profile_picture AS profilePicture,
                u.role AS role,
                u.status AS status
            FROM
                user u
            WHERE
                (:#{#req.keyword} IS NULL OR
                u.user_name LIKE CONCAT('%', :#{#req.keyword}, '%') OR
                u.email LIKE CONCAT('%', :#{#req.keyword}, '%'))
            AND (:#{#req.status} IS NULL OR u.status = :#{#req.status})
            AND (:#{#req.role} IS NULL OR u.role = :#{#req.role})
            AND (:#{#req.subscriptionType} IS NULL OR u.subscription_type LIKE :#{#req.subscriptionType})
            """, countQuery = """
            SELECT
                COUNT(u.id) FROM user u
            WHERE
                (:#{#req.keyword} IS NULL OR
                u.user_name LIKE CONCAT('%', :#{#req.keyword}, '%') OR
                u.email LIKE CONCAT('%', :#{#req.keyword}, '%'))
            AND (:#{#req.status} IS NULL OR u.status = :#{#req.status})
            AND (:#{#req.role} IS NULL OR u.role = :#{#req.role})
            AND (:#{#req.subscriptionType} IS NULL OR u.subscription_type LIKE :#{#req.subscriptionType})
            """, nativeQuery = true)
    Page<AdminUserResponse> getUsers(Pageable pageable, @Param("req") AdminFindUserRequest req);

    @Query(value = """
                    SELECT
                        u.id AS id,
                        u.user_name AS name,
                        u.email AS email,
                        u.password_hash AS password,
                        u.subscription_type AS subscriptionType,
                        u.profile_picture AS profilePicture,
                        u.role AS role,
                        u.status AS status
                    FROM user u
            """, nativeQuery = true)
    AdminUserResponse findUserById(String id);

    boolean existsByEmail(String email);

}
