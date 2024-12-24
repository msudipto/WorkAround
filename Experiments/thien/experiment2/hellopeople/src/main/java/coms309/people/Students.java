package coms309.people;

public class Students extends Person {
    private String UniversityId;
    private String Major;
    private int year;
    public Students(){

    }
    public Students(String universityId, String major, int year){
        super();
        this.UniversityId = universityId;
        this.Major= Major;
        this.year= year;
    }
    public String getUniversityId(){return this.UniversityId;}
    public String getMajor(){return this.Major;}
    public int getYear(){return this.year; }
}
