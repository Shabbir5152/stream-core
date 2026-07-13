package com.streamcore.user;

import com.streamcore.content.Movie;
import com.streamcore.subscription.BasicPlan;
import com.streamcore.subscription.PremiumPlan;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserAccessTest {

    @Test
    public void testUserValidationExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new RegularUser(null, "alice", "alice@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new RegularUser("U-1", "   ", "alice@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new RegularUser("U-1", "alice", null));
        assertThrows(IllegalArgumentException.class, () -> new RegularUser("U-1", "alice", "  "));
    }

    @Test
    public void testRegularUserDefaultSubscriptionAccess() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        assertEquals("Basic", user.getSubscriptionPlan().getPlanName());
        assertEquals("720p", user.getSubscriptionPlan().getMaxVideoQuality());

        Movie regularMovie = new Movie("M-1", "Regular Movie", 120, "Director", false);
        Movie premiumMovie = new Movie("M-2", "Premium Movie", 130, "Director", true);

        assertTrue(user.canAccess(regularMovie));
        assertFalse(user.canAccess(premiumMovie));
    }

    @Test
    public void testPremiumUserDefaultSubscriptionAccess() {
        User user = new PremiumUser("U-2", "bob", "bob@example.com");
        assertEquals("Premium", user.getSubscriptionPlan().getPlanName());
        assertEquals("4K UHD", user.getSubscriptionPlan().getMaxVideoQuality());

        Movie regularMovie = new Movie("M-1", "Regular Movie", 120, "Director", false);
        Movie premiumMovie = new Movie("M-2", "Premium Movie", 130, "Director", true);

        assertTrue(user.canAccess(regularMovie));
        assertTrue(user.canAccess(premiumMovie));
    }

    @Test
    public void testSetSubscriptionPlanValidation() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        assertThrows(IllegalArgumentException.class, () -> user.setSubscriptionPlan(null));
    }

    @Test
    public void testSubscriptionPlanNullContentException() {
        BasicPlan basic = new BasicPlan();
        PremiumPlan premium = new PremiumPlan();

        assertThrows(IllegalArgumentException.class, () -> basic.canAccess(null));
        assertThrows(IllegalArgumentException.class, () -> premium.canAccess(null));
    }

    @Test
    public void testDynamicUpgradeAccessChange() {
        User user = new RegularUser("U-1", "alice", "alice@example.com");
        Movie premiumMovie = new Movie("M-2", "Premium Movie", 130, "Director", true);

        assertFalse(user.canAccess(premiumMovie));

        user.setSubscriptionPlan(new PremiumPlan());
        assertEquals("Premium", user.getSubscriptionPlan().getPlanName());
        assertEquals("4K UHD", user.getSubscriptionPlan().getMaxVideoQuality());
        assertTrue(user.canAccess(premiumMovie));
    }

    @Test
    public void testUserFactoryCreation() {
        UserFactory factory = new UserFactory();
        User regular = factory.createRegular("U-1", "alice", "alice@example.com");
        User premium = factory.createPremium("U-2", "bob", "bob@example.com");

        assertNotNull(regular);
        assertNotNull(premium);
        assertEquals("alice", regular.getUsername());
        assertEquals("bob", premium.getUsername());
        assertEquals("Basic", regular.getSubscriptionPlan().getPlanName());
        assertEquals("Premium", premium.getSubscriptionPlan().getPlanName());
    }
}
