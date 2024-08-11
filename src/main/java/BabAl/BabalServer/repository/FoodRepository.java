package BabAl.BabalServer.repository;

import BabAl.BabalServer.domain.Food;
import BabAl.BabalServer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    // 사용자 이메일, 날짜로 특정 날짜 식사 기록 조회
    List<Food> findAllByUserAndCreatedAt(User user, LocalDate date);

    // 사용자에 해당하는 식사 기록 조회
    List<Food> findAllByUserAndCreatedAtBetween(User user, LocalDate startDate, LocalDate endDate);
}
