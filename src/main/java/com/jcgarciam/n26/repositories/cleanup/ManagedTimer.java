package com.jcgarciam.n26.repositories.cleanup;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Timer;

/**
 * ManagedTimer is just a regular java.util.Timer that can be injected into any Spring Bean
 */
@Component
@ApplicationScope
public class ManagedTimer extends Timer {
}
