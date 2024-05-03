package com.example.checkbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
public class MenuExpandableAdapter extends BaseExpandableListAdapter {
    private final Activity _context;
    private final List<MenuHeader> _listDataHeader;
    private final HashMap<String, List<String>> _listDataChild;
    private final TextView groupStatus;

    private final int qtyTextview;

    private BreakfastFragment fragment;


    private ExpandableListView accordion;


    //public int lastExpandedGroupPosition;

    //int selectedPosition = 0;

    //HashMap<Integer, Integer> childCheckedState = new HashMap<>();
    HashMap<Integer, Integer> childCheckboxState = new HashMap<>();
    ArrayList<String> listOfStatusFilters = new ArrayList<>();

    //ArrayList<ArrayList<Integer>> check_states = new ArrayList<ArrayList<Integer>>();
    public MenuExpandableAdapter(Activity _context, List<MenuHeader> _listDataHeader, HashMap<String, List<String>> _listDataChild, TextView groupStatus, int qtyTextview, ExpandableListView accordion, BreakfastFragment fragment) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        this.groupStatus = groupStatus;
        this.qtyTextview = qtyTextview;
        this.accordion = accordion;
        this.fragment = fragment;
    }


    /*public MenuExpandableAdapter(Activity _context, List<MenuHeader> _listDataHeader, HashMap<String, List<String>> _listDataChild, TextView groupStatus, TextView qtyTextview, ExpandableListView accordion, BreakfastFragment breakfastFragment) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        this.groupStatus = groupStatus;
        this.qtyTextview = qtyTextview;
        this.accordion = accordion;




    }*/

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getTitle())
                .size();
    }

    @Override
    public MenuHeader getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getTitle())
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition).getTitle();
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = Objects.requireNonNull(infalInflater).inflate(R.layout.filter_header_layout, null);

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        final MenuHeader headerText = (MenuHeader) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(infalInflater).inflate(R.layout.filter_child_layout, null);
        }

        final TextView filterName = convertView.findViewById(R.id.textviewFilterName);
        filterName.setText(childText);
        final ImageView incrementButton = convertView.findViewById(R.id.incrementButton);
        final TextView qtyTextView = convertView.findViewById(R.id.qtyTextview);
        final ImageView decrementButton = convertView.findViewById(R.id.decrementButton);




        // private void startActivity(Intent intent) {}
        View.OnClickListener clickListener = v -> {
            int quantity = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                quantity = childCheckboxState.getOrDefault(childPosition, 0);
            }


            // Increment or decrement the quantity based on the button that was clicked
            if (v.getId() == R.id.incrementButton) {

                quantity++;
                System.out.println(quantity);

                fragment.incrementQuantity(childText);

                listOfStatusFilters.add(_listDataChild.get(headerText.getTitle()).get(childPosition));
            } else if (v.getId() == R.id.decrementButton && quantity > 0) {
                quantity--;
                listOfStatusFilters.remove(_listDataChild.get(headerText.getTitle()).get(childPosition));
            }
            //childCheckboxState.put(groupPosition, quantity);
            childCheckboxState.put(childPosition, quantity);

            /*if (quantity > 0) {

                listOfStatusFilters.add(_listDataChild.get(headerText.getTitle()).get(childPosition));
            } else {
                listOfStatusFilters.remove(_listDataChild.get(headerText.getTitle()).get(childPosition));
            }*/

            if (_listDataChild.get(headerText.getTitle()) != null) {
                headerText.setActiveFilter(getCheckedStatusCombinedString());
                SharedPreferences sharedPreferences = _context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("test", headerText.getActiveFilter()).apply();
                groupStatus.setText(headerText.getActiveFilter());

                qtyTextView.setText(String.valueOf(quantity));



            }
            notifyDataSetChanged();

        };

        incrementButton.setOnClickListener(clickListener);
        decrementButton.setOnClickListener(clickListener);
        return convertView;
    }


    /*public void onGroupExpanded(int groupPosition) {

        if(groupPosition != lastExpandedGroupPosition){
            accordion.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);

        lastExpandedGroupPosition = groupPosition;

    }*/

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public String getCheckedStatusCombinedString() {
        StringBuilder status = new StringBuilder();
        for (int i = 0; i < listOfStatusFilters.size(); i++) {
            status.append(listOfStatusFilters.get(i));
            if (i != listOfStatusFilters.size() -1) {
                status.append(" , ");
            }
        }
        return status.toString();
    }

}
