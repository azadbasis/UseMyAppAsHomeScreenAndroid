package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by Nanosoft-Android on 1/11/2018.
 */

public class OfficialBean {
    /*    "employee_name": "Salima Jahan",
    "employee_email": "member.pr@sreda.gov.bd",
    "employee_phone": "+88029333688",
    "employee_mobile": "01843246976",
    "employee_image": "uploads/36/user_image/ba014de68983db6048cb8ec431bd160b.png",
    "employee_designation": "Member"*/

    private String employee_image;
    private String employee_name;
    private String employee_designation;
    private String employee_email;
    private String employee_phone;
    private String employee_mobile;
    private boolean isSelected;


    public OfficialBean(String employee_image, String employee_name, String employee_designation) {
        this.employee_image = employee_image;
        this.employee_name = employee_name;
        this.employee_designation = employee_designation;
    }


    public OfficialBean(String employee_image, String employee_name, String employee_designation, String employee_email, String employee_phone, String employee_mobile) {
        this.employee_image = employee_image;
        this.employee_name = employee_name;
        this.employee_designation = employee_designation;
        this.employee_email = employee_email;
        this.employee_phone = employee_phone;
        this.employee_mobile = employee_mobile;
    }

    public String getEmployee_image() {
        return employee_image;
    }

    public void setEmployee_image(String employee_image) {
        this.employee_image = employee_image;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_designation() {
        return employee_designation;
    }

    public void setEmployee_designation(String employee_designation) {
        this.employee_designation = employee_designation;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

    public String getEmployee_phone() {
        return employee_phone;
    }

    public void setEmployee_phone(String employee_phone) {
        this.employee_phone = employee_phone;
    }

    public String getEmployee_mobile() {
        return employee_mobile;
    }

    public void setEmployee_mobile(String employee_mobile) {
        this.employee_mobile = employee_mobile;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
