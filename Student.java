/**
 * Represents a student in the Student Management System.
 */
public class Student {
    private String name;
    private int id;
    private int semester;

    /**
     * Creates a new Student with the specified details.
     * @param name the student's name
     * @param id the student's ID
     * @param semester the current semester
     */
    public Student(String name, int id, int semester) {
        this.name = name;
        this.id = id;
        this.semester = semester;
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public int getSemester() { 
        return semester; 
    }

    public void setSemester(int semester) { 
        this.semester = semester; 
    }
}