package com.nortvis.app.service.verifier;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserVerifier {
  UserDetailsService userDetailsService();
}
