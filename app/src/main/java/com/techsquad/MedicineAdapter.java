package com.techsquad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter {

    private List<Medicine> medicineList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_need_layout, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MedicineViewHolder) holder).bindView(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (medicineList == null)
            return 0;
        else
            return medicineList.size();
    }

    void setMedicineList(List<Medicine> list) {
        medicineList = list;
        notifyDataSetChanged();
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name_medicine;
        TextView qty_medicine;
        Medicine data;

        MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            name_medicine = itemView.findViewById(R.id.name_medicine);
            qty_medicine = itemView.findViewById(R.id.qty_medicine);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            data = medicineList.get(position);
            name_medicine.setText(data.getName());
            qty_medicine.setText(String.format(Locale.getDefault(), "Amount Needed: %d", data.getQty()));
        }

        @Override
        public void onClick(View v) {

        }
    }
}
