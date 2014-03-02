public class Stack
{
    private class structNode
    {
        public short data;
        public structNode node;
    };

    private structNode stack;
    private int count;

    public void push(short data)
    {
        structNode s=new structNode();
        s.node=stack;
        s.data=data;
        stack=s;
        count++;
    }

    public short pop()
    {
        if(is_empty()) return 0;

        structNode temp;
        short data=stack.data;

        temp=stack.node;
        stack=temp;
        count--;
        return data;
    }

    public int on_top()
    {
        return stack.data;
    }

    public boolean is_empty()
    {
        return stack==null;
    }

    public int get_count()
    {
        return count;
    }
}
