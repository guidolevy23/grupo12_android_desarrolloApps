package com.example.ritmofit.ui.historial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ritmofit.R;
import com.example.ritmofit.model.HistorialItem;

import java.util.List;

/**
 * RecyclerView adapter for displaying attendance history records.
 * Uses DiffUtil for efficient list updates and proper date/time formatting.
 */
public class HistorialAdapter extends ListAdapter<HistorialItem, HistorialAdapter.HistorialViewHolder> {

    /**
     * DiffUtil callback for efficient list updates.
     * Compares HistorialItem objects to determine what has changed.
     */
    private static final DiffUtil.ItemCallback<HistorialItem> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<HistorialItem>() {
            
            @Override
            public boolean areItemsTheSame(@NonNull HistorialItem oldItem, @NonNull HistorialItem newItem) {
                // Items are the same if they have the same ID
                return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull HistorialItem oldItem, @NonNull HistorialItem newItem) {
                // Check if all relevant content is the same, handling null values
                return oldItem.equals(newItem) &&
                       safeEquals(oldItem.getClase(), newItem.getClase()) &&
                       safeEquals(oldItem.getSede(), newItem.getSede()) &&
                       safeEquals(oldItem.getFecha(), newItem.getFecha()) &&
                       safeEquals(oldItem.getHora(), newItem.getHora()) &&
                       oldItem.getDuracion() == newItem.getDuracion();
            }
            
            /**
             * Safely compares two objects, handling null values
             */
            private boolean safeEquals(Object obj1, Object obj2) {
                if (obj1 == null && obj2 == null) return true;
                if (obj1 == null || obj2 == null) return false;
                return obj1.equals(obj2);
            }
        };

    /**
     * Constructor for HistorialAdapter
     */
    public HistorialAdapter() {
        super(DIFF_CALLBACK);
    }

    /**
     * Updates the adapter with a new list of historial items.
     * This method uses DiffUtil for efficient updates.
     * 
     * @param newList The new list of HistorialItem objects
     */
    public void updateHistorialList(List<HistorialItem> newList) {
        submitList(newList);
    }

    /**
     * Clears all items from the adapter
     */
    public void clearItems() {
        submitList(null);
    }

    /**
     * Gets the HistorialItem at the specified position
     * 
     * @param position The position of the item
     * @return The HistorialItem at the position, or null if position is invalid
     */
    public HistorialItem getItemAtPosition(int position) {
        if (position >= 0 && position < getItemCount()) {
            return getItem(position);
        }
        return null;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialItem item = getItem(position);
        holder.bind(item);
    }

    /**
     * ViewHolder class for individual attendance record items.
     * Handles binding attendance data to UI components with proper formatting.
     */
    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvFecha;
        private final TextView tvHora;
        private final TextView tvClase;
        private final TextView tvSede;
        private final TextView tvDuracion;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // Initialize view references
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvClase = itemView.findViewById(R.id.tvClase);
            tvSede = itemView.findViewById(R.id.tvSede);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
        }

        /**
         * Binds a HistorialItem to the UI components with proper formatting.
         * Handles null values gracefully and uses proper Spanish locale formatting.
         * 
         * @param item The HistorialItem to bind to the views
         */
        public void bind(@NonNull HistorialItem item) {
            // Set formatted date using the model's Spanish locale formatting
            String formattedDate = item.getFormattedDate();
            tvFecha.setText(formattedDate.isEmpty() ? "Fecha no disponible" : formattedDate);
            
            // Set formatted time
            String formattedTime = item.getFormattedTime();
            tvHora.setText(formattedTime.isEmpty() ? "--:--" : formattedTime);
            
            // Set class name with fallback
            String className = item.getClase();
            tvClase.setText(className != null && !className.trim().isEmpty() ? className : "Clase no especificada");
            
            // Set location/sede with fallback
            String sede = item.getSede();
            tvSede.setText(sede != null && !sede.trim().isEmpty() ? sede : "Sede no especificada");
            
            // Set formatted duration using the model's formatting method
            String formattedDuration = item.getFormattedDuration();
            tvDuracion.setText(formattedDuration.isEmpty() ? "0min" : formattedDuration);
        }
    }
}