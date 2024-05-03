package com.example.checkbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BreakfastFragment extends Fragment {

    SharedPreferences sharedPreferences;
    TextView groupStatus;

    int qtyTextview;

    Button applyFiltersButton;

    TextView textviewReset;

    ExpandableListView expandablelistviewFilter;

    MainActivity mContext;
    List<MenuHeader> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    HashMap<String, Integer> quanityMap;

    MenuExpandableAdapter listAdapter;

    public BreakfastFragment() {
        // Required empty public constructor
    }

    /*public static EcommerceFiltersFragment newInstance() {
        EcommerceFiltersFragment fragment = new EcommerceFiltersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandablelistviewFilter = view.findViewById(R.id.expandablelistview_filter);
        applyFiltersButton = view.findViewById(R.id.applyFiltersBtn);
        textviewReset = view.findViewById(R.id.button_reset);
        groupStatus = view.findViewById(R.id.groupStatus);
        //qtyTextview = view.findViewById(R.id.qtyTextview);

        prepareListData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        return view;
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        quanityMap = new HashMap<String, Integer>();
        /*for (String item : color) {
            quanityMap.put(item, 1); // Initialize quantity to 1 for each item
        }*/
        // Adding child data
        listDataHeader.add(new MenuHeader("Fruit", "Fruit"));
        listDataHeader.add(new MenuHeader("Dinners", "Select"));

        //listDataHeader.add(new FilterHeader("Sort", "Select"));


        String MyFruit = "Apple, 15";

// Adding child data
        List<String> fruitItems = new ArrayList<>();
        fruitItems.add(MyFruit);
        fruitItems.add("Banana, 20");
        fruitItems.add("Cherry, 10");
        fruitItems.add("Peach, 12");
        fruitItems.add("Pear, 10");

        List<String> dinnerItems = new ArrayList<>();
        dinnerItems.add("Black");
        dinnerItems.add("Red");
        dinnerItems.add("White");
        dinnerItems.add("Green");
        dinnerItems.add("Blue");


        //List<String> bySize = Arrays.asList(getResources().getStringArray(R.array.fruit_items));
        //List<String> color = Arrays.asList(getResources().getStringArray(R.array.dinner_items));

        //String[] fruitItems = getResources().getStringArray(R.array.fruit_items);
        //String[] dinnerItems = getResources().getStringArray(R.array.dinner_items);

        /*for (int i = 0; i < fruitItems.size(); i++) {
            fruitItems.set(i, (i + 1) + ". " + fruitItems.get(i));
        }

        for (int i = 0; i < dinnerItems.size(); i++) {
            dinnerItems.set(i, (i + 1) + ". " + dinnerItems.get(i));
        }*/


        /*List<String> sort = new ArrayList<>();
        sort.add("Cost ($): highest first");
        sort.add("Cost ($): lowest first");*/

        listDataChild.put(listDataHeader.get(0).getTitle(), fruitItems);
        listDataChild.put(listDataHeader.get(1).getTitle(), dinnerItems);

        /*for (String item : fruitItems) {
            quanityMap.put(item, 0); // Initialize quantity to 1 for each item
        }

        for (String item : dinnerItems) {
            quanityMap.put(item, 1); // Initialize quantity to 1 for each item
        }*/

        listAdapter = new MenuExpandableAdapter(mContext, listDataHeader, listDataChild, groupStatus, qtyTextview, expandablelistviewFilter, this);

        expandablelistviewFilter.setAdapter(listAdapter);

        applyFiltersButton.setOnClickListener(v -> mContext.dismissFragment());

        textviewReset.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = (requireActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            String test = sharedPreferences.getString("test", "");
            groupStatus.setText(test);
            prepareListData();


        });

    }
    /*public void incrementQuantity(String itemName) {
        int quantity = quanityMap.get(itemName);
        quantity++;
        quanityMap.put(itemName, quantity);

        // Update the string-array item with the new quantity
        updateQuantityInArray(itemName, quantity);
    }*/
    int quantity = 0;
    public void incrementQuantity(String itemName) {
        quantity++;
        updateQuantityInArray(itemName, quantity);
    }

    private void updateQuantityInArray(String itemName, int quantity) {
        // Assuming that 'itemName' is a unique identifier for the item
        // You might need to adjust this based on your actual data structure
        for (MenuHeader header : listDataHeader) {
            List<String> items = listDataChild.get(header.getTitle());
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    String item = items.get(i);
                    if (item.startsWith(itemName)) {
                        items.set(i,itemName + " : " + quantity);
                        break;
                    }
                }
            }
        }

        // Notify the adapter that the data has changed
        listAdapter.notifyDataSetChanged();
    }
    /*private void updateQuantityInArray(String itemName, int quantity) {
        // Assuming that 'itemName' is a unique identifier for the item
        // You might need to adjust this based on your actual data structure
        for (MenuHeader header : listDataHeader) {
            List<String> items = listDataChild.get(header.getTitle());
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    String item = items.get(i);
                    if (item.startsWith(itemName)) {
                        items.set(i, itemName + " : " + quantity);
                        break;
                    }
                }
            }
        }*/
    /*private void updateQuantityInArray(String itemName, int quantityDelta) {
        // Assuming that 'itemName' is a unique identifier for the item
        // You might need to adjust this based on your actual data structure
        for (MenuHeader header : listDataHeader) {
            List<String> items = listDataChild.get(header.getTitle());
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    String item = items.get(i);
                    if (item.startsWith(itemName)) {
                        // Extract the current quantity from the item string
                        String[] parts = item.split(" : ");
                        if (parts.length >= 1) {
                            try {
                                int currentQuantity = Integer.parseInt(parts[1].trim());
                                int newQuantity = currentQuantity + quantityDelta;
                                items.set(i, itemName + " : " + newQuantity);
                            } catch (NumberFormatException e) {
                                // Handle parsing error
                            }
                        }
                        break;
                    }
                }
            }
        }

        // Notify the adapter that the data has changed
        listAdapter.notifyDataSetChanged();
    }*/


    // Notify the adapter that the data has changed
       // listAdapter.notifyDataSetChanged();






    /*private void updateQuantityInArray(String itemName, int quantity) {
        String[] fruitItems = getResources().getStringArray(R.array.fruit_items);
        for (int i = 0; i < fruitItems.length; i++) {
            if (fruitItems[i].equals(itemName)) {
                fruitItems[i] = itemName + " (Quantity: " + quantity + ")";
                break;
            }
        }

        // Update the string-array resource
        listAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, fruitItems);
        expandablelistviewFilter.setAdapter(adapter);
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mContext = (MainActivity) context;

        }
    }
}