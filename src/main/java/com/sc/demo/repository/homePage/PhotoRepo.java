package com.sc.demo.repository.homePage;

import com.sc.demo.model.homePage.HomePagePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepo extends JpaRepository<HomePagePhoto, Long> {
}
