package nickolaill.staniec.runeak.amagicalplace.Utilities;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;

public class ValueCalculator {
    public static double calculateCollectionValue(List<Card> cardsInCollection, ArrayList<Pair<String, Double>> scryfallPairList){
        double totalValue = 0.0;

        for(int i = 0; i < cardsInCollection.size(); i++){
            int quantity = cardsInCollection.get(i).getQuantity();
            String cardName = cardsInCollection.get(i).getTitle();
            String scryFallName = scryfallPairList.get(i).first;
            Double value = scryfallPairList.get(i).second;
            if(cardName.equalsIgnoreCase(scryFallName)){
                totalValue = totalValue + (value * quantity);
            }
        }

        return totalValue;
    }
}
