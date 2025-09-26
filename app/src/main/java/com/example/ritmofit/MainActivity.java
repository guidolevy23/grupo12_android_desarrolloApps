package com.example.ritmofit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ritmofit.auth.model.UnAuthenticationEvent;
import com.example.ritmofit.auth.repository.TokenRepository;
import com.example.ritmofit.auth.ui.AuthActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
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

    private NavController navController;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        handleAuth();

        bottomNav = findViewById(R.id.bottom_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavigation();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Configurar navegación personalizada
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                // Configurar opciones de navegación para limpiar la pila
                NavOptions.Builder navOptionsBuilder = new NavOptions.Builder()
                        .setLaunchSingleTop(true);

                // Si estamos navegando al home, limpiar toda la pila
                if (itemId == R.id.nav_home) {
                    navOptionsBuilder.setPopUpTo(R.id.nav_home, true);
                } else {
                    // Para otros destinos, pop hasta el home (pero mantenerlo en la pila)
                    navOptionsBuilder.setPopUpTo(R.id.nav_home, false);
                }

                try {
                    NavOptions navOptions = navOptionsBuilder.build();
                    navController.navigate(itemId, null, navOptions);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback a la navegación estándar
                    return NavigationUI.onNavDestinationSelected(item, navController);
                }
            });

            // Mantener sincronizado el ítem seleccionado
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.nav_home ||
                        destination.getId() == R.id.nav_reservas ||
                        destination.getId() == R.id.nav_perfil) {

                    // Actualizar el ítem seleccionado en la bottom navigation
                    bottomNav.getMenu().findItem(destination.getId()).setChecked(true);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void handleAuth() {
        if (!tokenRepository.hasToken()) {
            goToLogin();
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnAuthenticationEvent(UnAuthenticationEvent event) {
        goToLogin();
    }
}