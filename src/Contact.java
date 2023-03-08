public class Contact {
    public String first_name;
    public String last_name;
    public String number;
    private int id;

    public Contact() {
        this.first_name = "N/A";
        this.last_name = "N/A";
        this.number = "N/A";
        this.id = 0;
    }

    public void set_number(String num) {
        this.number = num;
    }

    public int get_id() {
        return this.id;
    }

    public static void main(String[] args) {
    Contact my_Contact = new Contact();
        System.out.println(my_Contact.id);
        my_Contact.get_id();

    }
}

