package pp.rsmmm.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pp.rsmmm.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByName(String name);

    public Optional<Member> findByName(String memberName);
}
