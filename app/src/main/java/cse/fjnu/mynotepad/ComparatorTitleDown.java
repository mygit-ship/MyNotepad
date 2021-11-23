package cse.fjnu.mynotepad;

import java.util.Comparator;

public class ComparatorTitleDown  implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        // TODO Auto-generated method stubll
        int result = 0;//result的值将决定person1和person2是否交换位置，这里是初始化result的值
        char n1f=n1.getTitle().charAt(0);
        char n2f=n2.getTitle().charAt(0);
        if (n1f < n2f) {
            result = 1;//当result的值是1，交换两个person的位置。
        }
        if (n1f > n2f) {
            result = -1;//当result的值是-1，保持它俩的顺序
        }
        return result;
    }
}
