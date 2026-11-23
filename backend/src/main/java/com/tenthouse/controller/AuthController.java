package com.tenthouse.controller;

import com.tenthouse.dto.LoginRequest;
import com.tenthouse.entity.User;
import com.tenthouse.repository.UserRepository;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authMgr;
    private final SecurityContextRepository ctxRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authMgr, SecurityContextRepository ctxRepo, UserRepository userRepo, PasswordEncoder encoder) {
        this.authMgr = authMgr; this.ctxRepo = ctxRepo; this.userRepo = userRepo; this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletRequest hReq, HttpServletResponse hRes) {
        Authentication auth = authMgr.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        SecurityContext ctx = SecurityContextHolder.createEmptyContext(); ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx); ctxRepo.saveContext(ctx, hReq, hRes);
        User u = userRepo.findByUsername(req.getUsername()).orElseThrow();
        return ResponseEntity.ok(Map.of("username", u.getUsername(), "fullName", u.getFullName(), "role", u.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        User u = userRepo.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of("authenticated", true, "username", u.getUsername(), "fullName", u.getFullName(), "role", u.getRole().name()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePwd(@RequestBody Map<String, String> body, Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal()))
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        User u = userRepo.findByUsername(auth.getName()).orElseThrow();
        if (!encoder.matches(body.getOrDefault("currentPassword", ""), u.getPasswordHash()))
            return ResponseEntity.badRequest().body(Map.of("error", "Current password is incorrect"));
        String next = body.getOrDefault("newPassword", "");
        if (next.length() < 6) return ResponseEntity.badRequest().body(Map.of("error", "Min 6 characters"));
        u.setPasswordHash(encoder.encode(next)); userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}