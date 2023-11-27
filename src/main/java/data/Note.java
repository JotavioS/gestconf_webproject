package data;

import com.google.gson.annotations.Expose;

public class Note {
	@Expose(serialize = true, deserialize = true)
    private long id;
	@Expose(serialize = true, deserialize = true)
    private String name;
	@Expose(serialize = true, deserialize = true)
    private String description;

    public Note(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
	@Override
    public String toString() { 
        return "Note [ id: "+id+", name: "+name+", description: "+ description+ " ]"; 
     }  
}
