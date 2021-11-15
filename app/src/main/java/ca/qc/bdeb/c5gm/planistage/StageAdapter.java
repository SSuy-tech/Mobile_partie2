package ca.qc.bdeb.c5gm.planistage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import ca.qc.bdeb.c5gm.planistage.data.Stage;

public class StageAdapter extends RecyclerView.Adapter<StageAdapter.StageViewHolder>  {

    private ArrayList<Stage> listeStages;
    private ArrayList<Stage> stagesCopie;
    private OnStageClickListener listener;

    public StageAdapter(ArrayList<Stage> ListeStages) {
        this.listeStages = ListeStages;
        stagesCopie = new ArrayList<Stage>();
    }

    public interface OnStageClickListener {
        void onEditClick(int position);

        void onPrioriteClick(int position);
    }


    public void setOnStageClickListener(OnStageClickListener listener) {
        this.listener = listener;
    }

    public static class StageViewHolder extends RecyclerView.ViewHolder {
        // Voici les vues de notre item:
        public TextView nom;
        public TextView prenom;
        public ImageView photo;
        public ImageView ivPriorite;

        public StageViewHolder(@NonNull View itemView, final OnStageClickListener listener) {
            super(itemView);

            // récupération des éléments du layout:
            nom = (TextView) itemView.findViewById(R.id.nom);
            prenom = itemView.findViewById(R.id.prenom);
            photo = itemView.findViewById(R.id.photo);
            ivPriorite = itemView.findViewById(R.id.img_priorite);

            // attribution des différents listeners:
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // vérifier que le listener n'est pas null
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // la position est valide?
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }

                }
            });

            ivPriorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // vérifier que le listener n'est pas null
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // la position est valide?
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPrioriteClick(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public StageAdapter.StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stage_view, parent, false);
        StageViewHolder cvh = new StageViewHolder(v, listener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull StageAdapter.StageViewHolder holder, int position) {
        Stage stage = listeStages.get(position);
        if (stage != null) {
            holder.nom.setText(stage.getEtudiant().getNom());
            holder.prenom.setText(stage.getEtudiant().getPrenom());
            if (stage.getEtudiant().getPhoto() != null) {
                holder.photo.setImageBitmap(stage.getEtudiant().getPhoto());
            } else {
                holder.photo.setImageResource(R.drawable.ic_image_24);
            }
            holder.ivPriorite.setImageResource(StageUtils.getImageFromPriorite(stage.getPriorite()));
        }
    }

    @Override
    public int getItemCount() {
        return listeStages.size();
    }

    /**
     * filtre les éléments de la liste
     * @param filtre bitfield utilisé pour filtrer les priorités
     */
    public void filtrer(byte filtre){

        if (filtre == StageUtils.FLAG_ALL){
            // restorer tous les stages
            listeStages.addAll(stagesCopie);
            stagesCopie.clear();
        } else {
            ArrayList<Stage> resultat = new ArrayList<>();

            // restorer tous les stages
            listeStages.addAll(stagesCopie);
            stagesCopie.clear();

            // filtrer
            for (Stage stage : listeStages){
                // Vérifie la correspondance:
                if ((stage.getPriorite().getFlag() & filtre) != 0 ){
                    resultat.add(stage);
                }
                else {
                    stagesCopie.add(stage);
                }
            }

            //n'afficher que les résultats filtrés:
            listeStages.clear();
            listeStages.addAll(resultat);
        }
        // Trier les résultats:
        Collections.sort(listeStages);
        // Raffraichir l'affichage:
        notifyDataSetChanged();
    }

}
