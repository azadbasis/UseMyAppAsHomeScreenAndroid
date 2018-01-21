package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.OfficialBean;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;

/**
 * Created by Nanosoft-Android on 1/11/2018.
 */

public class OffecialBeanAdapter extends RecyclerView.Adapter<OffecialBeanAdapter.OffecialHolder> {


    ArrayList<OfficialBean> officialBeanArrayList;
    Context context;
//    static final String BASE_URL = "http://192.168.0.115/sreda_erp/";
    static final String BASE_URL = "http://erp.sreda.gov.bd/";
    private static String filteredEmail;

    public OffecialBeanAdapter(Context context, ArrayList<OfficialBean> officialBeanArrayList) {
        this.context = context;
        this.officialBeanArrayList = officialBeanArrayList;
    }


    @Override
    public OffecialHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raw_offecial, null);

        return new OffecialHolder(view);
    }

    ArrayList<String> stringArrayList = new ArrayList<>();

    @Override
    public void onBindViewHolder(OffecialHolder holder, final int position) {

        final OfficialBean officialBean = officialBeanArrayList.get(position);
        holder.tvOffecialName.setText(officialBean.getEmployee_name());
        holder.tvOffecialDesignation.setText(officialBean.getEmployee_designation());
        //holder.imgOffecial.setImageBitmap("");
        Picasso.with(context).load(BASE_URL + officialBean.getEmployee_image()).resize(120, 60).into(holder.imgOffecial);

        holder.checkBoxOffecial.setOnCheckedChangeListener(null);
        holder.checkBoxOffecial.setChecked(officialBean.isSelected());
        holder.checkBoxOffecial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                officialBean.setSelected(isChecked);


                if (isChecked) {

                    String checkedEmail = officialBean.getEmployee_email();
                    stringArrayList.add(checkedEmail);
                    StringBuilder result = new StringBuilder();
                    for (String temp : stringArrayList) {
                        result.append(temp);
                        result.append(",");

                    }
                    filteredEmail = result.length() > 0 ? result.substring(0, result.length() - 1) : "";

                } else {
                    String checkedEmail = officialBean.getEmployee_email();
                    stringArrayList.remove(checkedEmail);
                    StringBuilder result = new StringBuilder();
                    for (String temp : stringArrayList) {
                        result.append(temp);
                        result.append(",");

                    }
                    filteredEmail = result.length() > 0 ? result.substring(0, result.length() - 1) : "";

                }
                Operation operation = new Operation(context);
                operation.saveString("filteredEmail", filteredEmail);
  Toast.makeText(context,
                            "selected person " + filteredEmail,
                            Toast.LENGTH_LONG).show();

            }
        });

        //in some cases, it will prevent unwanted situations holder.cbSelect.setOnCheckedChangeListener(null); //if true, your checkbox will be selected, else unselected holder.cbSelect.setChecked(objIncome.isSelected()); holder.cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() { @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //set your object's last status objIncome.setSelected(isChecked); }


    }

    @Override
    public int getItemCount() {
        return officialBeanArrayList.size();
    }

    class OffecialHolder extends RecyclerView.ViewHolder {


        ImageView imgOffecial;
        TextView tvOffecialName, tvOffecialDesignation;
        CheckBox checkBoxOffecial;

        public OffecialHolder(View view) {
            super(view);
            imgOffecial = (ImageView) view.findViewById(R.id.imgOffecial);
            tvOffecialName = (TextView) view.findViewById(R.id.tvOffecialName);
            tvOffecialDesignation = (TextView) view.findViewById(R.id.tvOffecialDesignation);
            checkBoxOffecial = (CheckBox) view.findViewById(R.id.checkBoxOffecial);


        }
    }
}
