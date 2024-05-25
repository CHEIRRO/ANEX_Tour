package com.example.kursach;

public class BUFFER {
    //private LinearLayout favoritesContainerRouts;
    //private LinearLayout favoritesContainerSights;
    //private RoutsDBH dbHelperRouts;
    //private SightsDBH dbHelperSights;





    //favoritesContainerRouts = findViewById(R.id.favorites_container_routs);
    //favoritesContainerSights = findViewById(R.id.favorites_container_sights);

    //dbHelperRouts = new RoutsDBH(this);
    //dbHelperSights = new SightsDBH(this);

    //loadFavoritesRouts();
    //loadFavoritesSights();





        /*private void loadFavoritesRouts() {
        favoritesContainerRouts.removeAllViews();
        List<Rout> favorites = dbHelperRouts.getFavoriteRouts();
        for (Rout rout : favorites) {
            View routView = getLayoutInflater().inflate(R.layout.item_scrollview_rout_favor, favoritesContainerRouts, false);

            TextView titleTextView = routView.findViewById(R.id.rout_title);
            TextView dataTextView = routView.findViewById(R.id.rout_lenght);
            TextView descriptionTextView = routView.findViewById(R.id.rout_description);
            ImageButton removeButton = routView.findViewById(R.id.delete_button_rout);
            removeButton.setTag(rout.getId());

            titleTextView.setText(rout.getTitle());
            dataTextView.setText(rout.getLength());
            descriptionTextView.setText(rout.getDescription());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer routeId = (Integer) v.getTag();
                    if (routeId != null) {
                        dbHelperRouts.removeFavorite(routeId);
                        loadFavoritesRouts();
                        Toast.makeText(FavoritesActivity.this, "Маршрут удален из избранного", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavoritesActivity.this, "Ошибка: ID маршрута не найден", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            favoritesContainerRouts.addView(routView);
        }
    }

    private void loadFavoritesSights() {
        favoritesContainerSights.removeAllViews();
        List<Sight> favorites = dbHelperSights.getFavoriteSights();
        for (Sight sight : favorites) {
            View sightView = getLayoutInflater().inflate(R.layout.item_scrollview_sight_favor, favoritesContainerSights, false);

            TextView titleTextView = sightView.findViewById(R.id.sight_title);
            TextView dataTextView = sightView.findViewById(R.id.sight_data);
            TextView descriptionTextView = sightView.findViewById(R.id.sight_description);
            ImageButton removeButton = sightView.findViewById(R.id.delete_button_sight);
            removeButton.setTag(sight.getId());

            titleTextView.setText(sight.getTitle());
            dataTextView.setText(sight.getData());
            descriptionTextView.setText(sight.getDescription());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer sightId = (Integer) v.getTag();
                    if (sightId != null) {
                        dbHelperSights.removeFavoriteSight(sightId);
                        loadFavoritesSights();
                        Toast.makeText(FavoritesActivity.this, "Достопримечательность удалена из избранного", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavoritesActivity.this, "Ошибка: ID достопримечательности не найден", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            favoritesContainerSights.addView(sightView);
        }
    }*/
}
