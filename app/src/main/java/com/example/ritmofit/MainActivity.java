package com.example.ritmofit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ritmofit.auth.model.UnAuthenticationEvent;
import com.example.ritmofit.auth.repository.TokenRepository;
import com.example.ritmofit.auth.ui.AuthActivity;
import com.example.ritmofit.security.model.SecurityEvent;
import com.example.ritmofit.security.service.SecurityService;
import com.example.ritmofit.security.ui.SecurityActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    TokenRepository tokenRepository;
    
    @Inject
    SecurityService securityService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        handleAuth();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navController);
            bottomNav.setOnItemReselectedListener(item -> {});
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void handleAuth() {
        // Verificar si viene de una autenticación de seguridad exitosa
        boolean securityAuthenticated = getIntent().getBooleanExtra("SECURITY_AUTHENTICATED", false);
        
        if (!tokenRepository.hasToken()) {
            goToLogin();
        } else if (securityService.shouldRequestAuthentication() && !securityAuthenticated) {
            goToSecurity();
        }
        // Si tiene token y no necesita seguridad (o ya se autenticó), continúa normalmente
    }

    private void goToLogin() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
    
    private void goToSecurity() {
        startActivity(new Intent(this, SecurityActivity.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnAuthenticationEvent(UnAuthenticationEvent event) {
        goToLogin();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSecurityEvent(SecurityEvent event) {
        switch (event.getType()) {
            case SECURITY_REQUIRED:
                goToSecurity();
                break;
            case SECURITY_FAILED:
                // Manejar fallo de seguridad si es necesario
                Toast.makeText(this, "Fallo en la autenticación de seguridad", Toast.LENGTH_SHORT).show();
                break;
            case SECURITY_SUCCESS:
                // La seguridad fue exitosa, continuar normalmente
                // No hacer nada aquí ya que SecurityActivity redirige a MainActivity
                break;
        }
    }
}
