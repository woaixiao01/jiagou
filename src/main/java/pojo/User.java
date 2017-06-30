package pojo;

import annotation.Excel;

public class User {

    @Excel(name = "field1")
    private String name;
    @Excel(name = "field2")
    private String nameEn;
    @Excel(name = "field3")
    private String age;
    @Excel(name = "field4")
    private String six;
    @Excel(name = "field5")
    private String weight;
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", nameEn=" + nameEn + ", age=" + age + ", six=" + six + ", weight=" + weight
				+ "]";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSix() {
		return six;
	}
	public void setSix(String six) {
		this.six = six;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}

    // ...getter setter
    
    
}
