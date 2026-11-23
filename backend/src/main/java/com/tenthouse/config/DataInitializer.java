package com.tenthouse.config;

import com.tenthouse.entity.BusinessDetails;
import com.tenthouse.entity.User;
import com.tenthouse.repository.BusinessDetailsRepository;
import com.tenthouse.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepo;
    private final BusinessDetailsRepository bizRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepo, BusinessDetailsRepository bizRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.bizRepo = bizRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        userRepo.findByUsername("admin").ifPresentOrElse(admin -> {
            if (!encoder.matches("Admin@123", admin.getPasswordHash())
                    && admin.getPasswordHash().startsWith("$2a$10$N9qo8uLOickgx2ZMRZoMye")) {
                admin.setPasswordHash(encoder.encode("Admin@123"));
                userRepo.save(admin);
            }
        }, () -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@tenthouse.local");
            admin.setFullName("Srinivasa Rao & Raghu Sai");
            admin.setPasswordHash(encoder.encode("Admin@123"));
            admin.setRole(User.Role.ADMIN);
            admin.setIsActive(true);
            userRepo.save(admin);
        });

        if (bizRepo.count() == 0) {
            BusinessDetails bd = new BusinessDetails();
            bd.setBusinessName("SRIAKAMATALLI TENT HOUSE");
            bd.setTagline("Making Every Celebration Special");
            bd.setOwnerNames("Srinivasa Rao & Raghu Sai");
            bd.setPrimaryMobile("9704993279");
            bd.setSecondaryMobile("6300675329");
            bd.setWhatsappPrimary("919704993279");
            bd.setWhatsappSecondary("916300675329");
            bd.setAddress("Chodavaram");
            bd.setCity("Vijayawada");
            bd.setState("Andhra Pradesh");
            bizRepo.save(bd);
        }
    }
}