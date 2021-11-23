package cse.fjnu.mynotepad;

import java.io.Serializable;
import java.util.List;

public class transferList  implements Serializable {
    List<Note> getNotes;

    public transferList(List<Note> getNotes){
        this.getNotes=getNotes;

    }
    public List<Note> getNotes(){
        return this.getNotes;
    }


}
