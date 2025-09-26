package com.example.ritmofit.security.ui;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.ritmofit.R;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test de integración para SecurityActivity
 * Verifica que la interfaz de usuario se muestre correctamente
 */
@RunWith(AndroidJUnit4.class)
public class SecurityActivityTest {

    @Test
    public void testSecurityActivityDisplaysCorrectly() {
        // Arrange
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SecurityActivity.class);

        // Act
        try (ActivityScenario<SecurityActivity> scenario = ActivityScenario.launch(intent)) {
            
            // Assert - verificar que los elementos UI están presentes
            Espresso.onView(ViewMatchers.withId(R.id.security_title))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                    
            Espresso.onView(ViewMatchers.withId(R.id.security_subtitle))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                    
            Espresso.onView(ViewMatchers.withId(R.id.authenticate_button))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                    
            // Verificar textos en español argentino
            Espresso.onView(ViewMatchers.withId(R.id.security_title))
                    .check(ViewAssertions.matches(ViewMatchers.withText("Autenticación requerida")));
        }
    }

    @Test
    public void testAuthenticateButtonIsClickable() {
        // Arrange
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, SecurityActivity.class);

        // Act
        try (ActivityScenario<SecurityActivity> scenario = ActivityScenario.launch(intent)) {
            
            // Assert - verificar que el botón es clickeable
            Espresso.onView(ViewMatchers.withId(R.id.authenticate_button))
                    .check(ViewAssertions.matches(ViewMatchers.isClickable()));
        }
    }
}
